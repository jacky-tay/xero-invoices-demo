package com.jkytay.xero.compose

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.jkytay.xero.compose.robot.ErrorScreenRobot
import com.jkytay.xero.ui.InvoiceErrorScreen
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ErrorScreenTest {

    internal interface ErrorScreenOnClick {
        fun onClick()
    }

    @get:Rule
    internal val composeTestRule = createComposeRule()
    private val robot: ErrorScreenRobot by lazy { ErrorScreenRobot(composeTestRule) }

    private lateinit var mockOnClick: ErrorScreenOnClick

    @Before
    fun setup() {
        mockOnClick = mockk(relaxed = true)
        setContent()
    }

    @Test
    fun `verify error screen exists`() {
        robot.rootNode().assertExists()
    }

    @Test
    fun `verify error screen retry  and is when clicked`() {
        robot.retryNode()
            .assertExists()
            .performClick()

        verify { mockOnClick.onClick() }
    }

    private fun setContent() {
        composeTestRule.setContent {
            InvoiceErrorScreen(
                onRetry = mockOnClick::onClick
            )
        }
    }
}
