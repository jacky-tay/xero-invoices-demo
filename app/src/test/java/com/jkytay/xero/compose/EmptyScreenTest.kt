package com.jkytay.xero.compose

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.jkytay.xero.compose.robot.EmptyScreenRobot
import com.jkytay.xero.ui.InvoiceEmptyScreen
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class EmptyScreenTest {

    internal interface EmptyScreenOnClick {
        fun onClick()
    }

    @get:Rule
    internal val composeTestRule = createComposeRule()
    private val robot: EmptyScreenRobot by lazy { EmptyScreenRobot(composeTestRule) }

    private lateinit var mockOnClick: EmptyScreenOnClick

    @Before
    fun setup() {
        mockOnClick = mockk(relaxed = true)
        setContent()
    }

    @Test
    fun `verify empty screen exists`() {
        robot.rootNode().assertExists()
    }

    @Test
    fun `verify empty screen reload button exist and callback is invoked when clicked`() {
        robot.reloadNode()
            .assertExists()
            .performClick()

        verify { mockOnClick.onClick() }
    }

    private fun setContent() {
        composeTestRule.setContent {
            InvoiceEmptyScreen(
                onReload = mockOnClick::onClick
            )
        }
    }
}
