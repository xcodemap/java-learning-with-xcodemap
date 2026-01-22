package com.xcodemap;

import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.PartialResponseContext;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Main {
    
    interface Assistant {
        TokenStream chat(String message);
    }

    public static void main(String[] args) {
        // 从 -D 参数获取 API 和 key
        String apiKey = System.getProperty("llm.apiKey");
        String apiUrl = System.getProperty("llm.apiUrl", "https://api.openai.com/v1");
        String modelName = System.getProperty("llm.modelName", "gpt-4");
        
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("错误: 请通过 -Dllm.apiKey=your_api_key 参数提供 API Key");
            System.exit(1);
        }
        
        System.out.println("正在初始化 LangChain4j TokenStream Demo...");
        System.out.println("API URL: " + apiUrl);
        System.out.println("Model Name: " + modelName);
        
        // 创建流式聊天模型
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(apiUrl)
                .modelName(modelName)
                .logRequests(true)
                .logResponses(true)
                .timeout(Duration.ofSeconds(60))
                .temperature(0.0)
                .build();
        
        // 创建日期工具实例
        DateTools dateTools = new DateTools();
        
        // 创建助手，集成工具
        Assistant assistant = AiServices.builder(Assistant.class)
                .streamingChatModel(model)
                .tools(dateTools)
                .build();
        
        // 测试问题
        String question = "今天的日期是多少？并计算和2025.12.10的差距";
        System.out.println("\n问题: " + question);
        System.out.println("回答: ");
        
        // 使用 CompletableFuture 控制等待
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        // 使用 tokenStream 流式输出
        TokenStream tokenStream = assistant.chat(question);
        tokenStream.onPartialResponseWithContext(new BiConsumer<PartialResponse, PartialResponseContext>() {
                    @Override
                    public void accept(PartialResponse partialResponse, PartialResponseContext partialResponseContext) {
                        System.out.print(partialResponse.text());
                        System.out.flush();
                    }
        })
        .onCompleteResponse(new Consumer<ChatResponse>() {
            @Override
            public void accept(ChatResponse chatResponse) {
                System.out.println("\n\n流式输出完成！");
                // 完成 Future
                future.complete(null);
            }
        })
        .onError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable error) {
                System.err.println("\n错误: " + error.getMessage());
                error.printStackTrace();
                // 异常完成 Future
                future.completeExceptionally(error);
            }
        })
        .start();
        
        // 等待流式输出完成
        try {
            future.get(); // 等待 Future 完成，无论是正常完成还是异常完成
        } catch (Exception e) {
            System.err.println("等待过程中发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}