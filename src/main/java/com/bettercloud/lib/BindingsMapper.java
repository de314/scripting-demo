package com.bettercloud.lib;

import com.bettercloud.lib.context.ScriptEnvironmentContext;
import com.fasterxml.jackson.databind.JsonNode;

public interface BindingsMapper {

    BasicJsonMapBindings to(JsonNode input, ScriptEnvironmentContext env);

    JsonNode from(BasicJsonMapBindings bindings, ScriptEnvironmentContext env);
}
