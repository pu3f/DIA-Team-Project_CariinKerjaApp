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
): RecyclerView.Adapter<BannerAdapter.BannerViewHolder>(){

    private val bannerItems: List<BannerItem>
    var pos: Int = 0

    init {
        this.bannerItems = bannerItems
    }

    inner class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imageView: RoundedImageView = itemView.findViewById(R.id.ivBanner)
        private val newActivity = itemView.context as AppCompatActivity
        private val intentWeb = Intent(Intent.ACTION_VIEW, Uri.parse("http://ideaco.co.id/"))
        private val intentPlayStore = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/"))

            fun image(bannerItem: BannerItem) {
                imageView.setImageResource(bannerItem.image)
                imageView.setOnClickListener {
                    Log.d("wherePosition", "$pos")
                    when(pos) {
                        0 -> {
                            Log.d("wherePosition0", "$pos")
                            newActivity.startActivity(intentWeb)
                        }
                        1 -> {
                            Log.d("wherePosition1", "$pos")
                            newActivity.startActivity(intentPlayStore)
                        }
                        2 -> {
                            //get isLogin conditions
                            val isLogin = Prefs.getBoolean(PrefsLoginConstant.IS_LOGIN, false)
                            if (isLogin) {
                                Log.d("wherePosition2", "$pos")
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
        pos = position
    }

    override fun getItemCount(): Int {
       return bannerItems.size
    }
}