package com.serialport.library.listener;

/**
 * 项目名称：HealthDetectLecon
 * 类描述：
 * 创建人：jianz
 * 创建时间：2017/2/27 9:48
 * 修改备注：
 */

public interface OnS6DataReceiverListener {
    void onS6DataReceive(byte[] buffer, int size);
}
