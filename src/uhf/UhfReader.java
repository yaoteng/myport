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
	
	//ioCtrlType类型
	static public final int	UHF_IO_CONTROL_IO_DIR	=	0;
	static public final int	UHF_IO_CONTROL_IO_SET_EL=	1;
	static public final int	UHF_IO_CONTROL_IO_GET_EL=	2;
	
	//取值0，2，4，6，分别对应不同波特率9600/19200/57600/115200
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
		//如果打包到fatjar里面，路径形式为：file:/D:/MyDocuments/MyEclipse/UhfReader/UhfReader_fat.jar!/main/main.jar
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
		
	//将data里面的字节数据，转换成十六进制字符串，开始转换的位置偏移为offset，转换长度为len，返回字符串，每个十六进制之间使用空格分隔
	public static String getHexStringFrombyte(byte[] data, int len, int offset)
	{
		return getHexStringFrombyte(data, len, offset, " ");
	}
	
	//同上，offset默认为0
	public static String getHexStringFrombyte(byte[] data, int len)
	{
		return getHexStringFrombyte(data, len, 0, " ");
	}
	
	//同上，可自定义分隔符号
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
	
	//change a byte array into a string，将字节数组转换成对应的字符串数据
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
	
	//将字符串转换成对应的ascii字节数组
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
		data=data.replace(" ", "");//删除空格分隔符号
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
	
	//设置校验标志
	public void SetCrcFlag(int flag)
	{
		this.flagCrc=flag;
	}
	
	//返回ture表示成功
	public boolean Connect(String cPort/*示例：COM3，字符串*/, int baudrateAndCrcFlag)
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
	 *                                         玖锐模块 Get Version
	 *
	 *********************************************************************************************************/
	//返回的版本号，字符串
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
	 *                                         Get 玖锐模块 power setting
	 *
	 *********************************************************************************************************/
	//返回功率
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
	 *                                         Set 玖锐模块 power
	 *
	 *********************************************************************************************************/
	public boolean SetPower (int uPower)
	{
		return ns.UhfSetPower(handle, (char)0, uPower, (byte)flagCrc)!=0;
	}

	
	/*
	 * 入参为如下固定的数值(|号前面的01、04等)
	 * 01|中国标准(920-925M)
	 * 04|中国标准(840-845M)
	 * 03|欧洲标准(865-868M)
	 * 02|美国标准(902-928M)
	 * 06|韩国标准(917-923M)
	 */
	public boolean SetFrequency(int uChannNum/*唯一有用的参数*/)
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
	
	//此指令只是启动硬件连续读数据，然后调用者需要自己启动线程，使用UhfRecvData循环读取标签数据。读完后，应该停止读取
	public int ReadInventory ()
	{
		ByteByReference uLenUii=new ByteByReference((byte)0);
		int error_code = ns.UhfReadInventory(handle, uLenUii, null);
		return error_code;
	}
	
	//UhfRecvData用于用户直接循环读取数据
	public boolean RecvData (StringBuffer PC, StringBuffer EPC, StringBuffer RSSI/*10进制的字符串*/)
	{
		byte []inventorySingleBuf=new byte[0xFF];
		int inventorySingleLen=RecvData(inventorySingleBuf);
		if(inventorySingleLen>0)
		{
			//showMessageBox("收到的标签数据:"+UhfReader.getHexStringFrombyte(inventorySingleBuf,inventorySingleLen));
			//返回的数据含义：PC(2字节)、EPC(len-5字节)、CRC(2字节)、RSSI(1字节)
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
	
	//UhfRecvData用于用户直接循环读取数据
	public int/*返回的数据长度*/ RecvData(byte[] uUii/*返回的数据缓冲*/)
	{
		int dataLen = ns.UhfRecvData(handle, uUii);
		return dataLen;
	}
	
	public int StopOperation ()
	{
		return ns.UhfStopOperation(handle, (byte)flagCrc);
	}
	
	public int ReadDataByEPC (String accessPwd/*000000,4字节默认密码，送入字符串*/, 
			int memBank/*详见文件开头UHF_DATA_SECTION_XX的定义*/, int sa, int dl,byte[] uDataReturn/*返回的数据*/)
	{
		return ns.UhfReadDataByEPC(handle, accessPwd, memBank, sa, dl, uDataReturn, (byte)flagCrc);
	}
	
	//选择模式设置
	public boolean SETselectXZ (int mode/*取值为00，01，02*/)
	{
		return ns.UhfSETselectXZ(handle, mode, (byte)flagCrc)!=0;
	}
	
	//仅用于读取TID区域的数据，调用此函数前，需要先使用UhfSETselectXZ(hCom,1,0);来设置一下选择标签的模式，此函数会返回uDataReturn有效数据长度
	public int ReadDataByTID (int sa, int dl,byte[] uDataReturn/*硬件返回的原始数据*/,ByteByReference uErrorCode/*返回的错误代码*/)
	{
		return ns.UhfReadDataByTID(handle, sa, dl, uDataReturn, uErrorCode, (byte)flagCrc);
	}
	
	//功能与UhfReadDataByEPC，唯一区别是读取前，会自动选择指定的标签
	public int ReadDataByXZEPC(String epcLabelStr/*需要自动选中的标签*/,String accessPwd/*000000,4字节默认密码，送入字符串*/, 
			int memBank/*详见文件开头UHF_DATA_SECTION_XX的定义*/, int sa, int dl,byte[] uDataReturn/*返回的数据*/)
	{
		return ns.UhfReadDataByXZEPC(handle, epcLabelStr, accessPwd, memBank, sa, dl, uDataReturn, (byte)flagCrc);
	}
	
	//返回true表示读取成功，则数组里面3个数值依次为：PC、EPC、RSSI
	public boolean InventorySingleTag (StringBuffer PC, StringBuffer EPC, StringBuffer RSSI/*10进制的字符串*/)
	{
		byte []inventorySingleBuf=new byte[0xFF];
		int inventorySingleLen=InventorySingleTag(inventorySingleBuf);
		if(inventorySingleLen>0)
		{
			//showMessageBox("收到的标签数据:"+UhfReader.getHexStringFrombyte(inventorySingleBuf,inventorySingleLen));
			//返回的数据含义：PC(2字节)、EPC(len-5字节)、CRC(2字节)、RSSI(1字节)
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

	//返回实际读取的数据长度，读取失败返回-1，返回值大于0表示有数据返回
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
	 * 玖锐模块 Add select,设置一个select
	 * 
	 *********************************************************************************************************/
	// 老的原型，已经不用int UhfAddFilter (SRECORD* pSRecord, UCHAR* STATUS,
	// byte flagCrc);
	public boolean AddFilter(int intSelTarget, int intAction,
			int intSelMemBank, int intSelPointer, int intMaskLen,
			int intTruncate, String txtSelMask) {
		return ns.UhfAddFilter(handle, intSelTarget, intAction, intSelMemBank, intSelPointer, intMaskLen, intTruncate, txtSelMask, (byte)flagCrc)!=0;
	}

	/**********************************************************************************************************
	 * 
	 * 玖锐模块 write data
	 * 
	 *********************************************************************************************************/
	// 此函数写之前，需要先手工选择标签，返回成功写入的字节数
	public int WriteDataByEPC(String uAccessPwd/* 000000,4字节默认密码，送入字符串 */,
			int uBank/* 详见文件开头UHF_DATA_SECTION_XX的定义 */, String uPtr/* 写内存的起始地址0000*/,
			int uCnt/* 写的个数00 03，最多255个字节 */, byte[] uWriteData/* 写入的数据 */,
			ByteByReference uErrorCode/* 返回的错误代码 */) {
		int lenWrite = ns.UhfWriteDataByEPC(handle, uAccessPwd, (byte)uBank, uPtr, (byte)uCnt, uWriteData, uErrorCode, (byte)flagCrc);
		return lenWrite;
	}

	// 这个函数与UhfWriteDataByEPC函数的区别是写数据前，内部会自动做选择设备附近标签操作
	public int WriteDataByEPCEx(String uAccessPwd/*000000,4字节默认密码，送入字符串*/,
			int uBank/* 详见文件开头UHF_DATA_SECTION_XX的定义 */, String uPtr/* 写内存的起始地址00 00*/,
			int uCnt/* 写的个数00 03 */, byte[] uWriteData/* 写入的数据 */,
			ByteByReference uErrorCode/* 返回的错误代码 */) {
		return ns.UhfWriteDataByEPCEx(handle, uAccessPwd, (byte)uBank, uPtr, (byte)uCnt, uWriteData, uErrorCode, (byte)flagCrc);
	}

	// 这个函数与UhfWriteDataByEPC函数的区别是写数据前，内部会自动做选择用户指定标签操作
	public int WriteDataByXZEPC(String EPCXZ, String uAccessPwd/*000000,4字节默认密码，送入字符串*/,
			int uBank/* 详见文件开头UHF_DATA_SECTION_XX的定义 */, String uPtr/*写内存的起始地址00 00*/,
			int uCnt/* 写的个数00 03 */, byte[] uWriteData/* 写入的数据 */,
			ByteByReference uErrorCode/* 返回的错误代码 */) {
		return ns.UhfWriteDataByXZEPC(handle, EPCXZ, uAccessPwd, (byte)uBank, uPtr, (byte)uCnt, uWriteData, uErrorCode, (byte)flagCrc);
	}

	// 这个函数与UhfWriteDataByEPC函数的区别是写数据前，内部会自动做选择标签操作，同时固定写入USER区
	public int WriteDataByUSER(
			String uAccessPwd/* 000000,4字节默认密码，送入字符串 */, String uPtr/*写内存的起始地址00 00*/,
			int uCnt/* 写的个数00 03 */, byte[] uWriteData/* 写入的数据 */,
			ByteByReference uErrorCode/* 返回的错误代码 */) {
		return ns.UhfWriteDataByUSER(handle, uAccessPwd, uPtr, (byte)uCnt, uWriteData, uErrorCode, (byte)flagCrc);
	}

	//返回实际擦除的字节数
	public int EraseDataByEPC(
			String uAccessPwd/* 000000,4字节默认密码，送入字符串 */,
			int uBank/* 详见文件开头UHF_DATA_SECTION_XX的定义 */,
			ByteByReference uErrorCode/* 返回的错误代码 */) {
		return ns.UhfEraseDataByEPC(handle, uAccessPwd, (byte)uBank, uErrorCode, (byte)flagCrc);
	}

	// 使用前需要先选中标签
	public boolean ChangeConfig(
			String uAccessPwd/* 000000,4字节默认密码，送入字符串 */,
			int Config/* 只使用了双字节数据 */, ByteByReference uErrorCode/* 返回的错误码 */) {
		return ns.UhfChangeConfig(handle, uAccessPwd, Config, uErrorCode, (byte)flagCrc)!=0;
	}

	// 4.25.NXP ReadProtect/Reset ReadProtect 指令
	public boolean SetReadProtect(
			String uAccessPwd/* 000000,4字节默认密码，送入字符串 */, int Config/*1表示启用ReadProtect，0表示取消ReadProtect*/,
			ByteByReference uErrorCode/* 返回的错误码 */) {
		return ns.UhfSetReadProtect(handle, uAccessPwd, Config, uErrorCode, (byte)flagCrc)!=0;
	}

	// ChangeEAS
	public boolean ChangeEAS(String uAccessPwd/* 000000,4字节默认密码，送入字符串 */,
			int ConfigPSF/* 0x01 代表设置 PSF 位为1，0x00 代表设置 PSF 位为0 */,
			ByteByReference uErrorCode/* 返回的错误码 */) {
		return ns.UhfChangeEAS(handle, uAccessPwd, ConfigPSF, uErrorCode, (byte)flagCrc)!=0;
	}

	// EAS_Alarm
	public boolean EASAlarm() {
		return ns.UhfEASAlarm(handle, (byte)flagCrc)!=0;
	}

	public boolean ChangeQTControl(
			String uAccessPwd/* 000000,4字节默认密码，送入字符串 */, int ReadWrite/*0x00: Read，0x01: Write*/,
			int Persistence/* 0x00: 写入标签挥发性存储区，0x01: 写入非挥发性存储区 */,
			String Payload/* 双字节的字符串 */, ByteByReference uErrorCode/* 返回的错误码 */)
	{
		return ns.UhfChangeQTControl(handle, uAccessPwd, ReadWrite, Persistence, Payload, uErrorCode, (byte)flagCrc)!=0;
	}

	/**********************************************************************************************************
	 * 
	 * 玖锐模块 lock memory
	 * 
	 *********************************************************************************************************/
	// 锁定
	public boolean LockMemByEPC(String EPCXZ, String uAccessPwd,
			String uLockData, ByteByReference uErrorCode) {
		return ns.UhfLockMemByEPC(handle, EPCXZ, uAccessPwd, uLockData, uErrorCode, (byte)flagCrc)!=0;
	}

	// 解锁
	public boolean UnLockMemByEPC(String EPCXZ, String uAccessPwd,
			String uLockData, ByteByReference uErrorCode) {
		return ns.UhfunLockMemByEPC(handle, EPCXZ, uAccessPwd, uLockData, uErrorCode, (byte)flagCrc)!=0;
	}

	/**********************************************************************************************************
	 * 
	 * 玖锐模块 kill tag
	 * 
	 *********************************************************************************************************/
	public boolean KillTagByEPC(String uKillPwd,ByteByReference uErrorCode) {
		return ns.UhfKillTagByEPC(handle, uKillPwd, uErrorCode, (byte)flagCrc)!=0;
	}

	public boolean SetMode(int mode/* 0高速识别，1防碰撞群读识别 */) {
		return ns.UhfSetMode(handle, (byte)mode, (byte)flagCrc)!=0;
	}// 设置读卡模式

	public boolean SaveConfig() {
		return ns.UhfSaveConfig(handle, (byte)flagCrc)!=0;
	}// 保存配置

	public boolean Sleep() {
		return ns.UhfSleep(handle, (byte)flagCrc)!=0;
	}// 休眠

	public boolean AutoFrequeC(boolean mode/* 0取消自动调频，1自动调频 */) {
		return ns.UhfAutoFrequeC(handle, mode?1:0, (byte)flagCrc)!=0;
	}// 设置发射连续载波

	public boolean SelectQ(int mode/* 取值为1到8 */) {
		return ns.UhfSelectQ(handle, mode, (byte)flagCrc)!=0;
	}// 设置发射连续载波

	public int ControIO(int ioCtrlType/* 详见上方UHF_IO_CONTROL_的定义 */,
			int param1/* 取值1~4 */, int param2) {
		return ns.UhfControIO(handle, ioCtrlType, param1, param2, (byte)flagCrc);
	}// 控制输入输出
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
