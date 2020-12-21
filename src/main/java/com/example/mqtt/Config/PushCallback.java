package com.example.mqtt.Config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * 发布消息的回调类
 *
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 *
 *  public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 *
 *  public void connectionLost(Throwable cause)在断开连接时调用。
 *
 *  public void deliveryComplete(MqttDeliveryToken token))
 *  接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
 *  由 MqttClient.connect 激活此回调。
 *
 */

public class PushCallback implements MqttCallback {

    public static String result = new String(  );

    private static final Logger LOGGER = LoggerFactory.getLogger(PushCallback.class);

    private MQTTSubscribe mqttSubsribe;

    public PushCallback(MQTTSubscribe subscribe) throws MqttException {
        this.mqttSubsribe = subscribe;
    }

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        LOGGER.info("---------------------连接断开，可以做重连");

        while (true){
            try {//如果没有发生异常说明连接成功，如果发生异常，则死循环
                Thread.sleep(1000);
                mqttSubsribe.init();
                break;
            }catch (Exception e){
                continue;
            }
        }

    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }


    public static String Topic = new String(  );
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Topic= topic;
        result = new String(message.getPayload(),"UTF-8");
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + result);
        //这里可以针对收到的消息做处理
        SaveMysql saveMysql = new SaveMysql();
        Date date = new Date(  );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String subDate = simpleDateFormat.format( date );

        JSONObject jsonObject = JSONObject.parseObject( result );
        String devices = jsonObject.getString( "devices" );

        JSONArray jsonArray = JSONArray.parseArray( devices );
        String services = jsonArray.getString( 0 );

        JSONObject jsonObject1 = JSONObject.parseObject( services );
        String services1 = jsonObject1.getString( "services" );

        JSONArray jsonArray1 = JSONArray.parseArray( services1 );
        String data = jsonArray1.getString( 0 );

        JSONObject jsonObject2 = JSONObject.parseObject( data );
        String serviceId = jsonObject2.getString( "serviceId" );

        if(result != null){
            String state = "在线";
            if (topic.equals( "/v1/devices/SN-40-663-b6b16e/datas" )){
                saveMysql.save( "巡更系统","/v1/devices/SN-40-663-b6b16e/datas",result,subDate,MQTTServer.Topic,result,MQTTServer.sendDate,state );
            } else if (topic.equals( "/v1/devices/SN-40-787-61c80e/datas" )&(serviceId.equals( "server_parkin")||serviceId.equals( "server_parkout" )||serviceId.equals( "server_paTbocarblack" ))){
                saveMysql.save( "停车系统","/v1/devices/SN-40-787-61c80e/datas",result,subDate,MQTTServer.Topic1,result,MQTTServer.sendDate,state );
            } else if (topic.equals( "/v1/devices/SN-40-787-61c80e/datas" )&(serviceId.equals( "server_elevators")||serviceId.equals( "server_aicAccessEventmessagedataaccess" ))){
                saveMysql.save( "电梯系统","/v1/devices/SN-40-787-61c80e/datas",result,subDate,MQTTServer.Topic1,result,MQTTServer.sendDate,state );
            } else if (topic.equals( "/v1/devices/SN-40-787-61c80e/datas" )&(serviceId.equals( "server_door")||serviceId.equals( "server_aicAccessReader" )||serviceId.equals( "server_aicCallerRecord" ))){
                saveMysql.save( "门禁系统","/v1/devices/SN-40-787-61c80e/datas",result,subDate,MQTTServer.Topic1,result,MQTTServer.sendDate,state );
            }
        }
    }

}