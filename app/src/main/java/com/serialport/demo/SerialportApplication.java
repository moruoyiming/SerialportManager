package com.serialport.demo;

import android.app.Application;

import com.serialport.library.core.SerialPortFinder;

/**
 * Created by Jian on 2017/8/7.
 */
public class SerialportApplication extends Application{

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
