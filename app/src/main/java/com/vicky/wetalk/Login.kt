package com.vicky.wetalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtemail:EditText
    private lateinit var edtpwd:EditText
    private lateinit var loginbtn:Button
    private lateinit var signupbtn: Button

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        edtemail=findViewById(R.id.edt_email)
        edtpwd=findViewById(R.id.edt_pwd)
        loginbtn=findViewById(R.id.loginbtn)
        signupbtn=findViewById(R.id.signupbtn)
        mAuth= FirebaseAuth.getInstance()

        signupbtn.setOnClickListener{
            val intent= Intent(this,Signup::class.java)
            startActivity(intent)
        }


        loginbtn.setOnClickListener{
            val email=edtemail.text.toString()
            val pwd=edtpwd.text.toString()

            if(email.isEmpty()||pwd.isEmpty()){
                Toast.makeText(this@Login, "Please Enter Details", Toast.LENGTH_SHORT).show()
            }
            else{
            login(email,pwd)}
        }

    }

    private fun login(email:String,pwd:String){
        //logic for verifying user

        mAuth.signInWithEmailAndPassword(email,pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent=Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@Login,"User doesn't exists",Toast.LENGTH_SHORT).show()
                }
            }

    }




}