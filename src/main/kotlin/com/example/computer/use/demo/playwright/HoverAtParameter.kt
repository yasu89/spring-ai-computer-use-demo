package com.example.computer.use.demo.playwright

import com.fasterxml.jackson.annotation.JsonProperty

data class HoverAtParameter(
    val x: Int,
    val y: Int,
    @JsonProperty("safety_decision")
    val safetyDecision: SafetyDecision? = null,
)
