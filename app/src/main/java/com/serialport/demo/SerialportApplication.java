package com.serialport.demo;

import android.app.Application;

import com.serialport.library.core.SerialPortFinder;

public class SerialportApplication extends Application{

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
