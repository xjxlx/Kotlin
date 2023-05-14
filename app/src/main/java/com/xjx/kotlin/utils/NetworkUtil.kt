package com.xjx.kotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.android.apphelper2.app.AppHelperManager
import com.android.apphelper2.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.net.Inet4Address
import java.net.NetworkInterface

/**
 * network util
 */
class NetworkUtil private constructor() {

    private var mIpAddress = ""
    private val mStateFlow = MutableStateFlow("")
    private val mScope = CoroutineScope(Dispatchers.IO)
    private val mConnectivityManager: ConnectivityManager by lazy {
        AppHelperManager.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val mRequest: NetworkRequest by lazy {
        NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }
    private val mCallBack = object : ConnectivityManager.NetworkCallback() {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) { // wifi connect
                val transportInfo = networkCapabilities.transportInfo
                if (transportInfo is WifiInfo) {
                    val properties = mConnectivityManager.getLinkProperties(network)
                    properties?.let {
                        // contain ipv4  and ipv6 ipaddress
                        val linkAddresses = it.linkAddresses
                        if (linkAddresses.size > 0) {
                            linkAddresses.forEach { ip ->
                                val address = ip.address
                                if (address != null) {
                                    val hostAddress = address.hostAddress
                                    if (hostAddress != null) {
                                        if ((!TextUtils.equals(hostAddress, "0.0.0.0")) && (!(hostAddress.contains(":")))) {
                                            LogUtil.e("hostAddress: wifi: $hostAddress")
                                            mScope.launch {
                                                mStateFlow.emit(hostAddress)
                                                mIpAddress = hostAddress
                                            }
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) { // Mobile connect
                // current use 2G/3G/4G
                val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                while (networkInterfaces.hasMoreElements()) {
                    val element = networkInterfaces.nextElement()
                    val inetAddresses = element.inetAddresses
                    if (inetAddresses != null) {
                        while (inetAddresses.hasMoreElements()) {
                            val address = inetAddresses.nextElement()
                            if (!address.isLoopbackAddress && address is Inet4Address) {
                                val hostAddress = address.getHostAddress()
                                if (hostAddress != null) {
                                    mScope.launch {
                                        mStateFlow.emit(hostAddress)
                                        mIpAddress = hostAddress
                                    }
                                    LogUtil.e("hostAddress: mobile: $hostAddress")
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {

        @JvmStatic
        val instance: NetworkUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkUtil()
        }
    }

    /**
     * must has permission
     * @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
     * @return current network is connect
     */
    val isNetworkConnected: Boolean
        get() {
            val network = mConnectivityManager.activeNetwork
            if (network != null) {
                val nc = mConnectivityManager.getNetworkCapabilities(network)
                if (nc != null) {
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) { // WIFI
                        return true
                    } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) { // mobile is connect
                        return true
                    }
                }
            }
            return false
        }

    /**
     * must has permission
     * @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
     * @return wifi is connect
     */
    val isWifiConnect: Boolean
        get() {
            val network = mConnectivityManager.activeNetwork
            if (network != null) {
                val nc = mConnectivityManager.getNetworkCapabilities(network)
                if (nc != null) {
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) { // WIFI
                        return true
                    }
                }
            }
            return false
        }

    /**
     * must has permission
     * @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
     * @return mobile is connect
     */
    val isMobileConnect: Boolean
        get() {
            val network = mConnectivityManager.activeNetwork
            if (network != null) {
                val nc = mConnectivityManager.getNetworkCapabilities(network)
                if (nc != null) {
                    if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) { // mobile is connect
                        return true
                    }
                }
            }
            return false
        }

    /**
     * @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
     * must has permission
     *@return if network is connect ,return the ipAddress, it can call back multiple count
     */
    suspend fun getIPAddress(block: (ipAddress: String) -> Unit) {
        mConnectivityManager.requestNetwork(mRequest, mCallBack)
        mStateFlow.first {
            LogUtil.e("hostAddress:------:> result---> $it")
            if (!TextUtils.isEmpty(it)) {
                block(it)
            }
            return@first false
        }
    }

    /**
     * @RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
     * must has permission
     * @return if network is connect ,return the ipAddress ,it only can call back one count
     */
    suspend fun getSingleIpAddress(block: (String) -> Unit) {
        if (TextUtils.isEmpty(mIpAddress)) {
            runCatching {
                mConnectivityManager.requestNetwork(mRequest, mCallBack)
                mStateFlow.first {
                    if (!TextUtils.isEmpty(it)) {
                        LogUtil.e("hostAddress:------:> result--->block---> $it")
                        block(it)
                        return@first true
                    }
                    return@first false
                }
            }.onFailure {
                block("network ---> error :" + it.message)
            }
        } else {
            block(mIpAddress)
        }
    }
}