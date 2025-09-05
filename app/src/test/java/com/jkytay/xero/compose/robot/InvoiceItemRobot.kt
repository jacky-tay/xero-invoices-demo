package com.jkytay.xero.compose.robot

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.unit.dp

internal class InvoiceItemRobot(rule: ComposeContentTestRule) : ComposeContentTestRule by rule {
    private fun hourlyRateSemanticsMatcher(rate: String): SemanticsMatcher {
        return hasText("Hourly Rate: $rate")
    }

    private fun nameSemanticsMatcher(name: String): SemanticsMatcher {
        return hasText(name)
    }

    private fun durationSemanticsMatcher(hour: Int): SemanticsMatcher {
        return hasText("Duration: $hour ${if (hour == 1) "hour" else "hours"}")
    }

    private fun totalSemanticsMatcher(value: String): SemanticsMatcher {
        return hasText(value)
    }

    fun invoiceLineItemNode(
        hourRate: String,
        name: String,
        duration: Int,
        total: String
    ): SemanticsNodeInteraction {
        val container = onNode(
            matcher = isContainer() and
                    hasAnyChild(hourlyRateSemanticsMatcher(hourRate)) and
                    hasAnyChild(nameSemanticsMatcher(name)) and
                    hasAnyChild(durationSemanticsMatcher(duration)) and
                    hasAnyChild(totalSemanticsMatcher(total)),
            useUnmergedTree = true
        )
        // assert hourly rate node bound rect
        assertHourlyNodeBoundToContainer(container = container, hourRate = hourRate)

        // assert name node bound rect
        assertNameNodeBoundToContainer(container = container, name = name)
        assertLayoutGap(
            lhs = onNode(hourlyRateSemanticsMatcher(hourRate), useUnmergedTree = true),
            rhs = onNode(nameSemanticsMatcher(name), useUnmergedTree = true),
            gap = 0.dp,
            relationship = LayoutRelationship.TopToBottom
        )
        assertLayoutGap(
            lhs = onNode(nameSemanticsMatcher(name), useUnmergedTree = true),
            rhs = onNode(durationSemanticsMatcher(duration), useUnmergedTree = true),
            gap = 0.dp,
            relationship = LayoutRelationship.BottomToTop
        )

        // assert duration bound rect
        assertDurationNodeBoundToContainer(container = container, duration = duration)

        // assert total node bound rect
        assertTotalNodeBoundToContainer(container = container, total = total)
        return container
    }

    // region Hourly rate node bound
    private fun assertHourlyNodeBoundToContainer(
        container: SemanticsNodeInteraction,
        hourRate: String,
    ) {
        assertLayoutGap(
            lhs = container,
            rhs = onNode(hourlyRateSemanticsMatcher(hourRate), useUnmergedTree = true),
            gap = 16.dp,
            relationship = LayoutRelationship.StartToStart
        )
        assertLayoutGap(
            lhs = container,
            rhs = onNode(hourlyRateSemanticsMatcher(hourRate), useUnmergedTree = true),
            gap = 12.dp,
            relationship = LayoutRelationship.TopToTop
        )
    }
    // endregion

    // region Name node bound

    private fun assertNameNodeBoundToContainer(
        container: SemanticsNodeInteraction,
        name: String,
    ) {
        assertLayoutGap(
            lhs = container,
            rhs = onNode(nameSemanticsMatcher(name), useUnmergedTree = true),
            gap = 16.dp,
            relationship = LayoutRelationship.StartToStart
        )
    }
    // endregion

    // region Duration node bound
    private fun assertDurationNodeBoundToContainer(
        container: SemanticsNodeInteraction,
        duration: Int,
    ) {
        assertLayoutGap(
            lhs = container,
            rhs = onNode(durationSemanticsMatcher(duration), useUnmergedTree = true),
            gap = 16.dp,
            relationship = LayoutRelationship.StartToStart
        )
        assertLayoutGap(
            lhs = onNode(durationSemanticsMatcher(duration), useUnmergedTree = true),
            rhs = container,
            gap = 12.dp,
            relationship = LayoutRelationship.BottomToBottom
        )
    }
    // endregion

    // region Total node bound
    private fun assertTotalNodeBoundToContainer(
        container: SemanticsNodeInteraction,
        total: String,
    ) {
        assertLayoutGap(
            lhs = onNode(matcher = totalSemanticsMatcher(value = total), useUnmergedTree = true),
            rhs = container,
            gap = 16.dp,
            relationship = LayoutRelationship.EndToEnd
        )
    }
    // endregion
}
