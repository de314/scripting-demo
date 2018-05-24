package com.bettercloud.lib.impl;

import com.bettercloud.lib.BasicJsonMapBindings;
import com.bettercloud.lib.BindingsMapper;
import com.bettercloud.lib.context.ScriptEnvironmentContext;
import com.bettercloud.lib.context.ScriptExecutionEnvironment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SimpleBindingsMapper implements BindingsMapper {

    private final ObjectMapper jsonObjectMapper;

    public SimpleBindingsMapper(ObjectMapper jsonObjectMapper) {
        this.jsonObjectMapper = jsonObjectMapper;
    }

    @Override
    public BasicJsonMapBindings to(JsonNode input, ScriptEnvironmentContext env) {
        return new BasicJsonMapBindings(jsonObjectMapper, jsonObjectMapper.convertValue(input, Map.class), env);
    }

    @Override
    public JsonNode from(BasicJsonMapBindings bindings, ScriptEnvironmentContext env) {
        ObjectNode responseNode = jsonObjectMapper.createObjectNode();

        try {
            System.out.println(jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bindings));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        switch (env.getExeEnvironment()) {

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
                responseNode.set("output", jsonObjectMapper.valueToTree(bindings.getOutput()));
                responseNode.set("errors", jsonObjectMapper.valueToTree(bindings.getErrorAggregator().getErrors()));
            }
        }


        return responseNode;
    }
}
