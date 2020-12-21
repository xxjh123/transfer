package com.example.mqtt.Config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 订阅端
 */
@Component
public class MQTTSubscribe {
    private static final Logger LOGGER = LoggerFactory.getLogger( MQTTSubscribe.class );
    public static final String HOST = "tcp://121.40.229.171:1883";  //你的mqtt地址
    private static final String clientid = "lptestsub";//测试clientId


    private String topic = "/v1/devices/#";//测试环境主题

    public MqttClient client;
    private MQTTConnect mqttConnect = new MQTTConnect();



    public void connect() throws MqttException {

        if (client == null) {

            client = new MqttClient( HOST, clientid, new MemoryPersistence() );
            client.setCallback( new PushCallback( MQTTSubscribe.this) );
        }
        MqttConnectOptions options = mqttConnect.getOptions();

        if (!client.isConnected()) {
            client.connect( options );
            LOGGER.info( "----------连接成功" );
        } else {
            client.disconnect();
            client.connect( mqttConnect.getOptions( options ) );
            LOGGER.info( "----------连接成功" );
        }
    }


    /**
     * 订阅某个主题，qos默认为0
     *
     * @param topic .
     */
    public void subscribe(String topic) {

        try {
            client.subscribe( topic);
            //MQTT 协议中订阅关系是持久化的，因此如果不需要订阅某些 Topic，需要调用 unsubscribe 方法取消订阅关系。
//            client.unsubscribe("需要解除订阅关系的主题");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void init() {

        try {
            connect();
            subscribe( topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
