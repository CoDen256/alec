package coden.alec.app.util

import org.apache.commons.text.StringSubstitutor


fun String.sub(vararg vars: String ): String{
    var result = this
    vars.forEach {
        result = result.replaceFirst("%s", it)
    }
    return result
}

fun String.inline(vararg vars: Pair<String, String?>): String{
    return StringSubstitutor.replace(this, vars.toMap(), "{", "}")
}