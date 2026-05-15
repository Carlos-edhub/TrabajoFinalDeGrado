package com.example.miaplicacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class OnboardingAdapter(
    private val slides: List<SlideData>
) : RecyclerView.Adapter<OnboardingAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_slide, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    override fun getItemCount() = slides.size

    class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val emoji: TextView = itemView.findViewById(R.id.txtEmoji)
        private val titulo: TextView = itemView.findViewById(R.id.txtTituloSlide)
        private val descripcion: TextView = itemView.findViewById(R.id.txtDescripcionSlide)

        fun bind(slide: SlideData) {
            emoji.text = slide.emoji
            titulo.text = slide.titulo
            descripcion.text = slide.descripcion
        }
    }
}
