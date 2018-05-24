package com.bettercloud.demo;

import com.bettercloud.demo.scripting.ScriptExecutionEnvironment;
import com.bettercloud.demo.scripting.ScriptingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
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
				ScriptExecutionEnvironment.DEVELOPMENT
		);
	}

	@PostMapping("/test")
	public JsonNode test(@RequestBody JsonNode scriptingTest) {
		return scriptingService.process(
				scriptingTest.path("src").asText("errors.record('MISSING SRC')"),
				scriptingTest.path("input"),
				ScriptExecutionEnvironment.DEVELOPMENT
		);
	}
}
