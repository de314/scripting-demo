package com.bettercloud.lib;

import com.bettercloud.lib.context.ScriptEnvironmentContext;
import com.bettercloud.lib.plugins.ScriptErrorAggregator;
import com.bettercloud.lib.plugins.ScriptLogAggregator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import javax.script.SimpleBindings;
import java.util.Map;

public class BasicJsonMapBindings extends SimpleBindings {

    @Getter private final ScriptLogAggregator logAggregator;
    @Getter private final ScriptErrorAggregator errorAggregator;

    public BasicJsonMapBindings(ObjectMapper jsonObjectMapper, Map input, ScriptEnvironmentContext ctx) {
        super();
        this.put("input", input);
        this.put("output", input);
        this.logAggregator = new ScriptLogAggregator(jsonObjectMapper, ctx.getMinLoggingLevel());
        this.put("console", logAggregator);
        this.errorAggregator = new ScriptErrorAggregator(jsonObjectMapper);
        this.put("errors", errorAggregator);
    }

    public Object getOutput() {
        return ((Map)get("nashorn.global")).get("output");
    }
}
