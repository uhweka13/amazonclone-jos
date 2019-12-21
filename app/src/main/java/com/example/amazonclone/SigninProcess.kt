package com.example.amazonclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class SigninProcess : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_process)

        val verifiedEmail = intent.getStringExtra("verifyemail")
        val tv_veri_email = findViewById<TextView>(R.id.tv_verified_email)
        tv_veri_email.setText(verifiedEmail)
        val btn_signin_final = findViewById<Button>(R.id.bt_signin_process)
        val tv_reset_password = findViewById<TextView>(R.id.tv_password_reset)

        btn_signin_final.setOnClickListener(View.OnClickListener {
            processUserDetails()
        })

        tv_reset_password.setOnClickListener(View.OnClickListener {
            passWordReset()
        })
    }

    fun processUserDetails(){
        val et_password_signin_password = findViewById<EditText>(R.id.et_password_signin_password)
        val password = et_password_signin_password.text.toString().trim()

        if (password.isEmpty()){

        }else{

            try {
                val email:String = intent.getStringExtra("verifyemail")
                mAuth = FirebaseAuth.getInstance()
                mAuth!!.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener (this){task ->

                        if (task.isSuccessful){

                            //fetch current user id
//                        val userId = mAuth!!.currentUser!!.uid

                            toHomeLogedIn()
                        } else{
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show()
                        }
                    }
            }catch (e:Exception){
                Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
            }


        }
    }

    fun toHomeLogedIn(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    fun passWordReset(){
        val intent = Intent(this, ForgotPass::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
