package com.example.photograhdeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 120
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    var loginButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById<Button>(R.id.login_loginBtn)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("197047722250-mghb5ftgrumtggus4ae46u2dbvcvn4eg.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()

        loginButton!!.setOnClickListener {
            signIn()

        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        googleSignInClient.revokeAccess()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    var user = mAuth.currentUser
                    val userRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("User").child("Auth").child(user!!.uid).child("profile")
                    val uidRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("User").child("Auth").child(user!!.uid).child("uid")
                    val nameRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("User").child("Auth").child(user!!.uid).child("name")
                    uidRef.setValue(user.uid)
                    nameRef.setValue(user.displayName.toString())
                    userRef.setValue(user.photoUrl.toString())
                    val intent = Intent(this, BottomActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)

                }
            }
    }
    }
