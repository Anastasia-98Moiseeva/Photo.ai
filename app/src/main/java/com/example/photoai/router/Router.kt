package com.example.photoai.router

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.photoai.R
import java.lang.ref.WeakReference

class Router(activity : FragmentActivity, container: Int) {
    private var num_stack = 0
    private val weakActivity = WeakReference(activity)
    private val fragmentContainer = container

    fun navigateTo(addToBack : Boolean = true, fragmentNew: () -> Fragment,
                   needAnimation : Boolean = false, changeStack : Int = 0,
                   intMessage: Int? = null, strMessage: String? = null) {

        val activity = weakActivity.get()

        activity?.run {
            if (changeStack != 0) {
                var change = changeStack
                while (change > 0){
                    supportFragmentManager.popBackStack()
                    change--
                }
            }

            val fragment = fragmentNew()
            val args = Bundle()
            if (intMessage != null) {
                args.putInt(com.example.photoai.fragments.intMessage, intMessage)
            }
            if (strMessage != null) {
                args.putString(com.example.photoai.fragments.strMessage, strMessage)
            }
            fragment.arguments = args

            val transaction = supportFragmentManager.beginTransaction()
            if (needAnimation) {
                transaction.setCustomAnimations(
                    R.anim.finish_animation,
                    R.anim.start_animation
                )
            }
            transaction.replace(fragmentContainer, fragment)
            if (addToBack) {
                transaction.addToBackStack(fragment::class.java.simpleName)
                num_stack ++
            }
            transaction.commit()
        }
    }
}
