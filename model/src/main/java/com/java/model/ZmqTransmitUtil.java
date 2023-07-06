package com.java.model;


import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ZmqTransmitUtil {

    private static ZContext mZContext;
    private static ZMQ.Socket mZContextSocket;
    private static String tcp = "tcp://192.168.124.4:6666";

    public static void main(String[] args) {
        initZmq();
    }

    private static void initZmq() {
        mZContext = new ZContext(1);
        mZContextSocket = mZContext.createSocket(SocketType.PAIR);
        System.out.println("initZmq !");

        boolean bind = mZContextSocket.bind(tcp);
        while (!Thread.currentThread().isInterrupted()) {
            byte[] recv = mZContextSocket.recv(0);
            String content = new String(recv, ZMQ.CHARSET);
            System.out.println("content:" + content);
        }
    }
}