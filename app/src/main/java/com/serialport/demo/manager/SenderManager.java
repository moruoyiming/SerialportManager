package com.serialport.demo.manager;


/**
 * Created by zhikang on 2017/11/27.
 */

public class SenderManager {


    private static SenderManager senderManager;
    private Sender sender;


    public static SenderManager getInstance() {
        if (senderManager == null) {
            synchronized (SenderManager.class) {
                if (senderManager == null) {
                    senderManager = new SenderManager(0);
                }
            }
        }
        return senderManager;
    }

    public SenderManager(int type) {
        sender = createSender(type);
    }

    public Sender getSender() {
        if (sender != null) {
            return sender;
        } else {
            sender = createSender(0);
            return sender;
        }
    }


    public static Sender createSender(int type) {
        switch (type) {
            case 0:
                return new AdapterSender();
            case 1:
            default:
                return new OldScreenSender();
        }
    }
}
