package com.serialport.library.listener;

/**
 * Created by Jian on 2017/8/7.
 */

public interface OnS6DataReceiverListener {
    void onS6DataReceive(byte[] buffer, int size);
}
