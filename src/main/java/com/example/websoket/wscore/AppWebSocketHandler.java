package com.example.websoket.wscore;

/**
 * @Author 王贞成
 * @Date 2018/5/10 14:01
 **/
public class AppWebSocketHandler extends BaseWebSocketHandler {

    /**
     * 要监听的AppId对应的连接
     */
    private String appId;

    public AppWebSocketHandler(String appId) {
        this.appId = appId;
    }

    @Override
    public boolean shouldHandle(WebSocketUtil tools, WsInfo wsInfo, int connType) {
        return null != appId && appId.equalsIgnoreCase(wsInfo.appId);
    }

    @Override
    public void onOpen(WebSocketUtil tools, WsInfo wsInfo) {
        super.onOpen(tools, wsInfo);
    }

    @Override
    public void onGetMsgBody(WebSocketUtil tools, WsInfo wsInfo) {
        super.onGetMsgBody(tools, wsInfo);
    }

    @Override
    public void onClose(WebSocketUtil tools, WsInfo wsInfo) {
        super.onClose(tools, wsInfo);
    }


}
