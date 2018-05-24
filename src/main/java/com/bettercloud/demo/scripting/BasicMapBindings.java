package com.bettercloud.demo.scripting;

import com.bettercloud.demo.scripting.impl.ScriptErrorAggregator;
import com.bettercloud.demo.scripting.impl.ScriptLogAggregator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import javax.script.SimpleBindings;
import java.util.Map;

public class BasicMapBindings extends SimpleBindings {

    @Getter private final ScriptLogAggregator logAggregator;
    @Getter private final ScriptErrorAggregator errorAggregator;

    public BasicMapBindings(ObjectMapper jsonObjectMapper, Map input, ScriptExecutionEnvironment env) {
        super();
        this.put("input", input);
        this.put("output", input);
        this.logAggregator = new ScriptLogAggregator(jsonObjectMapper, env.getMinLogLevel());
        this.put("console", logAggregator);
        this.errorAggregator = new ScriptErrorAggregator(jsonObjectMapper);
        this.put("errors", errorAggregator);
    }
}
