package com.example.computer.use.demo.springai

import com.example.computer.use.demo.playwright.PlaywrightService
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.google.genai.GoogleGenAiChatModel
import org.springframework.ai.google.genai.GoogleGenAiChatOptions
import org.springframework.ai.model.tool.DefaultToolCallingManager
import org.springframework.stereotype.Service

@Service
class PlaywrightServiceSpringAi(
    private val chatModel: GoogleGenAiChatModel,
) {
    fun computerUse(instruction: String) {
        val toolCallbackResolver =
            PlaywrightToolResolver(
                PlaywrightService(),
            )

        val toolCallingManager = DefaultToolCallingManager.builder().toolCallbackResolver(toolCallbackResolver).build()

        val chatOptions =
            GoogleGenAiChatOptions
                .builder()
                .temperature(1.0)
                .topP(0.95)
                .topK(40)
                .maxOutputTokens(8192)
                .model(GoogleGenAiChatModel.ChatModel.GEMINI_2_5_COMPUTER_USE_PREVIEW)
                .computerUse(true)
                .internalToolExecutionEnabled(false)
                .build()

        val userMessage = UserMessage(instruction)

        var prompt = Prompt(userMessage, chatOptions)
        var chatResponse = chatModel.call(prompt)

        while (chatResponse.hasToolCalls()) {
            println("---- Executing tool calls ----")
            val toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse)
            prompt = Prompt(toolExecutionResult.conversationHistory(), chatOptions)
            chatResponse = chatModel.call(prompt)
            println(chatResponse.result?.output?.text)
        }
        for (result in chatResponse.results) {
            println("------------ result ------------")
            println(result.output?.text)
            println(result.output?.toolCalls)
        }

        println(chatResponse.result?.output?.text)
        println(chatResponse.result?.output?.toolCalls)
        println(
            chatResponse.result
                ?.output
                ?.metadata
                ?.get("finishReason"),
        )
        println(chatResponse.hasToolCalls())
    }
}
