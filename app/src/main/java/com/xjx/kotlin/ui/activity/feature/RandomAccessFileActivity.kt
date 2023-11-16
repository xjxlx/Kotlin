package com.xjx.kotlin.ui.activity.feature

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.common.utils.RandomAccessFileUtil
import com.android.common.utils.permission.PermissionCallBackListener
import com.android.common.utils.permission.PermissionUtil
import com.xjx.kotlin.databinding.ActivityRandomAccessFileBinding

class RandomAccessFileActivity : BaseBindingTitleActivity<ActivityRandomAccessFileBinding>() {

    private val raf = RandomAccessFileUtil()
    private val permission = PermissionUtil.PermissionActivity(this)

    override fun initData(savedInstanceState: Bundle?) {
        permission.setCallBackListener(object : PermissionCallBackListener {
            override fun onCallBack(permission: String, isGranted: Boolean) {
                LogUtil.e("changeFile", permission)
            }
        })
        permission.request(Manifest.permission.READ_EXTERNAL_STORAGE)

        mBinding.btnStart.setOnClickListener {

            val filesDir = mActivity.filesDir
            val path = filesDir.absolutePath + "/Test.txt";
            raf.changeFile(path, "rw")
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityRandomAccessFileBinding {
        return ActivityRandomAccessFileBinding.inflate(inflater, container, true)
    }

    override fun getTitleContent(): String {
        return "测试随机读写流"
    }
}