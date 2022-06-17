package com.example.diateamproject.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.diateamproject.R
import com.example.diateamproject.model.onboarding.OnBoardingData
import com.example.diateamproject.util.ConvertHtml

class OnBoardingAdapter(
    private var context: Context,
    private var onBoardingDataList: List<OnBoardingData>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return onBoardingDataList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_onboarding, null)
        val title: TextView = view.findViewById(R.id.tvOnBoardTitle)
        val subTitle: TextView = view.findViewById(R.id.tvOnBoardSubTitle)
        val imageView: ImageView = view.findViewById(R.id.ivOnBoard)

        title.text = Html.fromHtml(onBoardingDataList[position].title)
        subTitle.text = onBoardingDataList[position].subTitle
        imageView.setImageResource(onBoardingDataList[position].imageUrl)

        container.addView(view)

        return view
    }
}