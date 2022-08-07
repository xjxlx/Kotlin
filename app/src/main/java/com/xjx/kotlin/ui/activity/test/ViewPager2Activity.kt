package com.xjx.kotlin.ui.activity.test

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.xjx.kotlin.R

class ViewPager2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager2)

        val vp2 = findViewById<ViewPager2>(R.id.vp2)

        val list = arrayListOf<Fragment>()
        list.add(Blank1Fragment.newInstance("1", "2"))
        list.add(Blank2Fragment.newInstance("1", "2"))
        val adapter = ViewPagerAdapter(this, list)
        vp2.adapter = adapter

        var index: Int = 0

        findViewById<Button>(R.id.btn_s).setOnClickListener {
            index = if (index == 0) {
                1
            } else {
                0
            }
            vp2.currentItem = index
        }
    }
}