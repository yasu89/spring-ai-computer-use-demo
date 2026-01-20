package com.example.computer.use.demo

import com.example.computer.use.demo.springai.PlaywrightServiceSpringAi
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class MainCommandLineRunner(
    private val service: PlaywrightServiceSpringAi,
) : CommandLineRunner {
    override fun run(vararg args: String) {
        val instruction = args.joinToString(" ")
        println("Instruction: $instruction")
        service.computerUse(instruction)
    }
}
