package com.serialport.library.protocol;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jian on 2017/8/7.
 */

public class BaseProtocol {
    /**
     * 指令组装
     *
     * @param bytes
     * @return
     */
    public static byte[] buildControllerProtocol(byte[]... bytes) {
        List<Byte> byteArray = new LinkedList<Byte>();
        for (byte[] bytes1 : bytes) {
            for (byte b : bytes1) {
                byteArray.add(b);
            }
        }
        byte[] ret = new byte[byteArray.size()];
        for (int i = 0; i < byteArray.size(); i++) {
            ret[i] = byteArray.get(i);
        }
        return ret;
    }
}
