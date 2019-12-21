package com.example.amazonclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPass : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        val btn_passWord_reset = findViewById<Button>(R.id.bt_send_reset_mail)

        btn_passWord_reset.setOnClickListener(View.OnClickListener {
            passWordResetMail()
        })
    }

    fun passWordResetMail(){
        val et_reset_email = findViewById<EditText>(R.id.et_password_reset_password)
        val emailFinal = et_reset_email.text.toString().trim()

        if (emailFinal.isEmpty()){

        }else{

            mAuth = FirebaseAuth.getInstance()
            mAuth!!.sendPasswordResetEmail(emailFinal)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@ForgotPass, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ForgotPass, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
