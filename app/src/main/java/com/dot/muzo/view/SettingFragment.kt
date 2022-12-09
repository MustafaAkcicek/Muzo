package com.dot.muzo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dot.muzo.R
import com.dot.muzo.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {

    private lateinit var fragmentBinding:FragmentSettingBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentBinding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = fragmentBinding.root

        firebaseAuth = FirebaseAuth.getInstance()

        clickListener()

        return view
    }

    fun clickListener(){
        signOutButton()
    }

    fun signOutButton(){
        fragmentBinding.signOutButton.setOnClickListener {
            if (firebaseAuth.currentUser !=null){
                firebaseAuth.signOut()
                findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
            }
        }
    }


}