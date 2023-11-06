package com.controller;

import com.config.GetHttpSessionConfigurator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pojo.FromMessage;
import com.pojo.Message;
import com.utils.JwtUtils;
import io.jsonwebtoken.Claims;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


///目前是画板在使用
@ServerEndpoint(value = "/instant/draw/{token}", configurator = GetHttpSessionConfigurator.class)
@Component
public class DrawEndpoint {

    //用来存储每一个客户端对象对应的ChatEndpoint对象,还有对应的id
    private static final Map<Integer, DrawEndpoint> onlineUsers = new ConcurrentHashMap<>();

    //和某个客户端连接对象，需要通过他来给客户端发送数据
    private Session session;
//

    @OnOpen
    //连接建立成功调用
    public void onOpen(Session session, EndpointConfig config, @PathParam("token") String token) {
        //需要通知其他的客户端，将所有的用户的用户名发送给客户端
        this.session = session;

//        HttpSession httpSession =
//                (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//            //获取用户id，前端发送的时候需要把token存入请求头里面
        Claims claims = JwtUtils.parseJwt(token);
        Integer user_id = (Integer) claims.get("id");

        //存储该链接对象
        onlineUsers.put(user_id, this);
    }


    @OnMessage
    //接收到消息时调用
    public void onMessage(String message, Session session, @PathParam("token") String token) {
        try {
            //获取客户端发送来的数据  {"toId":"5","message":"你好"}String
            ObjectMapper mapper = new ObjectMapper();
            Message mess = mapper.readValue(message, Message.class);

            //获取用户id
            Claims claims = JwtUtils.parseJwt(token);
            Integer user_id = (Integer) claims.get("id");

//           获取返回的信息对象，就是发了什么给谁
            FromMessage fromMessage = new FromMessage();
            fromMessage.setFromId(user_id);
            fromMessage.setMessage(message);
            fromMessage.setX(mess.getX());
            fromMessage.setY(mess.getY());
            fromMessage.setColor(mess.getColor());
            fromMessage.setThick(mess.getThick());
            fromMessage.setCanvasId(mess.getCanvasId());
            fromMessage.setFirstDraw(mess.getFirstDraw());
            fromMessage.setFinish(mess.getFinish());
            String FromMessageStr = mapper.writeValueAsString(fromMessage);

            //将数据推送给指定的客户端
            DrawEndpoint drawEndpoint = onlineUsers.get(mess.getToId());
            drawEndpoint.session.getBasicRemote().sendText(FromMessageStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    //连接关闭时调用,建议当用户退出聊天的时候进行主动关闭
    public void onClose(Session session, @PathParam("token") String token) {
        //获取用户id
        Claims claims = JwtUtils.parseJwt(token);
        Integer user_id = (Integer) claims.get("id");
        //移除连接对象
        System.out.println("关闭" + user_id);
        onlineUsers.remove(user_id);

    }

}