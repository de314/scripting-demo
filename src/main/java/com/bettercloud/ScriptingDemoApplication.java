package com.bettercloud;

import com.bettercloud.lib.context.ScriptEnvironmentContext;
import com.bettercloud.lib.context.ScriptExecutionEnvironment;
import com.bettercloud.lib.ScriptingService;
import com.bettercloud.lib.context.ScriptLogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ScriptingDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScriptingDemoApplication.class, args);
	}

	@Bean
	public ObjectMapper jsonObjectMapper() {
		return new ObjectMapper();
	}

	@Autowired private ScriptingService scriptingService;
	@Autowired private ObjectMapper jsonObjectMapper;

	@PostMapping("/nakedTest")
	public JsonNode nakedTest(@RequestBody String src) {
		return scriptingService.process(
				src,
				jsonObjectMapper.createObjectNode(),
                ScriptEnvironmentContext.builder()
                        .minLoggingLevel(ScriptLogLevel.TRACE)
                        .exeEnvironment(ScriptExecutionEnvironment.DEVELOPMENT)
                        .build()
		);
	}

	@PostMapping("/test")
	public JsonNode test(@RequestBody JsonNode scriptingTest) {
		return scriptingService.process(
				scriptingTest.path("src").asText("errors.record('MISSING SRC')"),
				scriptingTest.path("input"),
                ScriptEnvironmentContext.builder()
                        .minLoggingLevel(ScriptLogLevel.TRACE)
                        .exeEnvironment(ScriptExecutionEnvironment.DEVELOPMENT)
                        .build()
		);
	}
}
