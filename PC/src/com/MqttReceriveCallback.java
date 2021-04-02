package com;

import io.netty.channel.socket.SocketChannel;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class MqttReceriveCallback implements MqttCallback {

    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    private String socketfail;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 连接异常断开后，调用
     *
     * @param cause
     */
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println(sdf.format(System.currentTimeMillis()) + "  mqttClient异常断开：" + cause);
    }

    /**
     * 消息到达后，调用
     *
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(sdf.format(System.currentTimeMillis()) + "  mqttClient 接收消息内容 : " + new String(message.getPayload()));
        ArrayList<String> listarraybuf = new ArrayList<String>();
        boolean ifdo = false;
        HashMap<String, SocketChannel> socketlist_cl;
        Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
        while (webiter.hasNext()) {
            try {
                Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                socketfail = entry.getKey();
                SocketChannel socketcon = entry.getValue();
                if (socketcon.isOpen() && socketcon.isActive() && socketcon.isWritable()) {
                    socketcon.writeAndFlush(new String(message.getPayload()));
                } else {
                    listarraybuf.add(socketfail);
                    ifdo = true;
                }

            } catch (Exception e) {
                listarraybuf.add(socketfail);
                ifdo = true;
                e.getStackTrace();
            }
        }
        if (ifdo) {
            //socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
            for (int i = 0; i < listarraybuf.size(); i++) {
                socketlist.remove(listarraybuf.get(i));
            }
        }

    }

    /**
     * 消息发送成功后，调用
     *
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}