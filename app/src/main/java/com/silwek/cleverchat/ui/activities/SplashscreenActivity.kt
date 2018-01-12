package com.silwek.cleverchat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.silwek.cleverchat.R
import org.jetbrains.anko.longToast
import java.util.*


class SplashscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null)
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(AUTHPROVIDER)
                            .setTheme(R.style.AppTheme)
                            .build(),
                    RC_SIGN_IN);
        else
            goToMainScreen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                goToMainScreen()
                // ...
            } else {
                response?.let { longToast(it.toString()) }
            }
        }
    }

    fun goToMainScreen() {
        startActivity(Intent(this, ChatRoomsActivity::class.java))
        finish()
    }

    companion object {
        private val RC_SIGN_IN = 123

        private val AUTHPROVIDER = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
        )
    }

}
