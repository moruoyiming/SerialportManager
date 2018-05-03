package com.serialport.demo.manager;


import com.serialport.library.SerialportManager;
import com.serialport.library.protocol.OldSerialPortScreenProtocol;
import com.serialport.library.protocol.SerialPortProtocol;

/**
 * Created by Jian on 2017/8/7.
 */
public class OldScreenSender implements Sender {
    @Override
    public void sendScreenStandby(String sn) {
        byte[] scrrenbytes = OldSerialPortScreenProtocol.showDaiJiWindowProtocol();
        SerialportManager.getInstance().sendScreenBuffer(scrrenbytes);
    }

    @Override
    public void sendScreenReady() {
        byte[] bytes = OldSerialPortScreenProtocol.getTiShiXinXiProtocol("启动检测请按开始\r\n返回检测请按取消");
        SerialportManager.getInstance().sendScreenBuffer(bytes);
        byte[] bytes2 = OldSerialPortScreenProtocol.showTiShiXinXiWindowProtocol();
        SerialportManager.getInstance().sendScreenBuffer(bytes2);
    }

    @Override
    public void sendScreenCountDownView() {
        byte[] bytes = OldSerialPortScreenProtocol.showDaoJiShiWindowProtocol();
        SerialportManager.getInstance().sendScreenBuffer(bytes);
    }

    @Override
    public void sendScreenCountDown(String what) {
        byte[] bytes = OldSerialPortScreenProtocol.getDaoJiShiProtocol(what);
        SerialportManager.getInstance().sendScreenBuffer(bytes);
    }

    @Override
    public void sendScreenCountDownErrorView(String tip) {
        // don't do anything
    }

    @Override
    public void sendScreenError(String error) {
        byte[] bytes = OldSerialPortScreenProtocol.getTiShiXinXiProtocol(error);
        SerialportManager.getInstance().sendScreenBuffer(bytes);
        byte[] bytes2 = OldSerialPortScreenProtocol.showTiShiXinXiWindowProtocol();
        SerialportManager.getInstance().sendScreenBuffer(bytes2);
    }

    @Override
    public void sendScreenQRCodeMessage(String message) {
        byte[] bytes = OldSerialPortScreenProtocol.getErWeiMaProtocol(message);
        SerialportManager.getInstance().sendScreenBuffer(bytes);
    }

    @Override
    public void sendScreenQRView() {
        byte[] bytes2 = OldSerialPortScreenProtocol.showErWeiMaWindowProtocol();
        SerialportManager.getInstance().sendScreenBuffer(bytes2);
    }

    @Override
    public void sendScreenProgressMessage(int progress) {
        if (progress == 0) {
            byte[] bytes3 = OldSerialPortScreenProtocol.getJinDuTiaoProtocol(0);
            SerialportManager.getInstance().sendScreenBuffer(bytes3);
            byte[] bytes = OldSerialPortScreenProtocol.showJinDuTiaoWindowProtocol();
            SerialportManager.getInstance().sendScreenBuffer(bytes);
        } else {
            byte[] bytes3 = OldSerialPortScreenProtocol.getJinDuTiaoProtocol(progress);
            SerialportManager.getInstance().sendScreenBuffer(bytes3);
        }
    }

    @Override
    public void sendScreenDetectDeviceError(String time, String where) {
        if ("".equals(where)) {
            byte[] bytes = OldSerialPortScreenProtocol.getTiShiXinXiProtocol("检测过程出现短路，请确认" + where + "电极是否佩戴好！" + time + "秒后返回首页，请检查设备连接");
            SerialportManager.getInstance().sendScreenBuffer(bytes);
            byte[] bytes2 = OldSerialPortScreenProtocol.showTiShiXinXiWindowProtocol();
            SerialportManager.getInstance().sendScreenBuffer(bytes2);
        } else {
            byte[] bytes = OldSerialPortScreenProtocol.getTiShiXinXiProtocol(where);
            SerialportManager.getInstance().sendScreenBuffer(bytes);
            byte[] bytes2 = OldSerialPortScreenProtocol.showTiShiXinXiWindowProtocol();
            SerialportManager.getInstance().sendScreenBuffer(bytes2);
        }
    }

    @Override
    public void sendScreenDetectOverView() {
        byte[] bytes = OldSerialPortScreenProtocol.showTiJianJieShuWindowProtocol();
        SerialportManager.getInstance().sendScreenBuffer(bytes);
    }


    @Override
    public void sendScreenCheckDeviceHeadCallback() {
        // don't do anything
    }

    @Override
    public void sendScreenCheckDeviceHandCallback() {
        // don't do anything
    }

    @Override
    public void sendScreenCheckDeviceFootCallback() {
        // don't do anything
    }

    @Override
    public void sendCheckDeviceHead() {
        // don't do anything
    }

    @Override
    public void sendCheckDeviceHand() {
        // don't do anything
    }

    @Override
    public void sendCheckDeviceFoot() {
        // don't do anything
    }

    @Override
    public void sendScreemDeviceError(String error) {
        // don't do anything
    }

    @Override
    public void sendStartDetect() {
        SerialportManager.getInstance().sendBuffer(SerialPortProtocol.startDetect());
    }
}
