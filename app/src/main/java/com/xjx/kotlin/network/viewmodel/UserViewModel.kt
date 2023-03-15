package com.xjx.kotlin.network.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.helper.utils.LogUtil
import com.xjx.kotlin.network.ApiLogic.UserLogic
import com.xjx.kotlin.network.HttpResult
import com.xjx.kotlin.network.bean.UserInfoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author : 流星
 * @CreateDate: 2023/3/15-10:08
 * @Description:
 */
class UserViewModel : ViewModel() {

    private var mUser = MutableLiveData<UserInfoBean>()

    fun getUser() {
//        viewModelScope.launch {
//            flow {
//                emit(UserLogic.getUser())
//            }
//                .onStart {
//
//                }
//                .onCompletion {
//
//                }
//                .catch {
//                    emit()
//                }
//                .collect {
//                    when (it) {
//                        is String -> {}
//                    }
//                }
//        }




        viewModelScope.launch {
            flow<HttpResult<UserInfoBean>> {
                UserLogic.getUser()
            }
                .transform {
                    val code = it.code
                    val data = it.data
                    val msg = it.msg
                    if (code == 200) {
                        emit(data)
                    } else {
                        emit(msg)
                    }
                }
                .catch { error ->
                    emit(error.message)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    LogUtil.e("result ::: $it")
                    if (it is UserInfoBean) {
                        mUser.value = it
                    } else if (it is String) {
                        mUser.value
                    }
                }
        }
    }
}