package com.serialport.library.protocol;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Jian on 2017/8/7.
 */
public class SerialPortScreenProtocol2 extends BaseProtocol {
    //显示界面指令的基础协议，其中第4、5个字节为界面编号，
    private static byte[] protocolShowWindow = {(byte) 0xEE, (byte) 0xB1, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0xFF, (byte) 0xFC, (byte) 0xFF, (byte) 0xFF};

    private static byte[] handshakeStart = {(byte) 0xEE, (byte) 0x04, (byte) 0xFF, (byte) 0xFC, (byte) 0xFF, (byte) 0xFF};
    private static byte[] handshakeAck = {(byte) 0xEE, (byte) 0x55, (byte) 0xFF, (byte) 0xFC, (byte) 0xFF, (byte) 0xFF};
    //EE 55 FF FC FF FF

    private static byte[] protocolStart = {(byte) 0xEE};
    private static byte[] protocolCommand = {(byte) 0xB1, (byte) 0x10};
    private static byte[] protocolScreenId = {(byte) 0x00, (byte) 0x00};
    private static byte[] protocolControllerId = {(byte) 0x00, (byte) 0x00};
    private static byte[] protocolEnd = {(byte) 0xFF, (byte) 0xFC, (byte) 0xFF, (byte) 0xFF};

    //next image   EE B1 25 00 02 00 02 FF FC FF FF
    private static byte[] nextImage = {(byte) 0xEE, (byte) 0xB1, (byte) 0x25, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x01, (byte) 0xFF, (byte) 0xFC, (byte) 0xFF, (byte) 0xFF};
    //first image  EE B1 23 00 02 00 02 01 FF FC FF FF
    private static byte[] firstImage = {(byte) 0xEE, (byte) 0xB1, (byte) 0x23, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xFF, (byte) 0xFC, (byte) 0xFF, (byte) 0xFF};

    /**
     * 显示首页画面
     *
     * @return
     */
    public static byte[] showDetectStandy() {
        byte[] ret = Arrays.copyOf(protocolShowWindow, protocolShowWindow.length);
        ret[4] = (byte) 0x0E;
        return ret;
    }

    /**
     * 显示二维码画面
     *
     * @return
     */
    public static byte[] showDetectCode() {
        byte[] ret = Arrays.copyOf(protocolShowWindow, protocolShowWindow.length);
        ret[4] = (byte) 0x07;
        return ret;
    }

    /**
     * 显示准备体检画面
     *
     * @return
     */
    public static byte[] showDetectReady() {
        byte[] ret = Arrays.copyOf(protocolShowWindow, protocolShowWindow.length);
        ret[4] = (byte) 0x08;
        return ret;
    }

    /**
     * 显示体检倒计时.
     *
     * @return
     */
    public static byte[] showDetectCountDown() {
        byte[] ret = Arrays.copyOf(protocolShowWindow, protocolShowWindow.length);
        ret[4] = (byte) 0x09;
        return ret;
    }

    /**
     * 显示体检画面
     *
     * @return
     */
    public static byte[] showDetectDetecting() {
        byte[] ret = Arrays.copyOf(protocolShowWindow, nextImage.length);
        ret[4] = (byte) 0x0A;
        return ret;
    }

    /**
     * 显示体检结束画面
     *
     * @return
     */
    public static byte[] showDetectOver() {
        byte[] ret = Arrays.copyOf(protocolShowWindow, protocolShowWindow.length);
        ret[4] = (byte) 0x0B;
        return ret;
    }
    /**
     * 显示设备异常画面
     *
     * @return
     */
    public static byte[] showDeviceError() {
        byte[] ret = Arrays.copyOf(protocolShowWindow, protocolShowWindow.length);
        ret[4] = (byte) 0x0C;
        return ret;
    }
    /**
     * 显示提示信息画面
     *
     * @return
     */
    public static byte[] showMessageTip() {
        byte[] ret = Arrays.copyOf(protocolShowWindow, protocolShowWindow.length);
        ret[4] = (byte) 0x0D;
        return ret;
    }

    public static byte[] nextImage() {
        byte[] ret = Arrays.copyOf(nextImage, nextImage.length);
        return ret;
    }

    //  EE B1 23 00 02 00 02 01 FF FC FF FF
    //  EE B1 25 00 02 00 02 FF FC FF FF
    public static byte[] firstImage() {
        byte[] ret = Arrays.copyOf(firstImage, firstImage.length);
        return ret;
    }


    /**
     * 设置二维码命令
     * EE B1 10 00 01 00 01 32 32 32 32 32 32 FF FC FF FF
     * EE B1 10 00 01 00 01 77 77 77 FF FC FF FF
     */
    public static byte[] getErWeiMaProtocol(byte screenid, String code) {
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId, protocolScreenId.length);
        screenId[0] = (byte) 0x00;
        screenId[1] = screenid;
        byte[] controllerId = Arrays.copyOf(protocolControllerId, protocolControllerId.length);
        controllerId[0] = (byte) 0x00;
        controllerId[1] = (byte) 0x01;
        byte[] content = code.getBytes();

