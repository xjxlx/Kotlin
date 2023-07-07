package com.java.model;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ZmqTestClient {

    public static void main(String[] args) {
        System.out.println("ZmqTestClient:  --->");

        try (ZContext context = new ZContext()) {
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.connect("tcp://192.168.88.140:7777");
            System.out.println("connect:  --->");

            while (!Thread.currentThread().isInterrupted()) {
                // Block until a message is received
                byte[] reply = socket.recv(0);

                // Print the message
                System.out.println("Received: [" + new String(reply, ZMQ.CHARSET) + "]");

                // Send a response
                String response = "client Hello, world!";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
                System.out.println("connect:  --->" + response);
            }
        }
    }

}
