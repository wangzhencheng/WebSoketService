package com.example.websoket.wscore;

import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装的消息信息
 * @Author 王贞成
 * @Date 2018/5/10 10:53
 **/
public class WsInfo {

    public String appId;
    public String clientId;
    public String tag;
    public WebSocket ws;
    public Map<String, String> headers;
    public String body;

    public WsInfo(Map<String, String> headers, String body, WebSocket ws) {
        this.headers = headers;
        this.body = body;
        this.appId = headers.get("appId");
        this.clientId = headers.get("clientId");
        this.tag = headers.get("tag");
        this.ws = ws;
    }

    @Override
    public String toString() {
        return "WsInfo{" +
                "headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    /**
     * 用于解析消息字符串，获取header body等信息。
     * 协议类似http1.1   \r\n 表示一条头信息  独立空行\r\n表示已经结束
     *
     * @param source
     * @return
     * @author 2018/5/10 王贞成
     */
    public static WsInfo resolve(WebSocket ws, String source) {
        WsInfo wsInfo = null;
        if (source != null) {//&& null != ws
            HashMap<String, String> headers = new HashMap<>();
            String spliter = "\r\n";//行分隔符
            String kvSplit = ":";//键值对分隔符
            String line = null;

            int startIndex = 0;
            boolean wrongFormat = true;
            while (true) {
                int lineIndex = source.indexOf(spliter, startIndex);
                if (lineIndex > 0) {
                    line = source.substring(startIndex, lineIndex).trim();
                    if (line.length() == 0) {
                        startIndex = lineIndex + 2;
                        wrongFormat = false;
                        break;
                    }
                    String[] kvArr = line.split(kvSplit);
                    if (kvArr != null && kvArr.length == 2) {
                        headers.put(kvArr[0], kvArr[1]);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
                startIndex = lineIndex + 2;
                if (startIndex >= source.length()) break;
            }
            if (!wrongFormat)
                wsInfo = new WsInfo(headers, startIndex > 0 && startIndex < source.length() ? source.substring(startIndex) : null, ws);
        }
        return wsInfo;
    }

    public static void main(String[] args) {
        String source = "name";
        System.out.println(resolve(null, source));
    }
}
