package com.bettercloud.demo.scripting.impl;

import com.bettercloud.demo.scripting.ScriptLogLevel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.Date;
import java.util.List;

public class ScriptLogAggregator {

    @Getter
    private final List<String> logs;
    private final ObjectMapper jsonObjectMapper;
    private final ScriptLogLevel minLogLevel;

    public ScriptLogAggregator(ObjectMapper jsonObjectMapper, ScriptLogLevel minLogLevel) {
        this.jsonObjectMapper = jsonObjectMapper;
        this.minLogLevel = minLogLevel;
        logs = Lists.newArrayList();
    }

    public void error(Object...o) {
        record(ScriptLogLevel.ERROR, o);
    }

    public void log(Object...o) {
        info(o);
    }

    public void info(Object...o) {
        record(ScriptLogLevel.INFO, o);
    }

    public void warn(Object...o) {
        record(ScriptLogLevel.WARN, o);
    }

    public void debug(Object...o) {
        record(ScriptLogLevel.DEBUG, o);
    }

    public void trace(Object...o) {
        record(ScriptLogLevel.TRACE, o);
    }

    private void record(ScriptLogLevel level, Object...o) {
        if (level.ordinal() <= minLogLevel.ordinal() && o.length > 0) {
            for (Object logTarget : o) {
                try {
                    logs.add(String.format(
                            "[%s] %s - %s",
                            level,
                            new Date().toString(),
                            jsonObjectMapper.writeValueAsString(logTarget)
                    ));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
