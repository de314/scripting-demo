# Summary

```
ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
String jsSource = "var message = 'Hello, World!';    \n" +
                  "var iterations = 2 * 3 - 1;       \n" +
                  "for (var i=0;i<iterations;i++) {  \n" +
                  "  print(i + ': ' + message);      \n" +
                  "}";
engine.eval(jsSource);
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
