package com.pj.nsutgdscapp.ui.login

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pj.nsutgdscapp.ListActivity
import com.pj.nsutgdscapp.databinding.ActivityLoginBinding

import com.pj.nsutgdscapp.R
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = findViewById<TextInputEditText>(R.id.email)
        //val email = binding.email
        // val emailfield = binding.emailfield
        val emailfield = findViewById<TextInputLayout>(R.id.emailfield)
        val password = findViewById<TextInputEditText>(R.id.password)
        // val passwordfield = binding.passwordfield
        // val passwordfield = findViewById<TextInputLayout>(R.id.passwordfield)
        val login = binding.login
        val container = binding.container
        val loading = binding.loading

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both email / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                if (emailfield != null) {
                    emailfield.error = getString(loginState.emailError)
                    emailfield.setErrorIconDrawable(0)
                }
            } else {
                emailfield.error = null
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.failed != null) {
                showLoginMessage(loginResult.failed)
            }
            if (loginResult.incorrect != null) {
                showIncorrectMessage()
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            //finish()
        })

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                try {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e:Exception) {
                    Log.d("TAG",e.toString())
                }
                loading.visibility = View.VISIBLE
                loginViewModel.login(email.text.toString(), password.text.toString())
            }
            container.setOnClickListener {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(this,ListActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun showLoginMessage(failedUser: String) {
        val ad = AlertDialog.Builder(this)
        val msg = "$failedUser is not registered!"
        ad.setTitle("ALERT")
        ad.setMessage(msg)
        ad.setNeutralButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = ad.create()
        alert.show()
    }

    private fun showIncorrectMessage() {
        val ad = AlertDialog.Builder(this)
        val msg = "Incorrect Password!"
        ad.setTitle("ALERT")
        ad.setMessage(msg)
        ad.setNeutralButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = ad.create()
        alert.show()

    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}