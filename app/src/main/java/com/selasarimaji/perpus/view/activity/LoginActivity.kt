package com.selasarimaji.perpus.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_login.*
import android.view.inputmethod.InputMethodManager
import com.selasarimaji.perpus.R


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (checkLoginStatus()) return

        passwordInputLayout.editText?.setOnEditorActionListener { _, i, _ ->

            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                    this.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)

            if (i == EditorInfo.IME_ACTION_SEND){
                loginClicked()
                true
            }else
            false
        }

        loginButton.setOnClickListener {
            loginClicked()
        }
    }

    private fun checkLoginStatus() : Boolean{
        if (FirebaseAuth.getInstance().currentUser == null){
            return false
        }
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
        return true
    }

    private fun loginClicked(){
        emailInputLayout.error = ""
        passwordInputLayout.error = ""
        if (isEmailValid(emailInputLayout.editText?.text.toString()) && passwordInputLayout.editText?.text!!.isNotEmpty()){
            tryLogin(emailInputLayout.editText?.text.toString(),
                    passwordInputLayout.editText?.text.toString())
        }else if (passwordInputLayout.editText?.text!!.isEmpty()) {
            passwordInputLayout.error = "Password harus diisi"
        }
    }

    private fun isEmailValid(email: String) : Boolean {
        return if (email.contains(Regex(".+@.+\\..+"))) {
            true
        }else{
            emailInputLayout.error = "Email tidak valid"
            false
        }
    }

    private fun tryLogin(email: String, password: String){
        with(FirebaseAuth.getInstance()) {
            progressBar.visibility = View.VISIBLE
            this.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        progressBar.visibility = View.GONE
                        when {
                            task.isSuccessful -> {
                                finish()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            }
                            else -> {
                                Snackbar.make(loginButton, "Email / Password salah, coba kembali", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
        }
    }

}
