package webserver.mapping.mapper;

public class GetRegistrationMapper implements HttpMapper {
    @Override
    public byte[] handle() {

        return "<h1>GET /registration Handler</h1>".getBytes();
    }
}
