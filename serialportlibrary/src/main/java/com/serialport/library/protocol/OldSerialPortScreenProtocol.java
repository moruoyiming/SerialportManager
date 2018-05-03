package com.serialport.library.protocol;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by DJP on 2017/2/22.
 */
public class OldSerialPortScreenProtocol {
    //显示界面指令的基础协议，其中第4、5个字节为界面编号，
    private static byte[] protocolShowWindow={(byte)0xEE, (byte)0xB1, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFC, (byte)0xFF, (byte)0xFF};

    private static byte[] handshakeStart={(byte)0xEE ,(byte)0x04 ,(byte)0xFF ,(byte)0xFC ,(byte)0xFF ,(byte)0xFF};
    private static byte[] handshakeAck={(byte)0xEE ,(byte)0x55 ,(byte)0xFF ,(byte)0xFC ,(byte)0xFF ,(byte)0xFF};
    //EE 55 FF FC FF FF

    private static byte[] protocolStart={(byte)0xEE};
    private static byte[] protocolCommand={(byte)0xB1,(byte)0x10};
    private static byte[] protocolScreenId={(byte)0x00,(byte)0x00};
    private static byte[] protocolControllerId={(byte)0x00,(byte)0x00};
    private static byte[] protocolEnd={(byte)0xFF ,(byte)0xFC ,(byte)0xFF ,(byte)0xFF};

    /**
     * 显示待机画面
     * @return
     */
    public static byte[] showDaiJiWindowProtocol(){
        byte[] ret = Arrays.copyOf(protocolShowWindow,protocolShowWindow.length);
        return ret;
    }

    /**
     * 显示二维码画面
     * @return
     */
    public  static byte[] showErWeiMaWindowProtocol(){
        byte[] ret = Arrays.copyOf(protocolShowWindow,protocolShowWindow.length);
        ret[4] = (byte)0x01;
        return ret;
    }

    /**
     * 显示倒计时画面
     * @return
     */
    public static byte[] showDaoJiShiWindowProtocol(){
        byte[] ret = Arrays.copyOf(protocolShowWindow,protocolShowWindow.length);
        ret[4] = (byte)0x02;
        return ret;
    }

    /**
     * 显示检测进度条画面
     * @return
     */
    public static byte[] showJinDuTiaoWindowProtocol(){
        byte[] ret = Arrays.copyOf(protocolShowWindow,protocolShowWindow.length);
        ret[4] = (byte)0x03;
        return ret;
    }

    /**
     * 显示提示信息画面
     * @return
     */
    public static byte[] showTiShiXinXiWindowProtocol(){
        byte[] ret = Arrays.copyOf(protocolShowWindow,protocolShowWindow.length);
        ret[4] = (byte)0x04;
        return ret;
    }

    /**
     * 显示体检结束画面
     * @return
     */
    public static byte[] showTiJianJieShuWindowProtocol(){
        byte[] ret = Arrays.copyOf(protocolShowWindow,protocolShowWindow.length);
        ret[4] = (byte)0x05;
        return ret;
    }

    /**
     * 设置二维码命令
     * */
    public static byte[] getErWeiMaProtocol(String code){
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId,protocolScreenId.length);
        screenId[0]=(byte)0x00;
        screenId[1]=(byte)0x01;
        byte[] controllerId = Arrays.copyOf(protocolControllerId,protocolControllerId.length);
        controllerId[0]= (byte)0x00;
        controllerId[1]= (byte)0x01;
        byte[] content = code.getBytes();

