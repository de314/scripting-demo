package com.bettercloud.lib;

import com.bettercloud.lib.context.ScriptEnvironmentContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public interface ScriptingService {

    JsonNode process(String src, JsonNode input, ScriptEnvironmentContext env);

    JsonNode process(UUID scriptId, JsonNode input, ScriptEnvironmentContext env);
}
