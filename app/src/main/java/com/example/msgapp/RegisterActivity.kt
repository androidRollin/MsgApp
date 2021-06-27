package com.example.msgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var register_btn: Button
    private lateinit var email_register: TextView
    private lateinit var username_register: TextView
    private lateinit var password_register: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference

    private var firebaseUserID: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        toolbar = findViewById(R.id.toolbar_register)
        register_btn = findViewById(R.id.register_btn)
        email_register = findViewById(R.id.email_register)
        username_register = findViewById(R.id.username_register)
        password_register = findViewById(R.id.password_register)


        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            val intent = Intent (this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            registerUser()

        }


    }

    private fun registerUser() {

        val username: String = username_register.text.toString()
        val email: String = email_register.text.toString()
        val password: String = password_register.text.toString()

        if(username == "")
        {
            Toast.makeText(this@RegisterActivity, "Please write a username", Toast.LENGTH_LONG).show()
        }
        else if (email == "")
        {
            Toast.makeText(this@RegisterActivity, "Please write an email", Toast.LENGTH_LONG).show()
        }
        else if ( password == "")
        {
            Toast.makeText(this@RegisterActivity, "Please write a password", Toast.LENGTH_LONG).show()
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if ( task.isSuccessful)
                    {
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                        val userHashMap =  HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["username"] = username
                        userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/msgapp-c110c.appspot.com/o/profile_placeholder.png?alt=media&token=fb5c18fb-33fa-4bfc-be8d-343b88fdbd8e"
                        userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/msgapp-c110c.appspot.com/o/cover_placeholder.jpg?alt=media&token=a782b1dc-38e5-44d2-b222-79d07fe4fe19"
                        userHashMap["status"] = "offline"
                        userHashMap["search"] = username.lowercase(Locale.getDefault())
                        userHashMap["facebook"] = "https://m.facebook.com"
                        userHashMap["instagram"] = "https://m.instagram.com"
                        userHashMap["website"] = "https://www.gogogle.com"

                        refUsers.updateChildren(userHashMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful)
                                {
                                    val intent = Intent (this@RegisterActivity, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                    }
                    else
                    {
                        Toast.makeText(this@RegisterActivity, "Error Message: " + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }

        }
    }


}