package com.dot.muzo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.dot.muzo.R
import com.dot.muzo.databinding.FragmentRegisterBinding
import com.dot.muzo.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest

class RegisterFragment : Fragment() {

    private lateinit var fragmentBinding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view:View = fragmentBinding.root



        firebaseAuth = FirebaseAuth.getInstance()



        Util.bottomNavInActive(requireActivity())

        registerbuttonClick()

        return view
    }

    fun registerbuttonClick(){
        fragmentBinding.kayTOl.setOnClickListener {
            kaydol()
        }
    }

    fun kaydol(){
        val email = fragmentBinding.emailText.text.toString()
        val userName =fragmentBinding.userNameText.text.toString()
        val password = fragmentBinding.passwordText.text.toString()
        if (email.equals("") || password.equals("") || userName.equals("")){
            Toast.makeText(requireContext(),"İstenilen Verileri Lütfen Girin Sayın Haşmetlim!", Toast.LENGTH_LONG).show()
        } else{
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                firebaseAuth.currentUser!!.updateProfile(userProfileChangeRequest {
                    setDisplayName(userName)
                })
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }


}