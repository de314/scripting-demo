package com.bettercloud.lib.context;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScriptEnvironmentContext {

    private final ScriptLogLevel minLoggingLevel;
    private final ScriptExecutionEnvironment exeEnvironment;
}
