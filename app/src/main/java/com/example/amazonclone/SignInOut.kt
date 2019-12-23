package com.example.amazonclone

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kaopiz.kprogresshud.KProgressHUD


class SignInOut : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase?=null
    private lateinit var hud: KProgressHUD
    private lateinit var database: DatabaseReference
    var status:String = "close"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_out)

        val btn_sign_in = findViewById<Button>(R.id.bt_signin)
        val btn_register_user = findViewById<Button>(R.id.bt_signup)
        val tv_need_help = findViewById<TextView>(R.id.tv_need_help)
        findViewById<LinearLayout>(R.id.ly_need_help).visibility = View.GONE
        findViewById<LinearLayout>(R.id.layout_error).visibility = View.GONE

        //hide and unhide password/////////////////////////////////////////
        val cb_selected = findViewById<CheckBox>(R.id.cb_sign_up)
        val password_input = findViewById<EditText>(R.id.et_passWord_register)
        cb_selected.setOnClickListener(View.OnClickListener {
            if (cb_selected.isChecked) {
                password_input.setTransformationMethod(null)
            } else {
                password_input.setTransformationMethod(PasswordTransformationMethod())
            }
        })
        //......................................................................


        tv_need_help.setOnClickListener(View.OnClickListener {

            if (status == "close"){
                findViewById<LinearLayout>(R.id.ly_need_help).visibility = View.VISIBLE
                status = "open"
            }else if(status == "open"){
                findViewById<LinearLayout>(R.id.ly_need_help).visibility = View.GONE
                status = "close"
            }
        })

        //check for button clicked from previous page
        var keyName: String = intent.getStringExtra("signinKey")
        if (keyName == "in"){
            val b = findViewById<RadioButton>(R.id.rb_create_account2)
            b.isChecked = true
            findViewById<LinearLayout>(R.id.layout_create_account).visibility = View.GONE
            findViewById<LinearLayout>(R.id.layout_signin).visibility = View.VISIBLE
        }else if (keyName == "new"){

            val b = findViewById<RadioButton>(R.id.rb_create_account1)
            b.isChecked = true
            findViewById<LinearLayout>(R.id.layout_create_account).visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.layout_signin).visibility = View.GONE

        }

        //radio button selected switcher
        val rb_create_account = findViewById<RadioGroup>(R.id.rb_create_account)
        rb_create_account.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
