package com.example.photoai


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler


class MainActivity : AppCompatActivity() {

    private val LOADING_TIME = 2000

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, FragmentActivity::class.java)
            startActivity(intent)
            finish()
        }, LOADING_TIME.toLong())
    }
}
