package com.example.computer.use.demo.playwright

import com.fasterxml.jackson.annotation.JsonProperty

data class DragAndDropParameter(
    val x: Int,
    val y: Int,
    @JsonProperty("destination_x")
    val destinationX: Int,
    @JsonProperty("destination_y")
    val destinationY: Int,
    @JsonProperty("safety_decision")
    val safetyDecision: SafetyDecision? = null,
)
