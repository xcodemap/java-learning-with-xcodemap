package com.xcodemap.netty.quickstart;

/**
 * 消息类，包含id、topic和content字段
 */
public class Message {
    private String id;
    private String topic;
    private String content;

    public Message() {
    }

    public Message(String id, String topic, String content) {
        this.id = id;
        this.topic = topic;
        this.content = content;
    }

    public Message(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", topic='" + topic + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
