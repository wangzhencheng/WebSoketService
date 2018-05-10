package com.example.websoket.controller;

import com.example.websoket.wscore.WebSocketComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private WebSocketComponent webSocketComponent;

    @RequestMapping("/sendAll")
    public String sendAll() {
        webSocketComponent.getWebSocketUtil().sendAll("sendAll!");
        return "ok";
    }
}
