package com.example.mqtt.Config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MQTTServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQTTServer.class);
    public static final String HOST = "tcp://localhost:1883";  //你的mqtt地址  （2）
    private static final String clientid = "server";
    public static  String Topic =  "/v1/devices/SN-40-653-70wc4e/datas";
    public static  String Topic1 = "/v1/devices/SN-40-762-17fe7d/datas";

    public MqttClient client;
    public MqttTopic topic;
    public MqttMessage message;

    private static MQTTConnect mqttConnect = new MQTTConnect();

    public MQTTServer() throws MqttException {

        connect();
    }


    public void connect() throws MqttException {
        //防止重复创建MQTTClient实例
        if (client==null) {
            //就是这里的clientId，服务器用来区分用户的，不能重复
            client = new MqttClient(HOST, clientid, new MemoryPersistence());// MemoryPersistence设置clientid的保存形式，默认为以内存保存
            //client.setCallback(new PushCallback(MQTTServer.this));
        }
        MqttConnectOptions options = mqttConnect.getOptions();
        //判断拦截状态，这里注意一下，如果没有这个判断，是非常坑的
        if (!client.isConnected()) {
            client.connect(options);
            LOGGER.info("---------------------连接成功");
        }
    }


    public  boolean publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException,
            MqttException {

        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! " + token.isComplete());
        System.out.println(PushCallback.result);
        return token.isComplete();
    }

    /**
     * MQTT发送指令
     // @param page
     // @param equipment
     * @return
     * @throws MqttException
     */
    public void sendMQTTMessage(String topic, String data) throws MqttException {
        MQTTServer server = new MQTTServer();
        server.topic = server.client.getTopic(topic);
        server.message = new MqttMessage();
        server.message.setQos(0);
        server.message.setRetained(false);
        server.message.setPayload(data.getBytes());
        server.publish(server.topic , server.message);
    }

    public static String sendDate = new String(  );
    public void PublishMessage() throws MqttException {
        while (true) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                sendDate = simpleDateFormat.format( date );
                if (PushCallback.Topic.equals( "/v1/devices/SN-40-663-b6b16e/datas" )) {
                    sendMQTTMessage( Topic, PushCallback.result );
                } else if (PushCallback.Topic.equals( "/v1/devices/SN-40-787-61c80e/datas" )) {
                    sendMQTTMessage( Topic1, PushCallback.result );
                }

        }
    }
}
