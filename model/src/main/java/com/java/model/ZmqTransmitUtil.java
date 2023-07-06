package com.java.model;


import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ZmqTransmitUtil {

    private static String mClientTcp = "tcp://192.168.124.4:6666";
    private static ZContext mClientZContext;
    private static ZMQ.Socket mClientZContextSocket;
    private static ZContext mServiceZContext;
    private static ZMQ.Socket mServiceContextSocket;
    private static String mServiceTcp = "tcp://127.0.0.1:6666";

    public static void main(String[] args) {
        initServiceZmq();
        initClientZmq();
    }

    private static void initClientZmq() {
        mClientZContext = new ZContext(1);
        mClientZContextSocket = mClientZContext.createSocket(SocketType.PAIR);
        System.out.println("initClientZmq !");

        boolean bind = mClientZContextSocket.bind(mClientTcp);
        while (!Thread.currentThread().isInterrupted()) {
            byte[] recv = mClientZContextSocket.recv(0);
            String content = new String(recv, ZMQ.CHARSET);
            System.out.println("content:" + content);

            send(recv);
        }
    }

    private static void initServiceZmq() {
        try {
            mServiceZContext = new ZContext(1);
            mServiceContextSocket = mServiceZContext.createSocket(SocketType.PAIR);
            System.out.println("initServiceZmq !");
            boolean connect = mServiceContextSocket.connect(mServiceTcp);
            System.out.println("initServiceZmq connect !");
        } catch (Exception e) {
            System.out.println("initServiceZmq error: " + e.getMessage());
        }
    }

    private static void send(byte[] content) {
        if (mServiceContextSocket != null) {
            mServiceContextSocket.send(content, 0);
        }
    }
}