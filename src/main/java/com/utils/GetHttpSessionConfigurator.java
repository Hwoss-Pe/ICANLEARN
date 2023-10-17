package com.utils;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator{
        private static volatile ApplicationContext context;
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        System.out.println(httpSession);
//这里获取不到httpsession，由于本地直接访问的ws，可能没有生成
//        需要访问一个别的url进行获取session（未尝试）
        config.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}