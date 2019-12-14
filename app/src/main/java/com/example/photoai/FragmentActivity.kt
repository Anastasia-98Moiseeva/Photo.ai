package com.example.photoai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.photoai.fragments.MainFragment
import com.example.photoai.router.Router


class FragmentActivity : AppCompatActivity() {

    lateinit var router  : Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_layout)

        router = Router(this, R.id.fragment_container)

        if (savedInstanceState == null) router.navigateTo(false, ::MainFragment)
    }
}