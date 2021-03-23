package com;

import io.netty.channel.socket.SocketChannel;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class MqttReceriveCallback implements MqttCallback {

    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    private String socketfail;

    public MqttReceriveCallback() {
        // TODO Auto-generated constructor stub

    }

    public MqttReceriveCallback(MyMqttClient mymqttclient) {
        // TODO Auto-generated constructor stub

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("com.Client 接收消息内容 : " + new String(message.getPayload()));

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

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}