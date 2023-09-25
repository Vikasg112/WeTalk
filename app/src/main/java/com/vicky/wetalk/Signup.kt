package com.vicky.wetalk

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Signup : AppCompatActivity() {


    private lateinit var edtname: EditText
    private lateinit var edtemail: EditText
    private lateinit var edtpwd: EditText
    private lateinit var signupbtn: Button
    private lateinit var profiledp: ImageView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedimage: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        edtname = findViewById(R.id.edt__name)
        edtemail = findViewById(R.id.edt__email)
        edtpwd = findViewById(R.id.edt__pwd)
        signupbtn = findViewById(R.id.signupbtn)
        profiledp = findViewById(R.id.profile_dp)

        mAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        signupbtn.setOnClickListener {
            val name = edtname.text.toString()
            val email = edtemail.text.toString()
            val pwd = edtpwd.text.toString()

            if (name.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this@Signup, "Please Enter Details", Toast.LENGTH_SHORT).show()
            } else {
                signup(name, email, pwd)

            }
        }

        profiledp.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 123)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (data != null) {
                if (data.data != null) {
                    selectedimage = data.data!!
                    profiledp.setImageURI(selectedimage)
                }
            }
        }
    }


    private fun signup(name: String, email: String, pwd: String) {
        //logic of creating user

        mAuth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for jumping to home

                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@Signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@Signup, "Some error occured", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(user(name, email, uid))

        storage.reference.child("profile").child(uid).putFile(selectedimage)

    }



}