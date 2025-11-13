package com.xcodemap.netty.quickstart;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

/**
 * Message编码器，将Message对象编码为ByteBuf
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 将Message序列化为JSON字符串
        String jsonStr = JSON.toJSONString(msg);
        byte[] bytes = jsonStr.getBytes(StandardCharsets.UTF_8);
        // 写入ByteBuf（LengthFieldPrepender会自动添加长度字段）
        out.writeBytes(bytes);
    }
}

