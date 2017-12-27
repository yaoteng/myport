package com.yang.serialport.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class ByteUtils {

	/**
	 * 鐢ㄦ潵鎶妋ac瀛楃涓茶浆鎹负long
	 * 
	 * @param strMac
	 * @return
	 */
	public static long macToLong(String strMac) {
		byte[] mb = new BigInteger(strMac, 16).toByteArray();
		ByteBuffer mD = ByteBuffer.allocate(mb.length);
		mD.put(mb);
		long mac = 0;
		// 濡傛灉闀垮害绛変簬8浠ｈ〃娌℃湁琛�0;
		if (mD.array().length == 8) {
			mac = mD.getLong(0);
		} else if (mD.array().length == 9) {
			mac = mD.getLong(1);
		}
		return mac;
	}

	public static byte[] getBytes(Object obj) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bout);
		out.writeObject(obj);
		out.flush();
		byte[] bytes = bout.toByteArray();
		bout.close();
		out.close();

		return bytes;
	}

	/**
	 * 鍑芥暟鍚嶇О锛歨exStr2Byte</br> 鍔熻兘鎻忚堪锛歋tring 杞暟缁�
	 * 
	 * @param hex
	 * @return 淇敼鏃ュ織:</br>
	 *         <table>
	 *         <tr>
	 *         <th>鐗堟湰</th>
	 *         <th>鏃ユ湡</th>
	 *         <th>浣滆��</th>
	 *         <th>鎻忚堪</th>
	 *         <tr>
	 *         <td>0.1</td>
	 *         <td>2014-7-16</td>
	 *         <td>ZhaoQing</td>
	 *         <td>鍒濆鍒涘缓</td>
	 *         </table>
	 * @author ZhaoQing
	 */
	public static byte[] hexStr2Byte(String hex) {

		ByteBuffer bf = ByteBuffer.allocate(hex.length() / 2);
		for (int i = 0; i < hex.length(); i++) {
			String hexStr = hex.charAt(i) + "";
			i++;
			hexStr += hex.charAt(i);
			byte b = (byte) Integer.parseInt(hexStr, 16);
			bf.put(b);
		}
		return bf.array();
	}

	/**
	 * 鍑芥暟鍚嶇О锛歜yteToHex</br> 鍔熻兘鎻忚堪锛歜yte杞�16杩涘埗
	 * 
	 * @param b
	 * @return 淇敼鏃ュ織:</br>
	 *         <table>
	 *         <tr>
	 *         <th>鐗堟湰</th>
	 *         <th>鏃ユ湡</th>
	 *         <th>浣滆��</th>
	 *         <th>鎻忚堪</th>
	 *         <tr>
	 *         <td>0.1</td>
	 *         <td>2014-6-26</td>
	 *         <td>ZhaoQing</td>
	 *         <td>鍒濆鍒涘缓</td>
	 *         </table>
	 * @author ZhaoQing
	 */
	public static String byteToHex(byte b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;

		}
		return hex.toUpperCase(Locale.getDefault());

	}

	public static Object getObject(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(bi);
		Object obj = oi.readObject();
		bi.close();
		oi.close();
		return obj;
	}

	public static ByteBuffer getByteBuffer(Object obj) throws IOException {
		byte[] bytes = ByteUtils.getBytes(obj);
		ByteBuffer buff = ByteBuffer.wrap(bytes);

		return buff;
	}

	/**
	 * byte[] 杞瑂hort 2瀛楄妭
	 * 
	 * @param bytes
	 * @return
	 */
	public static short bytesToshort(byte[] bytes) {
		return (short) ((bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00));

	}

	/**
	 * byte 杞琁nt
	 * 
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte b) {
		return (b) & 0xff;
	}

	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[0] & 0xFF;
		addr |= ((bytes[1] << 8) & 0xFF00);
		addr |= ((bytes[2] << 16) & 0xFF0000);
		addr |= ((bytes[3] << 24) & 0xFF000000);
		return addr;
	}

	public static byte[] intToByte(int i) {

		byte[] abyte0 = new byte[4];
		abyte0[0] = (byte) (0xff & i);
		abyte0[1] = (byte) ((0xff00 & i) >> 8);
		abyte0[2] = (byte) ((0xff0000 & i) >> 16);
		abyte0[3] = (byte) ((0xff000000 & i) >> 24);
		return abyte0;

	}

	public static byte[] LongToByte(Long i) {

		byte[] abyte0 = new byte[8];
		abyte0[0] = (byte) (0xff & i);
		abyte0[1] = (byte) ((0xff00 & i) >> 8);
		abyte0[2] = (byte) ((0xff0000 & i) >> 16);
		abyte0[3] = (byte) ((0xff000000 & i) >> 24);
		abyte0[4] = (byte) ((0xff00000000l & i) >> 32);
		abyte0[5] = (byte) ((0xff0000000000l & i) >> 40);
		abyte0[6] = (byte) ((0xff000000000000l & i) >> 48);
		abyte0[7] = (byte) ((0xff00000000000000l & i) >> 56);
		return abyte0;

	}

	/**
	 * 鍑芥暟鍚嶇О锛歴hortChange</br> 鍔熻兘鎻忚堪锛歴hort 澶х杞皬绔�
	 * 
	 * @param mshort
	 * @return 淇敼鏃ュ織:</br>
	 *         <table>
	 *         <tr>
	 *         <th>鐗堟湰</th>
	 *         <th>鏃ユ湡</th>
	 *         <th>浣滆��</th>
	 *         <th>鎻忚堪</th>
	 *         <tr>
	 *         <td>0.1</td>
	 *         <td>2014-6-26</td>
	 *         <td>ZhaoQing</td>
	 *         <td>鍒濆鍒涘缓</td>
	 *         </table>
	 * @author ZhaoQing
	 */
	public static short shortChange(Short mshort) {

		mshort = (short) ((mshort >> 8 & 0xFF) | (mshort << 8 & 0xFF00));

		return mshort;
	}

	/**
	 * 鍑芥暟鍚嶇О锛歩ntChange</br> 鍔熻兘鎻忚堪锛歩nt 澶х杞皬绔�
	 * 
	 * @param mint
	 * @return 淇敼鏃ュ織:</br>
	 *         <table>
	 *         <tr>
	 *         <th>鐗堟湰</th>
	 *         <th>鏃ユ湡</th>
	 *         <th>浣滆��</th>
	 *         <th>鎻忚堪</th>
	 *         <tr>
	 *         <td>0.1</td>
	 *         <td>2014-6-26</td>
	 *         <td>ZhaoQing</td>
	 *         <td>鍒濆鍒涘缓</td>
	 *         </table>
	 * @author ZhaoQing
	 */
	public static int intChange(int mint) {

		mint = (int) (((mint) >> 24 & 0xFF) | ((mint) >> 8 & 0xFF00)
				| ((mint) << 8 & 0xFF0000) | ((mint) << 24 & 0xFF000000));

		return mint;
	}

	/**
	 * 鍑芥暟鍚嶇О锛歩ntChange</br> 鍔熻兘鎻忚堪锛歀ONG 澶х杞皬绔�
	 * 
	 * @param mint
	 * @return 淇敼鏃ュ織:</br>
	 *         <table>
	 *         <tr>
	 *         <th>鐗堟湰</th>
	 *         <th>鏃ユ湡</th>
	 *         <th>浣滆��</th>
	 *         <th>鎻忚堪</th>
	 *         <tr>
	 *         <td>0.1</td>
	 *         <td>2014-6-26</td>
	 *         <td>ZhaoQing</td>
	 *         <td>鍒濆鍒涘缓</td>
	 *         </table>
	 * @author ZhaoQing
	 */
	public static long longChange(long mint) {

		mint = (long) (((mint) >> 56 & 0xFF) | ((mint) >> 48 & 0xFF00)
				| ((mint) >> 24 & 0xFF0000) | ((mint) >> 8 & 0xFF000000)
				| ((mint) << 8 & 0xFF00000000l)
				| ((mint) << 24 & 0xFF0000000000l)
				| ((mint) << 40 & 0xFF000000000000l) | ((mint) << 56 & 0xFF00000000000000l));

		return mint;
	}

	/**
	 * 灏哹yte杞崲涓烘棤绗﹀彿鐨剆hort绫诲瀷
	 * 
	 * @param b
	 *            闇�瑕佽浆鎹㈢殑瀛楄妭鏁�
	 * @return 杞崲瀹屾垚鐨剆hort
	 */
	public static short byteToUshort(byte b) {
		return (short) (b & 0x00ff);
	}

	/**
	 * 灏哹yte杞崲涓烘棤绗﹀彿鐨刬nt绫诲瀷
	 * 
	 * @param b
	 *            闇�瑕佽浆鎹㈢殑瀛楄妭鏁�
	 * @return 杞崲瀹屾垚鐨刬nt
	 */
	public static int byteToUint(byte b) {
		return b & 0x00ff;
	}

	/**
	 * 灏哹yte杞崲涓烘棤绗﹀彿鐨刲ong绫诲瀷
	 * 
	 * @param b
	 *            闇�瑕佽浆鎹㈢殑瀛楄妭鏁�
	 * @return 杞崲瀹屾垚鐨刲ong
	 */
	public static long byteToUlong(byte b) {
		return b & 0x00ff;
	}

	/**
	 * 灏唖hort杞崲涓烘棤绗﹀彿鐨刬nt绫诲瀷
	 * 
	 * @param s
	 *            闇�瑕佽浆鎹㈢殑short
	 * @return 杞崲瀹屾垚鐨刬nt
	 */
	public static int shortToUint(short s) {
		return s & 0x00ffff;
	}

	/**
	 * 灏唖hort杞崲涓烘棤绗﹀彿鐨刲ong绫诲瀷
	 * 
	 * @param s
	 *            闇�瑕佽浆鎹㈢殑瀛楄妭鏁�
	 * @return 杞崲瀹屾垚鐨刲ong
	 */
	public static long shortToUlong(short s) {
		return s & 0x00ffff;
	}

	/**
	 * 灏唅nt杞崲涓烘棤绗﹀彿鐨刲ong绫诲瀷
	 * 
	 * @param i
	 *            闇�瑕佽浆鎹㈢殑瀛楄妭鏁�
	 * @return 杞崲瀹屾垚鐨刲ong
	 */
	public static long intToUlong(int i) {
		return i & 0x00ffffffff;
	}

	/**
	 * 灏唖hort杞崲鎴愬皬绔簭鐨刡yte鏁扮粍
	 * 
	 * @param s
	 *            闇�瑕佽浆鎹㈢殑short
	 * @return 杞崲瀹屾垚鐨刡yte鏁扮粍
	 */
	public static byte[] shortToLittleEndianByteArray(short s) {
		return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN)
				.putShort(s).array();
	}

	/**
	 * 灏唅nt杞崲鎴愬皬绔簭鐨刡yte鏁扮粍
	 * 
	 * @param i
	 *            闇�瑕佽浆鎹㈢殑int
	 * @return 杞崲瀹屾垚鐨刡yte鏁扮粍
	 */
	public static byte[] intToLittleEndianByteArray(int i) {
		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i)
				.array();
	}

	/**
	 * 灏唋ong杞崲鎴愬皬绔簭鐨刡yte鏁扮粍
	 * 
	 * @param l
	 *            闇�瑕佽浆鎹㈢殑long
	 * @return 杞崲瀹屾垚鐨刡yte鏁扮粍
	 */
	public static byte[] longToLittleEndianByteArray(long l) {
		return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(l)
				.array();
	}

	/**
	 * 灏唖hort杞崲鎴愬ぇ绔簭鐨刡yte鏁扮粍
	 * 
	 * @param s
	 *            闇�瑕佽浆鎹㈢殑short
	 * @return 杞崲瀹屾垚鐨刡yte鏁扮粍
	 */
	public static byte[] shortToBigEndianByteArray(short s) {
		return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(s)
				.array();
	}

	/**
	 * 灏唅nt杞崲鎴愬ぇ绔簭鐨刡yte鏁扮粍
	 * 
	 * @param i
	 *            闇�瑕佽浆鎹㈢殑int
	 * @return 杞崲瀹屾垚鐨刡yte鏁扮粍
	 */
	public static byte[] intToBigEndianByteArray(int i) {
		return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putInt(i)
				.array();
	}

	/**
	 * 灏唋ong杞崲鎴愬ぇ绔簭鐨刡yte鏁扮粍
	 * 
	 * @param l
	 *            闇�瑕佽浆鎹㈢殑long
	 * @return 杞崲瀹屾垚鐨刡yte鏁扮粍
	 */
	public static byte[] longToBigEndianByteArray(long l) {
		return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putLong(l)
				.array();
	}

	/**
	 * 灏唖hort杞崲涓�16杩涘埗瀛楃涓�
	 * 
	 * @param s
	 *            闇�瑕佽浆鎹㈢殑short
	 * @param isLittleEndian
	 *            鏄惁鏄皬绔簭锛坱rue涓哄皬绔簭false涓哄ぇ绔簭锛�
	 * @return 杞崲鍚庣殑瀛楃涓�
	 */
	public static String shortToHexString(short s, boolean isLittleEndian) {
		byte byteArray[] = null;
		if (isLittleEndian) {
			byteArray = shortToLittleEndianByteArray(s);
		} else {
			byteArray = shortToBigEndianByteArray(s);
		}
		return byteArrayToHexString(byteArray);
	}

	/**
	 * 灏唅nt杞崲涓�16杩涘埗瀛楃涓�
	 * 
	 * @param i
	 *            闇�瑕佽浆鎹㈢殑int
	 * @param isLittleEndian
	 *            鏄惁鏄皬绔簭锛坱rue涓哄皬绔簭false涓哄ぇ绔簭锛�
	 * @return 杞崲鍚庣殑瀛楃涓�
	 */
	public static String intToHexString(int i, boolean isLittleEndian) {
		byte byteArray[] = null;
		if (isLittleEndian) {
			byteArray = intToLittleEndianByteArray(i);
		} else {
			byteArray = intToBigEndianByteArray(i);
		}
		return byteArrayToHexString(byteArray);
	}

	/**
	 * 灏唋ong杞崲涓�16杩涘埗瀛楃涓�
	 * 
	 * @param l
	 *            闇�瑕佽浆鎹㈢殑long
	 * @param isLittleEndian
	 *            鏄惁鏄皬绔簭锛坱rue涓哄皬绔簭false涓哄ぇ绔簭锛�
	 * @return 杞崲鍚庣殑瀛楃涓�
	 */
	public static String longToHexString(long l, boolean isLittleEndian) {
		byte byteArray[] = null;
		if (isLittleEndian) {
			byteArray = longToLittleEndianByteArray(l);
		} else {
			byteArray = longToBigEndianByteArray(l);
		}
		return byteArrayToHexString(byteArray);
	}

	/**
	 * 灏嗗瓧鑺傛暟缁勮浆鎹㈡垚16杩涘埗瀛楃涓�
	 * 
	 * @param array
	 *            闇�瑕佽浆鎹㈢殑瀛楃涓�
	 * @param toPrint
	 *            鏄惁涓轰簡鎵撳嵃杈撳嚭锛屽鏋滀负true鍒欎細姣�4鑷繁娣诲姞涓�涓┖鏍�
	 * @return 杞崲瀹屾垚鐨勫瓧绗︿覆
	 */
	public static String byteArrayToHexString(byte[] array, boolean toPrint) {
		if (array == null) {
			return "null";
		}
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < array.length; i++) {
			sb.append(byteToHex(array[i]));
			if (toPrint && (i + 1) % 4 == 0) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * 灏嗗瓧鑺傛暟缁勮浆鎹㈡垚16杩涘埗瀛楃涓�
	 * 
	 * @param array
	 *            闇�瑕佽浆鎹㈢殑瀛楃涓�(瀛楄妭闂存病鏈夊垎闅旂)
	 * @return 杞崲瀹屾垚鐨勫瓧绗︿覆
	 */
	public static String byteArrayToHexString(byte[] array) {
		return byteArrayToHexString(array, false);
	}

	/**
	 * 灏嗗瓧鑺傛暟缁勮浆鎹㈡垚long绫诲瀷
	 * 
	 * @param bytes
	 *            瀛楄妭鏁版嵁
	 * @return long绫诲瀷
	 */
	public static long byteArrayToLong(byte[] bytes) {
		return ((((long) bytes[0] & 0xff) << 24)
				| (((long) bytes[1] & 0xff) << 16)
				| (((long) bytes[2] & 0xff) << 8) | (((long) bytes[3] & 0xff) << 0));
	}
	
	public static String hexString2binaryString(String hexString) {
	   
	    String binaryString = "";
	    for (int i = 0; i < hexString.length(); i++) {
	        //截取hexStr的一位
	        String hex = hexString.substring(i, i + 1);
	        //通过toBinaryString将十六进制转为二进制
	        String binary = Integer.toBinaryString(Integer.parseInt(hex, 16));
	        //因为高位0会被舍弃，先补上4个0
	        String tmp = "0000" + binary;
	        //取最后4位，将多补的0去掉
	        binaryString += tmp.substring(tmp.length() - 4);
	    }
	    return binaryString;
	}
}
