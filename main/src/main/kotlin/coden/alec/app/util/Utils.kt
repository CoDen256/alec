package coden.alec.app.util

fun String.s(vararg vars: String ): String{
    var result = this
    vars.forEach {
        result = result.replaceFirst("%s", it)
    }
    return result
}