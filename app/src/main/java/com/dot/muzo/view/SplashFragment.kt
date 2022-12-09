package com.dot.muzo.view

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dot.muzo.R
import com.dot.muzo.databinding.FragmentSplashBinding
import com.dot.muzo.util.Util


class SplashFragment : Fragment() {


    private lateinit var fragmentBinding: FragmentSplashBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentSplashBinding.inflate(inflater, container, false)
        val view: View = fragmentBinding.root

        Util.bottomNavInActive(requireActivity())

        Handler().postDelayed({

            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

        },2000)

        return view
    }
}