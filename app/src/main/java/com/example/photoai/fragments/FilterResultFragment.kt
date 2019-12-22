package com.example.photoai.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast


import androidx.fragment.app.Fragment
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.photoai.R
import com.example.photoai.router.Router
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import java.io.InputStream
import java.util.concurrent.CyclicBarrier


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FilterResultFragment : Fragment() {

    private var url = ""

    val barrier: CyclicBarrier? = CyclicBarrier(2)

    private lateinit var router : Router

    private var position = 1
    private var fileUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        position = arguments!!.getInt(intMessage)
        fileUri = Uri.parse(arguments!!.getString(strMessage))

        router = Router(requireActivity(), R.id.fragment_container)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.result_img_layout, container, false)

        val filterTextView = view.findViewById<TextView>(R.id.txt_result_filter)
        filterTextView.setText(resources.getStringArray(R.array.filter_titles)[position])
        val amazingTextView = view.findViewById<TextView>(R.id.text_amazing)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val photo = view.findViewById<ImageView>(R.id.img_result)

        sendRequest()

        barrier?.await()
        reCreateURL()
        getResultPhoto(progressBar, filterTextView, amazingTextView, photo)

        return view
    }

    fun reCreateURL(){
        val urlParts = url.split(getString(R.string.upload_url_part))
        url = urlParts[0] + getString(R.string.upload_url_part) +
                resources.getStringArray(R.array.filters)[position] +
                "/" + urlParts[1]
    }

    fun getResultPhoto(progressBar: ProgressBar, filterTextView: TextView,
                       amazingTextView: TextView, photo: ImageView) {

        Picasso.with(context).invalidate(url)
        Picasso.with(context)
            .load(url)
            .into(photo, object : Callback {
                @SuppressLint("ShowToast")
                override fun onError() {
                    progressBar.setVisibility(View.INVISIBLE)
                    val toast = Toast.makeText(context,
                        resources.getString(R.string.errorLoadingPhoto), Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }

                override fun onSuccess() {
                    setVisibility(
                        progressBar, filterTextView,
                        amazingTextView, photo
                    )
                }
            })
    }

    fun setVisibility(progressBar: ProgressBar, filterTextView: TextView,
                      amazingTextView: TextView, photo: ImageView){
        progressBar.setVisibility(View.INVISIBLE)
        filterTextView.setVisibility(View.VISIBLE)
        amazingTextView.setVisibility(View.VISIBLE)
        photo.setVisibility(View.VISIBLE)
    }


    fun InputStream.toFile(file: File) {
        file.outputStream().use { this.copyTo(it) }
    }

    fun genFile(): File {
        val fileInputStream : InputStream = activity?.contentResolver!!.openInputStream(this.fileUri)
        val path = Environment.getExternalStorageDirectory()
        val file = File(path, getString(R.string.new_img_name))
        fileInputStream.toFile(file)
        return file
    }

    fun sendRequest() {
        var res : MutableMap<Any?, Any?>? = null
        DoAsync {
            val config = HashMap<String, String>()
            config.put(getString(R.string.cloud_name), getString(R.string.cloud_name_value))
            val cloudinary = Cloudinary(config)
            activity?.let {
                res = cloudinary.uploader().unsignedUpload(genFile(),
                    getString(R.string.preset_value), ObjectUtils.emptyMap())
            }
            url = (res?.getValue(getString(R.string.url_key))).toString()
            barrier?.await()
        }.execute()
    }

    class DoAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }
}