//                Toast.makeText(applicationContext," On checked change : ${radio.text}",
//                    Toast.LENGTH_SHORT).show()

                if (radio.text == "Create account. New to Amazon?"){
                    findViewById<LinearLayout>(R.id.layout_create_account).visibility = View.VISIBLE
                    findViewById<LinearLayout>(R.id.layout_signin).visibility = View.GONE
                }else if (radio.text == "Sign-in. Already a customer?"){
                    findViewById<LinearLayout>(R.id.layout_create_account).visibility = View.GONE
                    findViewById<LinearLayout>(R.id.layout_signin).visibility = View.VISIBLE
                }
            })

        btn_sign_in.setOnClickListener(View.OnClickListener {
            signinemailCHeck()
        })

        btn_register_user.setOnClickListener(View.OnClickListener {
            registerUser()
        })
    }

    fun registerUser(){
        val et_user_name_register = findViewById<EditText>(R.id.et_user_register)
        val et_email_register = findViewById<EditText>(R.id.et_email_register)
        val et_passWord_register = findViewById<EditText>(R.id.et_passWord_register)

        val nameFinal = et_user_name_register.text.toString().trim()
        val emailFinal = et_email_register.text.toString().trim()
        val passwordFinal = et_passWord_register.text.toString().trim()


        if (nameFinal.isEmpty()){
            findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_name_error).visibility = View.VISIBLE
            var tv_details = findViewById<TextView>(R.id.tv_name_error)
            tv_details.setPadding(0, 0, 0, 10)
            tv_details.setText("Enter your name")

            val foo = findViewById<EditText>(R.id.et_user_register)
            foo.setBackgroundResource(R.drawable.edittexterror)
        }else{
            findViewById<TextView>(R.id.tv_name_error).visibility = View.GONE
            findViewById<TextView>(R.id.tv_name_error).visibility = View.GONE
            val foo = findViewById<EditText>(R.id.et_user_register)
            foo.setBackgroundResource(R.drawable.signinoutedittext)
        }

        if (emailFinal.isEmpty()){
            findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_email_error).visibility = View.VISIBLE
            var tv_details = findViewById<TextView>(R.id.tv_email_error)
            tv_details.setPadding(0, 0, 0, 10)
            tv_details.setText("Enter your email")

            val foo = findViewById<EditText>(R.id.et_email_register)
            foo.setBackgroundResource(R.drawable.edittexterror)
        }else{
            findViewById<TextView>(R.id.tv_email_error).visibility = View.GONE
            val foo = findViewById<EditText>(R.id.et_email_register)
            foo.setBackgroundResource(R.drawable.signinoutedittext)
        }

        if (passwordFinal.isEmpty()){
            findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_password_error).visibility = View.VISIBLE
            var tv_details = findViewById<TextView>(R.id.tv_password_error)
            tv_details.setPadding(0, 0, 0, 10)
            tv_details.setText("Enter your password")

            val foo = findViewById<EditText>(R.id.et_passWord_register)
            foo.setBackgroundResource(R.drawable.edittexterror)
        }else{
            findViewById<TextView>(R.id.tv_password_error).visibility = View.GONE
            val foo = findViewById<EditText>(R.id.et_passWord_register)
            foo.setBackgroundResource(R.drawable.signinoutedittext)
        }

        if (!nameFinal.isEmpty() && !emailFinal.isEmpty() && !passwordFinal.isEmpty()){
            findViewById<LinearLayout>(R.id.layout_error).visibility = View.GONE
            mAuth = FirebaseAuth.getInstance()
            mDatabase = FirebaseDatabase.getInstance()
            val mUsers: DatabaseReference = mDatabase!!.reference!!.child("Users")

            hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Creating User")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)

            hud.show()
            mAuth!!.createUserWithEmailAndPassword(emailFinal, passwordFinal)
                .addOnCompleteListener(this, OnCompleteListener {task ->
                    hud.dismiss()

                    if (task.isSuccessful){

                        //fetch current user id
                        val userId = mAuth!!.currentUser!!.uid

                        //update user profile information
                        val currentUserDb = mUsers!!.child(userId)
                        currentUserDb.child("name").setValue(nameFinal)
                        currentUserDb.child("email").setValue(emailFinal)
                        currentUserDb.child("uid").setValue(userId)

                        toLogin()

                    }else{
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                    }
                })
        }

    }

    fun toLogin(){
        val i = Intent(this@SignInOut, SignInOut::class.java)
        val signinKey:String = "in"
        //pass the details of the user to the next activity
        i.putExtra("signinKey", signinKey)

        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this@SignInOut.startActivity(i)
    }

    fun signinemailCHeck(){

        val et_email_sign_in = findViewById<EditText>(R.id.et_email_sign_in)
        val emailFinal = et_email_sign_in.text.toString().trim()

        if (emailFinal.isEmpty()){
            findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_email_error).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_password_error).visibility = View.GONE
            findViewById<TextView>(R.id.tv_name_error).visibility = View.GONE
            var tv_details = findViewById<TextView>(R.id.tv_email_error)
            tv_details.setPadding(0, 0, 0, 40)
            tv_details.setText("Enter your email or mobile phone number")

            val foo = findViewById<EditText>(R.id.et_email_sign_in)
            foo.setBackgroundResource(R.drawable.edittexterror)
        }else if(!emailFinal.contains("@")|| !emailFinal.contains(".")){
            findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_email_error).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_password_error).visibility = View.GONE
            var tv_details = findViewById<TextView>(R.id.tv_email_error)
            tv_details.setPadding(0, 0, 0, 40)
            tv_details.setText("Invalid email")

            val foo = findViewById<EditText>(R.id.et_email_sign_in)
            foo.setBackgroundResource(R.drawable.edittexterror)
        }else{
            findViewById<TextView>(R.id.tv_email_error).visibility = View.GONE
            val foo = findViewById<EditText>(R.id.et_email_sign_in)
            foo.setBackgroundResource(R.drawable.signinoutedittext)
        }


//        else{
//            findViewById<TextView>(R.id.tv_name_error).visibility = View.GONE
//            val foo = findViewById<EditText>(R.id.et_email_sign_in)
//            foo.setBackgroundResource(R.drawable.signinoutedittext)
//        }

        if(emailFinal.contains("@") && emailFinal.contains(".") && !emailFinal.isEmpty()){
            findViewById<LinearLayout>(R.id.layout_error).visibility = View.GONE

            mAuth = FirebaseAuth.getInstance()

            database = FirebaseDatabase.getInstance().getReference("Users")


            mAuth!!.fetchSignInMethodsForEmail(emailFinal)
                .addOnCompleteListener(OnCompleteListener<SignInMethodQueryResult?> { task ->
                    if (task.isSuccessful) {
                        val result = task.result
                        val signInMethods =
                            result!!.signInMethods
                        if (signInMethods!!.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                            // User can sign in with email/password
                            val i = Intent(this@SignInOut, SigninProcess::class.java)

                            //pass the details of the user to the next activity
                            i.putExtra("verifyemail", emailFinal)

                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            this@SignInOut.startActivity(i)

                            Toast.makeText(this, "size 1", Toast.LENGTH_LONG).show()
                        } else {
                            findViewById<LinearLayout>(R.id.layout_error).visibility = View.VISIBLE
                            findViewById<TextView>(R.id.tv_email_error).visibility = View.VISIBLE
                            findViewById<TextView>(R.id.tv_password_error).visibility = View.GONE
                            var tv_details = findViewById<TextView>(R.id.tv_email_error)
                            tv_details.setPadding(0, 0, 0, 40)
                            tv_details.setText("We cannot find an account with that email address")

                            val foo = findViewById<EditText>(R.id.et_email_sign_in)
                            foo.setBackgroundResource(R.drawable.edittexterror)

                        }
                    } else {
                        Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    fun registerUserEmail(){

    }
}
