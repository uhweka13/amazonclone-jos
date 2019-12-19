package com.example.amazonclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class SignInOut : AppCompatActivity() {

    var status:String = "close"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_out)

        val tv_need_help = findViewById<TextView>(R.id.tv_need_help)
        findViewById<LinearLayout>(R.id.ly_need_help).visibility = View.GONE

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
    }
}
