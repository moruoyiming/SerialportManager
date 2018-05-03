package com.serialport.library;


import android.util.Log;

import com.serialport.library.core.SerialPort;
import com.serialport.library.listener.OnS3DataReceiverListener;
import com.serialport.library.listener.OnS6DataReceiverListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * Created by Jian on 2017/8/7.
 */
public class SerialportManager {
    private String TAG = SerialportManager.class.getSimpleName();
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private String path = "/dev/ttyS3";
    private int baudrate = 9600;
    private OnS3DataReceiverListener onS3DataReceiverListener = null;
    private SerialPort mScreenSerialPort;
    private OutputStream mScreenOutputStream;
    private InputStream mScreenInputStream;
    private ScreenReadThread mScreenReadThread;
    private String screenpath = "/dev/ttyS6";
    private int screenbaudrate = 115200;
    private OnS6DataReceiverListener onS6DataReceiverListener = null;
    private boolean isStop = false;
    private static SerialportManager portUtil;
    private boolean isSupportScreenSerialPort = false;

    public static SerialportManager getInstance() {
        if (portUtil == null) {
            synchronized (SerialportManager.class) {
                if (portUtil == null) {
                    portUtil = new SerialportManager();
                }
            }
        }
        return portUtil;
    }

    private SerialportManager() {
        try {
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            if (isSupportScreenSerialPort) {
                mScreenSerialPort = new SerialPort(new File(screenpath), screenbaudrate, 0);
                mScreenOutputStream = mScreenSerialPort.getOutputStream();
                mScreenInputStream = mScreenSerialPort.getInputStream();
            }
        } catch (Exception e) {
            portUtil = null;
            e.printStackTrace();
        }
    }


    public void InitThread() {
        isStop = false;
        if (mReadThread == null) {
            mReadThread = new ReadThread();
            mReadThread.start();
        }
        if (isSupportScreenSerialPort) {
            if (mScreenReadThread == null) {
                mScreenReadThread = new ScreenReadThread();
                mScreenReadThread.start();
            }
        }
    }

    public void finishThread() {
        if (mReadThread != null) {
            mReadThread.interrupt();
            mReadThread = null;
        }
        if (mScreenReadThread != null) {
            mScreenReadThread.interrupt();
            mScreenReadThread = null;
        }
    }


    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        byte[] mBuffer = (cmd + "\r\n").getBytes();
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendScreenBuffer(byte[] mBuffer) {
        boolean result = true;
        if (isSupportScreenSerialPort) {
            try {
                if (mScreenOutputStream != null) {
                    mScreenOutputStream.write(mBuffer);
                } else {
                    result = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public void setOnS3DataReceiverListener(OnS3DataReceiverListener onS3DataReceiverListener) {
        this.onS3DataReceiverListener = onS3DataReceiverListener;
    }

    public void setOnS6DataReceiverListener(OnS6DataReceiverListener onS6DataReceiverListener) {
        this.onS6DataReceiverListener = onS6DataReceiverListener;
    }

    private class ScreenReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isStop && !isInterrupted()) {
                int size;
                try {
                    if (mScreenInputStream == null)
                        return;
                    byte[] buffer = new byte[64];
                    size = mScreenInputStream.read(buffer);
                    if (size > 0) {
                        if (null != onS6DataReceiverListener) {
                            onS6DataReceiverListener.onS6DataReceive(buffer, size);
                        }
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (!isStop && !isInterrupted()) {
                int size;
                try {
                    if (mInputStream == null) {
                        return;
                    }
                    byte[] buffer = new byte[64];
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        if (null != onS3DataReceiverListener) {
                            onS3DataReceiverListener.onS3DataReceive(buffer, size);
                        }
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    Log.i("readthread", "throw exception !" + e.toString());
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
            mReadThread = null;
        }
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        if (mScreenReadThread != null) {
            mScreenReadThread.interrupt();
            mScreenReadThread = null;
        }

        if (mScreenSerialPort != null) {
            mScreenSerialPort.close();
            mScreenSerialPort = null;
        }
        try {
            if (mScreenOutputStream != null) {
                mScreenOutputStream.close();
                mScreenOutputStream = null;
            }
            if (mScreenInputStream != null) {
                mScreenInputStream.close();
                mScreenInputStream = null;
            }

            if (mInputStream != null) {
                mInputStream.close();
                mInputStream = null;
            }

            if (mOutputStream != null) {
                mOutputStream.close();
                mOutputStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}