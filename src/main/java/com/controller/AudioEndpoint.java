package com.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/msgServer/{userId}")
@Component
@Scope("prototype")
public class AudioEndpoint {

    //      concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String userId = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketMap.put(userId, session);
        System.out.println(userId + " 建立成功...");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            this.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    @OnClose
    public void onClose() {
        System.out.println("close");
    }

    //   主动推送数据
    public void sendMessage(String message) throws IOException {
//        获取当前线程安全map的key放到枚举里面，这里面如果Enumeration<String> keys = Collections.enumeration(webSocketMap.keySet());也行
        Enumeration<String> keys = webSocketMap.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
//            校验当前id的值
            if (key.equals(this.userId)) {
                System.err.println("my id " + key);
                continue;
            }
//            校验是不是null
            if (webSocketMap.get(key) == null) {
                webSocketMap.remove(key);
                System.err.println(key + " : null");
                continue;
            }
            Session sessionValue = webSocketMap.get(key);
//            判断当前session的在线状态
            if (sessionValue.isOpen()) {
                System.out.println("发消息给: " + key + " ,message: " + message);
                sessionValue.getBasicRemote().sendText(message);
            } else {
                System.err.println(key + ": not open");
                sessionValue.close();
                webSocketMap.remove(key);
            }
        }
    }

    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        System.out.println("发送消息到:" + userId + "，内容:" + message);
        if (!Objects.isNull(userId) && webSocketMap.containsKey(userId)) {
//            获取对应的session后进行推流
            webSocketMap.get(userId).getBasicRemote().sendText(message);
        } else {
            System.out.println("id错误");
        }
    }
}