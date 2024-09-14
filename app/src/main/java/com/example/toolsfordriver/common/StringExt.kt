package com.example.toolsfordriver.common

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"
private const val DOT_OR_COMMA_PATTERN = "[,.]"

fun String.isValidEmail(): Boolean =
    this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean =
    this.isNotBlank() && this.length >= MIN_PASS_LENGTH
            && Pattern.compile(PASS_PATTERN).matcher(this).matches()

fun String.passwordMatches(repeated: String): Boolean =
    this == repeated