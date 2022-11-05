package coden.alec.app.util

fun String.sub(vararg vars: String ): String{
    var result = this
    vars.forEach {
        result = result.replaceFirst("%s", it)
    }
    return result
}