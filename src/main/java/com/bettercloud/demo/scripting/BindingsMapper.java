package com.bettercloud.demo.scripting;

import com.fasterxml.jackson.databind.JsonNode;

public interface BindingsMapper {

    BasicMapBindings to(JsonNode input, ScriptExecutionEnvironment env);

    JsonNode from(BasicMapBindings bindings, ScriptExecutionEnvironment env);
}
