package com.jkytay.xero.compose.robot

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.unit.Dp
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Custom matcher for role
fun hasRole(role: Role) = SemanticsMatcher("Searches for role: $role") {
    it.config.getOrNull(SemanticsProperties.Role) == role
}

// TODO replace to SemanticsProperties IsTraversalGroup
fun isContainer() = SemanticsMatcher("Search for isContainer") {
    it.config.getOrNull(SemanticsProperties.IsContainer) == true
}

fun assertLayoutGap(
    lhs: SemanticsNodeInteraction,
    rhs: SemanticsNodeInteraction,
    gap: Dp,
    relationship: LayoutRelationship
) {
    val lhsBound = lhs.getBoundsInRoot()
    val rhsBound = rhs.getBoundsInRoot()

    val diff = when (relationship) {
        LayoutRelationship.TopToTop -> rhsBound.top - lhsBound.top
        LayoutRelationship.TopToBottom -> rhsBound.top - lhsBound.bottom

        LayoutRelationship.StartToStart -> rhsBound.left - lhsBound.left
        LayoutRelationship.StartToEnd -> rhsBound.left - lhsBound.right

        LayoutRelationship.EndToStart -> rhsBound.right - lhsBound.left
        LayoutRelationship.EndToEnd -> rhsBound.right - lhsBound.right

        LayoutRelationship.BottomToTop -> rhsBound.top - lhsBound.bottom
        LayoutRelationship.BottomToBottom -> rhsBound.bottom - lhsBound.bottom
    }

    assertEquals(
        expected = gap,
        actual = diff,
        message = "$relationship, lhs: [$lhsBound], rhs: [$rhsBound]"
    )
}

enum class LayoutRelationship {
    TopToTop,
    TopToBottom,

    StartToStart,
    StartToEnd,

    EndToStart,
    EndToEnd,

    BottomToTop,
    BottomToBottom,
}
