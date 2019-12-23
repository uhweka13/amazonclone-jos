package com.example.amazonclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
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
        findViewById<LinearLayout>(R.id.layout_error).visibility = View.GONE


        val cb_selected = findViewById<CheckBox>(R.id.cd_password_login)
        val password_input = findViewById<EditText>(R.id.et_password_signin_password)
        cb_selected.setOnClickListener(View.OnClickListener {
            if (cb_selected.isChecked) {
                password_input.setTransformationMethod(null)
            } else {
                password_input.setTransformationMethod(PasswordTransformationMethod())
            }
        })

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

            findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_password_error).visibility = View.VISIBLE
            var tv_details = findViewById<TextView>(R.id.tv_password_error)
            tv_details.setPadding(0, 0, 0, 10)
            tv_details.setText("Enter your password")

            val foo = findViewById<EditText>(R.id.et_password_signin_password)
            foo.setBackgroundResource(R.drawable.edittexterror)

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

                            findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
                            findViewById<TextView>(R.id.tv_password_error).visibility = View.VISIBLE
                            var tv_details = findViewById<TextView>(R.id.tv_password_error)
                            tv_details.setPadding(0, 0, 0, 10)
                            tv_details.setText("Your password is incorrect")

                            val foo = findViewById<EditText>(R.id.et_password_signin_password)
                            foo.setBackgroundResource(R.drawable.edittexterror)

                        }
                    }
            }catch (e:Exception){
                findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_password_error).visibility = View.VISIBLE
                var tv_details = findViewById<TextView>(R.id.tv_password_error)
                tv_details.setPadding(0, 0, 0, 10)
                tv_details.setText("Authentication failed")

                val foo = findViewById<EditText>(R.id.et_password_signin_password)
                foo.setBackgroundResource(R.drawable.edittexterror)
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