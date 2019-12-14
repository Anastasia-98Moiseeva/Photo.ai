package com.example.photoai.fragments

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.photoai.AppConstants
import com.example.photoai.R
import com.example.photoai.router.Router


class FilterItem : Fragment() {

    private lateinit var router : Router

    private var position = 1

    private var fileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        position = this.arguments!!.getInt(message)
        router = Router(requireActivity(), R.id.fragment_container)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.filter_item_layout, container, false)
        var photo = view?.findViewById<ImageView>(R.id.img_gradient)!!
        val plusButton = view.findViewById<ImageButton>(R.id.img_btn_plus)

        val newPhotoButton = view.findViewById<Button>(R.id.btn_take_photo)
        newPhotoButton.setOnClickListener {
            makeNewPhoto()
        }

        val chosePhotoButton = view.findViewById<Button>(R.id.btn_photo)
        chosePhotoButton.setOnClickListener{
            pickPhotoFromGallery()
        }

        val resultButton = view.findViewById<Button>(R.id.btn_result)
        resultButton.setOnClickListener {
            //sendRequest()
        }
        return view
    }

    private fun makeNewPhoto() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = getActivity()!!.getContentResolver()
            .insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(getActivity()!!.getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, AppConstants.TAKE_PHOTO_REQUEST)
        }
    }

    private fun pickPhotoFromGallery() {
        val pickImageIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(pickImageIntent, AppConstants.PICK_PHOTO_REQUEST)
    }
}

