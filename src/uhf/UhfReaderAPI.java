package uhf;

import com.sun.jna.ptr.*;
import com.sun.jna.win32.StdCallLibrary;

public interface UhfReaderAPI extends StdCallLibrary {
	static final int	UHF_DATA_SECTION_RESERVED	=	0;
	static final int	UHF_DATA_SECTION_EPC 		=	1;
	static final int	UHF_DATA_SECTION_TID		=	2;
	static final int	UHF_DATA_SECTION_USER		=	3;
	
	//ioCtrlType����
	static final int	UHF_IO_CONTROL_IO_DIR	=	0;
	static final int	UHF_IO_CONTROL_IO_SET_EL=	1;
	static final int	UHF_IO_CONTROL_IO_GET_EL=	2;
	
	//ȡֵ0��2��4��6���ֱ��Ӧ��ͬ������9600/19200/57600/115200
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
	 *flagCrcȡֵ0��2��4��6���ֱ��Ӧ��ͬ������9600/19200/57600/115200
	 *********************************************************************************************************/
	int UhfReaderConnect (IntByReference hCom, String cPort/*ʾ����COM3���ַ���*/, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *										   Disconnect and close port
	 *flagCrc�����0����ߵĺ�����������˵�����˲�������0
	 *********************************************************************************************************/
	int UhfReaderDisconnect (IntByReference hCom, byte flagCrc);
	



	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� Get Version
	 *
	 *********************************************************************************************************/
	int UhfGetVersion (int hCom, byte[] uVersion/*���صİ汾�ţ��ַ���������ǰ��ҪԤ����ռ�*/, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Get ����ģ�� power setting
	 *
	 *********************************************************************************************************/
	int UhfGetPower (int hCom, byte[] uPower/*���ع��ʣ��ַ���������ǰ��ҪԤ����ռ�*/, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Set ����ģ�� power
	 *
	 *********************************************************************************************************/
	int UhfSetPower (int hCom, char uOption, int uPower, byte flagCrc);


	/**********************************************************************************************************
	 *
	 *                                         Get ����ģ�� frequency setting
	 *��������ʱ����
	 *********************************************************************************************************/
	//int UhfGetFrequency(int hCom, byte[] uFreMode, byte[] uFreBase, byte[] uBaseFre, byte[] uChannNum, byte[] uChannSpc, byte[] uFreHop, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Set ����ģ�� frequency
	 *
	 *********************************************************************************************************/
	int UhfSetFrequency(int hCom, byte uFreMode, byte uFreBase, byte[] uBaseFre, byte uChannNum/*Ψһ���õĲ���*/, byte uChannSpc, byte uFreHop, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Read ����ģ�� Uid
	 *
	 *********************************************************************************************************/
	int UhfGetReaderUID (int hCom, byte[] uUid, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� inventory
	 *
	 *********************************************************************************************************/
	int UhfStartInventory (int hCom, byte flagAnti, byte initQ, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         Get received data
	 *
	 *********************************************************************************************************/
	//��ָ��ֻ������Ӳ�����������ݣ�Ȼ���������Ҫ�Լ������̣߳�ʹ��UhfRecvDataѭ����ȡ��ǩ���ݡ������Ӧ��ֹͣ��ȡ
	int UhfReadInventory (int hCom, ByteByReference uLenUii, byte[] uUii);

	//UhfRecvData�����û�ֱ��ѭ����ȡ����
	int/*���ص����ݳ���*/ UhfRecvData(int hCom, byte[] uUii/*���ص����ݻ���*/);
	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� stop get
	 *
	 *********************************************************************************************************/
	int UhfStopOperation (int hCom, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� read data
	 *
	 *********************************************************************************************************/
	int UhfReadDataByEPC (int hCom, String accessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/, 
								 int memBank/*����ļ���ͷUHF_DATA_SECTION_XX�Ķ���*/, int sa, int dl,byte[] uDataReturn/*���ص�����*/, byte flagCrc);

	//ѡ��ģʽ����
	int UhfSETselectXZ (int hCom, int mode/*ȡֵΪ00��01��02*/, byte flagCrc);

	//�����ڶ�ȡTID��������ݣ����ô˺���ǰ����Ҫ��ʹ��UhfSETselectXZ(hCom,1,0);������һ��ѡ���ǩ��ģʽ���˺����᷵��uDataReturn��Ч���ݳ���
	int UhfReadDataByTID (int hCom, int sa, int dl,byte[] uDataReturn/*Ӳ�����ص�ԭʼ����*/,ByteByReference uErrorCode/*���صĴ������*/, byte flagCrc);

	//������UhfReadDataByEPC��Ψһ�����Ƕ�ȡǰ�����Զ�ѡ��ָ���ı�ǩ
	int UhfReadDataByXZEPC(int hCom, String epcLabelStr/*��Ҫ�Զ�ѡ�еı�ǩ*/,String accessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/, 
								 int memBank/*����ļ���ͷUHF_DATA_SECTION_XX�Ķ���*/, int sa, int dl,byte[] uDataReturn/*���ص�����*/, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� single inventory
	 *
	 *********************************************************************************************************/
	int UhfInventorySingleTag (int hCom, ByteByReference uLenUii, byte[] uUii , byte flagCrc);


	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� Add select,����һ��select
	 *
	 *********************************************************************************************************/
	//�ϵ�ԭ�ͣ��Ѿ�����int UhfAddFilter (int hCom, SRECORD* pSRecord, UCHAR* STATUS, byte flagCrc);
	int UhfAddFilter (int hCom, int intSelTarget, int intAction, int intSelMemBank,
							 int intSelPointer, int intMaskLen, int intTruncate, String txtSelMask, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� Delete select
	 *δʹ��
	 *********************************************************************************************************/
	int UhfDeleteFilterByIndex (int hCom, byte SINDEX, ByteByReference STATUS, byte flagCrc);//δʹ��

	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� Get select
	 *δʹ��
	 *********************************************************************************************************/
	int UhfStartGetFilterByIndex (int hCom, byte SINDEX, byte SNUM, ByteByReference STATUS, byte flagCrc);//δʹ��


	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� write data
	 *
	 *********************************************************************************************************/
	//�˺���д֮ǰ����Ҫ���ֹ�ѡ���ǩ
	int UhfWriteDataByEPC(int hCom, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/, byte uBank/*����ļ���ͷUHF_DATA_SECTION_XX�Ķ���*/, String uPtr/*д�ڴ����ʼ��ַ00 00*/, 
				byte uCnt/*д�ĸ���00 03�����255���ֽ�*/, byte[] uWriteData/*д�������*/, ByteByReference uErrorCode/*���صĴ������*/, byte flagCrc);


	//���������UhfWriteDataByEPC������������д����ǰ���ڲ����Զ���ѡ���豸������ǩ����
	int UhfWriteDataByEPCEx(int hCom, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/,byte uBank/*����ļ���ͷUHF_DATA_SECTION_XX�Ķ���*/, String uPtr/*д�ڴ����ʼ��ַ00 00*/, 
				byte uCnt/*д�ĸ���00 03*/, byte[] uWriteData/*д�������*/, ByteByReference uErrorCode/*���صĴ������*/, byte flagCrc);

	//���������UhfWriteDataByEPC������������д����ǰ���ڲ����Զ���ѡ���û�ָ����ǩ����
	int UhfWriteDataByXZEPC(int hCom, String EPCXZ, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/, byte uBank/*����ļ���ͷUHF_DATA_SECTION_XX�Ķ���*/, String uPtr/*д�ڴ����ʼ��ַ00 00*/, 
								   byte uCnt/*д�ĸ���00 03*/, byte[] uWriteData/*д�������*/, ByteByReference uErrorCode/*���صĴ������*/, byte flagCrc);

	//���������UhfWriteDataByEPC������������д����ǰ���ڲ����Զ���ѡ���ǩ������ͬʱ�̶�д��USER��
	int UhfWriteDataByUSER(int hCom, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/, String uPtr/*д�ڴ����ʼ��ַ00 00*/, 
								   byte uCnt/*д�ĸ���00 03*/, byte[] uWriteData/*д�������*/, ByteByReference uErrorCode/*���صĴ������*/, byte flagCrc);


	int UhfEraseDataByEPC(int hCom, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/,byte uBank/*����ļ���ͷUHF_DATA_SECTION_XX�Ķ���*/,
				ByteByReference uErrorCode/*���صĴ������*/, byte flagCrc);


	//ʹ��ǰ��Ҫ��ѡ�б�ǩ
	int UhfChangeConfig(int hCom, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/,
							   int Config/*ֻʹ����˫�ֽ�����*/,ByteByReference uErrorCode/*���صĴ�����*/, byte flagCrc);

	//4.25.NXP ReadProtect/Reset ReadProtect ָ��
	int UhfSetReadProtect(int hCom, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/,
							   int Config/*1��ʾ����ReadProtect��0��ʾȡ��ReadProtect*/,ByteByReference uErrorCode/*���صĴ�����*/, byte flagCrc);

	//ChangeEAS
	int UhfChangeEAS(int hCom, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/,
								 int ConfigPSF/*0x01 �������� PSF λΪ1��0x00 �������� PSF λΪ0*/,ByteByReference uErrorCode/*���صĴ�����*/, byte flagCrc);

	//EAS_Alarm
	int UhfEASAlarm(int hCom, byte flagCrc);

	int UhfChangeQTControl(int hCom, String uAccessPwd/*000000,4�ֽ�Ĭ�����룬�����ַ���*/,
								  int ReadWrite/*0x00: Read��0x01: Write*/,int Persistence/*0x00: д���ǩ�ӷ��Դ洢����0x01: д��ǻӷ��Դ洢��*/,
								  String Payload/*˫�ֽڵ��ַ���*/, ByteByReference uErrorCode/*���صĴ�����*/, byte flagCrc);
	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� lock memory
	 *
	 *********************************************************************************************************/
	//����
	int UhfLockMemByEPC (int hCom, String EPCXZ, String uAccessPwd, String uLockData, ByteByReference uErrorCode, byte flagCrc);

	//����
	int UhfunLockMemByEPC (int hCom, String EPCXZ, String uAccessPwd, String uLockData, ByteByReference uErrorCode, byte flagCrc);

	/**********************************************************************************************************
	 *
	 *                                         ����ģ�� kill tag
	 *
	 *********************************************************************************************************/
	int UhfKillTagByEPC (int hCom, String uKillPwd, ByteByReference uErrorCode, byte flagCrc);

	int UhfSetMode (int hCom, byte mode/*0����ʶ��1����ײȺ��ʶ��*/, byte flagCrc);//���ö���ģʽ
	int UhfSaveConfig (int hCom, byte flagCrc);//��������
	int UhfSleep (int hCom,  byte flagCrc);//����
	int UhfAutoFrequeC(int hCom, int mode/*0ȡ���Զ���Ƶ��1�Զ���Ƶ*/, byte flagCrc);//���÷��������ز�
	int UhfSelectQ(int hCom, int mode/*ȡֵΪ1��8*/, byte flagCrc);//���÷��������ز�
	
	int UhfControIO(int hCom, int ioCtrlType/*����Ϸ�UHF_IO_CONTROL_�Ķ���*/, int param1/*ȡֵ1~4*/,int param2,byte flagCrc);//�����������
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	//JR20X0 End
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
