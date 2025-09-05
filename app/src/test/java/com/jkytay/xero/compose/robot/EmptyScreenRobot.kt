package com.jkytay.xero.compose.robot

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule

internal class EmptyScreenRobot(rule: ComposeContentTestRule) : ComposeContentTestRule by rule {

    private val iconSemanticsMatcher = hasContentDescription("Empty") and hasRole(Role.Image)

    private val messageSemanticsMatcher = hasText("Looks like there is no invoices.")

    private val reloadSemanticsMatcher = hasText("Reload") and hasRole(Role.Button) and hasClickAction()

    fun rootNode(): SemanticsNodeInteraction {
        return onNode(
            matcher = isContainer() and
                    hasAnyChild(matcher = iconSemanticsMatcher) and
                    hasAnyChild(matcher = messageSemanticsMatcher) and
                    hasAnyChild(matcher = reloadSemanticsMatcher)
        )
    }

    fun reloadNode(): SemanticsNodeInteraction {
        return onNode(
            matcher = reloadSemanticsMatcher and
                    hasAnySibling(matcher = iconSemanticsMatcher) and
                    hasAnySibling(matcher = messageSemanticsMatcher)
        )
    }
}
