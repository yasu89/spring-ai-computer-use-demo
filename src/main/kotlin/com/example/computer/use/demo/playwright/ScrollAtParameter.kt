package com.example.computer.use.demo.playwright

import com.fasterxml.jackson.annotation.JsonProperty

data class ScrollAtParameter(
    val x: Int,
    val y: Int,
    val direction: String,
    val magnitude: Double = 800.0,
    @JsonProperty("safety_decision")
    val safetyDecision: SafetyDecision? = null,
)
