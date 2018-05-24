package com.bettercloud.lib.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.Date;
import java.util.List;

public class ScriptErrorAggregator {

    @Getter
    private final List<String> errors;
    private final ObjectMapper jsonObjectMapper;

    public ScriptErrorAggregator(ObjectMapper jsonObjectMapper) {
        this.jsonObjectMapper = jsonObjectMapper;
        errors = Lists.newArrayList();
    }

    public void record(Object...o) {
        if (o.length > 0) {
            for (Object logTarget : o) {
                try {
                    errors.add(String.format(
                            "%s - %s",
                            new Date().toString(),
                            jsonObjectMapper.writeValueAsString(logTarget)
                    ));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } else {
            errors.add(String.format(
                    "%s - ???",
                    new Date().toString()
            ));
        }
    }
}
