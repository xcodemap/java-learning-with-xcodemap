package com.xcodemap.netty.quickstart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty客户端
 */
public class NettyClient {
    private final String host;
    private final int port;
    private final Bootstrap bootstrap;
    private EventLoopGroup group;
    private boolean started = false;
    
    // 全局维护请求回复关系的HashMap
    private final ConcurrentHashMap<String, CompletableFuture<AckResult>> pendingRequests = new ConcurrentHashMap<>();

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.bootstrap = new Bootstrap();
    }

    public void start() {
        if (started) {
            return;
        }
        
        // 创建EventLoopGroup
        group = new NioEventLoopGroup();
        
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 添加长度字段解码器
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                        // 添加长度字段编码器
                        pipeline.addLast(new LengthFieldPrepender(4));
                        // 添加Message编码器
                        pipeline.addLast(new MessageEncoder());
                        // 添加AckResult解码器
                        pipeline.addLast(new AckResultDecoder());
                        // 添加业务处理器
                        pipeline.addLast(new ClientHandler(pendingRequests));
                    }
                });
        
        started = true;
    }

    public AckResult sendMessage(Message message) throws InterruptedException {
        if (!started) {
            throw new IllegalStateException("Client not started. Call start() first.");
        }
        
        CompletableFuture<AckResult> future = new CompletableFuture<>();

        try {
            // 将请求添加到全局HashMap中
            pendingRequests.put(message.getId(), future);
            
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            
            // 直接发送Message对象，编码器会自动处理序列化
            channelFuture.channel().writeAndFlush(message);
            System.out.println("客户端发送消息: " + message);
            
            // 等待响应
            AckResult result = future.get();
            
            channelFuture.channel().close();
            return result;
            
        } catch (Exception e) {
            // 如果发生异常，完成CompletableFuture
            future.completeExceptionally(e);
            throw new RuntimeException(e);
        } finally {
            // 清理HashMap中的请求
            pendingRequests.remove(message.getId());
        }
    }

    /**
     * 关闭客户端
     */
    public void shutdown() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    /**
     * 客户端处理器
     */
    private static class ClientHandler extends ChannelInboundHandlerAdapter {
        private final ConcurrentHashMap<String, CompletableFuture<AckResult>> pendingRequests;

        public ClientHandler(ConcurrentHashMap<String, CompletableFuture<AckResult>> pendingRequests) {
            this.pendingRequests = pendingRequests;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            // 解码器已经将ByteBuf转换为AckResult对象
            AckResult ackResult = (AckResult) msg;
            
            try {
                // 通过messageId查找对应的请求
                CompletableFuture<AckResult> future = pendingRequests.get(ackResult.getMessageId());
                if (future != null) {
                    System.out.println("客户端接收到确认: " + ackResult);
                    // 完成CompletableFuture
                    future.complete(ackResult);
                } else {
                    System.out.println("接收到未匹配的响应: " + ackResult);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                // 发生异常时，需要通知所有等待的请求
                for (CompletableFuture<AckResult> future : pendingRequests.values()) {
                    future.completeExceptionally(e);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            // 发生异常时，需要通知所有等待的请求
            for (CompletableFuture<AckResult> future : pendingRequests.values()) {
                future.completeExceptionally(cause);
            }
            ctx.close();
        }
    }
}