        ret = buildControllerProtocol(protocolStart, protocolCommand, screenId, controllerId, content, protocolEnd);
        return ret;
    }

    /**
     * 设置倒计时信息
     */
    public static byte[] getDaoJiShiProtocol(String time) {
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId, protocolScreenId.length);
        screenId[0] = (byte) 0x00;
        screenId[1] = (byte) 0x08;
        byte[] controllerId = Arrays.copyOf(protocolControllerId, protocolControllerId.length);
        controllerId[0] = (byte) 0x00;
        controllerId[1] = (byte) 0x02;
        byte[] content = time.getBytes();

        ret = buildControllerProtocol(protocolStart, protocolCommand, screenId, controllerId, content, protocolEnd);
        return ret;
    }

    /**
     * 设置倒计时信息
     */
    public static byte[] getDaoJiShiProtocol2(String time) {
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId, protocolScreenId.length);
        screenId[0] = (byte) 0x00;
        screenId[1] = (byte) 0x04;
        byte[] controllerId = Arrays.copyOf(protocolControllerId, protocolControllerId.length);
        controllerId[0] = (byte) 0x00;
        controllerId[1] = (byte) 0x02;
        byte[] content = time.getBytes();

        ret = buildControllerProtocol(protocolStart, protocolCommand, screenId, controllerId, content, protocolEnd);
        return ret;
    }

    /**
     * 设置检测进度条信息
     */
    public static byte[] getJinDuTiaoProtocol(int value) {
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId, protocolScreenId.length);
        screenId[0] = (byte) 0x00;
        screenId[1] = (byte) 0x0A;
        byte[] controllerId = Arrays.copyOf(protocolControllerId, protocolControllerId.length);
        controllerId[0] = (byte) 0x00;
        controllerId[1] = (byte) 0x01;
        byte[] content = intToByte4(value);

        ret = buildControllerProtocol(protocolStart, protocolCommand, screenId, controllerId, content, protocolEnd);
        return ret;
    }

    /**
     * 设置提示信息
     */
    public static byte[] getTiShiXinXiProtocol(String info) {
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId, protocolScreenId.length);
        screenId[0] = (byte) 0x00;
        screenId[1] = (byte) 0x0D;
        byte[] controllerId = Arrays.copyOf(protocolControllerId, protocolControllerId.length);
        controllerId[0] = (byte) 0x00;
        controllerId[1] = (byte) 0x01;
        //byte[] content = info.getBytes();
        byte[] content = info.getBytes(Charset.forName("GB2312"));
        //byte[] content = charArrayToByteArray(info.toCharArray());

        ret = buildControllerProtocol(protocolStart, protocolCommand, screenId, controllerId, content, protocolEnd);
        return ret;
    }

    /**
     * @param message
     * @return
     */
    public static byte[] showMessage(byte screenid, byte viewid, String message) {
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId, protocolScreenId.length);
        screenId[0] = (byte) 0x00;
        screenId[1] = screenid;
        byte[] controllerId = Arrays.copyOf(protocolControllerId, protocolControllerId.length);
        controllerId[0] = (byte) 0x00;
        controllerId[1] = viewid;
        //byte[] content = info.getBytes();
        byte[] content = message.getBytes(Charset.forName("GB2312"));
        //byte[] content = charArrayToByteArray(info.toCharArray());
        ret = buildControllerProtocol(protocolStart, protocolCommand, screenId, controllerId, content, protocolEnd);
        return ret;
    }

    public static byte[] getHandshakeStart() {
        return handshakeStart.clone();
    }

    public static byte[] getHandshakeAck() {
        return handshakeAck.clone();
    }


    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
            sb.append(" ");
        }
        return sb.toString();
    }

    private static byte[] charArrayToByteArray(char[] chars) {

        byte[] ret = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; i++) {
            int j = i * 2;
            char c = chars[i];
            //System.out.println(c);
            byte low = (byte) (c & 0xFF);
            byte high = (byte) (c >> 8);
            ret[j] = high;
            ret[j + 1] = low;
        }
        return ret;
    }

    private static byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    public static void main(String[] args) {
        byte[] data = showDetectCode();

        System.out.println("显示待机画面 ： " + bytesToHexString(data));
//        data=showErWeiMaWindowProtocol();
        System.out.println("显示二维码画面 ： " + bytesToHexString(data));
        data = showDetectCountDown();
        System.out.println("显示倒计时画面 ： " + bytesToHexString(data));
        data = showDetectDetecting();
        System.out.println("显示进度条画面 ： " + bytesToHexString(data));
        data = showMessageTip();
        System.out.println("显示提示信息画面 ： " + bytesToHexString(data));
        data = showDetectOver();
        System.out.println("显示体检结束画面 ： " + bytesToHexString(data));

//        data=getErWeiMaProtocol("知康");
        System.out.println("设置二维码 ： " + bytesToHexString(data));
        data = getDaoJiShiProtocol("10");
        System.out.println("设置倒计时 ： " + bytesToHexString(data));
        data = getJinDuTiaoProtocol(50);
        System.out.println("设置进度条 ： " + bytesToHexString(data));
        data = getTiShiXinXiProtocol("知康");
        System.out.println("设置提示信息 ： " + bytesToHexString(data));

    }

}
