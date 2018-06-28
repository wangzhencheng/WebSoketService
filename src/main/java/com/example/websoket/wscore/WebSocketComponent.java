package com.example.websoket.wscore;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 管理websocket的一个工具。配置xxApplication即可。
 * 可以指定端口号。
 * 在{@link WebSocketComponent#registerHandler(WebSocketMsgHandler)}
 * 注册对应App的socket消息处理器(通过添加头信息url:path 访问对应的Service)。
 *
 * @author 王贞成
 * @date 2018/5/10
 */
public class WebSocketComponent implements InitializingBean, DisposableBean, WebSocketUtil.CustomWebSocketListener {


    private WebSocketUtil webSocketUtil;
    private List<WebSocketMsgHandler> handlers = Collections.synchronizedList(new ArrayList<>());
    private WebSocketUtil.OnSendMsg onSendMsg;

    /**
     * 端口号
     */
    private int port = 9001;//"ws://" + window.location.hostname + ":9001"


    public WebSocketUtil getWebSocketUtil() {
        return webSocketUtil;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 添加handler
     *
     * @param handler
     * @return
     */
    public WebSocketComponent registerHandler(WebSocketMsgHandler handler) {
        this.handlers.add(handler);
        return this;
    }

    public void setOnSendMsg(WebSocketUtil.OnSendMsg onSendMsg) {
        this.onSendMsg = onSendMsg;
    }

    /**
     * 自动启动WebSocket
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public synchronized void start() {
        stop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    webSocketUtil = new WebSocketUtil(port);
                    webSocketUtil.setCustomWebSocketListener(WebSocketComponent.this);
                    webSocketUtil.setOnSendMsg(WebSocketComponent.this.onSendMsg);
                    webSocketUtil.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public synchronized void stop() {
        try {
            if (null != webSocketUtil)
                webSocketUtil.stop();
            webSocketUtil = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        stop();
    }


    @Override
    public void onOpen(WebSocketUtil tools, WsInfo wsInfo) {
        List<WebSocketMsgHandler> filterHandlers = filterHandlers(tools, wsInfo, WebSocketMsgHandler.Conn_Open);
        for (WebSocketMsgHandler handler : filterHandlers) {
            handler.onOpen(tools, wsInfo);
        }
    }

    /**
     * 获取消息后的处理。注意该方法不在同一线程。
     *
     * @param tools
     * @param wsInfo
     */
    @Override
    public void onGetMsgBody(WebSocketUtil tools, WsInfo wsInfo) {
        List<WebSocketMsgHandler> filterHandlers = filterHandlers(tools, wsInfo, WebSocketMsgHandler.Conn_Msg);
        for (WebSocketMsgHandler handler : filterHandlers) {
            handler.onGetMsgBody(tools, wsInfo);
        }
    }

    @Override
    public void onClose(WebSocketUtil tools, WsInfo wsInfo) {
        List<WebSocketMsgHandler> filterHandlers = filterHandlers(tools, wsInfo, WebSocketMsgHandler.Conn_Close);
        for (WebSocketMsgHandler handler : filterHandlers) {
            handler.onClose(tools, wsInfo);
        }
    }

    public List<WebSocketMsgHandler> filterHandlers(WebSocketUtil tools, WsInfo wsInfo, int connType) {
        List<WebSocketMsgHandler> filterHandlers = new ArrayList<>();
        for (WebSocketMsgHandler webSocketMsgHandler : this.handlers) {
            if (webSocketMsgHandler.shouldHandle(tools, wsInfo, connType)) {
                filterHandlers.add(webSocketMsgHandler);
            }
        }
        return filterHandlers;
    }

}
