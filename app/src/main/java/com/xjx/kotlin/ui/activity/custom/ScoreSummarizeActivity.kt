package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityScoreSummarizeBinding
import kotlinx.coroutines.launch

class ScoreSummarizeActivity : AppBaseBindingTitleActivity<ActivityScoreSummarizeBinding>() {

    override fun setTitleContent(): String {
        return "分数综合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityScoreSummarizeBinding {
        return ActivityScoreSummarizeBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnStart.setOnClickListener {
            val bottomArray = floatArrayOf(0.6f, 0.3f, 0.2f, 0.1f, 0.4f)
            val topArray = floatArrayOf(1f, 0.3f, 0.5f, 0.7f, 1f)
            val scoreArray = intArrayOf(20, 30, 50, 20, 100)
            mBinding.cvChart.setChartArray(bottomArray, topArray, scoreArray)

            mBinding.spvScore.setScore(70, 23)

            lifecycleScope.launch {
                mBinding.twvWrap.setExplain(70, "加油", "下次努力哦！", "呼吸时长足够，注意保持均匀的呼吸次数和平缓的心率，让情绪更稳定些效果更好哦！")
            }


            this.getExternalFilesDir("");
        }
    }
}