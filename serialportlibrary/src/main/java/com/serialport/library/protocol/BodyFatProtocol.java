package com.serialport.library.protocol;

import java.util.Arrays;

/**
 * Created by Jian on 2017/8/7.
 */
public class BodyFatProtocol extends BaseProtocol {

    private static byte[] baseStart = {(byte) 0xA5, (byte) 0x5A};

    private static byte[] infoLenth = {(byte) 0x02};

    private static byte[] motherboardNumber = {(byte) 0x02};

    private static byte[] command = {(byte) 0x02};

    private static byte[] bodyAsk = {(byte) 0x32};

    private static byte[] progressFeild = {(byte) 0x02, (byte) 0x3E};

    private static byte[] progressFaild = {(byte) 0x02, (byte) 0x3F};

    private static byte[] handshakeStart = {(byte) 0xA5, (byte) 0x5A, (byte) 0x02, (byte) 0x02, (byte) 0x3C, (byte) 0x3E};

    private static byte[] weightStart = {(byte) 0xA5, (byte) 0x5A, (byte) 0x02, (byte) 0x02, (byte) 0x36, (byte) 0x38};

    private static byte[] bodyfatStart = {(byte) 0xA5, (byte) 0x5A, (byte) 0x02, (byte) 0x02, (byte) 0x37, (byte) 0x39};

    private static byte[] handshakeAck = {(byte) 0xA5, (byte) 0x5A, (byte) 0x01, (byte) 0x3C, (byte) 0x3C};


    /**
     * 发送握手
     *
     * @return
     */
    public static byte[] sendHandShake() {
        return handshakeStart;
    }

    /**
     * 握手响应
     *
     * @return
     */
    public static byte[] getHandshakeAck() {
        return handshakeAck;
    }

    /**
     * 发送体脂
     *
     * @return
     */
    public static byte[] sendBodyFat() {
        return bodyfatStart;
    }

    /**
     * 发送体重
     *
     * @return
     */
    public static byte[] sendWeight() {
        return weightStart;
    }

    /**
     * 校验头部数据 A5 5A
     *
     * @param bytes
     * @return
     */
    public static boolean checkAsk(byte[] bytes) {
        if (bytes != null && bytes.length >= 2) {
            byte[] context = Arrays.copyOfRange(bytes, 0, 2);
            if (Arrays.equals(baseStart, context)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断 数据是否有错
     *
     * @param bytes
     * @return
     */
    public static boolean checkBodyContext(byte[] bytes) {
        if (bytes != null && bytes.length >= 4) {
            if (checkAsk(bytes)) {
                byte[] context = Arrays.copyOfRange(bytes, 2, 4);
                if (Arrays.equals(progressFaild, context)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取错误代号
     *
     * @param bytes
     * @return
     */
    public static int checkBodyErrorCode(byte[] bytes) {
        if (bytes != null && bytes.length >= 4) {
            if (checkBodyContext(bytes)) {
                byte[] errorcode = Arrays.copyOfRange(bytes, 4, 5);
                int x = errorcode[0] & 0xFF;
                return x;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

    }

    /**
     * 获取 体重值
     *
     * @param bytes
     * @return
     */
    public static double checkWeight(byte[] bytes) {
        if (bytes != null && bytes.length > 5) {
            if (checkAsk(bytes)) {
                byte[] sizeFeild = Arrays.copyOfRange(bytes, 2, 3);
                int size = sizeFeild[0] & 0xFF;
                byte[] context = Arrays.copyOfRange(bytes, 3, 3 + size);
                int x = context[0] & 0xFF;
                int y = context[1] & 0xFF;
                double weight = (x * 256 + y) / 10.0;
//                MyLog.i("xxx", "weight  " + weight);
                return weight;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取进度数据
     *
     * @param bytes
     * @return
     */
    public static int getProgress(byte[] bytes) {
        if (bytes != null && bytes.length > 4) {
            if (checkAskIsProgress(bytes)) {
                byte[] progress = Arrays.copyOfRange(bytes, 4, 5);
                int x = progress[0] & 0xFF;
                return x;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 校验数据是否为 进度条 数据
     *
     * @param bytes
     * @return
     */
    public static boolean checkAskIsProgress(byte[] bytes) {
        if (bytes != null && bytes.length > 4) {
            if (checkAsk(bytes)) {
                byte[] context = Arrays.copyOfRange(bytes, 2, 4);
                if (Arrays.equals(progressFeild, context)) {
//                    MyLog.i("xxx", Arrays.equals(progressFeild, context) + "");
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 返回体脂数据
     *
     * @param bytes
     * @return
     */
    public static byte[] checkBodyFatAsk(byte[] bytes) {
        if (bytes != null && bytes.length > 4) {
            if (checkAsk(bytes) && !checkAskIsProgress(bytes) && !checkBodyContext(bytes)) {
                byte[] what = Arrays.copyOfRange(bytes, 2, 3);
                if (Arrays.equals(bodyAsk, what)) {
                    byte[] context = Arrays.copyOfRange(bytes, 3, bytes.length);
                    return context;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
