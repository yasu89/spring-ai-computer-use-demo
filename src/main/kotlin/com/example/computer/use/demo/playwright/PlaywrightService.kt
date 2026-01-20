package com.example.computer.use.demo.playwright

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class PlaywrightService {
    companion object {
        const val VIEWPORT_WIDTH = 1280
        const val VIEWPORT_HEIGHT = 720
        const val VIEWPORT_NORMALIZATION_FACTOR = 1000
    }

    var playwright: Playwright = Playwright.create()

    private var _browser: Browser? = null
    private var _page: Page? = null

    val browser: Browser get() = _browser ?: error("browser not initialized")

    val page: Page get() = _page ?: error("page not initialized")

    fun launchBrowser() {
        _browser =
            playwright.chromium().launch(
                BrowserType
                    .LaunchOptions()
                    .setHeadless(false),
            )
    }

    fun newPage(): Page {
        _page = browser.newPage()
        return page
    }

    private fun denormalizeX(normalizedX: Int): Int = (normalizedX * VIEWPORT_WIDTH) / VIEWPORT_NORMALIZATION_FACTOR

    private fun denormalizeY(normalizedY: Int): Int = (normalizedY * VIEWPORT_HEIGHT) / VIEWPORT_NORMALIZATION_FACTOR

    private fun denormalizeMagnitudeX(normalizedMagnitude: Double): Double =
        normalizedMagnitude * VIEWPORT_WIDTH / VIEWPORT_NORMALIZATION_FACTOR

    private fun denormalizeMagnitudeY(normalizedMagnitude: Double): Double =
        normalizedMagnitude * VIEWPORT_HEIGHT / VIEWPORT_NORMALIZATION_FACTOR

    fun openWebBrowser(parameter: OnlySafetyDecisionParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        launchBrowser()
        val newPage = newPage()
        newPage.navigate("https://www.google.com/")
    }

    fun clickAt(parameter: ClickAtParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        val x = denormalizeX(parameter.x)
        val y = denormalizeY(parameter.y)
        page.mouse().click(x.toDouble(), y.toDouble())
    }

    fun hoverAt(parameter: HoverAtParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        val x = denormalizeX(parameter.x)
        val y = denormalizeY(parameter.y)
        page.mouse().move(x.toDouble(), y.toDouble())
    }

    fun typeTextAt(parameter: TypeTextAtParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        val x = denormalizeX(parameter.x)
        val y = denormalizeY(parameter.y)

        page.mouse().click(x.toDouble(), y.toDouble())
        if (parameter.clearBeforeTyping) {
            page.keyboard().press("Control+A")
            page.keyboard().press("Backspace")
        }
        page.keyboard().type(parameter.text)
        if (parameter.pressEnter) {
            page.keyboard().press("Enter")
        }
    }

    fun scrollDocument(parameter: ScrollDocumentParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        val deltaY =
            when (parameter.direction) {
                "up" -> -parameter.magnitude
                "down" -> parameter.magnitude
                else -> throw IllegalArgumentException("Unknown direction: ${parameter.direction}")
            }
        page.mouse().wheel(0.0, deltaY)
    }

    fun scrollAt(parameter: ScrollAtParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        val x = denormalizeX(parameter.x)
        val y = denormalizeY(parameter.y)
        val magnitudeX = denormalizeMagnitudeX(parameter.magnitude)
        val magnitudeY = denormalizeMagnitudeY(parameter.magnitude)
        val (deltaX, deltaY) =
            when (parameter.direction) {
                "up" -> 0.0 to -magnitudeY
                "down" -> 0.0 to magnitudeY
                "left" -> -magnitudeX to 0.0
                "right" -> magnitudeX to 0.0
                else -> throw IllegalArgumentException("Unknown direction: ${parameter.direction}")
            }
        page.mouse().move(x.toDouble(), y.toDouble())
        page.mouse().wheel(deltaX, deltaY)
    }

    fun goBack(parameter: OnlySafetyDecisionParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        page.goBack()
    }

    fun goForward(parameter: OnlySafetyDecisionParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        page.goForward()
    }

    fun navigate(parameter: NavigateParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        page.navigate(parameter.url)
    }

    fun keyCombination(parameter: KeyCombinationParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        page.keyboard().press(parameter.keys)
    }

    fun dragAndDrop(parameter: DragAndDropParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        val startX = denormalizeX(parameter.x)
        val startY = denormalizeY(parameter.y)
        val destinationX = denormalizeX(parameter.destinationX)
        val destinationY = denormalizeY(parameter.destinationY)

        page.mouse().move(startX.toDouble(), startY.toDouble())
        page.mouse().down()
        page.mouse().move(destinationX.toDouble(), destinationY.toDouble())
        page.mouse().up()
    }

    fun wait5Seconds(parameter: OnlySafetyDecisionParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        Thread.sleep(5000)
    }

    fun search(parameter: OnlySafetyDecisionParameter) {
        parameter.safetyDecision?.let { getSafetyConfirmation(it) }
        if (_page == null) {
            openWebBrowser(parameter)
            return
        }
        page.navigate("https://www.google.com/")
    }

    private fun getSafetyConfirmation(safetyDecision: SafetyDecision) {
        println("Safety confirmation required.")
        println("Decision: ${safetyDecision.decision}")
        println("Explanation: ${safetyDecision.explanation}")
        print("Proceed? (Yes/No): ")
        val input = readStandardInput()?.trim()
        if (input == "Yes") {
            return
        }
        throw SafetyConfirmationRejectedException(
            "Safety confirmation not granted.",
        )
    }

    private fun readStandardInput(): String? {
        val console = System.console()
        if (console != null) {
            return console.readLine()
        }
        return try {
            BufferedReader(InputStreamReader(System.`in`)).use { reader ->
                reader.readLine()
            }
        } catch (_: IOException) {
            null
        }
    }
}
