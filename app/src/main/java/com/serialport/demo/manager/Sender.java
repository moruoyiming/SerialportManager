package com.serialport.demo.manager;

/**
 * Created by Jian on 2017/8/7.
 */

public interface Sender {

    //屏幕：发送待机命令
    void sendScreenStandby(String sn);

    //屏幕：发送准备
    void sendScreenReady();

    //屏幕：展示倒计时页面
    void sendScreenCountDownView();

    //屏幕：发送倒计时
    void sendScreenCountDown(String what);

    //屏幕：倒计时接触点异常倒计时
    void sendScreenCountDownErrorView(String tip);

    //屏幕：发送错误信息
    void sendScreenError(String error);

    //屏幕：展示二维码
    void sendScreenQRCodeMessage(String message);

    //屏幕：展示扫码页面
    void sendScreenQRView();

    //屏幕：校验头电极回调
    void sendScreenCheckDeviceHeadCallback();

    //屏幕：校验头手极回调
    void sendScreenCheckDeviceHandCallback();

    //屏幕：校验脚电极回调
    void sendScreenCheckDeviceFootCallback();

    //主板：校验头电极
    void sendCheckDeviceHead();

    //主板：校验手电极
    void sendCheckDeviceHand();

    //主板：校验脚电极
    void sendCheckDeviceFoot();

    //屏幕：倒计时发送设备异常错误
    void sendScreemDeviceError(String error);

    //主板：检测
    void sendStartDetect();

    //屏幕：发送进度条
    void sendScreenProgressMessage(int progress);

    //屏幕：体检过程设备异常错误界面
    void sendScreenDetectDeviceError(String time, String where);

    //屏幕：体检结束
    void sendScreenDetectOverView();

}
