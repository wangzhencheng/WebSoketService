package com.example.websoket.wscore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * 抽象带有@WebSocketHandler注解方法，为可执行类。
 *
 * @Author 王贞成
 * @Date 2018/5/10 14:09
 **/
public class ClassUrlHandler {
    private String url;
    private Object target;
    private Method method;

    public ClassUrlHandler() {

    }

    public ClassUrlHandler(String url, Object target, Method method) {
        this.url = url;
        this.target = target;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void handle(WebSocketUtil tools, WsInfo wsInfo) {
        try {
            Parameter[] parameters = method.getParameters();
            if (null == parameters || parameters.length == 0) {
                method.invoke(target);
            } else if (parameters.length == 1) {
                Type type = parameters[0].getParameterizedType();
                if (WebSocketUtil.class.equals(type)) {
                    method.invoke(target, tools);
                }
                if (WsInfo.class.equals(type)) {
                    method.invoke(target, wsInfo);
                }
            } else if (parameters.length == 2) {
                if (WebSocketUtil.class.equals(parameters[0].getParameterizedType())) {
                    method.invoke(target, tools, wsInfo);
                } else {
                    method.invoke(target, wsInfo, tools);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
