package com.bettercloud.demo.scripting;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public interface ScriptingService {

    JsonNode process(String src, JsonNode input, ScriptExecutionEnvironment env);

    JsonNode process(UUID scriptId, JsonNode input, ScriptExecutionEnvironment env);
}
