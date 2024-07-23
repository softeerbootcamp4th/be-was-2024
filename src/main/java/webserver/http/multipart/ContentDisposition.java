package webserver.http.multipart;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.BitSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentDisposition {
    private static final Pattern BASE64_ENCODED_PATTERN = Pattern.compile("=\\?([0-9a-zA-Z-_]+)\\?B\\?([+/0-9a-zA-Z]+=*)\\?=");
    private static final Pattern QUOTED_PRINTABLE_ENCODED_PATTERN = Pattern.compile("=\\?([0-9a-zA-Z-_]+)\\?Q\\?([!->@-~]+)\\?=");
    private static final String INVALID_HEADER_FIELD_PARAMETER_FORMAT = "Invalid header field parameter format (as defined in RFC 5987)";
    private static final BitSet PRINTABLE = new BitSet(256);

    private final String type;
    private final String name;
    private final String filename;
    private final Charset charset;
    private final Long size;
    private final ZonedDateTime creationDate;
    private final ZonedDateTime modificationDate;
    private final ZonedDateTime readDate;

    /**
     * disposition := "Content-Disposition" ":" disposition-type *(";" disposition-param)
     * <p>
     * disposition-type := "inline" / "attachment" / extension-token ; values are not case-sensitive
     * <p>
     * disposition-param := filename-param / creation-date-param / modification-date-param / read-date-param / size-param / parameter
     */

    public ContentDisposition(String type, String name, String filename, Charset charset, Long size, ZonedDateTime creationDate, ZonedDateTime modificationDate, ZonedDateTime readDate) {
        this.type = type;
        this.name = name;
        this.filename = filename;
        this.charset = charset;
        this.size = size;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.readDate = readDate;
    }

    public ContentDisposition() {
        this(null, null, null, null, null, null, null, null);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public Charset getCharset() {
        return charset;
    }

    public Long getSize() {
        return size;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public ZonedDateTime getModificationDate() {
        return modificationDate;
    }

    public ZonedDateTime getReadDate() {
        return readDate;
    }

    public static ContentDisposition parse(String contentDisposition) {
        List<String> parts = tokenize(contentDisposition);
        String type = (String) parts.get(0);
        String name = null;
        String filename = null;
        Charset charset = null;
        Long size = null;
        ZonedDateTime creationDate = null;
        ZonedDateTime modificationDate = null;
        ZonedDateTime readDate = null;

        for (int i = 1; i < parts.size(); ++i) {
            String part = (String) parts.get(i);
            int eqIndex = part.indexOf(61);
            if (eqIndex == -1) {
                throw new IllegalArgumentException("Invalid content disposition format");
            }

            String attribute = part.substring(0, eqIndex);
            String value = part.startsWith("\"", eqIndex + 1) && part.endsWith("\"") ? part.substring(eqIndex + 2, part.length() - 1) : part.substring(eqIndex + 1);
            if (attribute.equals("name")) {
                name = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            } else if (attribute.equals("filename*")) {
                int idx1 = value.indexOf(39);
                int idx2 = value.indexOf(39, idx1 + 1);
                if (idx1 != -1 && idx2 != -1) {
                    charset = Charset.forName(value.substring(0, idx1).trim());
                    filename = URLDecoder.decode(value.substring(idx2 + 1), charset);
                } else {
                    filename = URLDecoder.decode(value, StandardCharsets.US_ASCII);
                }
            } else if (attribute.equals("filename") && filename == null) {
                if (!value.startsWith("=?")) {
                    if (value.indexOf(92) != -1) {
                        filename = decodeQuotedPairs(value);
                    } else {
                        filename = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    }
                } else {
                    Matcher matcher = BASE64_ENCODED_PATTERN.matcher(value);
                    if (matcher.find()) {
                        Base64.Decoder decoder = Base64.getDecoder();
                        StringBuilder builder = new StringBuilder();

                        do {
                            charset = Charset.forName(matcher.group(1));
                            byte[] decoded = decoder.decode(matcher.group(2));
                            builder.append(new String(decoded, charset));
                        } while (matcher.find());

                        filename = builder.toString();
                    } else {
                        matcher = QUOTED_PRINTABLE_ENCODED_PATTERN.matcher(value);
                        if (!matcher.find()) {
                            filename = value;
                        } else {
                            StringBuilder builder = new StringBuilder();

                            do {
                                charset = Charset.forName(matcher.group(1));
                                String decoded = URLDecoder.decode(matcher.group(2), charset);
                                builder.append(decoded);
                            } while (matcher.find());

                            filename = builder.toString();
                        }
                    }
                }
            } else if (attribute.equals("size")) {
                size = Long.parseLong(value);
            } else if (attribute.equals("creation-date")) {
                try {
                    creationDate = ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME);
                } catch (DateTimeParseException var21) {
                }
            } else if (attribute.equals("modification-date")) {
                try {
                    modificationDate = ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME);
                } catch (DateTimeParseException var20) {
                }
            } else if (attribute.equals("read-date")) {
                try {
                    readDate = ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME);
                } catch (DateTimeParseException var19) {
                }
            }
        }

        return new ContentDisposition(type, name, filename, charset, size, creationDate, modificationDate, readDate);
    }

    private static List<String> tokenize(String headerValue) {
        int index = headerValue.indexOf(59);
        String type = (index >= 0 ? headerValue.substring(0, index) : headerValue).trim();
        if (type.isEmpty()) {
            throw new IllegalArgumentException("Content-Disposition header must not be empty");
        } else {
            List<String> parts = new ArrayList();
            parts.add(type);
            if (index >= 0) {
                do {
                    int nextIndex = index + 1;
                    boolean quoted = false;

                    for (boolean escaped = false; nextIndex < headerValue.length(); ++nextIndex) {
                        char ch = headerValue.charAt(nextIndex);
                        if (ch == ';') {
                            if (!quoted) {
                                break;
                            }
                        } else if (!escaped && ch == '"') {
                            quoted = !quoted;
                        }

                        escaped = !escaped && ch == '\\';
                    }

                    String part = headerValue.substring(index + 1, nextIndex).trim();
                    if (!part.isEmpty()) {
                        parts.add(part);
                    }

                    index = nextIndex;
                } while (index < headerValue.length());
            }

            return parts;
        }
    }

    private static String decodeQuotedPairs(String filename) {
        StringBuilder sb = new StringBuilder();
        int length = filename.length();

        for (int i = 0; i < length; ++i) {
            char c = filename.charAt(i);
            if (filename.charAt(i) == '\\' && i + 1 < length) {
                ++i;
                char next = filename.charAt(i);
                if (next != '"' && next != '\\') {
                    sb.append(c);
                }

                sb.append(next);
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
