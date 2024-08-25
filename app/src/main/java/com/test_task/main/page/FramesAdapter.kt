package com.test_task.main.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test_task.R
import com.test_task.main.films.Frame

class FramesAdapter(private val imageList: List<Frame>):
    RecyclerView.Adapter<FramesAdapter.FrameHolder>() {

    class FrameHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(image: Frame) {

            val imageView = itemView.findViewById<ImageView>(R.id.frame_iv)
            imageView.setBackgroundResource(android.R.color.transparent)

            Glide
                .with(itemView.context)
                .load(image.previewUrl)
                .placeholder(R.drawable.ic_default_image)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.frame_item, parent, false)
        return FrameHolder(itemView)
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: FrameHolder, position: Int) {
        holder.bind(imageList[position])
    }
}