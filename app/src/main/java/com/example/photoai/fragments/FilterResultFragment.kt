package com.example.photoai.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.photoai.R
import com.example.photoai.router.Router
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import java.io.InputStream


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FilterResultFragment : Fragment() {

    private var url = ""

    private lateinit var router: Router

    private var position = 1
    private var fileUri: Uri? = null

    private lateinit var amazingTextView : TextView
    private lateinit var progressBar : ProgressBar
    private lateinit var photo : ImageView
    private lateinit var filterTextView : TextView
    private lateinit var share : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        position = arguments!!.getInt(intMessage)
        fileUri = Uri.parse(arguments!!.getString(strMessage))

        router = Router(requireActivity(), R.id.fragment_container)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.result_img_layout, container, false)

        filterTextView = view.findViewById(R.id.txt_result_filter)
        filterTextView.setText(resources.getStringArray(R.array.filter_titles)[position])
        amazingTextView = view.findViewById(R.id.text_amazing)
        progressBar = view.findViewById(R.id.progress_bar)
        photo = view.findViewById(R.id.img_result)

        share = view.findViewById(R.id.btn_share)
        share.setOnClickListener {
            shareResult()
        }

        var photoResult = PhotoResult()
        photoResult.execute()

        return view
    }

    fun shareResult() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "plain/text"
        intent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(intent, getString(R.string.share)))
    }

    fun reCreateURL() {
        val urlParts = url.split(getString(R.string.upload_url_part))
        url = urlParts[0] + getString(R.string.upload_url_part) +
                resources.getStringArray(R.array.filters)[position] +
                "/" + urlParts[1]
    }

    fun getResultPhoto(
        progressBar: ProgressBar, filterTextView: TextView,
        amazingTextView: TextView, photo: ImageView, share: Button) {

        Picasso.with(context).invalidate(url)
        Picasso.with(context)
            .load(url)
            .into(photo, object : Callback {
                @SuppressLint("ShowToast")
                override fun onError() {
                    progressBar.setVisibility(View.INVISIBLE)
                    val toast = Toast.makeText(
                        context,
                        resources.getString(R.string.errorLoadingPhoto), Toast.LENGTH_LONG
                    )
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }

                override fun onSuccess() {
                    setVisibility(
                        progressBar, filterTextView,
                        amazingTextView, photo, share
                    )
                }
            })
    }

    fun setVisibility(
        progressBar: ProgressBar, filterTextView: TextView,
        amazingTextView: TextView, photo: ImageView, share: Button
    ) {
        progressBar.setVisibility(View.INVISIBLE)
        filterTextView.setVisibility(View.VISIBLE)
        amazingTextView.setVisibility(View.VISIBLE)
        photo.setVisibility(View.VISIBLE)
        share.isEnabled = true
        share.visibility = View.VISIBLE
    }


    fun InputStream.toFile(file: File) {
        file.outputStream().use { this.copyTo(it) }
    }


    fun genFile(): File {
        val fileInputStream: InputStream = activity?.contentResolver!!.openInputStream(this.fileUri)
        val path = Environment.getExternalStorageDirectory()
        val file = File(path, getString(R.string.new_img_name))
        fileInputStream.toFile(file)
        return file
    }

    fun sendRequest(){
        var res: MutableMap<Any?, Any?>? = null
        val config = HashMap<String, String>()
        config.put(getString(R.string.cloud_name), getString(R.string.cloud_name_value))
        val cloudinary = Cloudinary(config)
        activity?.let {
            val file = genFile()
            res = cloudinary.uploader().unsignedUpload(file,
                getString(R.string.preset_value), ObjectUtils.emptyMap())
            file.delete()
        }
        url = (res?.getValue(getString(R.string.url_key))).toString()
    }


    @SuppressLint("StaticFieldLeak")
    internal inner class PhotoResult : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            sendRequest()
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            reCreateURL()
            getResultPhoto(progressBar, filterTextView, amazingTextView, photo, share)
        }
    }
}