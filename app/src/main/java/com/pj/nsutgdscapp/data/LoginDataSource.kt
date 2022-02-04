package com.pj.nsutgdscapp.data

import com.pj.nsutgdscapp.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val mainuser = LoggedInUser(username, "Admin")
            // if (username=="")
            return if (username=="test@admin.com" && password=="12345678") {
                Result.Success(mainuser)
            } else if (username=="test@admin.com" && password!="12345678"){
                Result.Incorrect(mainuser)
            } else {
                Result.Failure(mainuser)
            }

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}