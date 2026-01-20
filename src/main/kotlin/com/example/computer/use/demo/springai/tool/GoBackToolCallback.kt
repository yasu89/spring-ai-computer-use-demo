package com.example.computer.use.demo.springai.tool

import com.example.computer.use.demo.playwright.OnlySafetyDecisionParameter
import com.example.computer.use.demo.playwright.PlaywrightService
import com.example.computer.use.demo.springai.PlaywrightToolResolver
import com.google.common.collect.ImmutableMap
import com.google.genai.types.Schema
import com.google.genai.types.Type
import org.springframework.ai.tool.ToolCallback
import org.springframework.ai.tool.definition.DefaultToolDefinition
import org.springframework.ai.tool.definition.ToolDefinition
import org.springframework.ai.tool.execution.ToolCallResult
import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.readValue

class GoBackToolCallback(
    private val playwrightToolResolver: PlaywrightToolResolver,
    private val playwrightService: PlaywrightService,
) : ToolCallback {
    companion object {
        private const val TOOL_NAME = "go_back"
    }

    override fun getToolDefinition(): ToolDefinition =
        DefaultToolDefinition
            .builder()
            .name(TOOL_NAME)
            .description("Go back in the browser")
            .inputSchema(
                Schema
                    .builder()
                    .type(Type.Known.OBJECT)
                    .properties(
                        ImmutableMap.of(
                            "safety_decision",
                            Schema
                                .builder()
                                .type(Type.Known.OBJECT)
                                .title("safety_decision")
                                .description("safety decision")
                                .properties(
                                    ImmutableMap.of(
                                        "explanation",
                                        Schema
                                            .builder()
                                            .type(Type.Known.STRING)
                                            .title("explanation")
                                            .description("explanation")
                                            .build(),
                                        "decision",
                                        Schema
                                            .builder()
                                            .type(Type.Known.STRING)
                                            .title("decision")
                                            .description("decision")
                                            .build(),
                                    ),
                                ).build(),
                        ),
                    ).build()
                    .toJson(),
            ).build()

    override fun call(toolInput: String): ToolCallResult {
        println("go back...")
        val parameter = jacksonObjectMapper().readValue<OnlySafetyDecisionParameter>(toolInput)
        playwrightService.goBack(parameter)
        return playwrightToolResolver.getToolResponse(parameter.safetyDecision != null)
    }
}
