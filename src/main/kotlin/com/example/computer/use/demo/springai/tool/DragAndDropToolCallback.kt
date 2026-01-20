package com.example.computer.use.demo.springai.tool

import com.example.computer.use.demo.playwright.DragAndDropParameter
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

class DragAndDropToolCallback(
    private val playwrightToolResolver: PlaywrightToolResolver,
    private val playwrightService: PlaywrightService,
) : ToolCallback {
    companion object {
        private const val TOOL_NAME = "drag_and_drop"
    }

    override fun getToolDefinition(): ToolDefinition =
        DefaultToolDefinition
            .builder()
            .name(TOOL_NAME)
            .description("Drag from the specified position and drop at another position")
            .inputSchema(
                Schema
                    .builder()
                    .type(Type.Known.OBJECT)
                    .properties(
                        ImmutableMap.of(
                            "x",
                            Schema
                                .builder()
                                .type(Type.Known.INTEGER)
                                .title("x")
                                .description("start x coordinate (normalized 0-1000)")
                                .build(),
                            "y",
                            Schema
                                .builder()
                                .type(Type.Known.INTEGER)
                                .title("y")
                                .description("start y coordinate (normalized 0-1000)")
                                .build(),
                            "destination_x",
                            Schema
                                .builder()
                                .type(Type.Known.INTEGER)
                                .title("destination_x")
                                .description("destination x coordinate (normalized 0-1000)")
                                .build(),
                            "destination_y",
                            Schema
                                .builder()
                                .type(Type.Known.INTEGER)
                                .title("destination_y")
                                .description("destination y coordinate (normalized 0-1000)")
                                .build(),
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
                    ).required("x", "y", "destination_x", "destination_y")
                    .build()
                    .toJson(),
            ).build()

    override fun call(toolInput: String): ToolCallResult {
        println("drag and drop...")
        val parameter = jacksonObjectMapper().readValue<DragAndDropParameter>(toolInput)
        playwrightService.dragAndDrop(parameter)
        return playwrightToolResolver.getToolResponse(parameter.safetyDecision != null)
    }
}
