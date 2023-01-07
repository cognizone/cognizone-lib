package zone.cogni.lib.http.headers;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class HttpHeadersProperties {

  private List<HttpHeader> headers;

  public enum Type {
    plain
  }

  @Data
  public static class HttpHeader {
    @NotNull
    private Type type = Type.plain;
    @NotBlank
    private String key;
    @NotBlank
    private String value;
  }
}
