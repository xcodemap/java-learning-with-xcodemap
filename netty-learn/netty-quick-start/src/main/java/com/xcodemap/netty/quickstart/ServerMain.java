package com.xcodemap.netty.quickstart;

/**
 * Netty服务端启动类
 */
public class ServerMain {
    public static void main(String[] args) {
        int port = 8080;
        
        try {
            NettyServer server = new NettyServer(port);
            System.out.println("启动Netty服务端，监听端口: " + port);
            server.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
