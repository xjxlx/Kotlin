package com.xjx.kotlin.ui.activity.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.helper.player.app.BaseBuddyVideoPlayer
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.Debuger
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

        val inflate = layoutInflater.inflate(R.layout.item_test_player, null, false)
        val videoPlayer = inflate.findViewById<BaseBuddyVideoPlayer>(R.id.bdvp)

        //外部辅助的旋转，帮助全屏
        val orientationUtils = OrientationUtils(this, videoPlayer)
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false)
        val videoUrl = "http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4";

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
                    orientationUtils.setEnable(true)
                }

                override fun onQuitFullscreen(url: String, vararg objects: Any) {
                    super.onQuitFullscreen(url, *objects)
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[0]) //title
                    Debuger.printfError("***** onQuitFullscreen **** " + objects[1]) //当前非全屏player
                    if (orientationUtils != null) {
                        orientationUtils.backToProtVideo()
                    }
                }
            })
            .setLockClickListener { view, lock ->
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock)
                }
            }
            .build(videoPlayer)

        videoPlayer.getFullscreenButton()
            .setOnClickListener(View.OnClickListener { //直接横屏
                orientationUtils.resolveByClick()

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                videoPlayer.startWindowFullscreen(this@GsyPlayerActivity, true, true)
            })

        mBinding.root.addView(inflate)
    }
}