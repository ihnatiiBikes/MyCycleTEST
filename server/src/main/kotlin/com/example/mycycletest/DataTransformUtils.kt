package com.example.mycycletest

object DataTransformUtils {
    fun transformEircode(eircodeStr: String): String {
        return eircodeStr.replace(Regex("\\s+"), "").uppercase()
    }

    fun transformPhoneNumber(phoneStr: String): String {
        val replacedPlus = phoneStr.replace("+", "00")
        return replacedPlus.filter { it.isDigit() }
    }
}
