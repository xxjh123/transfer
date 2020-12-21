package com.example.mqtt;

import com.example.mqtt.Config.MQTTServer;
import com.example.mqtt.Config.MQTTSubscribe;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MqttApplication {
    @Autowired
    private MQTTSubscribe mqttSubscribe;
    ///private MQTTServer mqttServer;
    //接受订阅的接口和消息,mqtt消费端
    private MQTTServer mqttServer;
    {
        try {
            mqttServer = new MQTTServer();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @PostConstruct
    public void consumeMqttClient() throws MqttException {

        mqttSubscribe.init();           // 订阅 消息
        mqttServer.PublishMessage();   // 发布 消息
    }



    public static void main(String[] args) {
        SpringApplication.run( MqttApplication.class, args );
    }

}
