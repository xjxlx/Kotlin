package com.xjx.kotlin.ui.activity.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityDescriptionViewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class DescriptionViewActivity : AppBaseBindingTitleActivity<ActivityDescriptionViewBinding>() {

    override fun setTitleContent(): String {
        return "文字描述的View"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityDescriptionViewBinding {
        return ActivityDescriptionViewBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnStart.setOnClickListener {

            lifecycleScope.launch {
                delay(2000)

                val score = Random.nextInt(10, 90)
                val content =
                    "“神死了……魔灭了……我还活着……为什么？为何让我从远古神墓中复出，我将何去何从？”\n" + "神魔陵园除了安葬着人类历代的最强者、异类中的顶级修炼者外，其余每一座坟墓都埋葬着一位远古的神或魔，这是一片属于神魔的安息之地。一个平凡的青年死去万载岁月之后，从远古神墓中复活而出，如林的神魔墓碑令青年感到异常震撼。\n" + "悠悠万载，沧海桑田。\n" + "原本有一海峡之隔的仙幻大陆和魔幻大 [2] 陆相连在了一起，道法与魔法并存，真气与斗气同在，东方神龙与西方巨龙共舞……"
                mBinding.vDv.onScore(score, score, score, score, content)
            }
        }
    }
}