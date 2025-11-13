package com.xcodemap.netty.quickstart;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * AckResult解码器，将ByteBuf解码为AckResult对象
 * 注意：此解码器位于LengthFieldBasedFrameDecoder之后，接收的是已经处理过粘包的完整ByteBuf
 */
public class AckResultDecoder extends MessageToMessageDecoder<ByteBuf> {
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        // 读取响应内容
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String jsonStr = new String(bytes, StandardCharsets.UTF_8);
        
        // 反序列化为AckResult对象
        AckResult ackResult = JSON.parseObject(jsonStr, AckResult.class);
        out.add(ackResult);
    }
}

