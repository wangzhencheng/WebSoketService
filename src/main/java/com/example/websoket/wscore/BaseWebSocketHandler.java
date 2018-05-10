package com.example.websoket.wscore;

/**
 * 处理websocket请求的基类。
 */
public class BaseWebSocketHandler implements WebSocketMsgHandler {

    @Override
    public boolean shouldHandle(WebSocketUtil tools, WsInfo wsInfo, int connType) {
        return false;
    }

    @Override
    public void onOpen(WebSocketUtil tools, WsInfo wsInfo) {
        wsInfo.ws.send("onOpen:" + wsInfo);
    }

    @Override
    public void onGetMsgBody(WebSocketUtil tools, WsInfo wsInfo) {
        wsInfo.ws.send("onGetMsgBody:" + wsInfo);
    }

    @Override
    public void onClose(WebSocketUtil tools, WsInfo wsInfo) {
        wsInfo.ws.send("onClose:" + wsInfo);
    }
}
