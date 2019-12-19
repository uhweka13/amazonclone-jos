package com.example.amazonclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bt_signin = findViewById<Button>(R.id.bt_signIn)
        val bt_signup = findViewById<Button>(R.id.bt_signup)

        bt_signin.setOnClickListener(View.OnClickListener {

            val i = Intent(this@MainActivity, SignInOut::class.java)
            val signinKey:String = "in"
            //pass the details of the user to the next activity
            i.putExtra("signinKey", signinKey)

            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this@MainActivity.startActivity(i)
        })

        bt_signup.setOnClickListener(View.OnClickListener {
            val i = Intent(this@MainActivity, SignInOut::class.java)
            val signinKey:String = "new"
            //pass the details of the user to the next activity
            i.putExtra("signinKey", signinKey)

            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this@MainActivity.startActivity(i)
        })
    }
}