        ret = buildControllerProtocol(protocolStart,protocolCommand,screenId,controllerId,content,protocolEnd);
        return ret;
    }

    /**
     * 设置倒计时信息
     * */
    public static byte[] getDaoJiShiProtocol(String time){
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId,protocolScreenId.length);
        screenId[0]=(byte)0x00;
        screenId[1]=(byte)0x02;
        byte[] controllerId = Arrays.copyOf(protocolControllerId,protocolControllerId.length);
        controllerId[0]= (byte)0x00;
        controllerId[1]= (byte)0x01;
        byte[] content = time.getBytes();

        ret = buildControllerProtocol(protocolStart,protocolCommand,screenId,controllerId,content,protocolEnd);
        return ret;
    }

    /**
     * 设置检测进度条信息
     * */
    public static byte[] getJinDuTiaoProtocol(int value){
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId,protocolScreenId.length);
        screenId[0]=(byte)0x00;
        screenId[1]=(byte)0x03;
        byte[] controllerId = Arrays.copyOf(protocolControllerId,protocolControllerId.length);
        controllerId[0]= (byte)0x00;
        controllerId[1]= (byte)0x01;
        byte[] content = intToByte4(value);

        ret = buildControllerProtocol(protocolStart,protocolCommand,screenId,controllerId,content,protocolEnd);
        return ret;
    }

    /**
     * 设置提示信息
     * */
    public static byte[] getTiShiXinXiProtocol(String info){
        byte[] ret;
        byte[] screenId = Arrays.copyOf(protocolScreenId,protocolScreenId.length);
        screenId[0]=(byte)0x00;
        screenId[1]=(byte)0x04;
        byte[] controllerId = Arrays.copyOf(protocolControllerId,protocolControllerId.length);
        controllerId[0]= (byte)0x00;
        controllerId[1]= (byte)0x01;
        //byte[] content = info.getBytes();
        byte[] content = info.getBytes(Charset.forName("GB2312"));
        //byte[] content = charArrayToByteArray(info.toCharArray());

        ret = buildControllerProtocol(protocolStart,protocolCommand,screenId,controllerId,content,protocolEnd);
        return ret;
    }

    public static byte[] getHandshakeStart(){
        return handshakeStart.clone();
    }

    public static byte[] getHandshakeAck(){
        return handshakeAck.clone();
    }


    /*
    * 指令组装
    * */
    private static byte[] buildControllerProtocol(byte[]...bytes){
        List<Byte> byteArray = new LinkedList<Byte>();
        for(byte[] bytes1 : bytes){
            for(byte b : bytes1){
                byteArray.add(b);
            }
        }

        byte[] ret = new byte[byteArray.size()];
        for(int i = 0;i<byteArray.size();i++){
            ret[i]=byteArray.get(i);
        }
        return ret;
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

    private static byte[] charArrayToByteArray(char[] chars){

        byte[] ret = new byte[chars.length*2];
        for(int i=0;i<chars.length;i++){
            int j=i*2;
            char c =chars[i] ;
            //System.out.println(c);
            byte low =(byte) (c & 0xFF);
            byte high = (byte)(c>>8);
            ret[j] =high;
            ret[j+1]=low;
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

    public static void main(String[] args){
        byte[] data = showDaiJiWindowProtocol();

        System.out.println("显示待机画面 ： " + bytesToHexString(data));
        data=showErWeiMaWindowProtocol();
        System.out.println("显示二维码画面 ： " + bytesToHexString(data));
        data=showDaoJiShiWindowProtocol();
        System.out.println("显示倒计时画面 ： " + bytesToHexString(data));
        data=showJinDuTiaoWindowProtocol();
        System.out.println("显示进度条画面 ： " + bytesToHexString(data));
        data=showTiShiXinXiWindowProtocol();
        System.out.println("显示提示信息画面 ： " + bytesToHexString(data));
        data=showTiJianJieShuWindowProtocol();
        System.out.println("显示体检结束画面 ： " + bytesToHexString(data));

        data=getErWeiMaProtocol("知康");
        System.out.println("设置二维码 ： " + bytesToHexString(data));
        data=getDaoJiShiProtocol("10");
        System.out.println("设置倒计时 ： " + bytesToHexString(data));
        data=getJinDuTiaoProtocol(50);
        System.out.println("设置进度条 ： " + bytesToHexString(data));
        data=getTiShiXinXiProtocol("知康");
        System.out.println("设置提示信息 ： " + bytesToHexString(data));

    }

}
