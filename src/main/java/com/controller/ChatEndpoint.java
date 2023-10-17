package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pojo.FromMessage;
import com.utils.GetHttpSessionConfigurator;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


///目前是房间内聊天在使用
@ServerEndpoint(value = "/room/{room-id}", configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {

    //    采用的是roomId
    private static final Map<Integer, List<ChatEndpoint>> Roomers = new ConcurrentHashMap<>();
    private boolean isHost;


    //和某个客户端连接对象，需要通过他来给客户端发送数据
    private Session session;

    @OnOpen
    //连接建立成功调用
    public void onOpen(Session session, EndpointConfig config, @PathParam("room-id") Integer roomId) {

        this.session = session;
        //存储该链接对象
//        这里不能发送roomId，应该放一个
        List<ChatEndpoint> list;
        if (Roomers.containsKey(roomId)) {
            list = Roomers.get(roomId);
        } else {
            list = new ArrayList<>();
            isHost = true;
        }
        list.add(this);
        Roomers.put(roomId, list);
//        Roomers.put(roomId,this);
    }

    @OnMessage
    //接收到消息时调用
    public void onMessage(String message, Session session, @PathParam("room-id") Integer roomId) {
        try {
//            这里约定房主是1，其他用户就是0
            //获取客户端发送来的数据  {"fromId":"1","message":"你好"}String
            ObjectMapper mapper = new ObjectMapper();
            FromMessage fromMessage = mapper.readValue(message, FromMessage.class);

//                fromId如果是1证明是房主发的，只需要把信息推给用户，相反就推给用户，但是两都需要进行渲染
//            第二种思路是前端渲染好页面再发送信息

            List<ChatEndpoint> list = Roomers.get(roomId);
            String FromMessageStr = mapper.writeValueAsString(fromMessage);
            for (ChatEndpoint chatEndpoint : list) {
                chatEndpoint.session.getBasicRemote().sendText(FromMessageStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClose
    public void onClose(Session session, @PathParam("room-id") Integer roomId) {
        List<ChatEndpoint> connections = Roomers.get(roomId);
        if (connections != null) {


            // 移除连接对象
            connections.remove(this);

            // 向其他房间内的连接对象发送消息
            for (Integer key : Roomers.keySet()) {
                if (!key.equals(roomId)) {
                    continue;
                }
                List<ChatEndpoint> connections2 = Roomers.get(key);
                for (ChatEndpoint chatEndpoint : connections2) {
                    try {
                        FromMessage fromMessage = new FromMessage();
                        if (isHost) {
                            fromMessage.setMessage("房主离开房间");
                        } else {
                            fromMessage.setMessage("用户离开房间");
                        }
                        ObjectMapper objectMapper = new ObjectMapper();
                        String fromMessageStr = objectMapper.writeValueAsString(fromMessage);
                        chatEndpoint.session.getBasicRemote().sendText(fromMessageStr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 如果房间内没有连接了，可以将房间从 Roomers 中移除
            if (connections.isEmpty()) {
                Roomers.remove(roomId);
            }
        }
    }

}