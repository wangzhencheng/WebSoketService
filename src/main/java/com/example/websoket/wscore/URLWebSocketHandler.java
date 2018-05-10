package com.example.websoket.wscore;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 配合{@link WebSocketHandler}注册普通的bean为WebSocketHandler。
 * bean需要在{@link URLWebSocketHandler#registerHandler(Object)}添加注册
 * @Author 王贞成
 * @Date 2018/5/10 14:05
 **/
public class URLWebSocketHandler extends AppWebSocketHandler {


    //保存注册的handler
    private HashMap<String, ClassUrlHandler> classUrlHandlerHashMap = new HashMap<>();

    public URLWebSocketHandler(String appId) {
        super(appId);
    }

    /**
     * 利用反射，从含有@WebSocketHandler注解的方法中添加对应的handler
     *
     * @param target
     * @return
     */
    public URLWebSocketHandler registerHandler(Object target) {
        if (null != target) {
            Method[] methods = target.getClass().getDeclaredMethods();
            if (null != methods) {
                for (Method m : methods) {
                    m.setAccessible(true);
                    WebSocketHandler webSocketHandler = m.getDeclaredAnnotation(WebSocketHandler.class);
                    if (webSocketHandler != null) {
                        String path = webSocketHandler.value();
                        if (classUrlHandlerHashMap.containsKey(path)) {
                            throw new RuntimeException("WebSocketHandler中path重复:" + path);
                        }
                        classUrlHandlerHashMap.put(path, new ClassUrlHandler(path, target, m));
                    }
                }
            }
        }
        return this;
    }

    @Override
    public void onGetMsgBody(WebSocketUtil tools, WsInfo wsInfo) {
        String url = wsInfo.headers.get("url");
        ClassUrlHandler handler = classUrlHandlerHashMap.get(url);
        if (null != handler) {
            handler.handle(tools, wsInfo);
        }
    }

}
