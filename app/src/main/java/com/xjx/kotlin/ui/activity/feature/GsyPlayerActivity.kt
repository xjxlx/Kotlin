package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.helper.player.app.BaseBuddyVideoPlayer
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.xjx.kotlin.R
import com.xjx.kotlin.databinding.ActivityGsyPlayerBinding

class GsyPlayerActivity : BaseBindingTitleActivity<ActivityGsyPlayerBinding>() {
    override fun getTitleContent(): String {
        return "测试GsyPlayer"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityGsyPlayerBinding {
        return ActivityGsyPlayerBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val inflate = LayoutInflater.from(this)
            .inflate(R.layout.item_gsyplayer, null, false)
        val video = inflate.findViewById<BaseBuddyVideoPlayer>(R.id.iv_today_video)


        findViewById<ViewGroup>(R.id.ll_parent).addView(inflate)

        player(video)
    }

    private fun player(video: BaseBuddyVideoPlayer) {
        //外部辅助的旋转，帮助全屏
        val orientationUtils = OrientationUtils(this, video)
        //初始化不打开外部的旋转
        orientationUtils.isEnable = false
        val videoUrl = "http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4"
        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption.setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setAutoFullWithSize(true)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setUrl(videoUrl)
            .setCacheWithPlay(false)
            .setVideoTitle("测试视频")
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String, vararg objects: Any) {
                    super.onPrepared(url, *objects)
                    //开始播放了才能旋转和全屏
                    orientationUtils.isEnable = true
                }

                override fun onQuitFullscreen(url: String, vararg objects: Any) {
                    super.onQuitFullscreen(url, *objects)
                    orientationUtils.backToProtVideo()
                }
            })
            .setLockClickListener { view, lock -> //配合下方的onConfigurationChanged
                orientationUtils.isEnable = !lock
            }
            .build(video)
        video.getFullscreenButton()
            .setOnClickListener(View.OnClickListener {
                orientationUtils.resolveByClick() //直接横屏
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                video.startWindowFullscreen(mActivity, true, true)
            })
    }
}