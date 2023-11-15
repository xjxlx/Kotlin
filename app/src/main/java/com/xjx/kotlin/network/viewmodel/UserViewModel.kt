package com.xjx.kotlin.network.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.common.utils.LogUtil
import com.android.http.client.HttpResult
import com.xjx.kotlin.network.bean.UserInfoBean
import com.xjx.kotlin.network.logic.UserLogic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
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