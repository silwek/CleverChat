package com.silwek.cleverchat.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import com.silwek.cleverchat.R
import com.silwek.cleverchat.getDatabaseFactory
import com.silwek.cleverchat.getLandingActivityIntent
import kotlinx.android.synthetic.main.fragment_account.*


/**
 * A placeholder fragment containing a simple view.
 */
class AccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onResume() {
        super.onResume()
        val user = getDatabaseFactory().getUserDatabase()?.getCurrentUser()
        user?.let {
            val authUser = user.authObject as FirebaseUser
            with(user) {
                fieldDisplayName.text = name ?: ""
                fieldEmail.text = authUser.email ?: ""
            }
        }
        btSignOut.setOnClickListener {
            context?.let { it1 ->
                AuthUI.getInstance()
                        .signOut(it1)
                        .addOnCompleteListener {
                            getLandingActivityIntent()?.let { it2 ->
                                startActivity(it2)
                                activity?.finish()
                            }
                        }
            }
        }
    }
}
