package uhf;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.sun.jna.Native;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

public class UhfReader {
	static public final int	UHF_DATA_SECTION_RESERVED	=	0;
	static public final int	UHF_DATA_SECTION_EPC 		=	1;
	static public final int	UHF_DATA_SECTION_TID		=	2;
	static public final int	UHF_DATA_SECTION_USER		=	3;
	
	//ioCtrlType����
	static public final int	UHF_IO_CONTROL_IO_DIR	=	0;
	static public final int	UHF_IO_CONTROL_IO_SET_EL=	1;
	static public final int	UHF_IO_CONTROL_IO_GET_EL=	2;
	
	//ȡֵ0��2��4��6���ֱ��Ӧ��ͬ������9600/19200/57600/115200
	static public final int	UHF_BAUDRATE_9600	=	0;
	static public final int	UHF_BAUDRATE_19200	=	2;
	static public final int	UHF_BAUDRATE_57600	=	4;
	static public final int	UHF_BAUDRATE_115200	=	6;
	
	public static UhfReaderAPI ns=null;
	private int handle=0;
	private int flagCrc=0;
	
	//Initial automatically
	static{
		String parentFolder="";
		
//    	URL url = UhfReaderAPI.class.getClass().getResource("/");
//    	if(url != null){
//    		File file = new File(url.getFile());
//    		parentFolder=file.getParent();
//    	}
//    	else
    	{
    		parentFolder=GetModuleFolder();
    		if(parentFolder.substring(parentFolder.length()-1,parentFolder.length()).compareTo("/")==0){
    			parentFolder=parentFolder.substring(0, parentFolder.length()-1);
    		}
    	}
		String dllPath;
    	String[] paths = new String[]{"/UhfReader_API.dll"};
    	for (int i = 0; i < paths.length; i++) {
    		dllPath = parentFolder + paths[i];
    		try {
            	dllPath = URLDecoder.decode(dllPath, "UTF-8");
            	System.out.println("try to load"+dllPath);
            	ns=(UhfReaderAPI)Native.loadLibrary(dllPath, UhfReaderAPI.class);
    		}catch (Throwable e) {
    			if(i==paths.length-1)
    				e.printStackTrace();
    		}
        	if(ns!=null)
        		break;
		}
	}
	
	private static String GetModuleFolder()
	{
		//��������fatjar���棬·����ʽΪ��file:/D:/MyDocuments/MyEclipse/UhfReader/UhfReader_fat.jar!/main/main.jar
		String LOCATION="";
	    try {
	        LOCATION = URLDecoder.decode(UhfReader.class.getProtectionDomain()
	            .getCodeSource().getLocation().getFile(), "UTF-8");
	        if(LOCATION.length()<=4)
	        	return LOCATION;
	        if(LOCATION.substring(0,1).compareTo("/")==0){
	        	LOCATION=LOCATION.substring(1);
	        }else if(LOCATION.length()>8){
	        	if(LOCATION.substring(0, 6).compareToIgnoreCase("file:/")==0){
	        		LOCATION=LOCATION.substring(6);
	        	}
	        }
	        
	        LOCATION=LOCATION.replace("!/main/main.jar", "");
	        if(LOCATION.length()>8){
	        	if(LOCATION.substring(LOCATION.length()-4).compareToIgnoreCase(".jar")==0){
	        		int lastIndex = LOCATION.lastIndexOf('/');
	        		if(lastIndex>0){
	        			LOCATION=LOCATION.substring(0, lastIndex+1);
	        		}
	        	}
	        }
	    } catch (java.io.UnsupportedEncodingException e) {
	        LOCATION = "";
	    }
		return LOCATION;
	}
		
	//��data������ֽ����ݣ�ת����ʮ�������ַ�������ʼת����λ��ƫ��Ϊoffset��ת������Ϊlen�������ַ�����ÿ��ʮ������֮��ʹ�ÿո�ָ�
	public static String getHexStringFrombyte(byte[] data, int len, int offset)
	{
		return getHexStringFrombyte(data, len, offset, " ");
	}
	
