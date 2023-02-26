package com.cornellappdev.uplift.util

/**
 * Returns a flavor message corresponding to the numeric wait time input.
 * Requires: [waitTime] is between [0..100].
 */
fun waitTimeFlavorText(waitTime: Int): String {
    if (waitTime < 25) return "Usually not busy"
    else if (waitTime < 50) return "Usually not too busy"
    else if (waitTime < 75) return "Usually pretty busy"
    return "Usually very busy"
}