package com.example.photoai.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photoai.R
import com.example.photoai.router.Router
import com.example.test.adapters.ListAdapter

val message = "mes"

class MainFragment : Fragment() {

    val arrayOfImages : Array<Int> = arrayOf(
        R.drawable.logo,
        R.drawable.logo,
        R.drawable.logo)

    private lateinit var router : Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router = Router(requireActivity(), R.id.fragment_container)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.fragment_list, container, false)
        val buttons : RecyclerView = layout.run { findViewById(R.id.recycler_list) }

        buttons.layoutManager = LinearLayoutManager(
            inflater.context, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?

        buttons.adapter =
            ListAdapter(createElements(), arrayOfImages, ::onElementClick)

        return layout
    }

    private fun createElements() : Array<String> {
        return resources.getStringArray(R.array.filters)
    }

    private fun onElementClick(position : Int){
    }
}
