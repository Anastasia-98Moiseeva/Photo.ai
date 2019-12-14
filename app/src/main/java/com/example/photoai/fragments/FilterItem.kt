package com.example.photoai.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.photoai.R
import com.example.photoai.router.Router


class FilterItem : Fragment() {

    private lateinit var router : Router

    private var position = 1


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
        return view
    }
}

