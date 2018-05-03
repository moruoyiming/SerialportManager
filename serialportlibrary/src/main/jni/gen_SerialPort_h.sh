
#!/bin/sh
javah -o SerialPort.h -jni com.serialport.library.core.SerialPort


//根据 SerialPort.java 类文件生成 c语言头文件 。
//cd 到目录java/ 下执行 javah -o SerialPort.h -jni com.serialport.library.core.SerialPort
//com.serialport.library.core 为包名。