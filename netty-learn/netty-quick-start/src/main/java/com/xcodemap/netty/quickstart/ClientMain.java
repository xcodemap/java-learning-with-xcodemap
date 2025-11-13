package com.xcodemap.netty.quickstart;

/**
 * Netty客户端启动类
 */
public class ClientMain {
    public static void main(String[] args) {
        int port = 8080;
        String host = "localhost";
        NettyClient client = null;
        
        try {
            client = new NettyClient(host, port);
            client.start(); // 启动客户端
            
            System.out.println("Netty客户端已启动，连接到: " + host + ":" + port);
            
            // 发送测试消息
            Message message1 = new Message("msg-001", "test-topic-1", "Hello Netty!");
            System.out.println("发送消息1: " + message1);
            AckResult result1 = client.sendMessage(message1);
            System.out.println("测试1结果: " + result1);
            
            /*Message message2 = new Message("msg-002", "test-topic-2", "FastJSON序列化测试");
            System.out.println("发送消息2: " + message2);
            AckResult result2 = client.sendMessage(message2);
            System.out.println("测试2结果: " + result2);*/
            
            System.out.println("客户端测试完成!");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            if (client != null) {
                client.shutdown();
            }
        }
    }
}
