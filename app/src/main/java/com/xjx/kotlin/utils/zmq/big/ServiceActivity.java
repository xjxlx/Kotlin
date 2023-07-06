package com.xjx.kotlin.utils.zmq.big;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xjx.kotlin.R;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.UUID;

public class ServiceActivity extends AppCompatActivity {

    private String tcp;
    private EditText et_ip;
    private TextView tv_content;
    private StringBuffer stringBuffer = new StringBuffer();
    private ZContext mContext;
    private ZMQ.Socket socketService;
    private ZMQ.Socket clientService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        et_ip = findViewById(R.id.et_ip);
        tv_content = findViewById(R.id.tv_content);
        tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        findViewById(R.id.btn_test).setOnClickListener(v -> initTcpService());
        findViewById(R.id.btn_test2).setOnClickListener(v -> initClient());
        findViewById(R.id.btn_test3).setOnClickListener(v -> {
            if (socketService != null) {
                socketService.close();
            }
        });
        tcp = et_ip.getText().toString();
        et_ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tcp = et_ip.getText().toString();
            }
        });

        initZContext();
    }

    /**
     * 初始化ZContext
     */
    private void initZContext() {
        mContext = new ZContext(1);
    }


    /**
     * 初始化TCP服务
     */
    private void initTcpService() {
        stringBuffer.append("初始化服务端--->" + tcp).append("\n");
        new Thread(() -> {
            try {
                // Socket to talk to clients
                socketService = mContext.createSocket(SocketType.PAIR);
                socketService.bind(tcp);
                stringBuffer.append("服务端初始化成功").append("\n");
                setData();
                while (!Thread.currentThread().isInterrupted()) {
                    // Block until a message is received
                    byte[] reply = socketService.recv(0);
                    stringBuffer.append("服务端接收消息--->" + new String(reply, ZMQ.CHARSET)).append("\n");
                    // Send a response
                    String response = "服务端的消息(" + UUID.randomUUID() + ")";
                    socketService.send(response.getBytes(ZMQ.CHARSET), 0);
                    stringBuffer.append("服务端发送消息--->" + response).append("\n");
                    setData();
                }
            } catch (Exception e) {
                Log.d("ZJW_LOG", "初始化服务端异常--->" + e.getMessage());
                setData();
                e.printStackTrace();
            }
        }).start();
    }


    /**
     * 初始化客户端
     */
    private void initClient() {
        stringBuffer.append("客户端连接--->" + tcp).append("\n");
        String uuid = UUID.randomUUID().toString();
        new Thread(() -> {
            try {
                clientService = mContext.createSocket(SocketType.PAIR);
//                clientService = mContext.createSocket(SocketType.REQ);//TODO 服务端对应SocketType.REP
                clientService.connect(tcp);
                String message = "客户端的消息(" + uuid + ")";
                clientService.send(message);
                stringBuffer.append("客户端发送消息--->" + message).append("\n");
                byte[] recv = clientService.recv();
                stringBuffer.append("客户端接收到服务端发送的数据---->" + new String(recv)).append("\n");
                setData();
            } catch (Exception e) {
                stringBuffer.append("客户端连接发送异常--->" + e.getMessage()).append("\n");
                setData();
                e.printStackTrace();
            }
        }).start();
    }

    private void setData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_content.setText(stringBuffer.toString());
            }
        });
    }

}
