# Summary

Nashorn javascript is based on ECMAScript 5.1 but future versions of nashorn will include support for ECMAScript 6:

> The current strategy for Nashorn is to follow the ECMAScript specification. When we release with JDK 8 we will be aligned with ECMAScript 5.1. The follow up major release of Nashorn will align with ECMAScript Edition 6.

## Executing Raw JavaScript

```java
ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
String jsSource = "var message = 'Hello, World!';    \n" +
                  "var iterations = 2 * 3 - 1;       \n" +
                  "for (var i=0;i<iterations;i++) {  \n" +
                  "  print(i + ': ' + message);      \n" +
                  "}";
engine.eval(jsSource);
```

## Executing from a file

Javascript code can either be evaluated directly by passing javascript code as a string as shown above. Or you can pass a file reader pointing to your .js script file:

**src/main/resources/test.js**
```javascript
var message = "Hello, World!";

for (var i=0;i<10;i++) {
    print(message);
}

this.output = { complete: true };
``` |

**src/main/java/Main.java**
```java
ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
URL resource = this.getClass().getClassLoader().getResource("test.js");
Bindings bindings = new SimpleBindings();
engine.eval(new FileReader(resource.getFile()), bindings);
System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(bindings));
```

# Running

Only `System.out.println` tests for now...

See `./demo-scripting/src/test/java/com/bettercloud/demo/scripting/NashornTest.java`

# Resources

## The Good Ones

- [Tutorial](http://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/)
- [Best Incomplete Docs](https://wiki.openjdk.java.net/display/Nashorn/Nashorn+extensions)
- [Performance](http://stackoverflow.com/a/33945116)

## The Other Ones

- [Oracle Intro](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html)
- [Shitty Docs](http://docs.oracle.com/javase/8/docs/technotes/guides/scripting/nashorn/)
