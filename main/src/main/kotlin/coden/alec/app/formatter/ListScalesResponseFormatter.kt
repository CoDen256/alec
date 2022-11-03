package coden.alec.app.formatter

import coden.alec.data.Scale

interface ListScalesResponseFormatter {
    fun format(response: List<Scale>): String
}