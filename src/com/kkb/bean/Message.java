package com.kkb.bean;

public class Message {
    // 后端接收到json的数据格式
    // {result:"", data:{}}  data可以是数组，也可以是其它格式的数据

    // 状态码 0 表示成功，-1 表示失败
    private int status;
    // 消息内容
    private String result;
    // 消息所携带的一组数据
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Message() {
    }

    public Message(int status, String result) {
        this.status = status;
        this.result = result;
    }

    public Message(String result) {
        this.result = result;
    }

    public Message(int status) {
        this.status = status;
    }

    public Message(int status, String result, Object data) {
        this.status = status;
        this.result = result;
        this.data = data;
    }
}
