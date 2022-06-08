package com.example.diateamproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.diateamproject.databinding.ItemBannerBinding
import com.example.diateamproject.util.BannerItem

class BannerAdapter internal constructor(
    bannerItems: MutableList<BannerItem>,
    viewPager: ViewPager2
): RecyclerView.Adapter<BannerAdapter.BannerViewHolder>(){

    private val bannerItems: List<BannerItem>

    init {
        this.bannerItems = bannerItems
    }

    class BannerViewHolder(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root ) {

            fun image(bannerItem: BannerItem) {
                binding.ivBanner.setImageResource(bannerItem.image)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(
                    parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.image(bannerItems[position])
    }

    override fun getItemCount(): Int {
       return bannerItems.size
    }
}