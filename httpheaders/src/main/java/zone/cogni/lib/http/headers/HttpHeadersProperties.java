package zone.cogni.lib.http.headers;

import lombok.Data;

import java.util.List;

@Data
public class HttpHeadersProperties {

  private List<HttpHeader> headers;

  public enum Type {
    plain
  }

  @Data
  public static class HttpHeader {
    private Type type;
    private String key;
    private String value;
  }
}
