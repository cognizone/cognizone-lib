package zone.cogni.lib.http.headers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
public class HttpHeadersConfiguration {

  @Bean
  public CreateHttpHeaderFilter httpHeaderFilter() {
    return new CreateHttpHeaderFilter(httpHeadersProperties());
  }

  @Bean
  @ConfigurationProperties("cognizone.http")
  @Validated
  public HttpHeadersProperties httpHeadersProperties() {
    return new HttpHeadersProperties();
  }
}
