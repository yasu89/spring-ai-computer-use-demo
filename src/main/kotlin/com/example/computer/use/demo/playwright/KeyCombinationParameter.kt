package com.example.computer.use.demo.playwright

import com.fasterxml.jackson.annotation.JsonProperty

data class KeyCombinationParameter(
    val keys: String,
    @JsonProperty("safety_decision")
    val safetyDecision: SafetyDecision? = null,
)
