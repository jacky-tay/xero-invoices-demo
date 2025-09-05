package com.jkytay.xero.compose.robot

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule

internal class ErrorScreenRobot(rule: ComposeContentTestRule) : ComposeContentTestRule by rule {

    private val iconSemanticsMatcher = hasContentDescription("Error") and hasRole(Role.Image)

    private val messageSemanticsMatcher = hasText("Opps, something went wrong!!")

    private val retrySemanticsMatcher = hasText("Retry") and hasRole(Role.Button) and hasClickAction()

    fun rootNode(): SemanticsNodeInteraction {
        return onNode(
            matcher = isContainer() and
                    hasAnyChild(matcher = iconSemanticsMatcher) and
                    hasAnyChild(matcher = messageSemanticsMatcher) and
                    hasAnyChild(matcher = retrySemanticsMatcher)
        )
    }

    fun retryNode(): SemanticsNodeInteraction {
        return onNode(
            matcher = retrySemanticsMatcher and
                    hasAnySibling(matcher = iconSemanticsMatcher) and
                    hasAnySibling(matcher = messageSemanticsMatcher)
        )
    }
}
