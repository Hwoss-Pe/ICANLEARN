package com.utils;

import com.service.RoomService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;


@Component
public class UUIDUtil {

    @Autowired
    private  RoomService roomService;

    public static String simpleUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
