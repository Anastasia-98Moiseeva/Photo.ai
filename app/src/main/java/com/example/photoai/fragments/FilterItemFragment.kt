package com.example.photoai.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.cloudinary.Cloudinary
import com.example.photoai.AppConstants
import com.example.photoai.R
import com.example.photoai.router.Router
import com.squareup.picasso.Picasso
import com.cloudinary.utils.ObjectUtils


class FilterItemFragment : Fragment() {

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
            val res = sendRequest()
            router.navigateTo(true, ::FilterResultFragment,
                changeStack = 1, transportedMessage = position.toString() + "***" + res)
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


    fun loadPhoto(context: Context?, fileUri : Uri?, photo : ImageView){
        Picasso
            .with(context)
            .setLoggingEnabled(true)

        Picasso
            .with(context)
            .load(fileUri)
            .into(photo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val photo = view?.findViewById<ImageView>(R.id.img_gradient)!!
        val plusImg = view?.findViewById<ImageButton>(R.id.img_btn_plus)!!
        val cardView = view?.findViewById<CardView>(R.id.beautifulButton)!!

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.PICK_PHOTO_REQUEST) {
                fileUri = data?.data
            }
            photo.setBackgroundColor(Color.TRANSPARENT)
            plusImg.setAlpha(0f)
            cardView.cardElevation = 0f
            cardView.setCardBackgroundColor(resources.getColor(R.color.background))
            cardView.background.alpha = 0
            loadPhoto(context, fileUri, photo)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun sendRequest() : Any? {
        var res : MutableMap<Any?, Any?>? = null
        doAsync {
            val config = HashMap<String, String>()
            config.put("cloud_name", "dbovyb11z")
            val cloudinary = Cloudinary(config)
            res = cloudinary.uploader().unsignedUpload(
                "https://indicator.ru/thumb/640x0/filters:quality(75)/imgs/2019/08/05/10/3489638/19e9fc77f9acba1ed7d73ba45a8776abb4903566.jpg",
                "unsignedpreset", ObjectUtils.emptyMap())
        }.execute()
        return res?.get("url")
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }
}

