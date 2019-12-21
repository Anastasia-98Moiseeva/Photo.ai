package com.example.photoai

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.photoai.fragments.FilterListFragment
import com.example.photoai.router.Router


class FragmentActivity : AppCompatActivity() {

    lateinit var router  : Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_layout)

        createToolBar()

        router = Router(this, R.id.fragment_container)

        if (savedInstanceState == null) router.navigateTo(false, ::FilterListFragment)
    }

    fun createToolBar(){
        val textView = findViewById(R.id.txt_photo_ai) as TextView

        val paint = textView.getPaint()
        val width = paint.measureText(R.string.app_name.toString())

        val textShader = LinearGradient(
            0f, 0f, width, textView.getTextSize(),
            intArrayOf(
                getResources().getColor(R.color.startGradientColor),
                getResources().getColor(R.color.finishGradientColor)
            ), null, Shader.TileMode.CLAMP
        )
        textView.getPaint().setShader(textShader)
    }
}