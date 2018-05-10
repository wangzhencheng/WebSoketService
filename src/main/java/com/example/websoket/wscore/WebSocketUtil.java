package com.example.websoket.wscore;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * WebSocket工具类,管理连接、发送消息。
 * 注意格式
 * header是k:v形式，具体如下：(客户端连接后，需要发送一次消息，作认证记录，否则不会记录在案，不会推送消息。)
 * k:v\r\nk1:v2\r\n\r\nbodyMsg
 */
public class WebSocketUtil extends WebSocketServer {

    private Map<WebSocket, WsInfo> clienIdSockets;
    private CustomWebSocketListener customWebSocketListener;


    public WebSocketUtil(int port) {
        super(new InetSocketAddress(port));
        clienIdSockets = Collections.synchronizedMap(
                new HashMap<>()
        );

    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        WsInfo wsInfo = clienIdSockets.remove(conn);
        if (null != wsInfo && null != customWebSocketListener) {
            customWebSocketListener.onClose(this, wsInfo);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        WsInfo wsInfo = WsInfo.resolve(conn, message);
        if (wsInfo != null) {
            WsInfo lastWsInfo = clienIdSockets.put(conn, wsInfo);
            onGetMsgBody(wsInfo, lastWsInfo == null);
        }
    }

    public synchronized int sendAll(String msg) {
        int count = 0;
        ArrayList<WsInfo> wsInfos = new ArrayList<>(clienIdSockets.values());
        for (WsInfo wsInfo : wsInfos) {
            if (sendMsg(wsInfo, msg)) {
                count++;
            }
        }
        return count;
    }

    public synchronized boolean sendMsg(WsInfo wsInfo, String msg) {
        int successCount = 0;
        if (null != msg && wsInfo != null && (wsInfo.ws.getReadyState() == WebSocket.READYSTATE.OPEN
                || wsInfo.ws.getReadyState() == WebSocket.READYSTATE.CONNECTING)) {
            wsInfo.ws.send(msg);
            successCount++;
        }
        return successCount > 0;
    }

    public synchronized int sendMsg(List<WsInfo> wsInfos, String msg) {
        int successCount = 0;
        for (WsInfo wsInfo : wsInfos) {
            if (sendMsg(wsInfo, msg)) successCount++;
        }
        return successCount;
    }

    public synchronized List<WsInfo> getWsInfoByAppId(String appId) {
        List<WsInfo> wsInfos = new ArrayList<>();
        if (appId == null) {
            return null;
        }

        for (Map.Entry<WebSocket, WsInfo> entry : clienIdSockets.entrySet()) {
            if (appId.equals(entry.getValue().appId)) {
                wsInfos.add(entry.getValue());
                break;
            }
        }

        return wsInfos;
    }

    public synchronized List<WsInfo> getWsInfoByTag(String appId, String tag) {
        List<WsInfo> wsInfos = new ArrayList<>();
        if (appId == null || tag == null) {
            return null;
        }
        for (Map.Entry<WebSocket, WsInfo> entry : clienIdSockets.entrySet()) {
            if (appId.equals(entry.getValue().appId) && tag.equals(entry.getValue().tag)) {
                wsInfos.add(entry.getValue());
                break;
            }
        }
        return wsInfos;
    }

    public synchronized WsInfo getWsInfoByClientId(String appId, String clientId) {
        WsInfo wsInfo = null;
        if (appId == null || clientId == null) {
            return null;
        }
        for (Map.Entry<WebSocket, WsInfo> entry : clienIdSockets.entrySet()) {
            if (appId.equals(entry.getValue().appId) && entry.getValue().clientId.equals(clientId)) {
                wsInfo = entry.getValue();
                break;
            }
        }
        return wsInfo;
    }

    /**
     * 获取实际消息体。
     *
     * @param wsInfo
     */
    protected void onGetMsgBody(WsInfo wsInfo, boolean isFirstTime) {
        //处理自己的业务逻辑
        if (null != customWebSocketListener) {
            if (isFirstTime) {
                customWebSocketListener.onOpen(this, wsInfo);
            } else {
                customWebSocketListener.onGetMsgBody(this, wsInfo);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {

    }


    public CustomWebSocketListener getCustomWebSocketListener() {
        return customWebSocketListener;
    }

    public void setCustomWebSocketListener(CustomWebSocketListener customWebSocketListener) {
        this.customWebSocketListener = customWebSocketListener;
    }

    /**
     * 后续可以添加额外的监听。
     */
    public interface CustomWebSocketListener {
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


}
