package com.example.websoket.wscore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个Method为WebSocket的处理.
 * 该方法会自动回调两个参数{@link WebSocketUtil} {@link WsInfo}
 * @Author 王贞成
 * @Date 2018/5/10 14:23
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketHandler {

    /**
     * 对应要处理的路径。ws客户端发送的消息中，满足{@link WebSocketUtil}的协议规定，
     * 并添加url:/handlerPath 进行匹配
     *
     * @return
     */
    String value();
}
