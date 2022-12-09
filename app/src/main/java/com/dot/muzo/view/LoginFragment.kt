package com.dot.muzo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.dot.muzo.R
import com.dot.muzo.databinding.FragmentLoginBinding
import com.dot.muzo.util.Util.Companion.bottomNavInActive
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {


    private lateinit var fragmentBinding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        val view:View = fragmentBinding.root

        firebaseAuth = FirebaseAuth.getInstance()

        bottomNavInActive(requireActivity())

        if(firebaseAuth.currentUser != null){
            findNavController().navigate(R.id.action_loginFragment_to_feedFragment)
        }

        clickListener()

        return view
    }


    fun clickListener(){
        loginButtonClick()
        signUpClicked()
    }

    fun loginButtonClick(){
        fragmentBinding.loginButton.setOnClickListener {
            signInClicked()
        }
    }



    fun signInClicked(){
        val email = fragmentBinding.emailText.text.toString()
        val password = fragmentBinding.passwordText.text.toString()

        if (email.equals("") || password.equals("")){
            Toast.makeText(requireContext(),"enter email and password!", Toast.LENGTH_LONG).show()
        } else{
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                findNavController().navigate(R.id.action_loginFragment_to_feedFragment)
            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }

    fun signUpClicked(){
        fragmentBinding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


    }


}