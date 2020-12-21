package com.example.mqtt.Config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MQTTConnect {
    private String userName = "client";  //生成配置对象，用户名，密码等
    private String passWord = "client";

    //    * 把配置里的 cleanSession 设为false，客户端掉线后 服务器端不会清除session，
//    * 当重连后可以接收之前订阅主题的消息。当客户端上线后会接受到它离线的这段时间的消息，
//    * 如果短线需要删除之前的消息则可以设置为true
    public MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(10);
        //设置心跳
        options.setKeepAliveInterval(20);
        return options;
    }

    public MqttConnectOptions getOptions(MqttConnectOptions options) {

        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);
        return options;
    }


}
