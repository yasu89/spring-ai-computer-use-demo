package com.example.computer.use.demo.springai.tool

import com.example.computer.use.demo.playwright.PlaywrightService
import com.example.computer.use.demo.playwright.TypeTextAtParameter
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

class TypeTextAtToolCallback(
    private val playwrightToolResolver: PlaywrightToolResolver,
    private val playwrightService: PlaywrightService,
) : ToolCallback {
    companion object {
        private const val TOOL_NAME = "type_text_at"
    }

    override fun getToolDefinition(): ToolDefinition =
        DefaultToolDefinition
            .builder()
            .name(TOOL_NAME)
            .description("Click the specified location and type text")
            .inputSchema(
                Schema
                    .builder()
                    .type(Type.Known.OBJECT)
                    .properties(
                        ImmutableMap
                            .builder<String, Schema>()
                            .put(
                                "x",
                                Schema
                                    .builder()
                                    .type(Type.Known.INTEGER)
                                    .title("x")
                                    .description("x coordinate (normalized 0-1000)")
                                    .build(),
                            ).put(
                                "y",
                                Schema
                                    .builder()
                                    .type(Type.Known.INTEGER)
                                    .title("y")
                                    .description("y coordinate (normalized 0-1000)")
                                    .build(),
                            ).put(
                                "text",
                                Schema
                                    .builder()
                                    .type(Type.Known.STRING)
                                    .title("text")
                                    .description("search query")
                                    .build(),
                            ).put(
                                "press_enter",
                                Schema
                                    .builder()
                                    .type(Type.Known.BOOLEAN)
                                    .title("press_enter")
                                    .description("press Enter after typing")
                                    .build(),
                            ).put(
                                "clear_before_typing",
                                Schema
                                    .builder()
                                    .type(Type.Known.BOOLEAN)
                                    .title("clear_before_typing")
                                    .description("clear existing text before typing")
                                    .build(),
                            ).put(
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
                            ).build(),
                    ).required("x", "y", "text")
                    .build()
                    .toJson(),
            ).build()

    override fun call(toolInput: String): ToolCallResult {
        println("type text...")
        val parameter = jacksonObjectMapper().readValue<TypeTextAtParameter>(toolInput)
        playwrightService.typeTextAt(parameter)
        return playwrightToolResolver.getToolResponse(parameter.safetyDecision != null)
    }
}
