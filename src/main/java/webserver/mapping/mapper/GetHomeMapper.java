package webserver.mapping.mapper;

public class GetHomeMapper implements HttpMapper {
    @Override
    public byte[] handle() {

        return "<h1>GET / Handler</h1>".getBytes();
    }
}
