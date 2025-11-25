package com.team.eatcleanapp.domain.utils

object EmailValidator {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

    fun isValid(email: String): Boolean {
        return email.matches(emailRegex)
    }
}
