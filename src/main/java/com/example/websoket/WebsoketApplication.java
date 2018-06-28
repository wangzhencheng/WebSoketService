package com.example.websoket;

import com.example.websoket.wscore.URLWebSocketHandler;
import com.example.websoket.wscore.WebSocketComponent;
import com.example.websoket.service.DemoAppHandlerService;
import com.example.websoket.wscore.WebSocketUtil;
import com.example.websoket.wscore.WsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 基于Java-WebSocket二次封装。
 * 模块核心代码在wscore中，可以copy到其他项目中使用。
 */
@SpringBootApplication
public class WebsoketApplication {


    /**
     * 第一步 自定义Handler处理业务逻辑
     */
    @Autowired
    private DemoAppHandlerService demoAppHandlerService;

    /**
     * 第二步 添加WebSocket的URL映射处理器。
     * 可以重写 {@link URLWebSocketHandler}，处理onOpen onClose事件，比如记录用户连接状态和appId、clientId。
     * 建立连接后，客户端需要发送一次有效有信息的数据（参考{@link com.example.websoket.wscore.WebSocketUtil}）注释，
     * 用于真正记录连接和验证身份。
     * 也就是第一次消息，对应onOpen 第二次消息才会走自定义的handler
     * @return
     */
    @Bean
    public URLWebSocketHandler demoAppWebSocketHandler() {
        //DemoApp 与客户端头信息中的 appId一致。
        URLWebSocketHandler urlWebSocketHandler = new URLWebSocketHandler("DemoApp");
        urlWebSocketHandler.registerHandler(demoAppHandlerService);
        return urlWebSocketHandler;
    }

    /**
     * 注册一个websocket组件，指定端口号9001
     * 发送消息：webSocketComponent.getWebSocketUtil().getWsInfoByClientId()
     * @return
     */
    @Bean
    public WebSocketComponent webSocketComponent() {
        WebSocketComponent webSocketComponent = new WebSocketComponent();
        webSocketComponent.setPort(9001);
        webSocketComponent.registerHandler(demoAppWebSocketHandler());
        webSocketComponent.setOnSendMsg(new WebSocketUtil.OnSendMsg() {
            @Override
            public String onSendMsg(WebSocketUtil webSocketUtil, WsInfo wsInfo, String msg) {
                return wsInfo.headerString+msg;
            }
        });
        return webSocketComponent;
    }


    public static void main(String[] args) {
        SpringApplication.run(WebsoketApplication.class, args);
    }

}
