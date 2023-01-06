package zone.cogni.lib.http.headers;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpHeaderFilter implements Filter {

  private final HttpHeadersProperties httpHeadersProperties;

  public HttpHeaderFilter(HttpHeadersProperties httpHeadersProperties) {
    this.httpHeadersProperties = httpHeadersProperties;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpHeadersProperties.getHeaders()
                         .forEach(httpHeader -> handle(httpResponse, httpHeader));
    chain.doFilter(request, response);
  }

  private void handle(HttpServletResponse response, HttpHeadersProperties.HttpHeader httpHeader) {
    String value = getValue(httpHeader);
    response.addHeader(httpHeader.getKey(), value);
  }

  private String getValue(HttpHeadersProperties.HttpHeader httpHeader) {
    if(httpHeader.getType() == HttpHeadersProperties.Type.plain) {
      return httpHeader.getValue();
    }
    //future stuff, spelExpression or so...
    throw new RuntimeException("HttpHeader type not supported: " + httpHeader.getType());
  }
}
