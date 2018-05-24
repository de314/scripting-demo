package com.bettercloud.demo.scripting;

import lombok.Getter;

public enum ScriptExecutionEnvironment {

    PRODUCTION(ScriptLogLevel.WARN),
    TESTING(ScriptLogLevel.INFO),
    DEVELOPMENT(ScriptLogLevel.TRACE);

    @Getter
    private final ScriptLogLevel minLogLevel;

    ScriptExecutionEnvironment(ScriptLogLevel minLogLevel) {
        this.minLogLevel = minLogLevel;
    }
}
