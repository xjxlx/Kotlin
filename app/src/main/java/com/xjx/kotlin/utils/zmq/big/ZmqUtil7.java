package com.xjx.kotlin.utils.zmq.big;

import com.android.apphelper2.utils.LogUtil;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZmqUtil7 {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    private int port = 6666;
    private ZMQ.Socket mServiceSocket;
    private boolean isBind = false;

    public static void log(String content) {
        LogUtil.e("ZMQ-NEW", content);
    }

    public void initService(String ip) {
        String tcp = "tcp://" + ip + ":" + port;
        log("tcp:" + tcp);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try (ZContext context = new ZContext()) {
                    // Socket to talk to clients
                    mServiceSocket = context.createSocket(SocketType.DEALER);
                    isBind = mServiceSocket.bind(tcp);

                    while (!Thread.currentThread().isInterrupted()) {
                        // Block until a message is received
                        byte[] reply = mServiceSocket.recv(0);
                        log("Received: [" + new String(reply, ZMQ.CHARSET) + "]");

                        String response = "Hello, world!";
                        mServiceSocket.send(response.getBytes(ZMQ.CHARSET), 0);

                        log("Received: [" + response + "]");
                    }
                } catch (ZMQException e) {
                    log("service zmq error: [" + e.getMessage() + "]");
                }
            }
        });
        executorService.shutdown();
    }

    public void send(String content) {
        if (isBind) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // Send a response
                    String response = "Hello, world!";
                    mServiceSocket.send(response.getBytes(ZMQ.CHARSET), 0);
                }
            });
            executorService.shutdown();
        } else {
            log("isBind is fail !");
        }
    }

    public void initClient(String ip) {
        String tcp = "tcp://" + ip + ":" + port;
        log("tcp:" + tcp);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try (ZContext ctx = new ZContext()) {
                    ZMQ.Socket client = ctx.createSocket(SocketType.DEALER);

                    // client.setIdentity(identity.getBytes(ZMQ.CHARSET));
                    client.connect(tcp);

                    int requestNbr = 0;
                    while (!Thread.currentThread().isInterrupted()) {
                        //  Tick once per second, pulling in arriving messages
                        byte[] recv = client.recv(0);
                        String content = new String(recv, ZMQ.CHARSET);
                        log("recv:" + content);
                    }
                }
            }
        });
        executorService.shutdown();
    }
}
