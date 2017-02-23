package com.bettercloud.demo.scripting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by davidesposito on 2/22/17.
 */
public class NashornTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ScriptEngine engine;

    @Before
    public void setup() {
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
    }

    @Test
    public void test_print() throws ScriptException {
        ScriptObjectMirror output = (ScriptObjectMirror) engine.eval("print('Hello, World!');\n" +
                "var param = { test: true, alert: 'it works' };\n" +
                "var yap = 2;\n" +
                "param");

        System.out.println("output = " + output);
        output.entrySet().forEach(e -> {
            System.out.println(e);
        });
        assertTrue((Boolean) output.get("test"));
        assertEquals("it works", output.get("alert"));
    }

    @Test
    public void test_output() throws ScriptException {
        ScriptObjectMirror output = (ScriptObjectMirror) engine.eval("print('Hello, World!');\n" +
                "var param = { test: true, alert: 'it works' };\n" +
                "var yap = 2;\n" +
                "param");

        System.out.println("output = " + output);
        output.entrySet().forEach(e -> {
            System.out.println(e);
        });
    }

    @Test
    public void test_context() throws ScriptException {
        ScriptContext ctx = new SimpleScriptContext();
        engine.eval("var test = { testing: true }\n" +
                "this.another = { testing: true }", ctx);
        System.out.println("ctx = " + ctx);
        Object testCtx = ctx.getAttribute("test");
        System.out.println("testCtx = " + testCtx);
        testCtx = ctx.getAttribute("another");
        System.out.println("testCtx = " + testCtx);
    }

    @Test
    public void test_bindings_00() throws ScriptException, JsonProcessingException {
        Bindings bindings = new SimpleBindings();
        Map<String, Object> input = Maps.newHashMap();
        input.put("testing", true);
        bindings.put("input", input);
        engine.eval("print(this.input)\n" +
                "print(this.input.testing)\n" +
                "this.input.another = 16", bindings);
        System.out.println("bindings = " + bindings);
        String bindingsJson = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(bindings);
        System.out.println("bindingsJson = " + bindingsJson);
    }

    @Test
    public void test_bindings_01() throws ScriptException, JsonProcessingException {
        Bindings bindings = new SimpleBindings();
        Map<String, Object> input = Maps.newHashMap();
        input.put("testing", true);
        bindings.put("input", input);
        engine.eval("print(this.input)\n" +
                "print(this.input.testing)\n" +
                "this.input.another = 16\n" +
                "this.output = 'a string'\n" +
                "this.out2 = { an: 'object' }\n" +
                "this.out3 = [ 'arr', 'vals', 'are', 'here' ]", bindings);
        bindings = (Bindings) convert(bindings);
        System.out.println("bindings = " + bindings);
        String bindingsJson = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(bindings);
        System.out.println("bindingsJson = " + bindingsJson);
    }

    private Object convert(Bindings root) {
        if (root == null) {
            return null;
        }
        if (ScriptObjectMirror.class.isAssignableFrom(root.getClass()) && ((ScriptObjectMirror)root).isArray()) {
            return Lists.newArrayList(root.values());
        }
        Map<String, Object> replacements = Maps.newHashMap();
        root.entrySet().forEach(e -> {
            if (ScriptObjectMirror.class.isAssignableFrom(e.getValue().getClass())) {
                replacements.put(e.getKey(), convert(ScriptObjectMirror.class.cast(e.getValue())));
            }
        });
        replacements.entrySet().forEach(e -> root.put(e.getKey(), e.getValue()));
        return root;
    }
}
