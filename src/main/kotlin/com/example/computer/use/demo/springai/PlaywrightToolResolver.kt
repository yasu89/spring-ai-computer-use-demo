package com.example.computer.use.demo.springai

import com.example.computer.use.demo.playwright.PlaywrightService
import com.example.computer.use.demo.springai.tool.ClickAtToolCallback
import com.example.computer.use.demo.springai.tool.DragAndDropToolCallback
import com.example.computer.use.demo.springai.tool.GoBackToolCallback
import com.example.computer.use.demo.springai.tool.GoForwardToolCallback
import com.example.computer.use.demo.springai.tool.HoverAtToolCallback
import com.example.computer.use.demo.springai.tool.KeyCombinationToolCallback
import com.example.computer.use.demo.springai.tool.NavigateToolCallback
import com.example.computer.use.demo.springai.tool.OpenWebBrowserToolCallback
import com.example.computer.use.demo.springai.tool.ScrollAtToolCallback
import com.example.computer.use.demo.springai.tool.ScrollDocumentToolCallback
import com.example.computer.use.demo.springai.tool.SearchToolCallback
import com.example.computer.use.demo.springai.tool.TypeTextAtToolCallback
import com.example.computer.use.demo.springai.tool.Wait5SecondsToolCallback
import com.google.genai.types.FunctionResponsePart
import com.microsoft.playwright.Page
import com.microsoft.playwright.options.ScreenshotType
import org.springframework.ai.tool.ToolCallback
import org.springframework.ai.tool.execution.ToolCallResult
import org.springframework.ai.tool.resolution.ToolCallbackResolver
import tools.jackson.module.kotlin.jacksonObjectMapper

class PlaywrightToolResolver(
    private val playwrightService: PlaywrightService,
) : ToolCallbackResolver {
    private val tollCallbacks: Map<String, ToolCallback> =
        mapOf(
            "open_web_browser" to OpenWebBrowserToolCallback(this, playwrightService),
            "click_at" to ClickAtToolCallback(this, playwrightService),
            "hover_at" to HoverAtToolCallback(this, playwrightService),
            "type_text_at" to TypeTextAtToolCallback(this, playwrightService),
            "scroll_document" to ScrollDocumentToolCallback(this, playwrightService),
            "scroll_at" to ScrollAtToolCallback(this, playwrightService),
            "wait_5_seconds" to Wait5SecondsToolCallback(this, playwrightService),
            "go_back" to GoBackToolCallback(this, playwrightService),
            "go_forward" to GoForwardToolCallback(this, playwrightService),
            "search" to SearchToolCallback(this, playwrightService),
            "navigate" to NavigateToolCallback(this, playwrightService),
            "key_combination" to KeyCombinationToolCallback(this, playwrightService),
            "drag_and_drop" to DragAndDropToolCallback(this, playwrightService),
        )

    override fun resolve(toolName: String): ToolCallback =
        tollCallbacks[toolName]
            ?: throw IllegalArgumentException("No tool callback found for tool name: $toolName")

    fun getToolResponse(needSafetyAcknowledgement: Boolean): ToolCallResult {
        val page = playwrightService.page
        page.waitForLoadState()
        Thread.sleep(500)

        val screenshotBytes = page.screenshot(Page.ScreenshotOptions().setType(ScreenshotType.PNG))
        val response: MutableMap<String, Any?> =
            mutableMapOf(
                "url" to page.url(),
            )
        if (needSafetyAcknowledgement) {
            response["safety_acknowledgement"] = true
        }

        return ToolCallResult
            .builder()
            .content(jacksonObjectMapper().writeValueAsString(response))
            .metadata(
                mapOf(
                    "gemini.functionResponse.partsJson" to
                        arrayOf(
                            FunctionResponsePart.fromBytes(screenshotBytes, "image/png").toJson(),
                        ),
                ),
            ).build()
    }
}
