package com.yang.serialport.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AppendToFile {  
    /** 
     * A����׷���ļ���ʹ��RandomAccessFile 
     */  
    public static void appendMethodA(String fileName, String content) {  
        try {  
            // ��һ����������ļ���������д��ʽ  
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");  
            // �ļ����ȣ��ֽ���  
            long fileLength = randomFile.length();  
            //��д�ļ�ָ���Ƶ��ļ�β��  
            randomFile.seek(fileLength);  
            randomFile.writeBytes(content);  
            randomFile.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * B����׷���ļ���ʹ��FileWriter 
     */  
    public static void appendMethodB(String fileName, String content) {  
        try {  
            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�  
            FileWriter writer = new FileWriter(fileName, true);  
            writer.write(content);  
            writer.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}