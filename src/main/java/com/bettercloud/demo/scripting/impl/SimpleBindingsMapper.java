package com.bettercloud.demo.scripting.impl;

import com.bettercloud.demo.scripting.BasicMapBindings;
import com.bettercloud.demo.scripting.BindingsMapper;
import com.bettercloud.demo.scripting.ScriptExecutionEnvironment;
import com.bettercloud.demo.scripting.ScriptLogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Service;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class SimpleBindingsMapper implements BindingsMapper {

    private final ObjectMapper jsonObjectMapper;

    public SimpleBindingsMapper(ObjectMapper jsonObjectMapper) {
        this.jsonObjectMapper = jsonObjectMapper;
    }

    @Override
    public BasicMapBindings to(JsonNode input, ScriptExecutionEnvironment env) {
        return new BasicMapBindings(jsonObjectMapper, jsonObjectMapper.convertValue(input, Map.class), env);
    }

    @Override
    public JsonNode from(BasicMapBindings bindings, ScriptExecutionEnvironment env) {
        ObjectNode responseNode = jsonObjectMapper.createObjectNode();

        switch (env) {

            case DEVELOPMENT: {
                // TODO: input
            }

            case TESTING: {
                if (bindings.containsKey("stats")) {
                    responseNode.set("stats", jsonObjectMapper.valueToTree(bindings.get("stats")));
                }
                responseNode.set("logs", jsonObjectMapper.valueToTree(bindings.getLogAggregator().getLogs()));
            }

            case PRODUCTION:
            default: {
                if (bindings.containsKey("output")) {
                    Object output = bindings.get("output");
                    JsonNode outputNode = jsonObjectMapper.valueToTree(output);
                    responseNode.set("output", outputNode);
                } else {
                    responseNode.set("output", jsonObjectMapper.createObjectNode());
                }

                responseNode.set("errors", jsonObjectMapper.valueToTree(bindings.getErrorAggregator().getErrors()));
            }
        }


        return responseNode;
    }
}
