package com.serialport.demo.manager;


import com.serialport.library.SerialportManager;
import com.serialport.library.protocol.SerialPortProtocol;
import com.serialport.library.protocol.SerialPortScreenProtocol;
import com.serialport.library.protocol.SerialPortScreenProtocol2;

/**
 * Created by Jian on 2017/8/7.
 */

public class AdapterSender implements Sender {
    @Override
    public void sendScreenStandby(String sn) {
        byte[] scrrenbytes = SerialPortScreenProtocol2.showDetectStandy();
        SerialportManager.getInstance().sendScreenBuffer(scrrenbytes);
        byte[] bytes1 = SerialPortScreenProtocol2.showMessage((byte) 0x0E, (byte) 0x01, sn);
        SerialportManager.getInstance().sendScreenBuffer(bytes1);
    }

    @Override
    public void sendScreenReady() {
        byte[] bytes2 = SerialPortScreenProtocol2.showDetectReady();
        SerialportManager.getInstance().sendScreenBuffer(bytes2);
    }

    @Override
    public void sendScreenCountDownView() {
        byte[] bytes = SerialPortScreenProtocol2.showDetectCountDown();
        SerialportManager.getInstance().sendScreenBuffer(bytes);
        byte[] bytes2 = SerialPortScreenProtocol2.firstImage();
        SerialportManager.getInstance().sendScreenBuffer(bytes2);
        SerialportManager.getInstance().sendScreenBuffer(SerialPortScreenProtocol2.showMessage((byte) 0x09, (byte) 0x02, ""));
        SerialportManager.getInstance().sendScreenBuffer(SerialPortScreenProtocol2.showMessage((byte) 0x09, (byte) 0x03, ""));
        SerialportManager.getInstance().sendScreenBuffer(SerialPortScreenProtocol2.showMessage((byte) 0x09, (byte) 0x04, ""));
    }

    @Override
    public void sendScreenCountDown(String what) {
        byte[] bytes = SerialPortScreenProtocol2.nextImage();
        SerialportManager.getInstance().sendScreenBuffer(bytes);
    }

    @Override
    public void sendScreenCountDownErrorView(String tip) {
        byte[] bytes1 = SerialPortScreenProtocol2.showMessage((byte) 0x0C, (byte) 0x02, tip);
        SerialportManager.getInstance().sendScreenBuffer(bytes1);
    }

    @Override
    public void sendScreenError(String error) {
        byte[] bytes1 = SerialPortScreenProtocol2.getTiShiXinXiProtocol(error);
        SerialportManager.getInstance().sendScreenBuffer(bytes1);
        byte[] bytes2 = SerialPortScreenProtocol2.showMessageTip();
        SerialportManager.getInstance().sendScreenBuffer(bytes2);
    }

    @Override
    public void sendScreenQRCodeMessage(String message) {
        byte[] bytes = SerialPortScreenProtocol2.getErWeiMaProtocol((byte) 0x07, message);
        SerialportManager.getInstance().sendScreenBuffer(bytes);
    }

    @Override
    public void sendScreenQRView() {
        byte[] bytes2 = SerialPortScreenProtocol2.showDetectCode();
        SerialportManager.getInstance().sendScreenBuffer(bytes2);
    }

    @Override
    public void sendScreenProgressMessage(int progress) {
        if (progress == 0) {
            byte[] bytes3 = SerialPortScreenProtocol2.getJinDuTiaoProtocol(0);
            SerialportManager.getInstance().sendScreenBuffer(bytes3);
            byte[] bytes = SerialPortScreenProtocol2.showDetectDetecting();
            SerialportManager.getInstance().sendScreenBuffer(bytes);
        } else {
            byte[] bytes3 = SerialPortScreenProtocol2.getJinDuTiaoProtocol(progress);
            SerialportManager.getInstance().sendScreenBuffer(bytes3);
        }

    }

    @Override
    public void sendScreenDetectDeviceError(String time, String where) {
        if ("".equals(where)) {
            byte[] message = SerialPortScreenProtocol2.showMessage((byte) 0x0C, (byte) 0x02, time);
            SerialportManager.getInstance().sendScreenBuffer(message);
        } else {
            byte[] deviceserror = SerialPortScreenProtocol2.showDeviceError();
            SerialportManager.getInstance().sendScreenBuffer(deviceserror);
            byte[] message = SerialPortScreenProtocol2.showMessage((byte) 0x0C, (byte) 0x02, time);
            SerialportManager.getInstance().sendScreenBuffer(message);
            byte[] what = SerialPortScreenProtocol2.showMessage((byte) 0x0C, (byte) 0x01, where);
            SerialportManager.getInstance().sendScreenBuffer(what);
        }

    }

    @Override
    public void sendScreenDetectOverView() {
        byte[] bytes2 = SerialPortScreenProtocol2.showDetectOver();
        SerialportManager.getInstance().sendScreenBuffer(bytes2);
    }


    @Override
    public void sendScreenCheckDeviceHeadCallback() {
        SerialportManager.getInstance().sendScreenBuffer(SerialPortScreenProtocol.showMessage((byte) 0x09, (byte) 0x02, "准备就绪"));
    }

    @Override
    public void sendScreenCheckDeviceHandCallback() {
        SerialportManager.getInstance().sendScreenBuffer(SerialPortScreenProtocol.showMessage((byte) 0x09, (byte) 0x03, "准备就绪"));
    }

    @Override
    public void sendScreenCheckDeviceFootCallback() {
        SerialportManager.getInstance().sendScreenBuffer(SerialPortScreenProtocol.showMessage((byte) 0x09, (byte) 0x04, "准备就绪"));
    }

    @Override
    public void sendCheckDeviceHead() {
        SerialportManager.getInstance().sendBuffer(SerialPortProtocol.checkDeviceHead());
    }

    @Override
    public void sendCheckDeviceHand() {
        SerialportManager.getInstance().sendBuffer(SerialPortProtocol.checkDeviceHand());
    }

    @Override
    public void sendCheckDeviceFoot() {
        SerialportManager.getInstance().sendBuffer(SerialPortProtocol.checkDeviceFoot());
    }

    @Override
    public void sendScreemDeviceError(String error) {
        byte[] deviceserror = SerialPortScreenProtocol2.showDeviceError();
        SerialportManager.getInstance().sendScreenBuffer(deviceserror);
        byte[] message = SerialPortScreenProtocol2.showMessage((byte) 0x0C, (byte) 0x01, error);
        SerialportManager.getInstance().sendScreenBuffer(message);
    }

    @Override
    public void sendStartDetect() {
        SerialportManager.getInstance().sendBuffer(SerialPortProtocol.startDetect());
    }
}
