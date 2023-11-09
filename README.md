# cognizone-lib

## Http header
Easy way to add http headers based on configuration.

### Gradle part
```groovy
  implementation("zone.cogni.lib:httpheaders:0.0.3")
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
_Note 1: `type` can be omitted. If not set default `plain` will be used._

_Note 2: At the moment, `type` can only be equal to `plain`.
This will just return the value as specified in the configuration.
In the future types like `spelExpression` and such can be added._

---

## Method timer
Library that can report how much time each method execution takes by just adding an annotation.

_Note, due to default spring class-proxy limitations for Beans, 
annotations will only be picked up when called from outside the Bean.
A workaround for this is being created._ 

### Gradle part
```groovy
  implementation("zone.cogni.lib:methodtimer:0.0.3")
```
### Java part
Method timer needs to have aspectJ proxying enabled: 
Just add `@EnableAspectJAutoProxy` in your  `@Configuration` or `@SpringBootApplication` class.
### YAML example
To enable the timers just add this (by default timers are disabled). 
In java
```
cognizone:
  methodtimer:
    enabled: true
```
### Usage
Just add `@TimedMethod` annotation to the method to be timed (needs to be a method inside a Bean that is called from the outside).
- By default the timing name will be `simple classname` + `method name`.
You can overwrite this by setting the `value()` of the `@TimedMethod` annotation.
- The timer name can be altered by adding a `@TimerName` on parameters of the method.
This will do a `toString()` on that method parameter and the result is appended to the default timer name.
- The first time a `@TimedMethod` annotation is hit in a thread, a new timer is started.
Once that method finishes, the timers of all annotated methods will be reported.  
  - To avoid having a method starting a new timer if there is none already running for the current thread, 
  you can set `canBeParent` to false on `@TimedMethod`. 
- At the moment only logging with SLF4J is supported. 
So reporting is only done via log info and logger `zone.cogni.lib.methodtimer.impl.Slf4jTimerReport`.

