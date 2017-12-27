package com.yang.serialport.manage;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import com.yang.serialport.exception.NoSuchPort;
import com.yang.serialport.exception.NotASerialPort;
import com.yang.serialport.exception.PortInUse;
import com.yang.serialport.exception.ReadDataFromSerialPortFailure;
import com.yang.serialport.exception.SendDataToSerialPortFailure;
import com.yang.serialport.exception.SerialPortInputStreamCloseFailure;
import com.yang.serialport.exception.SerialPortOutputStreamCloseFailure;
import com.yang.serialport.exception.SerialPortParameterFailure;
import com.yang.serialport.exception.TooManyListeners;

/**
 * 涓插彛绠＄悊
 * 
 * @author yangle
 */
public class SerialPortManager {

	/**
	 * 鏌ユ壘鎵�鏈夊彲鐢ㄧ鍙�
	 * 
	 * @return 鍙敤绔彛鍚嶇О鍒楄〃
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<String> findPort() {
		// 鑾峰緱褰撳墠鎵�鏈夊彲鐢ㄤ覆鍙�
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier
				.getPortIdentifiers();
		ArrayList<String> portNameList = new ArrayList<String>();
		// 灏嗗彲鐢ㄤ覆鍙ｅ悕娣诲姞鍒癓ist骞惰繑鍥炶List
		while (portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}
		return portNameList;
	}

	/**
	 * 鎵撳紑涓插彛
	 * 
	 * @param portName
	 *            绔彛鍚嶇О
	 * @param baudrate
	 *            娉㈢壒鐜�
	 * @return 涓插彛瀵硅薄
	 * @throws SerialPortParameterFailure
	 *             璁剧疆涓插彛鍙傛暟澶辫触
	 * @throws NotASerialPort
	 *             绔彛鎸囧悜璁惧涓嶆槸涓插彛绫诲瀷
	 * @throws NoSuchPort
	 *             娌℃湁璇ョ鍙ｅ搴旂殑涓插彛璁惧
	 * @throws PortInUse
	 *             绔彛宸茶鍗犵敤
	 */
	public static final SerialPort openPort(String portName, int baudrate)
			throws SerialPortParameterFailure, NotASerialPort, NoSuchPort,
			PortInUse {
		try {
			// 閫氳繃绔彛鍚嶈瘑鍒鍙�
			CommPortIdentifier portIdentifier = CommPortIdentifier
					.getPortIdentifier(portName);
			// 鎵撳紑绔彛锛屽苟缁欑鍙ｅ悕瀛楀拰涓�涓猼imeout锛堟墦寮�鎿嶄綔鐨勮秴鏃舵椂闂达級
			CommPort commPort = portIdentifier.open(portName, 2000);
			// 鍒ゆ柇鏄笉鏄覆鍙�
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				try {
					// 璁剧疆涓�涓嬩覆鍙ｇ殑娉㈢壒鐜囩瓑鍙傛暟
					serialPort.setSerialPortParams(baudrate,
							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
				} catch (UnsupportedCommOperationException e) {
					throw new SerialPortParameterFailure();
				}
				return serialPort;
			} else {
				// 涓嶆槸涓插彛
				throw new NotASerialPort();
			}
		} catch (NoSuchPortException e1) {
			throw new NoSuchPort();
		} catch (PortInUseException e2) {
			throw new PortInUse();
		}
	}

	/**
	 * 鍏抽棴涓插彛
	 * 
	 * @param serialport
	 *            寰呭叧闂殑涓插彛瀵硅薄
	 */
	public static void closePort(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
	}

	/**
	 * 寰�涓插彛鍙戦�佹暟鎹�
	 * 
	 * @param serialPort
	 *            涓插彛瀵硅薄
	 * @param order
	 *            寰呭彂閫佹暟鎹�
	 * @throws SendDataToSerialPortFailure
	 *             鍚戜覆鍙ｅ彂閫佹暟鎹け璐�
	 * @throws SerialPortOutputStreamCloseFailure
	 *             鍏抽棴涓插彛瀵硅薄鐨勮緭鍑烘祦鍑洪敊
	 */
	public static void sendToPort(SerialPort serialPort, byte[] order)
			throws SendDataToSerialPortFailure,
			SerialPortOutputStreamCloseFailure {
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			out.write(order);
			out.flush();
		} catch (IOException e) {
			throw new SendDataToSerialPortFailure();
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				throw new SerialPortOutputStreamCloseFailure();
			}
		}
	}

	/**
	 * 浠庝覆鍙ｈ鍙栨暟鎹�
	 * 
	 * @param serialPort
	 *            褰撳墠宸插缓绔嬭繛鎺ョ殑SerialPort瀵硅薄
	 * @return 璇诲彇鍒扮殑鏁版嵁
	 * @throws ReadDataFromSerialPortFailure
	 *             浠庝覆鍙ｈ鍙栨暟鎹椂鍑洪敊
	 * @throws SerialPortInputStreamCloseFailure
	 *             鍏抽棴涓插彛瀵硅薄杈撳叆娴佸嚭閿�
	 */
	public static byte[] readFromPort(SerialPort serialPort)
			throws ReadDataFromSerialPortFailure,
			SerialPortInputStreamCloseFailure {
		InputStream in = null;
		byte[] bytes = null;
		try {
			in = serialPort.getInputStream();
			// 鑾峰彇buffer閲岀殑鏁版嵁闀垮害
			int bufflenth = in.available();
			while (bufflenth != 0) {
				// 鍒濆鍖朾yte鏁扮粍涓篵uffer涓暟鎹殑闀垮害
				bytes = new byte[bufflenth];
				in.read(bytes);
				bufflenth = in.available();
			}
			
			
			/*int bytesRead = in.read(bytes);
		    while (bytesRead != -1) {
		    	
		        
		        bytesRead = in.read(bytes);
		       
		        return bytes;
		    }*/
		} catch (IOException e) {
			throw new ReadDataFromSerialPortFailure();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				
			} catch (IOException e) {
				throw new SerialPortInputStreamCloseFailure();
			}
		}
		return bytes;
	}

	/**
	 * 娣诲姞鐩戝惉鍣�
	 * 
	 * @param port
	 *            涓插彛瀵硅薄
	 * @param listener
	 *            涓插彛鐩戝惉鍣�
	 * @throws TooManyListeners
	 *             鐩戝惉绫诲璞¤繃澶�
	 */
	public static void addListener(SerialPort port,
			SerialPortEventListener listener) throws TooManyListeners {
		try {
			// 缁欎覆鍙ｆ坊鍔犵洃鍚櫒
			port.addEventListener(listener);
			// 璁剧疆褰撴湁鏁版嵁鍒拌揪鏃跺敜閱掔洃鍚帴鏀剁嚎绋�
			port.notifyOnDataAvailable(true);
			// 璁剧疆褰撻�氫俊涓柇鏃跺敜閱掍腑鏂嚎绋�
			port.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			throw new TooManyListeners();
		}
	}
}
