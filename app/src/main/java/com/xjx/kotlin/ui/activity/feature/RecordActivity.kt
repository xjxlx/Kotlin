package com.xjx.kotlin.ui.activity.feature

import android.Manifest
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.ViewGroup
import com.android.common.utils.permission.PermissionMultipleCallBackListener
import com.android.common.utils.permission.PermissionUtil
import com.android.common.utils.FileUtil
import com.android.common.utils.LogUtil
import com.android.common.utils.ToastUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.xjx.kotlin.databinding.ActivityRecordBinding
import java.io.File
import java.io.IOException

class RecordActivity : AppBaseBindingTitleActivity<ActivityRecordBinding>() {

    private val mPermission = PermissionUtil.PermissionActivity(this)
    private val mPermissionArray: Array<String> =
        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)

    private val mVideoFile: File by lazy {
        val rootPath = FileUtil.instance.getRootPath(this)
        return@lazy File(rootPath, "video.mp4")
    }
    private val mSurfaceHolder: SurfaceHolder by lazy {
        return@lazy mBinding.sfv.holder
    }
    private val tag = "Recorde - Video"
    private var mCamera: Camera? = null
    private var mPositionParameters = 0
    private var mPhoneWidth: Int = 0
    private var mPhoneHeight: Int = 0
    private var mediaRecorder: MediaRecorder? = null
    private var sizeForVideo: Camera.Size? = null
    private var isRecording = false

    private var isRelease // 是否释放了所有的配置
            = false
    private val isPause // 是否暂停的状态
            = false

    override fun setTitleContent(): String {
        return "录制视频"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityRecordBinding {
        return ActivityRecordBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPermission.requestArray(mPermissionArray, object : PermissionMultipleCallBackListener {
            override fun onCallBack(allGranted: Boolean, map: Map<String, Boolean>) {
                if (allGranted) {
                    initVideo()
                }
            }
        })

        mBinding.btnStart.setOnClickListener {
            startRecorder()
        }
        mBinding.btnStop.setOnClickListener {
            stop()
        }
    }

    private fun initVideo() {
        // 设置分辨率
        mSurfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                LogUtil.e(tag, "surfaceCreated!")
                // 初始化摄像头
                initCamera();
                // 初始化录制
                initMediaRecorder();
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                LogUtil.e(tag, "surfaceChanged!")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                LogUtil.e(tag, "surfaceDestroyed!")
            }
        })
        // 屏幕常亮
        mSurfaceHolder.setKeepScreenOn(true);
    }

    private fun initCamera() {
        if (mCamera != null) { // 避免重复性的去创建摄像机，否则会崩溃
            return
        }
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        // 检测是否有摄像头
        if (mCamera == null) {
            ToastUtil.show("您的设备没有摄像头，暂时无法使用");
            LogUtil.e(tag, "您的设备没有摄像头，暂时无法使用!")
            return
        }

        val parameters = mCamera!!.parameters
        /*
         * 1：设置预览尺寸,因为预览的尺寸和最终是录制视频的尺寸无关，所以我们选取最大的数值
         * 2：获取手机支持的预览尺寸，这个一定不能随意去设置，某些手机不支持，一定会崩溃
         * 3：通常最大的是手机的分辨率，这样可以让预览画面尽可能清晰并且尺寸不变形，前提是TextureView的尺寸是全屏或者接近全屏
         */
        // 使用推荐的尺寸去处理
        sizeForVideo = parameters.preferredPreviewSizeForVideo
        if (sizeForVideo != null) {
            LogUtil.e(tag, "size:--->推荐尺寸的宽高为：width：" + sizeForVideo?.width + "  height--->:" + sizeForVideo!!.height);
            // 设置预览的尺寸
            //设置图片尺寸  就拿预览尺寸作为图片尺寸,其实他们基本上是一样的
            parameters.setPreviewSize(sizeForVideo!!.width, sizeForVideo!!.height);
        }

        //缩短Recording启动时间
        parameters.setRecordingHint(true);

        //   是否支持影像稳定能力，支持则开启
        if (parameters.isVideoStabilizationSupported()) {
            parameters.setVideoStabilization(true);
        }

        // 寻找合适的尺寸，避免录制异常
        setCameraParameters(parameters);

        mCamera?.setDisplayOrientation(90);
        // 预览摄像机
        mCamera?.setPreviewDisplay(mSurfaceHolder)
        mCamera?.startPreview()
        // 解锁摄像机
        mCamera?.unlock();
    }

    private fun initMediaRecorder() {
        try {
            LogUtil.e(tag, "initMediaRecorder")
            // 创建录制对象
            mediaRecorder = MediaRecorder()
            mediaRecorder?.reset()
            if (mCamera == null) {
                initCamera()
            }

            // mMediaRecorder.setCamera(camera);之前是需要将摄像头解除锁定 camera.unlock()
            mediaRecorder?.setCamera(mCamera)
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.DEFAULT) //设置音频输入源
            mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.DEFAULT) //设置视频输入源

            //设置视频的摄像头角度 只会改变录制的视频角度
            mediaRecorder?.setOrientationHint(270)
            //设置记录会话的最大持续时间（毫秒）
            mediaRecorder?.setMaxDuration(5 * 1000)
            // mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            //设置最大录制的大小2M 单位，字节
            mediaRecorder?.setMaxFileSize(2 * 1024 * 1024)

            // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4  如果使用了setProfile（）方法的话，那么这里就不能设置，否则就会导致崩溃
            // mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//音频输出格式
            // mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//设置音频的编码格式
            // mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);//设置图像编码格式

            /*
             * 1:设置视频尺寸，通常搭配码率一起使用，可调整视频清晰度
             * 2:此处一定不要胡乱设置，某些机型上不支持的话，会直接崩溃掉，最好直接用系统推荐的尺寸去处理
             */if (sizeForVideo != null) {
                // 设置视频的尺寸
                if (mPhoneWidth == 0 && mPhoneHeight == 0) {
                    mPhoneWidth = sizeForVideo!!.width
                    mPhoneHeight = sizeForVideo!!.height
                }
                setProfile(mediaRecorder!!)

                // 设置尺寸的大小
                // mediaRecorder.setVideoSize(mPhoneWidth, mPhoneHeight);
                //设置比特率,比特率是每一帧所含的字节流数量,比特率越大每帧字节越大,画面就越清晰,算法一般是 5 * 选择分辨率宽 * 选择分辨率高,一般可以调整5-10,比特率过大也会报错
                //  mediaRecorder.setVideoEncodingBitRate(sizeForVideo.width * sizeForVideo.height);//设置视频的比特率
            }

            // 设置录制文件的保存路径
            mediaRecorder?.setOutputFile(mVideoFile.absolutePath)
            mediaRecorder?.setOnErrorListener { mr, what, extra ->
                // 发生错误，停止录制
                stop()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            LogUtil.e(tag, "init:MediaRecorder --->Error:" + e.message)
        }
    }

    private fun setCameraParameters(parameters: Camera.Parameters) {
        var isParameters = false
        try {
            mCamera!!.parameters = parameters
        } catch (e: Exception) {
            e.printStackTrace()
            isParameters = true
            LogUtil.e(tag, "机型适配错误")
        } finally {
            if (isParameters) {
                //如果机型适配错误 查找合适的size
                val previewSizes = parameters.supportedPreviewSizes
                var w = 720
                var h = 480
                if (!previewSizes.isEmpty()) {
                    val size = previewSizes[mPositionParameters]
                    w = size.width
                    h = mPhoneHeight //不赋值高
                }
                mPhoneWidth = w
                LogUtil.e(tag, "机型适配错误后选定的尺寸$w:$h")
                parameters.setPreviewSize(w, h)
                mPositionParameters++
                //集合不为空
                if (previewSizes.isNotEmpty() && mPositionParameters < previewSizes!!.size) {
                    setCameraParameters(parameters)
                } else {
                    //为空走一次就行了
                    if (mPositionParameters === 1) {
                        setCameraParameters(parameters)
                    }
                }
            }
        }
    }

    /**
     * 设置手机的参数
     *
     * @param mediaRecorder 媒体对象
     */
    private fun setProfile(mediaRecorder: MediaRecorder) {
        var profile: CamcorderProfile? = null
        if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_1080P)
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P)
        } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)) {
            profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P)
        }
        if (profile != null) {
            mediaRecorder.setProfile(profile)
            val videoBitRate = profile.videoBitRate
            // 此处必须设置，如果使用原生的profile设置的话，录制的大小很容易超过预设的值，导致录制时间不够
            mediaRecorder.setVideoEncodingBitRate(sizeForVideo!!.width * sizeForVideo!!.height) //设置视频的比特率
        }
    }

    private fun startRecorder() {
        LogUtil.e(tag, "start")
        if (!isRecording) {
            try {
                if (mediaRecorder != null) {
                    mediaRecorder!!.prepare() //准备
                    mediaRecorder!!.start() //开启
                }
                isRecording = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 停止录像
     */
    private fun stop() {
        LogUtil.e(tag, "stop")
        if (isRecording) {
            try {
                // 如果正在录制，停止并释放资源
                if (mediaRecorder != null) {
                    mediaRecorder!!.stop()
                }
                isRecording = false
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                LogUtil.e(tag, "停止录像失败：" + e.message)
                releaseAll()
            }
        }
    }

    private fun releaseCamera() {
        // 清空摄像机
        try {
            LogUtil.e(tag, "释放资源")
            if (mCamera != null) {
                try {
                    var camera = mCamera
                    camera!!.setPreviewCallback(null)
                    camera.stopPreview()
                    camera.setOneShotPreviewCallback(null)
                    camera.lock()
                    camera.release()
                    camera = null
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        } catch (ignored: java.lang.Exception) {
        }
    }

    /**
     * 释放录像机
     */
    private fun releaseRecorder() {
        LogUtil.e("releaseRecorder")
        if (isRecording) {
            try {
                // 如果正在录制，停止并释放资源
                mediaRecorder!!.stop()
                mediaRecorder!!.setCamera(null)
                mediaRecorder!!.release()
                mediaRecorder = null
                isRecording = false
                isRelease = true
            } catch (ignored: java.lang.Exception) {
            }
        }
    }

    private fun releaseAll() {
        releaseCamera()
        releaseRecorder()
    }

    fun getCamera(): Camera? {
        var camera: Camera? = null
        val info = CameraInfo()
        val cnt = Camera.getNumberOfCameras()
        for (i in 0 until cnt) {
            Camera.getCameraInfo(i, info)
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    camera = Camera.open(i)
                    break
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }
        }
        return camera
    }

    override fun onStop() {
        super.onStop()
        LogUtil.e(tag, "onStop")
        releaseAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(tag, "onDestroy")
        releaseAll()
    }
}