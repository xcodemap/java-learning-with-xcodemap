package com.xcodemap.netty.quickstart;

/**
 * 确认结果类，包含messageId和status字段
 */
public class AckResult {
    private String messageId;
    private String status;

    public AckResult() {
    }

    public AckResult(String messageId, String status) {
        this.messageId = messageId;
        this.status = status;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AckResult{" +
                "messageId='" + messageId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
