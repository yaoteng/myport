package uhf;

import com.sun.jna.ptr.*;
import com.sun.jna.win32.StdCallLibrary;

public interface UhfReaderAPI extends StdCallLibrary {
	static final int	UHF_DATA_SECTION_RESERVED	=	0;
	static final int	UHF_DATA_SECTION_EPC 		=	1;
	static final int	UHF_DATA_SECTION_TID		=	2;
	static final int	UHF_DATA_SECTION_USER		=	3;
	
	//ioCtrlType类型
	static final int	UHF_IO_CONTROL_IO_DIR	=	0;
	static final int	UHF_IO_CONTROL_IO_SET_EL=	1;
	static final int	UHF_IO_CONTROL_IO_GET_EL=	2;
	
	//取值0，2，4，6，分别对应不同波特率9600/19200/57600/115200
	static final int	UHF_BAUDRATE_9600	=	0;
	static final int	UHF_BAUDRATE_19200	=	2;
	static final int	UHF_BAUDRATE_57600	=	4;
	static final int	UHF_BAUDRATE_115200	=	6;
	
	/**********************************************************************************************************
	 *
	 *                                          UhfReader_API header file
	 *
	 **********************************************************************************************************
	 * FileName:        UhfReader_API.h
	 *
	 * Dependencies:    NONE
	 *
	 * Company:         Jiuray Technologies
	 *
	 * Author           Date              Version           Comment
	 *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * Fzjsf            2014-11-18        1.0               File Created
	 *********************************************************************************************************/