	//ͬ�ϣ�offsetĬ��Ϊ0
	public static String getHexStringFrombyte(byte[] data, int len)
	{
		return getHexStringFrombyte(data, len, 0, " ");
	}
	
	//ͬ�ϣ����Զ���ָ�����
	public static String getHexStringFrombyte(byte[] data, int len, int offset, String seprator)
	{
		StringBuilder stringBuilder = new StringBuilder("");  
		if (data == null || data.length <= 0) {  
			return null;  
		}
		if(len>data.length-offset)
			len=data.length-offset;
		
        for (int i = offset; i < len+offset; i++) {
        	if(stringBuilder.length()>0)
        		stringBuilder.append(seprator);
            int v = data[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
	}
	
	//change a byte array into a string�����ֽ�����ת���ɶ�Ӧ���ַ�������
	public static String getStringFrombyte(byte[] data)
	{
		int i = 0;
		for ( i = 0; i < data.length; i++) {
			if(data[i] == 0)
				break;
		}
		int len = i;
		byte[]str=new byte[len];
		for (int j = 0; j < len; j++) {
			str[j]=data[j];
		}
		try {
			return new String(str,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	//���ַ���ת���ɶ�Ӧ��ascii�ֽ�����
	public static byte[] getBytesFromString(String data)
	{
		try {
			byte[]curStr = data.getBytes("GBK");
			return curStr;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] getBytesFromHexString(String data)
	{
		data=data.replace(" ", "");//ɾ���ո�ָ�����
		int len = data.length()/2;
		if(len<=0)
			return null;
		byte[]buf = new byte[len];
		for (int i = 0; i < len*2; i+=2) {
			String hexStr = data.substring(i, i+2);
			buf[i/2]=(byte)(0xff & Integer.parseInt(hexStr,16));
		}
		return buf;
	}
	
	//����У���־
	public void SetCrcFlag(int flag)
	{
		this.flagCrc=flag;
	}
	
	//����ture��ʾ�ɹ�
	public boolean Connect(String cPort/*ʾ����COM3���ַ���*/, int baudrateAndCrcFlag)
	{
		IntByReference handleVal = new IntByReference(0);
		int error_code = ns.UhfReaderConnect(handleVal, cPort, (byte) baudrateAndCrcFlag);
		this.handle=handleVal.getValue();
		return error_code!=0;
	}
	
	public boolean Disconnect()
	{
		IntByReference handleVal = new IntByReference(handle);
		int error_code = ns.UhfReaderDisconnect(handleVal, (byte) flagCrc);
		handle=handleVal.getValue();
		return error_code!=0;
	}
	

	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� Get Version
	 *
	 *********************************************************************************************************/
	//���صİ汾�ţ��ַ���
	public String GetVersion ()
	{
		byte[]uVersion=new byte[100];
		int result = ns.UhfGetVersion(handle, uVersion, (byte)flagCrc);
		if(result!=0)
		{
			return getStringFrombyte(uVersion);
		}
		return "";
	}

	/**********************************************************************************************************
	 *
	 *                                         Get ����ģ�� power setting
	 *
	 *********************************************************************************************************/
	//���ع���
	public int GetPower ()
	{
		String powerString;
		byte[]uPower=new byte[100];
		int result = ns.UhfGetPower(handle, uPower, (byte)flagCrc);
		if(result != 0)
			powerString=getStringFrombyte(uPower);
		else
			powerString="0";
		return Integer.parseInt(powerString)/100;
	}

	/**********************************************************************************************************
	 *
	 *                                         Set ����ģ�� power
	 *
	 *********************************************************************************************************/
	public boolean SetPower (int uPower)
	{
		return ns.UhfSetPower(handle, (char)0, uPower, (byte)flagCrc)!=0;
	}

	
	/*
	 * ���Ϊ���¹̶�����ֵ(|��ǰ���01��04��)
	 * 01|�й���׼(920-925M)
	 * 04|�й���׼(840-845M)
	 * 03|ŷ�ޱ�׼(865-868M)
	 * 02|������׼(902-928M)
	 * 06|������׼(917-923M)
	 */
	public boolean SetFrequency(int uChannNum/*Ψһ���õĲ���*/)
	{
		return ns.UhfSetFrequency(handle, (byte)0, (byte)0, null, (byte)uChannNum, (byte)0, (byte)0, (byte)flagCrc)!=0;
	}
	
	public int GetReaderUID(byte[]uUid)
	{
		return ns.UhfGetReaderUID(handle, uUid, (byte)flagCrc);
	}
	
	public int StartInventory ()
	{
		return ns.UhfStartInventory(handle, (byte)0, (byte)0, (byte)flagCrc);
	}
	
	//��ָ��ֻ������Ӳ�����������ݣ�Ȼ���������Ҫ�Լ������̣߳�ʹ��UhfRecvDataѭ����ȡ��ǩ���ݡ������Ӧ��ֹͣ��ȡ
	public int ReadInventory ()
	{
		ByteByReference uLenUii=new ByteByReference((byte)0);
		int error_code = ns.UhfReadInventory(handle, uLenUii, null);
		return error_code;
	}
	
	//UhfRecvData�����û�ֱ��ѭ����ȡ����
	public boolean RecvData (StringBuffer PC, StringBuffer EPC, StringBuffer RSSI/*10���Ƶ��ַ���*/)
	{
		byte []inventorySingleBuf=new byte[0xFF];
		int inventorySingleLen=RecvData(inventorySingleBuf);
		if(inventorySingleLen>0)
		{
			//showMessageBox("�յ��ı�ǩ����:"+UhfReader.getHexStringFrombyte(inventorySingleBuf,inventorySingleLen));
			//���ص����ݺ��壺PC(2�ֽ�)��EPC(len-5�ֽ�)��CRC(2�ֽ�)��RSSI(1�ֽ�)
			if(PC!=null)
				PC.append(UhfReader.getHexStringFrombyte(inventorySingleBuf,2));
			if(EPC!=null)
				EPC.append(UhfReader.getHexStringFrombyte(inventorySingleBuf,inventorySingleLen-5, 2));
			if(RSSI!=null)
			{
				String hexStr = UhfReader.getHexStringFrombyte(inventorySingleBuf,1,inventorySingleLen-1);
				int val = Integer.parseInt(hexStr,16);
				if(val>127){
					val = (val&0xFF)-0xFF;
				}
				RSSI.append(val);
			}
			//String CRC = UhfReader.getHexStringFrombyte(inventorySingleBuf,2,inventorySingleLen-3);
			return true;
		}
		return false;
	}
	
	//UhfRecvData�����û�ֱ��ѭ����ȡ����
	public int/*���ص����ݳ���*/ RecvData(byte[] uUii/*���ص����ݻ���*/)
	{
		int dataLen = ns.UhfRecvData(handle, uUii);
		return dataLen;
	}
	
	public int StopOperation ()
	{
		return ns.UhfStopOperation(handle, (byte)flagCrc);
	}
	
	public int ReadDataByEPC (String accessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/, 
			int memBank/*����ļ���ͷUHF_DATA_SECTION_XX�Ķ���*/, int sa, int dl,byte[] uDataReturn/*���ص�����*/)
	{
		return ns.UhfReadDataByEPC(handle, accessPwd, memBank, sa, dl, uDataReturn, (byte)flagCrc);
	}
	
	//ѡ��ģʽ����
	public boolean SETselectXZ (int mode/*ȡֵΪ00��01��02*/)
	{
		return ns.UhfSETselectXZ(handle, mode, (byte)flagCrc)!=0;
	}
	
	//�����ڶ�ȡTID��������ݣ����ô˺���ǰ����Ҫ��ʹ��UhfSETselectXZ(hCom,1,0);������һ��ѡ���ǩ��ģʽ���˺����᷵��uDataReturn��Ч���ݳ���
	public int ReadDataByTID (int sa, int dl,byte[] uDataReturn/*Ӳ�����ص�ԭʼ����*/,ByteByReference uErrorCode/*���صĴ������*/)
	{
		return ns.UhfReadDataByTID(handle, sa, dl, uDataReturn, uErrorCode, (byte)flagCrc);
	}
	
	//������UhfReadDataByEPC��Ψһ�����Ƕ�ȡǰ�����Զ�ѡ��ָ���ı�ǩ
	public int ReadDataByXZEPC(String epcLabelStr/*��Ҫ�Զ�ѡ�еı�ǩ*/,String accessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/, 
			int memBank/*����ļ���ͷUHF_DATA_SECTION_XX�Ķ���*/, int sa, int dl,byte[] uDataReturn/*���ص�����*/)
	{
		return ns.UhfReadDataByXZEPC(handle, epcLabelStr, accessPwd, memBank, sa, dl, uDataReturn, (byte)flagCrc);
	}
	
	//����true��ʾ��ȡ�ɹ�������������3����ֵ����Ϊ��PC��EPC��RSSI
	public boolean InventorySingleTag (StringBuffer PC, StringBuffer EPC, StringBuffer RSSI/*10���Ƶ��ַ���*/)
	{
		byte []inventorySingleBuf=new byte[0xFF];
		int inventorySingleLen=InventorySingleTag(inventorySingleBuf);
		if(inventorySingleLen>0)
		{
			//showMessageBox("�յ��ı�ǩ����:"+UhfReader.getHexStringFrombyte(inventorySingleBuf,inventorySingleLen));
			//���ص����ݺ��壺PC(2�ֽ�)��EPC(len-5�ֽ�)��CRC(2�ֽ�)��RSSI(1�ֽ�)
			if(PC!=null)
				PC.append(UhfReader.getHexStringFrombyte(inventorySingleBuf,2));
			if(EPC!=null)
				EPC.append(UhfReader.getHexStringFrombyte(inventorySingleBuf,inventorySingleLen-5, 2));
			if(RSSI!=null)
			{
				String hexStr = UhfReader.getHexStringFrombyte(inventorySingleBuf,1,inventorySingleLen-1);
				int val = Integer.parseInt(hexStr,16);
				if(val>127){
					val = (val&0xFF)-0xFF;
				}
				RSSI.append(val);
			}
			//String CRC = UhfReader.getHexStringFrombyte(inventorySingleBuf,2,inventorySingleLen-3);
			return true;
		}
		return false;
	}

	//����ʵ�ʶ�ȡ�����ݳ��ȣ���ȡʧ�ܷ���-1������ֵ����0��ʾ�����ݷ���
	public int InventorySingleTag (byte[] uUii)
	{
		ByteByReference bLenUii=new ByteByReference((byte)0);
		boolean result = ns.UhfInventorySingleTag(handle, bLenUii, uUii, (byte)flagCrc)!=0;
		if(!result)
			return -1;
		return bLenUii.getValue();
	}
	
	/**********************************************************************************************************
	 * 
	 * ����ģ�� Add select,����һ��select
	 * 
	 *********************************************************************************************************/
	// �ϵ�ԭ�ͣ��Ѿ�����int UhfAddFilter (SRECORD* pSRecord, UCHAR* STATUS,
	// byte flagCrc);
	public boolean AddFilter(int intSelTarget, int intAction,
			int intSelMemBank, int intSelPointer, int intMaskLen,
			int intTruncate, String txtSelMask) {
		return ns.UhfAddFilter(handle, intSelTarget, intAction, intSelMemBank, intSelPointer, intMaskLen, intTruncate, txtSelMask, (byte)flagCrc)!=0;
	}

	/**********************************************************************************************************
	 * 
	 * ����ģ�� write data
	 * 
	 *********************************************************************************************************/
	// �˺���д֮ǰ����Ҫ���ֹ�ѡ���ǩ�����سɹ�д����ֽ���
	public int WriteDataByEPC(String uAccessPwd/* 000000,4�ֽ�Ĭ�����룬�����ַ��� */,
			int uBank/* ����ļ���ͷUHF_DATA_SECTION_XX�Ķ��� */, String uPtr/* д�ڴ����ʼ��ַ0000*/,
			int uCnt/* д�ĸ���00 03�����255���ֽ� */, byte[] uWriteData/* д������� */,
			ByteByReference uErrorCode/* ���صĴ������ */) {
		int lenWrite = ns.UhfWriteDataByEPC(handle, uAccessPwd, (byte)uBank, uPtr, (byte)uCnt, uWriteData, uErrorCode, (byte)flagCrc);
		return lenWrite;
	}

	// ���������UhfWriteDataByEPC������������д����ǰ���ڲ����Զ���ѡ���豸������ǩ����
	public int WriteDataByEPCEx(String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/,
			int uBank/* ����ļ���ͷUHF_DATA_SECTION_XX�Ķ��� */, String uPtr/* д�ڴ����ʼ��ַ00 00*/,
			int uCnt/* д�ĸ���00 03 */, byte[] uWriteData/* д������� */,
			ByteByReference uErrorCode/* ���صĴ������ */) {
		return ns.UhfWriteDataByEPCEx(handle, uAccessPwd, (byte)uBank, uPtr, (byte)uCnt, uWriteData, uErrorCode, (byte)flagCrc);
	}

	// ���������UhfWriteDataByEPC������������д����ǰ���ڲ����Զ���ѡ���û�ָ����ǩ����
	public int WriteDataByXZEPC(String EPCXZ, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/,
			int uBank/* ����ļ���ͷUHF_DATA_SECTION_XX�Ķ��� */, String uPtr/*д�ڴ����ʼ��ַ00 00*/,
			int uCnt/* д�ĸ���00 03 */, byte[] uWriteData/* д������� */,
			ByteByReference uErrorCode/* ���صĴ������ */) {
		return ns.UhfWriteDataByXZEPC(handle, EPCXZ, uAccessPwd, (byte)uBank, uPtr, (byte)uCnt, uWriteData, uErrorCode, (byte)flagCrc);
	}

	// ���������UhfWriteDataByEPC������������д����ǰ���ڲ����Զ���ѡ���ǩ������ͬʱ�̶�д��USER��
	public int WriteDataByUSER(
			String uAccessPwd/* 000000,4�ֽ�Ĭ�����룬�����ַ��� */, String uPtr/*д�ڴ����ʼ��ַ00 00*/,
			int uCnt/* д�ĸ���00 03 */, byte[] uWriteData/* д������� */,
			ByteByReference uErrorCode/* ���صĴ������ */) {
		return ns.UhfWriteDataByUSER(handle, uAccessPwd, uPtr, (byte)uCnt, uWriteData, uErrorCode, (byte)flagCrc);
	}

	//����ʵ�ʲ������ֽ���
	public int EraseDataByEPC(
			String uAccessPwd/* 000000,4�ֽ�Ĭ�����룬�����ַ��� */,
			int uBank/* ����ļ���ͷUHF_DATA_SECTION_XX�Ķ��� */,
			ByteByReference uErrorCode/* ���صĴ������ */) {
		return ns.UhfEraseDataByEPC(handle, uAccessPwd, (byte)uBank, uErrorCode, (byte)flagCrc);
	}

	// ʹ��ǰ��Ҫ��ѡ�б�ǩ
	public boolean ChangeConfig(
			String uAccessPwd/* 000000,4�ֽ�Ĭ�����룬�����ַ��� */,
			int Config/* ֻʹ����˫�ֽ����� */, ByteByReference uErrorCode/* ���صĴ����� */) {
		return ns.UhfChangeConfig(handle, uAccessPwd, Config, uErrorCode, (byte)flagCrc)!=0;
	}

	// 4.25.NXP ReadProtect/Reset ReadProtect ָ��
	public boolean SetReadProtect(
			String uAccessPwd/* 000000,4�ֽ�Ĭ�����룬�����ַ��� */, int Config/*1��ʾ����ReadProtect��0��ʾȡ��ReadProtect*/,
			ByteByReference uErrorCode/* ���صĴ����� */) {
		return ns.UhfSetReadProtect(handle, uAccessPwd, Config, uErrorCode, (byte)flagCrc)!=0;
	}

	// ChangeEAS
	public boolean ChangeEAS(String uAccessPwd/* 000000,4�ֽ�Ĭ�����룬�����ַ��� */,
			int ConfigPSF/* 0x01 �������� PSF λΪ1��0x00 �������� PSF λΪ0 */,
			ByteByReference uErrorCode/* ���صĴ����� */) {
		return ns.UhfChangeEAS(handle, uAccessPwd, ConfigPSF, uErrorCode, (byte)flagCrc)!=0;
	}

	// EAS_Alarm
	public boolean EASAlarm() {
		return ns.UhfEASAlarm(handle, (byte)flagCrc)!=0;
	}

	public boolean ChangeQTControl(
			String uAccessPwd/* 000000,4�ֽ�Ĭ�����룬�����ַ��� */, int ReadWrite/*0x00: Read��0x01: Write*/,
			int Persistence/* 0x00: д���ǩ�ӷ��Դ洢����0x01: д��ǻӷ��Դ洢�� */,
			String Payload/* ˫�ֽڵ��ַ��� */, ByteByReference uErrorCode/* ���صĴ����� */)
	{
		return ns.UhfChangeQTControl(handle, uAccessPwd, ReadWrite, Persistence, Payload, uErrorCode, (byte)flagCrc)!=0;
	}

	/**********************************************************************************************************
	 * 
	 * ����ģ�� lock memory
	 * 
	 *********************************************************************************************************/
	// ����
	public boolean LockMemByEPC(String EPCXZ, String uAccessPwd,
			String uLockData, ByteByReference uErrorCode) {
		return ns.UhfLockMemByEPC(handle, EPCXZ, uAccessPwd, uLockData, uErrorCode, (byte)flagCrc)!=0;
	}

	// ����
	public boolean UnLockMemByEPC(String EPCXZ, String uAccessPwd,
			String uLockData, ByteByReference uErrorCode) {
		return ns.UhfunLockMemByEPC(handle, EPCXZ, uAccessPwd, uLockData, uErrorCode, (byte)flagCrc)!=0;
	}

	/**********************************************************************************************************
	 * 
	 * ����ģ�� kill tag
	 * 
	 *********************************************************************************************************/
	public boolean KillTagByEPC(String uKillPwd,ByteByReference uErrorCode) {
		return ns.UhfKillTagByEPC(handle, uKillPwd, uErrorCode, (byte)flagCrc)!=0;
	}

	public boolean SetMode(int mode/* 0����ʶ��1����ײȺ��ʶ�� */) {
		return ns.UhfSetMode(handle, (byte)mode, (byte)flagCrc)!=0;
	}// ���ö���ģʽ

	public boolean SaveConfig() {
		return ns.UhfSaveConfig(handle, (byte)flagCrc)!=0;
	}// ��������

	public boolean Sleep() {
		return ns.UhfSleep(handle, (byte)flagCrc)!=0;
	}// ����

	public boolean AutoFrequeC(boolean mode/* 0ȡ���Զ���Ƶ��1�Զ���Ƶ */) {
		return ns.UhfAutoFrequeC(handle, mode?1:0, (byte)flagCrc)!=0;
	}// ���÷��������ز�

	public boolean SelectQ(int mode/* ȡֵΪ1��8 */) {
		return ns.UhfSelectQ(handle, mode, (byte)flagCrc)!=0;
	}// ���÷��������ز�

	public int ControIO(int ioCtrlType/* ����Ϸ�UHF_IO_CONTROL_�Ķ��� */,
			int param1/* ȡֵ1~4 */, int param2) {
		return ns.UhfControIO(handle, ioCtrlType, param1, param2, (byte)flagCrc);
	}// �����������
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//JR20X0 End
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isConnect()
	{
		return handle!=0;
	}
}
