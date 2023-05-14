package com.xjx.kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.xjx.kotlin.utils.zmq.send.ZmqUtil

class ZmqActivity : AppCompatActivity() {

    private val mZmqUtil: ZmqUtil by lazy {
        return@lazy ZmqUtil()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zmq)


        findViewById<EditText>(R.id.et_input).apply {
            visibility = View.VISIBLE
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    mZmqUtil.sendMessage(s.toString())
                }
            })
        }
    }
}