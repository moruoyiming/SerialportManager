# SerialportManager

## JNI 串口通讯库 SerialPort开发封装


  ### 前言
 
  最近工作比较清闲，闲来无事，把原先项目用到的串口通讯项目所涉及到的知识及项目简化出来一个库，方便以后开发新项目。同时希望
   
  对其他小伙伴有所帮助。项目涉及到 ndk工程构建及硬件串口通讯。期间涉及到硬件屏幕功能开发这里不做多介绍。
   
  下面从NDK项目构建开始说起。
  
  NDK是Google为便于Android开发提供的一种原生开发集：Native Development Kit，而且也是一个包含API、构建工具、交叉编译、调
  试器、文档示例等一系列的工具集，可以帮助开发者快速开发C（或C++）的动态库，并能自动将so和java应用一起打包成APK。
  
  与NDK密切相关的另一个词汇则是JNI，它是NDK开发中的枢纽，Java与底层交互绝大多数都是通过它来完成的，那么接下来看看什么是
  JNI?
  
  JNI：Java Native Interface 也就是java本地接口，它是一个协议，这个协议用来沟通java代码和本地代码(c/c++)。通过这个
  协议，Java类的某些方法可以使用原生实现，同时让它们可以像普通的Java方法一样被调用和使用，而原生方法也可以使用Java对象，
  调用和使用Java方法。也就是说，使用JNI这种协议可以实现：java代码调用c/c++代码，而c/c++代码也可以调用java代码。
  
  那为什么要使用NDK开发呢？
  
  我们都知道，java是半解释型语言，很容易被反汇编后拿到源代码文件，在开发一些重要协议时，我们为了安全起见，使用C语言来编写
  这些重要的部分，来增大系统的安全性。
  
  在一些复杂性的计算中，要求高性能的场景中，C/C++更加的有效率，代码也更便于复用。
  
  当然还有其他的优点，这些都驱使我们选择相对来说高效和安全的DNK来开发我们的应用程序。
  
  ### NDK环境搭建
  
  #### 1.下载NDK
  
   首先下载NDK，可以从AndroidStudio中的SDK Manager中下载，也可自己单独下载
   
   ![image](https://github.com/moruoyiming/SerialportManager/blob/master/pics/QQ20180503-151610%402x.png)
   
   点击按钮进入 
   
   ![image](https://github.com/moruoyiming/SerialportManager/blob/master/pics/QQ20180503-151818%402x.png)
   
   
   或者进入http://www.androiddevtools.cn/ 下载
   
   [Windows 64-bit](https://dl.google.com/android/repository/android-ndk-r16-beta1-windows-x86_64.zip?utm_source=androiddevtools&utm_medium=website?raw=true)
   
   [Mac OS X](https://dl.google.com/android/repository/android-ndk-r16-beta1-darwin-x86_64.zip?utm_source=androiddevtools&utm_medium=website?raw=true)
   
   如单独下载
  
    1). 解压NDK的zip包，注意路径目录不要出现空格和中文，这里建议大家把包解压到SDK目录里面，并命名为ndk-bundle，好处是，启动AS的时候会检查它并直接添加到ndk.dir中，减少我们的配置工作；
   
    2). 配置path : 把解压好的路径添加到环境变量path中；
   
    3). ndk-build：cd到解压后NDK的根目录，执行ndk-build命令。
   
   #### 2.安装配置NDK
   
   AndroidStudio 点击File -> Other Settings -> Default Project Strjucture  如图
   
   ![image](https://github.com/moruoyiming/SerialportManager/blob/master/pics/QQ20180503-155357%402x.png)
   
   ![image](https://github.com/moruoyiming/SerialportManager/blob/master/pics/QQ20180503-155822%402x.png)
   
   到这里NDK配置完成，接下来 开始 NDK 开发。
   
   
   ### NDK项目开发
   
   在library 中的 build.gradle  文件中的 defaultConfig 中 配置
   
       ndk {
               moduleName "serial_port"
               // 设置支持的SO库架构
               abiFilters 'armeabi', 'x86', 'armeabi-v7a', 'x86_64', 'arm64-v8a'
           }
           
           
   在android 中配置
   
    sourceSets { main { jni.srcDirs = ['src/main/jni', 'src/main/jni/'] } }
       externalNativeBuild {
           ndkBuild {
               path 'src/main/jni/Android.mk'
           }
       }
       
   如图
   
   ![image](https://github.com/moruoyiming/SerialportManager/blob/master/pics/QQ20180503-164512%402x.png)
   
   Android.mk 文件中配置如下内容   [Android.mk用法详解](https://www.cnblogs.com/reality-soul/p/6893248.html)
   
       #
       # Copyright 2009 Cedric Priscal
       # 
       # Licensed under the Apache License, Version 2.0 (the "License");
       # you may not use this file except in compliance with the License.
       # You may obtain a copy of the License at
       # 
       # http://www.apache.org/licenses/LICENSE-2.0
       # 
       # Unless required by applicable law or agreed to in writing, software
       # distributed under the License is distributed on an "AS IS" BASIS,
       # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       # See the License for the specific language governing permissions and
       # limitations under the License. 
       #
       
       LOCAL_PATH := $(call my-dir)
       
       include $(CLEAR_VARS)
       
       TARGET_PLATFORM := android-3
       LOCAL_MODULE    := serial_port      //项目名称
       LOCAL_SRC_FILES := SerialPort.c     //底层c
       LOCAL_LDLIBS    := -llog
       
       include $(BUILD_SHARED_LIBRARY)
       
   在main目录下创建一个jni文件目录，并将 Android.mk 文件放到jni文件下
   
   
   直接使用网上 SerialPort.java 类，里边封装底层方法
   
    package com.serialport.library.core;
   
    import java.io.File;
    import java.io.FileDescriptor;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
   
    /**
    * Created by Jian on 2017/8/7.
    * 用来加载SO文件，通过JNI的方式打开关闭串口
    */
    public class SerialPort {
   
       private static final String TAG = "SerialPort";
       /*
        * Do not remove or rename the field mFd: it is used by native method close();
        */
       private FileDescriptor mFd;
       private FileInputStream mFileInputStream;
       private FileOutputStream mFileOutputStream;
   
       public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
   
   		/* Check access permission */
           if (!device.canRead() || !device.canWrite()) {
               try {
                   /* Missing read/write permission, trying to chmod the file */
                   Process su;
                   su = Runtime.getRuntime().exec("/system/bin/su");
                   String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                           + "exit\n";
                   su.getOutputStream().write(cmd.getBytes());
                   if ((su.waitFor() != 0) || !device.canRead()
                           || !device.canWrite()) {
                       throw new SecurityException();
   
                   }
               } catch (Exception e) {
                   e.printStackTrace();
                   throw new SecurityException();
               }
           }
   
           mFd = open(device.getAbsolutePath(), baudrate, flags);
           if (mFd == null) {
               throw new IOException();
           }
           mFileInputStream = new FileInputStream(mFd);
           mFileOutputStream = new FileOutputStream(mFd);
       }
   
       // Getters and setters
       public InputStream getInputStream() {
           return mFileInputStream;
       }
   
       public OutputStream getOutputStream() {
           return new FileOutputStream(mFd);
       }
   
       // JNI
       private native static FileDescriptor open(String path, int baudrate, int flags);
   
       public native void close();
   
       static {
           System.loadLibrary("serial_port");
       }
    }
    
    
   点击"View->Tool Windows->Terminal"，即在Studio中进行终端命令行工具.执行如下命令生成c语言头文件:
   
   cd 到目录java/ 下执行 javah -o SerialPort.h -jni com.serialport.library.core.SerialPort
    
    javah -o SerialPort.h -jni com.serialport.library.core.SerialPort
    
   com.serialport.library.core 为包名。
   
   SerialPort.h 文件如下
  
      /* DO NOT EDIT THIS FILE - it is machine generated */
      #include <jni.h>
      /* Header for class com_serialport_library_core_SerialPort */
      
      #ifndef _Included_com_serialport_library_core_SerialPort
      #define _Included_com_serialport_library_core_SerialPort
      #ifdef __cplusplus
      extern "C" {
      #endif
      /*
       * Class:     com_serialport_library_core_SerialPort
       * Method:    open
       * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
       */
      JNIEXPORT jobject JNICALL Java_com_serialport_library_core_SerialPort_open
        (JNIEnv *, jclass, jstring, jint, jint);
      
      /*
       * Class:     com_serialport_library_core_SerialPort
       * Method:    close
       * Signature: ()V
       */
      JNIEXPORT void JNICALL Java_com_serialport_library_core_SerialPort_close
        (JNIEnv *, jobject);
      
      #ifdef __cplusplus
      }
      #endif
      #endif
   
   
   并把 SerialPort.h 头文件转移到jni文件夹下
   
   创建实现头文件的.C源文件，将 com_serialport_library_core 为SerialPort.java 文件位置，将该 path 替换成其他 项目包名 符号.换成_
    
    /*
     * Copyright 2009-2011 Cedric Priscal
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    
    #include <termios.h>
    #include <unistd.h>
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <fcntl.h>
    #include <string.h>
    #include <jni.h>
    #include "SerialPort.h"
    #include "android/log.h"
    static const char *TAG = "serial_port";
    #define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
    #define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
    #define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)
    
    static speed_t getBaudrate(jint baudrate) {
        switch (baudrate) {
            case 0:
                return B0;
            case 50:
                return B50;
            case 75:
                return B75;
            case 110:
                return B110;
            case 134:
                return B134;
            case 150:
                return B150;
            case 200:
                return B200;
            case 300:
                return B300;
            case 600:
                return B600;
            case 1200:
                return B1200;
            case 1800:
                return B1800;
            case 2400:
                return B2400;
            case 4800:
                return B4800;
            case 9600:
                return B9600;
            case 19200:
                return B19200;
            case 38400:
                return B38400;
            case 57600:
                return B57600;
            case 115200:
                return B115200;
            case 230400:
                return B230400;
            case 460800:
                return B460800;
            case 500000:
                return B500000;
            case 576000:
                return B576000;
            case 921600:
                return B921600;
            case 1000000:
                return B1000000;
            case 1152000:
                return B1152000;
            case 1500000:
                return B1500000;
            case 2000000:
                return B2000000;
            case 2500000:
                return B2500000;
            case 3000000:
                return B3000000;
            case 3500000:
                return B3500000;
            case 4000000:
                return B4000000;
            default:
                return -1;
        }
    }
    
    /*
     * Class:     com_serialport_library_core_SerialPort
     * Method:    open
     * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
     */
    JNIEXPORT jobject JNICALL Java_com_serialport_library_core_SerialPort_open
            (JNIEnv *env, jclass thiz, jstring path, jint baudrate, jint flags) {
        int fd;
        speed_t speed;
        jobject mFileDescriptor;
    
        /* Check arguments */
        {
            speed = getBaudrate(baudrate);
            if (speed == -1) {
                /* TODO: throw an exception */
                LOGE("Invalid baudrate");
                return NULL;
            }
        }
    
        /* Opening device */
        {
            jboolean iscopy;
            const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
            LOGD("Opening serial port %s with flags 0x%x", path_utf, O_RDWR | flags);
            fd = open(path_utf, O_RDWR | flags);
            LOGD("open() fd = %d", fd);
            (*env)->ReleaseStringUTFChars(env, path, path_utf);
            if (fd == -1) {
                /* Throw an exception */
                LOGE("Cannot open port");
                /* TODO: throw an exception */
                return NULL;
            }
        }
    
        /* Configure device */
        {
            struct termios cfg;
            LOGD("Configuring serial port");
            if (tcgetattr(fd, &cfg)) {
                LOGE("tcgetattr() failed");
                close(fd);
                /* TODO: throw an exception */
                return NULL;
            }
    
            cfmakeraw(&cfg);
            cfsetispeed(&cfg, speed);
            cfsetospeed(&cfg, speed);
    
            if (tcsetattr(fd, TCSANOW, &cfg)) {
                LOGE("tcsetattr() failed");
                close(fd);
                /* TODO: throw an exception */
                return NULL;
            }
        }
    
        /* Create a corresponding file descriptor */
        {
            jclass cFileDescriptor = (*env)->FindClass(env, "java/io/FileDescriptor");
            jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor, "<init>", "()V");
            jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor, "descriptor", "I");
            mFileDescriptor = (*env)->NewObject(env, cFileDescriptor, iFileDescriptor);
            (*env)->SetIntField(env, mFileDescriptor, descriptorID, (jint) fd);
        }
    
        return mFileDescriptor;
    }
    
    /*
     * Class:     com_serialport_library_core_SerialPort
     * Method:    close
     * Signature: ()V
     */
    JNIEXPORT void JNICALL Java_com_serialport_library_core_SerialPort_close
            (JNIEnv *env, jobject thiz) {
        jclass SerialPortClass = (*env)->GetObjectClass(env, thiz);
        jclass FileDescriptorClass = (*env)->FindClass(env, "java/io/FileDescriptor");
    
        jfieldID mFdID = (*env)->GetFieldID(env, SerialPortClass, "mFd", "Ljava/io/FileDescriptor;");
        jfieldID descriptorID = (*env)->GetFieldID(env, FileDescriptorClass, "descriptor", "I");
    
        jobject mFd = (*env)->GetObjectField(env, thiz, mFdID);
        jint descriptor = (*env)->GetIntField(env, mFd, descriptorID);
    
        LOGD("close(fd = %d)", descriptor);
        close(descriptor);
    }
    
    
   ## 到此 NDK 项目搭建完成。接下来 介绍一下 SerialPortManager 类库
