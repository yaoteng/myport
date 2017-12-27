/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialporttx;

/**
 *
 * @author Administrator
 */
 
import gnu.io.SerialPort;
import java.util.Observer;
import java.util.*;

public class Test implements Observer{ 
	 
	SerialReader sr=new SerialReader(); 
    public Test()
    {    
       openSerialPort(""); //鎵撳紑涓插彛銆�
    } 
    public void update(Observable o, Object arg){    
    	String mt=new String((byte[])arg);  
    	System.out.println("---"+mt); //涓插彛鏁版嵁 
    } 
    
    /**
     * 寰�涓插彛鍙戦�佹暟鎹�,瀹炵幇鍙屽悜閫氳�?.
     * @param string message
     */
    public void send(String message)
    {
    	Test test = new Test();
    	test.openSerialPort(message);
    }
	
    /**
     * 鎵撳紑涓插彛
     * @param String message
     */
	public void openSerialPort(String message)
    { 
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();  
        String port="COM3";
        String rate = "115200";
        String dataBit = ""+SerialPort.DATABITS_8;
        String stopBit = ""+SerialPort.STOPBITS_1;
        String parity = ""+SerialPort.PARITY_NONE;    
        int parityInt = SerialPort.PARITY_NONE; 
        params.put( SerialReader.PARAMS_PORT, port ); // 绔彛鍚嶇�?
        params.put( SerialReader.PARAMS_RATE, rate ); // 娉㈢壒鐜�?
        params.put( SerialReader.PARAMS_DATABITS,dataBit  ); // 鏁版嵁浣�?
        params.put( SerialReader.PARAMS_STOPBITS, stopBit ); // 鍋滄浣�?
        params.put( SerialReader.PARAMS_PARITY, parityInt ); // 鏃犲鍋舵牎楠�
        params.put( SerialReader.PARAMS_TIMEOUT,100 ); // 璁惧瓒呮椂鏃堕�? 1绉�
        params.put( SerialReader.PARAMS_DELAY, 100 ); // 绔彛鏁版嵁鍑嗗鏃堕棿 1绉�
        try {
			sr.open(params);
		    sr.addObserver(this);
			if(message!=null&&message.length()!=0)
			 {  
				sr.start();  
				sr.run(message);  
			 } 
		} catch (Exception e) { 
		}
    }
    
	
	 public String Bytes2HexString(byte[] b) { 
		   String ret = ""; 
		   for (int i = 0; i < b.length; i++) { 
			     String hex = Integer.toHexString(b[i] & 0xFF); 
			     if (hex.length() == 1) { 
			       hex = '0' + hex; 
				     } 
			     ret += hex.toUpperCase(); 
			   }
		return ret;
	   }


	  public  String hexString2binaryString(String hexString) {
	  if (hexString == null || hexString.length() % 2 != 0)
		 return null;
		 String bString = "", tmp;
		 for (int i = 0; i < hexString.length(); i++) {
		 tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			  bString += tmp.substring(tmp.length() - 4);
		  }
		 return bString;
	  } 
} 