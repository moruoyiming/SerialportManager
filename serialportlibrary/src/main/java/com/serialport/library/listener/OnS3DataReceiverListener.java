package com.serialport.library.listener;

/**
 * Created by Jian on 2017/8/7.
 */
public interface OnS3DataReceiverListener {
    void onS3DataReceive(byte[] buffer, int size);
}
