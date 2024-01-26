package com.xjx.kotlin.ui.activity.feature

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.xjx.kotlin.databinding.ActivityTextToSpeechBinding
import java.util.Locale

class TextToSpeechActivity : BaseBindingTitleActivity<ActivityTextToSpeechBinding>() {

    private val MY_TTS_CHECK_CODE: Int = 100
    private val tag = "TTS"
    private var mSpeech: TextToSpeech? = null

    private var listener = TextToSpeech.OnInitListener { status ->
        if (TextToSpeech.SUCCESS == status) {
            LogUtil.e(tag, "init  success !")
            val language = mSpeech?.setLanguage(Locale.CHINA)
            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                LogUtil.e(tag, "数据丢失或者不支持！")
            }
        } else {
            LogUtil.e(tag, "init  error !")
        }
    }

    override fun getTitleContent(): String {
        return "文字转换语音"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTextToSpeechBinding {
        return ActivityTextToSpeechBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mSpeech = TextToSpeech(this, listener)

        val content = "做的不错，非常好"

        mBinding.btnCheck.setOnClickListener {
            checkTTSEngine()
        }

        mBinding.btnStart.setOnClickListener {
            textToSpeech(content)
        }
    }

    private fun textToSpeech(content: String) {
        // mSpeech?.setPitch(1.2f);    // 语调范围从0.5到2.0
        // mSpeech?.setSpeechRate(0.8f);    // 语速范围从0.1到2.0
        mSpeech?.setPitch(1.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        mSpeech?.speak(content, TextToSpeech.QUEUE_FLUSH, null, "123")
    }

    private fun checkTTSEngine() {
        LogUtil.e(tag, "checkTTSEngine")
        val checkIntent = Intent()
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA)
        startActivityForResult(checkIntent, MY_TTS_CHECK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LogUtil.e(tag, "onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_TTS_CHECK_CODE) {
            LogUtil.e(tag, "onActivityResult --->success")
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                mSpeech = TextToSpeech(this, listener)
            } else {
                // 设置默认的语音引擎
                LogUtil.e(tag, "onActivityResult --->set default tts ")
                val installIntent = Intent()
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(installIntent)
            }
        }
    }

    private fun setDefaultTTSEngine() {
        val intent = Intent()
        intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSpeech?.stop(); // 不管是否正在朗读TTS都被打断
        mSpeech?.shutdown(); // 关闭，释放资源
    }
}