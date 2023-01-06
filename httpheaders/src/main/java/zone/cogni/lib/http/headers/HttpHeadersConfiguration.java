package zone.cogni.lib.http.headers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpHeadersConfiguration {

  @Bean
  public HttpHeaderFilter httpHeaderFilter() {
    return new HttpHeaderFilter(httpHeadersProperties());
  }

  @Bean
  @ConfigurationProperties("cognizone.http")
  public HttpHeadersProperties httpHeadersProperties() {
    return new HttpHeadersProperties();
  }
}
