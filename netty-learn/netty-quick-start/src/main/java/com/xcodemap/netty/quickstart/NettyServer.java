package com.xcodemap.netty.quickstart;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.charset.StandardCharsets;

/**
 * Netty服务端
 */
public class NettyServer {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加长度字段解码器，解决TCP粘包问题
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                            // 添加长度字段编码器
                            pipeline.addLast(new LengthFieldPrepender(4));
                            // 添加业务处理器
                            pipeline.addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Netty服务器启动，监听端口: " + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 服务端处理器
     */
    private static class ServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf byteBuf = (ByteBuf) msg;
            try {
                // 读取消息内容
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                String jsonStr = new String(bytes, StandardCharsets.UTF_8);
                
                // 反序列化为Message对象
                Message message = JSON.parseObject(jsonStr, Message.class);
                System.out.println("服务端接收到消息: " + message);
                
                // 创建AckResult响应，使用Message的id
                AckResult ackResult = new AckResult(message.getId(), "SUCCESS");
                
                // 序列化AckResult并发送
                String ackJson = JSON.toJSONString(ackResult);
                byte[] ackBytes = ackJson.getBytes(StandardCharsets.UTF_8);
                ByteBuf responseBuf = ctx.alloc().buffer(ackBytes.length);
                responseBuf.writeBytes(ackBytes);
                
                ctx.writeAndFlush(responseBuf);
                System.out.println("服务端发送确认: " + ackResult);
                
            } catch (Exception e) {
                e.printStackTrace();
                // 发送错误响应，尝试从消息中获取id
                String messageId = "unknown";
                try {
                    byte[] bytes = new byte[byteBuf.readableBytes()];
                    byteBuf.readBytes(bytes);
                    String jsonStr = new String(bytes, StandardCharsets.UTF_8);
                    Message message = JSON.parseObject(jsonStr, Message.class);
                    messageId = message.getId();
                } catch (Exception ex) {
                    // 如果无法解析消息，使用默认id
                }
                
                AckResult errorResult = new AckResult(messageId, "ERROR");
                String errorJson = JSON.toJSONString(errorResult);
                byte[] errorBytes = errorJson.getBytes(StandardCharsets.UTF_8);
                ByteBuf errorBuf = ctx.alloc().buffer(errorBytes.length);
                errorBuf.writeBytes(errorBytes);
                ctx.writeAndFlush(errorBuf);
            } finally {
                byteBuf.release();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
