package com.pj.nsutgdscapp.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val failed: String? = null,
    val incorrect: Int? = null,
    val error: Int? = null
)