package com.xjx.kotlin.ui.activity.audio

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.apphelper2.utils.GsonUtil
import com.android.apphelper2.utils.LogUtil
import com.android.apphelper2.utils.permission.PermissionMultipleCallBackListener
import com.android.apphelper2.utils.permission.PermissionUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityRecordingBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import technology.cariad.cda.audiorecorder.AudioRecorder
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

class RecordingActivity : AppBaseBindingTitleActivity<ActivityRecordingBinding>() {

    private val tag = "Recording :"
    private val delayTime = 10000
    private val frameCount = 256
    private val mScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(Dispatchers.IO)
    }
    private val mPauseFlag: AtomicBoolean by lazy { return@lazy AtomicBoolean() }
    private val mMutex = Mutex()
    private val permission: PermissionUtil.PermissionActivity = PermissionUtil.PermissionActivity(this@RecordingActivity)

    @OptIn(DelicateCoroutinesApi::class)
    private val mCoroutineRecordingContext: CoroutineContext by lazy {
        return@lazy newSingleThreadContext("Recording")
    }

    @OptIn(DelicateCoroutinesApi::class)
    private val mCoroutineControlContext: CoroutineContext by lazy {
        return@lazy newSingleThreadContext("Control")
    }

    override fun setTitleContent(): String {
        return "Recording"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityRecordingBinding {
        return ActivityRecordingBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // 注册
        mBinding.btnCreate.setOnClickListener {
            register()
        }

        // 解绑
        mBinding.btnDestroy.setOnClickListener {
            unregister()
        }

        // 暂停
        mBinding.btnPause.setOnClickListener {
            pause()
        }

        // 重新开始
        mBinding.btnResume.setOnClickListener {
            resume()
        }

        // 开始
        mBinding.btnStart.setOnClickListener {
            permission.requestArray(arrayOf<String>(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                object : PermissionMultipleCallBackListener {
                    override fun onCallBack(allGranted: Boolean, map: MutableMap<String, Boolean>) {
                        if (allGranted) {
                            LogUtil.e(tag, "权限满足，开始录音！")
                            start()
                        } else {
                            LogUtil.e(tag, "权限不足，无法录音！ map：$map")
                        }
                    }
                })
        }

        // 停止
        mBinding.btnEnd.setOnClickListener {
            stop()
        }
    }

    private fun resume() {
        mScope.launch(context = mCoroutineControlContext) {
            mMutex.withLock {
                mPauseFlag.set(false)
                LogUtil.e(tag, "录音重新开始！，thread: ${Thread.currentThread().name}")
            }
        }
    }

    private fun pause() {
        mScope.launch(context = mCoroutineControlContext) {
            mMutex.withLock {
                mPauseFlag.set(true)
                LogUtil.e(tag, "录音暂停！，thread: ${Thread.currentThread().name}")
            }
        }
    }

    private fun register() {
        mScope.launch(context = mCoroutineControlContext) {
            val create = AudioRecorder.create()
            LogUtil.e(tag, "录音文件创建成功 ：$create  ，thread: ${Thread.currentThread().name}")
        }
    }

    private fun unregister() {
        mScope.launch(context = mCoroutineControlContext) {
            AudioRecorder.delete()
            LogUtil.e(tag, "录音文件解绑成功，thread: ${Thread.currentThread().name}")
        }
    }

    private fun start() {
        mScope.launch(context = mCoroutineControlContext) {
            if (mFile.exists()) {
                val delete = mFile.delete()
                LogUtil.e(tag, "文件删除成功：$delete ，thread: ${Thread.currentThread().name}")
            }

            mMutex.withLock {
                mPauseFlag.set(false)
                LogUtil.e(tag, "录音重新开始！，thread: ${Thread.currentThread().name}")
            }

            LogUtil.e(tag, "开始录音 ，thread: ${Thread.currentThread().name}")
            AudioRecorder.startRecording()

            withContext(mCoroutineRecordingContext) {
                LogUtil.e(tag, "开始采集数据 ，thread: ${Thread.currentThread().name}")
                val audioData = ByteArray(frameCount * 7 * 2)
                val list: ArrayList<Byte> = arrayListOf()
                // 本地的最大缓存数据量
                val cacheSize: Int = 8 * frameCount * 2
                val fos = FileOutputStream(mFile)

                try {
                    while (true) {
                        if (!mPauseFlag.get()) {
                            val streamRead = AudioRecorder.streamRead(audioData, frameCount, delayTime)
                            //  //因为Cariad audio用两个byte来表示一路（共有7路音频数据）数据，所以每14个取前两个数据
                            LogUtil.e(tag, "streamRead ---> $streamRead  audioData：${GsonUtil.toJson(audioData)}")
                            for (i in 0 until streamRead) {
                                list.add(audioData[i * 14])
                                list.add(audioData[i * 14 + 1])
                                list.add(audioData[i * 14 + 2])
                                list.add(audioData[i * 14 + 3])
                            }
                            if (list.size >= cacheSize) {
                                if (!mPauseFlag.get()) {
                                    writeFile(list, fos)
                                    list.clear()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    LogUtil.e(tag, "采集音频出了异常：${e.message}")
                }
            }
        }
    }

    private fun writeFile(list: ArrayList<Byte>, fos: FileOutputStream) {
        runCatching {
            val toByteArray = list.toByteArray()
            fos.write(toByteArray)
            fos.flush()
            LogUtil.e("写入数据成功！")
        }.onFailure {
            LogUtil.e("写入数据异常：${it.message}")
        }
    }

    private fun stop() {
        pause()
        mScope.launch(context = mCoroutineControlContext) {
            AudioRecorder.stopRecording()
            LogUtil.e(tag, "停止录音 ，thread: ${Thread.currentThread().name}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
        unregister()
    }

    private val mFile: File by lazy {
        val filesDir = filesDir
        val ss = Date()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val pcmFileName = format.format(ss.time) + "_2.pcm"
        LogUtil.e(tag, "本地录音文件名称：$pcmFileName")
        return@lazy File(filesDir, pcmFileName)
    }
}