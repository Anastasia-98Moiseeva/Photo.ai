package com.example.test.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.photoai.R


class ListAdapter(private val names : Array<String>, private val arrayOfImages : Array<Int>,
                  private val onClick : (Int) -> Unit)
    : RecyclerView.Adapter<ButtonHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ButtonHolder {

        val rootView = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.button_layout,
            viewGroup, false)

        val button : ImageButton = rootView.findViewById(R.id.btn_filter_item)
        return ButtonHolder(rootView, button, onClick)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: ButtonHolder, pos: Int) {
        val img = arrayOfImages[pos]
        holder.button.setImageResource(img)
        holder.button.scaleType = ImageView.ScaleType.CENTER_CROP
        holder.button.adjustViewBounds = false
    }
}

class ButtonHolder(view : View, val button : ImageButton, val onClick : (Int) -> Unit) : RecyclerView.ViewHolder(view) {
    init {
        button.setOnClickListener {
            onClick(adapterPosition)
        }
    }
}