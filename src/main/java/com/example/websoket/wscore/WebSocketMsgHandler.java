package com.example.websoket.wscore;

/**
 * 用于接收WebSocket消息时的监听
 *
 * @Author 王贞成
 * @Date 2018/5/10 10:34
 **/
public interface WebSocketMsgHandler {

    int Conn_Open = 0;
    int Conn_Msg = 1;
    int Conn_Close = 2;

    /**
     * 该监听是否处理该消息
     *
     * @param tools
     * @param wsInfo
     * @return
     */
    boolean shouldHandle(WebSocketUtil tools, WsInfo wsInfo, int connType);

    /**
     * 不代表实际第一次open。实际代表第一次发送合法消息。
     * 单纯的open会因无身份信息被忽略。
     *
     * @param tools
     * @param wsInfo
     */
    void onOpen(WebSocketUtil tools, WsInfo wsInfo);

    void onGetMsgBody(WebSocketUtil tools, WsInfo wsInfo);

    void onClose(WebSocketUtil tools, WsInfo wsInfo);

}
