package com.example.computer.use.demo.playwright

import com.fasterxml.jackson.annotation.JsonProperty

data class TypeTextAtParameter(
    val x: Int,
    val y: Int,
    val text: String,
    @JsonProperty("press_enter")
    val pressEnter: Boolean = true,
    @JsonProperty("clear_before_typing")
    val clearBeforeTyping: Boolean = true,
    @JsonProperty("safety_decision")
    val safetyDecision: SafetyDecision? = null,
)
