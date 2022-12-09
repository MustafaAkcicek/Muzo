package com.dot.muzo.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.dot.muzo.R
import com.dot.muzo.databinding.FragmentUploadBinding
import com.dot.muzo.util.Util
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*


class UploadFragment : Fragment() {


    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var fragmentBinding: FragmentUploadBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentUploadBinding.inflate(inflater, container, false)
        val view: View = fragmentBinding.root

        Util.bottomNavActive(requireActivity())
        registerLaunchers()
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        clickListener()

        return view
    }

    fun clickListener() {
        selectImageViewClick()
        uploadClick()
    }

    fun selectImageViewClick() {
        fragmentBinding.imageViewSelect.setOnClickListener {
            selectImage()
        }
    }

    fun uploadClick() {
        fragmentBinding.uploadButton.setOnClickListener {
            upload()
        }
    }

    fun upload() {

        fragmentBinding.loadingAnimation.visibility = View.VISIBLE

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        if (selectedPicture != null) {
        }
        imageReference.putFile(selectedPicture!!).addOnSuccessListener {
            //download url -> firestore
            val uploadPictureReference = storage.reference.child("images").child(imageName)
            uploadPictureReference.downloadUrl.addOnSuccessListener {
                val downloadUrl = it.toString()

                if (auth.currentUser != null) {
                    val postMap = hashMapOf<String, Any>()
                    postMap.put("downloadURL", downloadUrl)
                    postMap.put("userName",auth.currentUser!!.displayName!!)
                    postMap.put("userEmail", auth.currentUser!!.email!!)
                    postMap.put("comment", fragmentBinding.commentText.text.toString())
                    postMap.put("date", com.google.firebase.Timestamp.now())

                    firestore.collection("Posts").add(postMap).addOnSuccessListener {
                        fragmentBinding.loadingAnimation.visibility = View.GONE
                        findNavController().navigate(R.id.action_uploadFragment_to_feedFragment)

                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                }


            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }


    fun selectImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(
                    requireView(),
                    "Permission needed for Galery",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Give Permission") {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            } else {
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intetToGalery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intetToGalery)
        }
    }


    private fun registerLaunchers() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        selectedPicture?.let {
                            fragmentBinding.imageViewSelect.setImageURI(it)
                        }
                    }
                }

            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //permission granted
                    val intetToGalery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intetToGalery)
                } else {
                    //permission denied
                    Toast.makeText(
                        requireContext(),
                        "Permission Needed Haşmetli",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


    }
}