package com.utils;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.ApplicationContext;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator{
    private static volatile ApplicationContext context;
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        System.out.println(httpSession);
//这里获取不到httpsession，由于本地直接访问的ws，可能没有生成
//        需要访问一个别的url进行获取session（未尝试）
        config.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}