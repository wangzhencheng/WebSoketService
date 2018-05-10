package com.example.websoket.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 用于执行CMD命令的辅助类
 */
public class CMDTools {

    private Process process;
    private OnGetLine onGetLine;

    public void setOnGetLine(OnGetLine onGetLine) {
        this.onGetLine = onGetLine;
    }


    public void destroy() {
        if (process != null)
            process.destroy();
        process = null;
    }

    public void destroyForcibly() {
        if (process != null)
            process.destroyForcibly();
        process = null;
    }

    //如果运行springBoot jar，输出含 "Tomcat started on port(s)" 时表示已经启动了。
    protected void onGetLine(String tag, String line) {
        if (null != onGetLine) {
            onGetLine.onGetLine(this, tag, line);
        }
    }

    public void execJar(String jarPath) {
        String cmd = "java -Dfile.encoding=UTF-8 -jar " + jarPath;
        exec(cmd);
    }


    public void exec(String cmd) {
        destroyForcibly();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Runtime runtime = Runtime.getRuntime();
                try {
                    //"java -Dfile.encoding=UTF-8 -jar " + jarPath
                    process = runtime.exec(cmd);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != process)
                                readInputStreamByLine("ErrorStream", process.getErrorStream());
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != process)
                                readInputStreamByLine("InputStream", process.getInputStream());
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void readInputStreamByLine(String tag, InputStream inputStream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                onGetLine(tag, line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public interface OnGetLine {
        void onGetLine(CMDTools cmdTools, String tag, String line);
    }

    public static void main(String[] args) throws Exception {


    }


}
