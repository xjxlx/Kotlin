package com.java.model;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ZmqTestService {

    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            System.out.println("ZmqTestService:  --->");
            // Socket to talk to clients
            ZMQ.Socket socket = context.createSocket(SocketType.REP);
            socket.bind("tcp://192.168.88.140:7777");

            System.out.println("Received:  --->");

            while (!Thread.currentThread().isInterrupted()) {
                // Block until a message is received
                byte[] reply = socket.recv(0);

                // Print the message
                System.out.println(
                        "Received: [" + new String(reply, ZMQ.CHARSET) + "]"
                );

                // Send a response
                String response = "service Hello, world!";
                socket.send(response.getBytes(ZMQ.CHARSET), 0);
                System.out.println("Received:  --->" + response);

            }
        }
    }

}
