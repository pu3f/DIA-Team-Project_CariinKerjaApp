package com.example.diateamproject.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.diateamproject.R
import com.example.diateamproject.activity.LoginActivity
import com.example.diateamproject.fragment.ProfileFragment
import com.example.diateamproject.util.BannerItem
import com.example.diateamproject.util.PrefsLoginConstant
import com.makeramen.roundedimageview.RoundedImageView
import com.pixplicity.easyprefs.library.Prefs


class BannerAdapter internal constructor(
    bannerItems: MutableList<BannerItem>,
    viewPager: ViewPager2
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    private val bannerItems: List<BannerItem>

    init {
        this.bannerItems = bannerItems
    }

    inner class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: RoundedImageView = itemView.findViewById(R.id.ivBanner)
        var newActivity = itemView.context as AppCompatActivity

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
        val newActivity = holder.newActivity
        val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse("https://toptalentapp.com/"))
        val intentPlayStore = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/"))
        holder.image(bannerItems[position])
        holder.imageView.setOnClickListener {
            Log.d("positionsxx", "$position")
            when (position) {
                0 -> {
                    Log.d("positions0", "$position")
                    newActivity.startActivity(intentWeb)
                }
                1 -> {
                    newActivity.startActivity(intentPlayStore)
                }
                2 -> {
                    //get isLogin conditions
                    val isLogin = Prefs.getBoolean(PrefsLoginConstant.IS_LOGIN, false)
                    if (isLogin) {
                        val fragment: Fragment = ProfileFragment()
                        val fragmentManger = newActivity.supportFragmentManager
                        val transaction: FragmentTransaction = fragmentManger.beginTransaction()
                        transaction.replace(R.id.content, fragment).commit()
                    } else {
                        val intentLogin = Intent(newActivity, LoginActivity::class.java)
                        newActivity.startActivity(intentLogin)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return bannerItems.size
    }
}