package com.dot.muzo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dot.muzo.R
import com.dot.muzo.adapter.FeedRecyclerAdapter
import com.dot.muzo.databinding.FragmentFeedBinding
import com.dot.muzo.model.Post
import com.dot.muzo.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main_fragment.*
import kotlinx.android.synthetic.main.activity_main_fragment.view.*
import kotlinx.android.synthetic.main.fragment_upload.view.*


class FeedFragment : Fragment() {


    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var fragmentBinding:FragmentFeedBinding

    val postArrayList : ArrayList<Post> = arrayListOf()
    var adapter : FeedRecyclerAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBinding = FragmentFeedBinding.inflate(inflater, container, false)
        val view:View = fragmentBinding.root


        Util.bottomNavActive(requireActivity())

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        fragmentBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FeedRecyclerAdapter(postArrayList)
        fragmentBinding.recyclerView.adapter = adapter

        getDataFromFirestore()




        return view
    }






    fun getDataFromFirestore() {

        db.collection("Posts").orderBy("date",
            Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(requireContext(),exception.localizedMessage, Toast.LENGTH_LONG).show()
            } else {

                if (snapshot != null) {
                    if (!snapshot.isEmpty) {

                        postArrayList.clear()

                        val documents = snapshot.documents
                        for (document in documents) {
                            val comment= document.get("comment").toString()
                            val useremail = document.get("email").toString()
                            val downloadUrl = document.get("downloadURL").toString()
                            val userName = document.get("userName").toString()
                            //val timestamp = document.get("date") as Timestamp
                            //val date = timestamp.toDate()

                            val post = Post(useremail,comment,userName,downloadUrl)
                            postArrayList.add(post)
                            println(post.comment)
                        }
                        adapter!!.notifyDataSetChanged()

                    }
                }

            }
        }

    }

}