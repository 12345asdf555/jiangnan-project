package com;

import io.netty.channel.socket.SocketChannel;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MyMqttClient {
    public static MqttClient mqttClient = null;
    private static MemoryPersistence memoryPersistence = null;
    private static MqttConnectOptions mqttConnectOptions = null;
    private String socketfail;
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    private String ip;
    private String ip1;
    public MyMqttClient mqtt;
    public MqttReceriveCallback mqttReceriveCallback = new MqttReceriveCallback();

    /*
     * static { init("MQTT_FX_Client"); }
     */

    public void init(String clientId) {
        try {
            File file = new File("PC/IPconfig.txt");
            //File file = new File("IPconfig.txt");
            String filePath = file.getCanonicalPath();
            FileInputStream in = new FileInputStream(filePath);
            //FileInputStream in = new FileInputStream("IPconfig.txt");
            InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
            BufferedReader bufReader = new BufferedReader(inReader);
            String line = null;
            int writetime = 0;

            while ((line = bufReader.readLine()) != null) {
                if (writetime == 0) {
                    ip = line;
                    writetime++;
                } else {
                    ip1 = line;
                    writetime = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] values = ip.split(",");

        //鍒濆鍖栬繛鎺ヨ缃璞�
        mqttConnectOptions = new MqttConnectOptions();
        //鍒濆鍖朚qttClient
        if (null != mqttConnectOptions) {
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setConnectionTimeout(300);
            mqttConnectOptions.setKeepAliveInterval(300);
            try {
                memoryPersistence = new MemoryPersistence();
                if (null != memoryPersistence && null != clientId) {
                    mqttClient = new MqttClient("tcp://" + values[0] + ":1883", clientId, memoryPersistence);
                    //mqttClient = new MqttClient("tcp://192.168.8.155:1883", clientId,memoryPersistence);
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("mqttConnectOptions");
        }

        //璁剧疆杩炴帴鍜屽洖璋�
        if (null != mqttClient) {
            try {
                mqttClient.setCallback(mqttReceriveCallback);
                mqttClient.connect(mqttConnectOptions);
            } catch (MqttException e) {
                e.printStackTrace();
            } finally {
                if (mqttClient.isConnected()) {
                    System.out.println("mqtt连接成功！");
                } else {
                    System.out.println("mqtt连接失败！");
                }
            }
        } else {
            System.out.println("mqttClient未连接");
        }
    }

    //	鍏抽棴杩炴帴
    public void closeConnect() {
        //鍏抽棴瀛樺偍鏂瑰紡
        if (null != memoryPersistence) {
            try {
                memoryPersistence.close();
            } catch (MqttPersistenceException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("memoryPersistence is null");
        }

        //		鍏抽棴杩炴帴
        if (null != mqttClient) {
            if (mqttClient.isConnected()) {
                try {
                    mqttClient.disconnect();
                    mqttClient.close();
                } catch (MqttException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                System.out.println("mqttClient is not connect");
            }
        } else {
            System.out.println("mqttClient is null");
        }
    }

    //	鍙戝竷娑堟伅
    public void publishMessage(String pubTopic, String message, int qos) {
        if (null != mqttClient && mqttClient.isConnected()) {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setQos(qos);
            mqttMessage.setPayload(message.getBytes());

            MqttTopic topic = mqttClient.getTopic(pubTopic);

            if (null != topic) {
                try {
                    MqttDeliveryToken publish = topic.publish(mqttMessage);
                    if (!publish.isComplete()) {
                        //System.out.println("娑堟伅鍙戝竷鎴愬姛:"+message);
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

        } else {
            try {
                mqttClient.connect(mqttConnectOptions);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            //reConnect();
        }

    }

    //	閲嶆柊杩炴帴
    public void reConnect() {
        if (null != mqttClient) {
            if (!mqttClient.isConnected()) {
                if (null != mqttConnectOptions) {
                    try {
                        mqttClient.connect(mqttConnectOptions);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("mqttConnectOptions is null");
                }
            } else {
                System.out.println("mqttClient is null or connect");
            }
        } else {
            init("mqttclient");
        }

    }

    //	璁㈤槄涓婚
    public void subTopic(String topic) {
        if (null != mqttClient && mqttClient.isConnected()) {
            try {
                mqttClient.subscribe(topic, 0);
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("subTopic mqttClient is notConnect");
        }
    }

    //	娓呯┖涓婚
    public void cleanTopic(String topic) {
        if (null != mqttClient && !mqttClient.isConnected()) {
            try {
                mqttClient.unsubscribe(topic);
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("mqttClient is error");
        }
    }

    //  澶勭悊涓婁紶涓嬪彂鏁版嵁
    public void webdata(String str) {
        //mqtt.publishMessage("weldmes/upparams", str, 0);
//		ArrayList<String> listarraybuf = new ArrayList<String>();
//		boolean ifdo = false;
//		HashMap<String, SocketChannel> socketlist_cl;
//		Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
//		while(webiter.hasNext())
//		{
//			try{
//				Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
//				socketfail = entry.getKey();
//				SocketChannel socketcon = entry.getValue();
//				if(socketcon.isOpen() && socketcon.isActive() && socketcon.isWritable()) {
//					socketcon.writeAndFlush(str);
//				}else {
//					listarraybuf.add(socketfail);
//					ifdo = true;
//				}
//
//			}catch (Exception e) {
//				listarraybuf.add(socketfail);
//				ifdo = true;
//				e.getStackTrace();
//			}
//		}
//		if(ifdo){
//			//socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
//			for(int i=0;i<listarraybuf.size();i++){
//				socketlist.remove(listarraybuf.get(i));
//			}
//		}

        /*
         * ArrayList<String> listarraybuf = new ArrayList<String>(); boolean ifdo =
         * false; HashMap<String, SocketChannel> socketlist_cl; synchronized
         * (socketlist){ socketlist_cl = (HashMap<String, SocketChannel>)
         * socketlist.clone(); } Iterator<Entry<String, SocketChannel>> webiter =
         * socketlist_cl.entrySet().iterator(); while(webiter.hasNext()) { try{
         * Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>)
         * webiter.next(); socketfail = entry.getKey(); SocketChannel socketcon =
         * entry.getValue(); socketcon.writeAndFlush(str);
         *
         * }catch (Exception e) { listarraybuf.add(socketfail); ifdo = true;
         * e.getStackTrace(); } } if(ifdo){ synchronized (socketlist){ //socketlist_cl =
         * (HashMap<String, SocketChannel>) socketlist.clone(); for(int
         * i=0;i<listarraybuf.size();i++){ socketlist.remove(listarraybuf.get(i)); } } }
         */

    }
}