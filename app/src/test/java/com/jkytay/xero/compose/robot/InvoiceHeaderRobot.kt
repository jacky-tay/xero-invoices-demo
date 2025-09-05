package com.jkytay.xero.compose.robot

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.printToString
import androidx.compose.ui.unit.dp

internal class InvoiceHeaderRobot(rule: ComposeContentTestRule) : ComposeContentTestRule by rule {
    private fun totalSemanticsMatcher(total: String): SemanticsMatcher {
        return hasText("Total: $total")
    }

    private fun dateSemanticsMatcher(date: String): SemanticsMatcher {
        return hasText(date)
    }

    private fun descriptionSemanticsMatcher(description: String): SemanticsMatcher {
        return hasText(description)
    }

    private fun buttonSemanticsMatcher(id: String, isExpand: Boolean): SemanticsMatcher {
        return hasClickAction() and hasRole(Role.Button) and hasAnyChild(
            matcher = hasContentDescription("Invoice $id is ${if (isExpand) "expanded" else "collapsed"}") and
                    hasRole(Role.Image)
        )
    }

    fun invoiceHeaderNode(
        id: String,
        total: String,
        date: String,
        description: String? = null,
        isExpand: Boolean,
    ): SemanticsNodeInteraction {
        val containerSemanticsMatcher = isContainer() and
                hasAnyChild(totalSemanticsMatcher(total)) and
                hasAnyChild(dateSemanticsMatcher(date)) and
                hasAnyChild(buttonSemanticsMatcher(id = id, isExpand = isExpand))
        val container = onNode(
            matcher = if (description != null) {
                containerSemanticsMatcher and hasAnyChild(descriptionSemanticsMatcher(description))
            } else {
                containerSemanticsMatcher
            },
            useUnmergedTree = true
        )

        // assert total bound rect
        assertTotalNodeBoundToContainer(
            container = container,
            total = total,
            hasDescription = description != null
        )

        // assert date bound rect
        assertDateNodeBoundToContainer(
            container = container,
            date = date,
            hasDescription = description != null
        )

        // assert description bound rect
        if (description != null) {
            assertDescriptionNodeBoundToContainer(container = container, description = description)
        }

        // assert expand collapsed icon bound rect
        assertExpandCollapseIconNodeBoundToContainer(
            container = container,
            id = id,
            isExpand = isExpand
        )

        return container
    }

    // region Total node bound
    private fun assertTotalNodeBoundToContainer(
        container: SemanticsNodeInteraction,
        total: String,
        hasDescription: Boolean,
    ) {
        assertLayoutGap(
            lhs = container,
            rhs = onNode(totalSemanticsMatcher(total), useUnmergedTree = true),
            gap = 16.dp,
            relationship = LayoutRelationship.StartToStart
        )
        assertLayoutGap(
            lhs = container,
            rhs = onNode(totalSemanticsMatcher(total), useUnmergedTree = true),
            // two-line lists https://m3.material.io/components/lists/specs#129ef830-d120-45fe-94b4-a6b4346a87b4
            // three-line lists https://m3.material.io/components/lists/specs#583e3432-0dc0-4a18-bfb4-a0924f67891b
            gap = if (hasDescription) 12.dp else 8.dp,
            relationship = LayoutRelationship.TopToTop
        )
    }
    // endregion

    // region Date node bound
    private fun assertDateNodeBoundToContainer(
        container: SemanticsNodeInteraction,
        date: String,
        hasDescription: Boolean,
    ) {
        assertLayoutGap(
            lhs = container,
            rhs = onNode(dateSemanticsMatcher(date), useUnmergedTree = true),
            gap = 16.dp,
            relationship = LayoutRelationship.StartToStart
        )
        if (!hasDescription) {
            assertLayoutGap(
                lhs = onNode(dateSemanticsMatcher(date), useUnmergedTree = true),
                rhs = container,
                gap = 8.dp, // two line lists vertical padding is 8dp
                relationship = LayoutRelationship.BottomToBottom
            )
        }
    }
    // endregion

    // region Description node bound
    private fun assertDescriptionNodeBoundToContainer(
        container: SemanticsNodeInteraction,
        description: String,
    ) {
        assertLayoutGap(
            lhs = container,
            rhs = onNode(descriptionSemanticsMatcher(description), useUnmergedTree = true),
            gap = 16.dp,
            relationship = LayoutRelationship.StartToStart
        )
        assertLayoutGap(
            lhs = onNode(descriptionSemanticsMatcher(description), useUnmergedTree = true),
            rhs = container,
            gap = 12.dp,
            relationship = LayoutRelationship.BottomToBottom
        )
    }
    // endregion

    // region Toggle Button node bound
    private fun assertExpandCollapseIconNodeBoundToContainer(
        container: SemanticsNodeInteraction,
        id: String,
        isExpand: Boolean,
    ) {
        assertLayoutGap(
            lhs = onNode(
                matcher = buttonSemanticsMatcher(id = id, isExpand = isExpand),
                useUnmergedTree = true
            ),
            rhs = container,
            gap = 20.dp,
            relationship = LayoutRelationship.EndToEnd
        )
    }
    // endregion
}
