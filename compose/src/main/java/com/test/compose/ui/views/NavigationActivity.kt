package com.test.compose.ui.views

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.common.base.compose.BaseTitleActivity
import com.android.common.utils.LogUtil
import com.google.gson.Gson
import java.io.Serializable

class NavigationActivity : BaseTitleActivity() {

    object Route {
        const val ROUTE_FIRST = "routeFirst"
        const val ROUTE_SECOND = "routeSecond"
        const val ROUTE_THIRD = "routeThird"
        const val ROUTE_FOUR = "route_four"
    }

    object Parameter {
        const val NAME = "name"
        const val AGE = "age"
    }

    data class User(val name: String? = null, val age: Int = 0) : Serializable {
        override fun toString(): String {
            return "User(name=$name, age=$age)"
        }
    }

    override fun getTitleContent(): String {
        return "Navigation"
    }

    /**
     * 注意事项：
     *  1：startDestination 和 第一个页面，必须route 路径保持一致
     *  2：composable 标注的 route ,必须和跳转过来的路径保持一致，如果有参数，则必修声明，不然会报错
     *  3：声明composable的 接收：route
     *          格式："${Route.ROUTE_FIRST}/{key}"
     *          route = "${Route.ROUTE_FIRST}/{${Parameter.NAME}}"
     *  4：发送 跳转的方式：
     *     带基本参数的跳转，格式为："${Route.ID}/参数的具体值"
     *     navController.navigate("${Route.ROUTE_THIRD}/$name")
     */
    @Preview
    @Composable
    override fun InitTitleView() {
        val navController = rememberNavController()  //导航控制器
        NavHost(
            navController = navController, startDestination = "${Route.ROUTE_FIRST}/{${Parameter.NAME}}", //启始页
            builder = {
                // 1: navigation的无参数跳转
                composable(route = "${Route.ROUTE_FIRST}/{${Parameter.NAME}}") { //route: 表示路由名称，跳转时需要
                    FirstScreen {
                        // 跳转时候，不需要额外的参数，直接写跳转的路径
                        navController.navigate(Route.ROUTE_SECOND)
                    }
                }

                // 2:navigation的基本参数跳转
                // 格式： object Detail:Screen(route = "$DETAIL?id={id},name={name},password={password}")

                // 例子：
                //  composable(route = "路由名B/{参数名b}",
                //      arguments = listOf(
                //         navArgument("参数名b") {
                //           type = NavType.IntType   //参数类型
                //          defaultValue = 18        //默认值
                //          nullable = true          //是否可空
                //      }
                //  )
                //)
                composable(route = Route.ROUTE_SECOND) {
                    val name = "张三"
                    SecondScreen(name) {
                        // 2：带基本参数的跳转，格式为："${Route.ID}/参数的具体值"
                        navController.navigate("${Route.ROUTE_THIRD}/$name")
                    }
                }

                // 3：带基本参数的接收，带数据类型的跳转
                // 接收的格式为："上一个Route.ID/{具体的参数名}
                // 例如： "${Route.ROUTE_THIRD}/{${Parameter.NAME}}""
                composable(route = "${Route.ROUTE_THIRD}/{${Parameter.NAME}}") {
                    val name = it.arguments?.getString(Parameter.NAME)
                    ThirdScreen(name) {
                        val user = User(name = "李四", age = 22)
                        val json = Gson().toJson(user)
                        navController.navigate("${Route.ROUTE_FOUR}/${json}")
                    }
                }

                // 4: 带数据类型的参数
                composable(route = "${Route.ROUTE_FOUR}/{${Parameter.NAME}}") {
                    val content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val user = it.arguments?.getParcelable(Parameter.NAME, User::class.java)
                        "user:" + user.toString()
                    } else {
                        val string = it.arguments?.getString(Parameter.NAME)
                        "string:$string"
                    }
                    FourScreen(content) {
                        navController.navigate("${Route.ROUTE_FIRST}/${content}")

                        // 返回上个界面
                        navController.popBackStack()
                    }
                }
            })
    }

    @Composable
    fun FirstScreen(navigateTo: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
            horizontalAlignment = Alignment.CenterHorizontally,  //横向居中
            verticalArrangement = Arrangement.Center,  //纵向居中
        ) {
            LogUtil.e("FirstScreen")
            Text(text = "这是第一个界面")
            Button(onClick = { navigateTo.invoke() }) {
                Text(text = "跳转到第二页")
            }
        }
    }

    @Composable
    fun SecondScreen(name: String, navigateTo: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
            horizontalAlignment = Alignment.CenterHorizontally,  //横向居中
            verticalArrangement = Arrangement.Center,  //纵向居中
        ) {
            LogUtil.e("SecondScreen")
            Text(text = "这是第二个界面 传递的参数为:$name")
            Button(onClick = { navigateTo.invoke() }) {
                Text(text = "跳转到第三页")
            }
        }
    }

    @Composable
    fun ThirdScreen(value: String?, navigateTo: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally,  //横向居中
            verticalArrangement = Arrangement.Center,  //纵向居中
        ) {
            LogUtil.e("ThirdScreen")
            Text(text = "这是第三个界面，接收第三个界面的参数位：: $value")
            Button(onClick = { navigateTo.invoke() }) {
                Text(text = "跳转到第四页")
            }
        }
    }

    @Composable
    fun FourScreen(content: String?, navigateTo: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow),
            horizontalAlignment = Alignment.CenterHorizontally,  //横向居中
            verticalArrangement = Arrangement.Center,  //纵向居中
        ) {
            LogUtil.e("FourScreen")
            Text(text = "这是第四个界面 内容: $content")
            Button(onClick = { navigateTo.invoke() }) {
                Text(text = "回到第一页")
            }
        }
    }
}