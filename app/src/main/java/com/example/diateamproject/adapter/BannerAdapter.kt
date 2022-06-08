package com.example.diateamproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.diateamproject.R
import com.example.diateamproject.util.BannerItem
import com.makeramen.roundedimageview.RoundedImageView

class BannerAdapter internal constructor(
    bannerItems: MutableList<BannerItem>,
    viewPager: ViewPager2
): RecyclerView.Adapter<BannerAdapter.BannerViewHolder>(){

    private val bannerItems: List<BannerItem>

    init {
        this.bannerItems = bannerItems
    }

    class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imageView: RoundedImageView = itemView.findViewById(R.id.ivBanner)

            fun image(bannerItem: BannerItem) {
                imageView.setImageResource(bannerItem.image)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_banner,
                    parent,
                    false
                ),
            )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.image(bannerItems[position])
    }

    override fun getItemCount(): Int {
       return bannerItems.size
    }
}