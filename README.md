# cognizone-lib

## Http header
Easy way to add http headers based on configuration.

### Gradle part
```groovy
  implementation("zone.cogni.lib:httpheaders:0.0.1")
```
### Java part
Just add this in your `@Configuration` or `@SpringBootApplication` class
```java
@EnableHttpHeaders
```

### YAML configuration example
```yaml
cognizone:
  http:
    headers:
      - type: plain
        key: X-Frame-Options
        value: sameorigin
      - type: plain
        key: X-My-Server
        value: El serveros
```
At the moment, `type` can only be equal to `plain`.
This will just return the value as specified in the configuration.
In the future types like `spelExpression` and such can be added.
