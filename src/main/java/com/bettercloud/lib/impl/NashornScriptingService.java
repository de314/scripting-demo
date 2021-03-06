package com.bettercloud.lib.impl;

import com.bettercloud.lib.BasicJsonMapBindings;
import com.bettercloud.lib.BindingsMapper;
import com.bettercloud.lib.ScriptingService;
import com.bettercloud.lib.context.ScriptEnvironmentContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.UUID;

@Service
public class NashornScriptingService implements ScriptingService {

    private final BindingsMapper bindingsMapper;
    private final ScriptEngine engine;
    private final Compilable compilable;

    public NashornScriptingService(BindingsMapper bindingsMapper) {
        this.bindingsMapper = bindingsMapper;
        this.engine = new ScriptEngineManager().getEngineByName("nashorn");
        this.compilable = (Compilable) engine;
    }

    @Override
    public JsonNode process(String src, JsonNode input, ScriptEnvironmentContext env) {
        boolean failed = false;

        // TODO: check for verbose stats
        long startTime = System.currentTimeMillis();
        long inputWeight = input.toString().length();
        Timer prepInputTimer = new Timer();
        BasicJsonMapBindings bindings = bindingsMapper.to(input, env);
        prepInputTimer.stop();

        Timer compilationTimer = new Timer();
        Timer executionTimer = new Timer();
        try {
            compilationTimer.start();
            CompiledScript compiledScript = compilable.compile(src);
            compilationTimer.stop();
            // reset time to capture execution
            executionTimer.start();
            compiledScript.eval(bindings);
            executionTimer.stop();
        } catch (Throwable e) {
            failed = true;
            bindings.getErrorAggregator().record(e.getMessage());
        }

        // TODO: check for verbose stats
        bindings.put("stats", ScriptRunStats.builder()
                .failed(failed)
                .startTime(startTime)
                .inputWeight(inputWeight)
                .inputPrepDuration(prepInputTimer.getDuration())
                .compilationDuration(compilationTimer.getDuration())
                .executionDuration(executionTimer.getDuration())
                .build());

        Timer outputPrepTimer = new Timer();
        JsonNode output = bindingsMapper.from(bindings, env);
        outputPrepTimer.stop();

        ((ObjectNode)output.path("stats"))
                .put("duration", System.currentTimeMillis() - startTime)
                .put("outputPrepDuration", outputPrepTimer.getDuration())
                // TODO: check for verbose stats
                .put("outputWeight", output.path("output").toString().length())
                .put("totalOutputWeight", output.toString().length());
        return output;
    }

    @Override
    public JsonNode process(UUID scriptId, JsonNode input, ScriptEnvironmentContext env) {
        return null;
    }

    @Data
    @Builder
    public static class ScriptRunStats {
        private final boolean failed;
        private final long startTime;
        private final long inputWeight;
        private final long inputPrepDuration;
        private final long compilationDuration;
        private final long executionDuration;
        private final long outputPrepDuration;
    }

    @Data
    @NoArgsConstructor
    private static class Timer {
        private long startTime = System.currentTimeMillis();
        private long endTime;
        public void start() {
            this.startTime = System.currentTimeMillis();
        }
        public void stop() {
            this.endTime = System.currentTimeMillis();
        }
        public long getDuration() {
            return this.endTime - this.startTime;
        }
    }
}