	/**********************************************************************************************************
	 *
	 *                                         Open port and connect
	 *flagCrc取值0，2，4，6，分别对应不同波特率9600/19200/57600/115200
	 *********************************************************************************************************/
	int UhfReaderConnect (IntByReference hCom, String cPort/*示例：COM3，字符串*/, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *										   Disconnect and close port
	 *flagCrc这个送0，后边的函数如无特殊说明，此参数均送0
	 *********************************************************************************************************/
	int UhfReaderDisconnect (IntByReference hCom, byte flagCrc);
	



	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 Get Version
	 *
	 *********************************************************************************************************/
	int UhfGetVersion (int hCom, byte[] uVersion/*返回的版本号，字符串，调用前需要预分配空间*/, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Get 玖锐模块 power setting
	 *
	 *********************************************************************************************************/
	int UhfGetPower (int hCom, byte[] uPower/*返回功率，字符串，调用前需要预分配空间*/, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Set 玖锐模块 power
	 *
	 *********************************************************************************************************/
	int UhfSetPower (int hCom, char uOption, int uPower, byte flagCrc);


	/**********************************************************************************************************
	 *
	 *                                         Get 玖锐模块 frequency setting
	 *引函数暂时不用
	 *********************************************************************************************************/
	//int UhfGetFrequency(int hCom, byte[] uFreMode, byte[] uFreBase, byte[] uBaseFre, byte[] uChannNum, byte[] uChannSpc, byte[] uFreHop, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Set 玖锐模块 frequency
	 *
	 *********************************************************************************************************/
	int UhfSetFrequency(int hCom, byte uFreMode, byte uFreBase, byte[] uBaseFre, byte uChannNum/*唯一有用的参数*/, byte uChannSpc, byte uFreHop, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Read 玖锐模块 Uid
	 *
	 *********************************************************************************************************/
	int UhfGetReaderUID (int hCom, byte[] uUid, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 inventory
	 *
	 *********************************************************************************************************/
	int UhfStartInventory (int hCom, byte flagAnti, byte initQ, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Get received data
	 *
	 *********************************************************************************************************/
	//此指令只是启动硬件连续读数据，然后调用者需要自己启动线程，使用UhfRecvData循环读取标签数据。读完后，应该停止读取
	int UhfReadInventory (int hCom, ByteByReference uLenUii, byte[] uUii);

	//UhfRecvData用于用户直接循环读取数据
	int/*返回的数据长度*/ UhfRecvData(int hCom, byte[] uUii/*返回的数据缓冲*/);
	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 stop get
	 *
	 *********************************************************************************************************/
	int UhfStopOperation (int hCom, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 read data
	 *
	 *********************************************************************************************************/
	int UhfReadDataByEPC (int hCom, String accessPwd/*000000,4字节默认密码，送入字符串*/, 
								 int memBank/*详见文件开头UHF_DATA_SECTION_XX的定义*/, int sa, int dl,byte[] uDataReturn/*返回的数据*/, byte flagCrc);

	//选择模式设置
	int UhfSETselectXZ (int hCom, int mode/*取值为00，01，02*/, byte flagCrc);

	//仅用于读取TID区域的数据，调用此函数前，需要先使用UhfSETselectXZ(hCom,1,0);来设置一下选择标签的模式，此函数会返回uDataReturn有效数据长度
	int UhfReadDataByTID (int hCom, int sa, int dl,byte[] uDataReturn/*硬件返回的原始数据*/,ByteByReference uErrorCode/*返回的错误代码*/, byte flagCrc);

	//功能与UhfReadDataByEPC，唯一区别是读取前，会自动选择指定的标签
	int UhfReadDataByXZEPC(int hCom, String epcLabelStr/*需要自动选中的标签*/,String accessPwd/*000000,4字节默认密码，送入字符串*/, 
								 int memBank/*详见文件开头UHF_DATA_SECTION_XX的定义*/, int sa, int dl,byte[] uDataReturn/*返回的数据*/, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 single inventory
	 *
	 *********************************************************************************************************/
	int UhfInventorySingleTag (int hCom, ByteByReference uLenUii, byte[] uUii , byte flagCrc);


	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 Add select,设置一个select
	 *
	 *********************************************************************************************************/
	//老的原型，已经不用int UhfAddFilter (int hCom, SRECORD* pSRecord, UCHAR* STATUS, byte flagCrc);
	int UhfAddFilter (int hCom, int intSelTarget, int intAction, int intSelMemBank,
							 int intSelPointer, int intMaskLen, int intTruncate, String txtSelMask, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 Delete select
	 *未使用
	 *********************************************************************************************************/
	int UhfDeleteFilterByIndex (int hCom, byte SINDEX, ByteByReference STATUS, byte flagCrc);//未使用

	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 Get select
	 *未使用
	 *********************************************************************************************************/
	int UhfStartGetFilterByIndex (int hCom, byte SINDEX, byte SNUM, ByteByReference STATUS, byte flagCrc);//未使用


	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 write data
	 *
	 *********************************************************************************************************/
	//此函数写之前，需要先手工选择标签
	int UhfWriteDataByEPC(int hCom, String uAccessPwd/*000000,4字节默认密码，送入字符串*/, byte uBank/*详见文件开头UHF_DATA_SECTION_XX的定义*/, String uPtr/*写内存的起始地址00 00*/, 
				byte uCnt/*写的个数00 03，最多255个字节*/, byte[] uWriteData/*写入的数据*/, ByteByReference uErrorCode/*返回的错误代码*/, byte flagCrc);


	//这个函数与UhfWriteDataByEPC函数的区别是写数据前，内部会自动做选择设备附近标签操作
	int UhfWriteDataByEPCEx(int hCom, String uAccessPwd/*000000,4字节默认密码，送入字符串*/,byte uBank/*详见文件开头UHF_DATA_SECTION_XX的定义*/, String uPtr/*写内存的起始地址00 00*/, 
				byte uCnt/*写的个数00 03*/, byte[] uWriteData/*写入的数据*/, ByteByReference uErrorCode/*返回的错误代码*/, byte flagCrc);

	//这个函数与UhfWriteDataByEPC函数的区别是写数据前，内部会自动做选择用户指定标签操作
	int UhfWriteDataByXZEPC(int hCom, String EPCXZ, String uAccessPwd/*000000,4字节默认密码，送入字符串*/, byte uBank/*详见文件开头UHF_DATA_SECTION_XX的定义*/, String uPtr/*写内存的起始地址00 00*/, 
								   byte uCnt/*写的个数00 03*/, byte[] uWriteData/*写入的数据*/, ByteByReference uErrorCode/*返回的错误代码*/, byte flagCrc);

	//这个函数与UhfWriteDataByEPC函数的区别是写数据前，内部会自动做选择标签操作，同时固定写入USER区
	int UhfWriteDataByUSER(int hCom, String uAccessPwd/*000000,4字节默认密码，送入字符串*/, String uPtr/*写内存的起始地址00 00*/, 
								   byte uCnt/*写的个数00 03*/, byte[] uWriteData/*写入的数据*/, ByteByReference uErrorCode/*返回的错误代码*/, byte flagCrc);


	int UhfEraseDataByEPC(int hCom, String uAccessPwd/*000000,4字节默认密码，送入字符串*/,byte uBank/*详见文件开头UHF_DATA_SECTION_XX的定义*/,
				ByteByReference uErrorCode/*返回的错误代码*/, byte flagCrc);


	//使用前需要先选中标签
	int UhfChangeConfig(int hCom, String uAccessPwd/*000000,4字节默认密码，送入字符串*/,
							   int Config/*只使用了双字节数据*/,ByteByReference uErrorCode/*返回的错误码*/, byte flagCrc);

	//4.25.NXP ReadProtect/Reset ReadProtect 指令
	int UhfSetReadProtect(int hCom, String uAccessPwd/*000000,4字节默认密码，送入字符串*/,
							   int Config/*1表示启用ReadProtect，0表示取消ReadProtect*/,ByteByReference uErrorCode/*返回的错误码*/, byte flagCrc);

	//ChangeEAS
	int UhfChangeEAS(int hCom, String uAccessPwd/*000000,4字节默认密码，送入字符串*/,
								 int ConfigPSF/*0x01 代表设置 PSF 位为1，0x00 代表设置 PSF 位为0*/,ByteByReference uErrorCode/*返回的错误码*/, byte flagCrc);

	//EAS_Alarm
	int UhfEASAlarm(int hCom, byte flagCrc);

	int UhfChangeQTControl(int hCom, String uAccessPwd/*000000,4字节默认密码，送入字符串*/,
								  int ReadWrite/*0x00: Read，0x01: Write*/,int Persistence/*0x00: 写入标签挥发性存储区，0x01: 写入非挥发性存储区*/,
								  String Payload/*双字节的字符串*/, ByteByReference uErrorCode/*返回的错误码*/, byte flagCrc);
	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 lock memory
	 *
	 *********************************************************************************************************/
	//锁定
	int UhfLockMemByEPC (int hCom, String EPCXZ, String uAccessPwd, String uLockData, ByteByReference uErrorCode, byte flagCrc);

	//解锁
	int UhfunLockMemByEPC (int hCom, String EPCXZ, String uAccessPwd, String uLockData, ByteByReference uErrorCode, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         玖锐模块 kill tag
	 *
	 *********************************************************************************************************/
	int UhfKillTagByEPC (int hCom, String uKillPwd, ByteByReference uErrorCode, byte flagCrc);

	int UhfSetMode (int hCom, byte mode/*0高速识别，1防碰撞群读识别*/, byte flagCrc);//设置读卡模式
	int UhfSaveConfig (int hCom, byte flagCrc);//保存配置
	int UhfSleep (int hCom,  byte flagCrc);//休眠
	int UhfAutoFrequeC(int hCom, int mode/*0取消自动调频，1自动调频*/, byte flagCrc);//设置发射连续载波
	int UhfSelectQ(int hCom, int mode/*取值为1到8*/, byte flagCrc);//设置发射连续载波
	
	int UhfControIO(int hCom, int ioCtrlType/*详见上方UHF_IO_CONTROL_的定义*/, int param1/*取值1~4*/,int param2,byte flagCrc);//控制输入输出
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//JR20X0 End
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
