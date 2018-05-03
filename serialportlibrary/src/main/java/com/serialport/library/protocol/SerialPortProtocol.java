package com.serialport.library.protocol;

import java.util.Arrays;

/**
 * Created by Jian on 2017/8/7.
 */

public class SerialPortProtocol extends BaseProtocol {
    public static byte[] baseStart = {(byte) 0x01};
    public static byte[] baseEnd = {(byte) 0x04};

    private static byte[] checkDeviceHead = {(byte) 0x31, (byte) 0x32, (byte) 0x30, (byte) 0x3A};
    private static byte[] checkDeviceHand = {(byte) 0x31, (byte) 0x32, (byte) 0x30, (byte) 0x38};
    private static byte[] checkDeviceFoot = {(byte) 0x31, (byte) 0x32, (byte) 0x30, (byte) 0x36};

    private static byte[] checkDeviceSuccess = {(byte) 0x38, (byte) 0x32, (byte) 0x30, (byte) 0x32, (byte) 0x31, (byte) 0x32, (byte) 0x30, (byte) 0x30};

    private static byte[] checkDeviceFailed = {(byte) 0x38, (byte) 0x32, (byte) 0x30, (byte) 0x32, (byte) 0x31, (byte) 0x32, (byte) 0x30, (byte) 0x33};

    private static byte[] checkDeviceError = {(byte) 0x38, (byte) 0x32, (byte) 0x30, (byte) 0x32, (byte) 0x31, (byte) 0x32, (byte) 0x3F, (byte) 0x3F};

    private static byte[] checkDeviceError2 = {(byte) 0x38, (byte) 0x32, (byte) 0x30, (byte) 0x32, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30};

    //控制字 0x80  长度 01  安装xx 0x01-0xff  00为无按键
    private static byte[] orderButton = {(byte) 0x38, (byte) 0x30, (byte) 0x31, (byte) 0x36, (byte) 0x30, (byte) 0x30};

    private static byte[] deviceInfo = {(byte) 0x31, (byte) 0x31, (byte) 0x30, (byte) 0x30};

    private static byte[] startDetect = {(byte) 0x31, (byte) 0x32, (byte) 0x30, (byte) 0x30};

    private static byte[] startAdapterDetect = {(byte) 0x31, (byte) 0x33, (byte) 0x30, (byte) 0x30};

    private static byte[] startResponse = {(byte) 0x38, (byte) 0x32, (byte) 0x30, (byte) 0x32, (byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30};

    private static byte[] deviceError = {(byte) 0x30, (byte) 0x33};

    private static byte[] deviceError2 = {(byte) 0x30, (byte) 0x32};

    private static byte[] deviceError3 = {(byte) 0x3F, (byte) 0x3F};

    private static byte[] detectDataStart = {(byte) 0x39, (byte) 0x32, (byte) 0x30, (byte) 0x35};

    private static byte[] detectAdapterDataStart = {(byte) 0x39, (byte) 0x32, (byte) 0x32, (byte) 0x3f};

    private static byte[] detectDataEnd = {(byte) 0x04};

    private static byte[] detectEnd = {(byte) 0x01, (byte) 0x30, (byte) 0x30};

    /**
     * get device versioncode
     * request  0x01 0x31 0x31 0x30 0x30 0x4
     * reponse  01 39 31 30 3C 35 36 33 31 32 3E 33 30 33 30 33 31 3C 32 33 3D 3B 3F 34 30 31 36 35 31 04
     *
     * @return
     */

    public static byte[] getDeviceVersion() {
        byte[] ret = buildControllerProtocol(baseStart, deviceInfo, baseEnd);
        return ret;
    }

    /**
     * start detect\ 命令字 0x12
     * <p>
     * request  0x01 0x31 0x32 0x30 0x30 0x4
     * 控制字类型  00：读所有单次模式  0x01-0x16：读单通道数据  0x8f 都所有通道自适应模式
     *
     * @return
     */
    public static byte[] startDetect() {
        byte[] ret = buildControllerProtocol(baseStart, startDetect, baseEnd);
        return ret;
    }

    public static byte[] startAdapterDetect() {
        byte[] ret = buildControllerProtocol(baseStart, startAdapterDetect, baseEnd);
        return ret;
    }

    /**
     * 检查头电极
     *
     * @return
     */
    public static byte[] checkDeviceHead() {
        byte[] ret = buildControllerProtocol(baseStart, checkDeviceHead, baseEnd);
        return ret;
    }

    /**
     * 检查手电极
     *
     * @return
     */
    public static byte[] checkDeviceHand() {
        byte[] ret = buildControllerProtocol(baseStart, checkDeviceHand, baseEnd);
        return ret;
    }

    /**
     * 检查脚电极
     *
     * @return
     */
    public static byte[] checkDeviceFoot() {
        byte[] ret = buildControllerProtocol(baseStart, checkDeviceFoot, baseEnd);
        return ret;
    }

    /**
     * 判断是否体检最后一针
     *
     * @param temp
     * @return 0x01 0x30 0x30
     */
    public static boolean isDetectEnd(String temp) {
        if (temp.startsWith("01 30 30")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param mBufferTemp
     * @return
     */
    public static boolean isDetectEnd(byte[] mBufferTemp) {
        byte[] context = Arrays.copyOfRange(mBufferTemp, 0, 3);
        boolean response = Arrays.equals(SerialPortProtocol.detectEnd, context);
        return response;
    }

    /**
     * 命令字 按键
     *
     * @param x1
     * @param x2
     * @param x3
     * @param x4
     * @param x5
     * @param x6
     * @return
     */

    public static byte[] orderButton(byte x1, byte x2, byte x3, byte x4, byte x5, byte x6) {
        byte[] retTemp = Arrays.copyOf(orderButton, orderButton.length);
        retTemp[0] = x1;
        retTemp[1] = x2;
        retTemp[2] = x3;
        retTemp[3] = x4;
        retTemp[4] = x5;
        retTemp[6] = x6;
        byte[] ret = buildControllerProtocol(baseStart, retTemp, baseEnd);
        return ret;

    }

    /**
     * 开始体检响应数据
     *
     * @return
     */
    public static byte[] getStartResponse() {
        return startResponse;
    }

    public static byte[] getDeviceError() {
        return deviceError;
    }

    public static byte[] getDeviceError2() {
        return deviceError2;
    }
    public static byte[] getDeviceError3() {
        return deviceError3;
    }
    public static byte[] getDetectStart() {
        return detectDataStart;
    }

    public static byte[] getAdapterDetectStart() {
        return detectAdapterDataStart;
    }

    public static byte[] getDetectEnd() {
        return detectDataEnd;
    }

    public static byte[] checkDeviceSuccess() {
        return checkDeviceSuccess;
    }

    public static byte[] checkCheckDeviceFailed() {
        return checkDeviceFailed;
    }

    public static byte[] checkCheckDeviceError() {
        return checkDeviceError;
    }

    public static byte[] checkCheckDeviceError2() {
        return checkDeviceError2;
    }
}
