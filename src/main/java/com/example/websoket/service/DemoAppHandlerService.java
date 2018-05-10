package com.example.websoket.service;

import com.example.websoket.wscore.WebSocketHandler;
import com.example.websoket.wscore.WebSocketUtil;
import com.example.websoket.wscore.WsInfo;
import org.springframework.stereotype.Service;

/**
 * 测试DemoAppHandler
 * @Author 王贞成
 * @Date 2018/5/10 14:40
 **/
@Service
public class DemoAppHandlerService {

    @WebSocketHandler("/hello")
    public void hello(WebSocketUtil tools, WsInfo wsInfo) {
        wsInfo.ws.send("hello11111,this is msg from server!" + Math.random());
    }

    @WebSocketHandler("/hello2")
    public void hello2(WebSocketUtil tools, WsInfo wsInfo) {
        wsInfo.ws.send("hello222,this is msg from server!" + Math.random());
    }
}
