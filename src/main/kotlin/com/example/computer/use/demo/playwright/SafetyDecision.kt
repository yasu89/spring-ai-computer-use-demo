package com.example.computer.use.demo.playwright

data class SafetyDecision(
    val explanation: String,
    val decision: String,
) {
    init {
        require(decision == "require_confirmation") {
            "Invalid decision: $decision. Only 'require_confirmation' is allowed."
        }
    }
}
