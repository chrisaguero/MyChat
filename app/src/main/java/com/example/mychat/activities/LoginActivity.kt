package com.example.mychat.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mychat.R
import com.example.mychat.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
//import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    //Definimos un objeto para acceder a la autenticación de Firebase
    private lateinit var auth : FirebaseAuth

    //Definimos un objeto para acceder a los elementos de la pantalla xml
    private lateinit var binding: ActivityLoginBinding

    //private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicia autenticacion
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        //definir eventos onclick
        binding.loginButton.setOnClickListener { loginUser() }
        binding.createButton.setOnClickListener { createUser() }


        //valida si hay usuario autenticado cuando se presente el app en pantalla
        checkUser()
    }

    private fun checkUser(){
        val currentUser = auth.currentUser

        if(currentUser != null){ //Si hay un usuario entonces paso a pantalla de actividad de chats
            val intent = Intent(this, ListOfChatsActivity::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)

            finish()//cierra actividad actual
        }
    }

    private fun createUser(){
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        Log.d("Registrándose","Haciendo llamado a creación")
        //Utilizo el objeto auth para hacer el registro...
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                Log.d("Registrándose","se registró")
                checkUser() //valida si hay usuario
            } else { //si hubo hubo error...
                task.exception?.let {
                    Log.e("Registrándose","Error de registró")
                    println(task.exception.toString())
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun loginUser(){
        //Recupero la información que el usuario escribió en el App
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        Log.d("Autenticandose","Haciendo llamado de autenticación")
        //Utilizo el objeto auth para hacer el registro...
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){ //usuario pudo autenticarse
                Log.d("Autenticando","se autenticó")
                checkUser()
            } else {
                task.exception?.let {
                    Log.e("Autenticando","Error de Autenticación")
                    println(task.exception.toString())
                    Toast.makeText(baseContext,"Fallo", Toast.LENGTH_LONG).show()
                }
            }
            Log.d("Autenticando","Sale del proceso...")

        }
    }
}