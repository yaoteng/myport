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


import gnu.io.*;
import java.io.*; 
import java.util.*;  
 
 
public class SerialReader extends Observable implements Runnable,SerialPortEventListener
    {
    static CommPortIdentifier portId;
    int delayRead = 100;
    int numBytes; // buffer涓殑�?�為檯鏁版嵁瀛楄妭鏁�?
    private static byte[] readBuffer = new byte[1024]; // 4k鐨刡uffer绌洪�?,缂撳瓨涓插彛璇诲叆鐨勬暟鎹�
    static Enumeration portList;
    InputStream inputStream;
    OutputStream outputStream;
    static SerialPort serialPort;
    HashMap serialParams;
    Thread readThread;//鏈潵鏄痵tatic绫诲瀷鐨�?
    //绔彛鏄惁鎵撳紑浜�?
    boolean isOpen = false;
    // 绔彛璇诲叆鏁版嵁浜嬩欢瑙﹀彂鍚�?,绛夊緟n姣鍚庡啀璇诲�?,浠ヤ究璁╂暟鎹竴娆℃�ц�?��
    public static final String PARAMS_DELAY = "delay read"; // 寤舵椂绛夊緟绔彛鏁版嵁鍑嗗鐨勬椂闂�
    public static final String PARAMS_TIMEOUT = "timeout"; // 瓒呮椂鏃堕棿
    public static final String PARAMS_PORT = "port name"; // 绔彛鍚嶇�?
    public static final String PARAMS_DATABITS = "data bits"; // 鏁版嵁浣�?
    public static final String PARAMS_STOPBITS = "stop bits"; // 鍋滄浣�?
    public static final String PARAMS_PARITY = "parity"; // 濂囧伓鏍￠獙
    public static final String PARAMS_RATE = "rate"; // 娉㈢壒鐜�?

    public boolean isOpen(){
    	return isOpen;
    }
    /**
     * 鍒濆鍖栫鍙ｆ搷浣滅殑鍙傛�?.
     * @throws SerialPortException 
     * 
     * @see
     */
    public SerialReader()
    {
    	isOpen = false;
    }

    public void open(HashMap params) 
    { 
    	serialParams = params;
    	if(isOpen){
    		close();
    	}
        try
        {
            // 鍙傛暟鍒濆鍖�
            int timeout = Integer.parseInt( serialParams.get( PARAMS_TIMEOUT )
                .toString() );
            int rate = Integer.parseInt( serialParams.get( PARAMS_RATE )
                .toString() );
            int dataBits = Integer.parseInt( serialParams.get( PARAMS_DATABITS )
                .toString() );
            int stopBits = Integer.parseInt( serialParams.get( PARAMS_STOPBITS )
                .toString() );
            int parity = Integer.parseInt( serialParams.get( PARAMS_PARITY )
                .toString() );
            delayRead = Integer.parseInt( serialParams.get( PARAMS_DELAY )
                .toString() );
            String port = serialParams.get( PARAMS_PORT ).toString();
            // 鎵撳紑绔彛
            portId = CommPortIdentifier.getPortIdentifier( port );
            serialPort = ( SerialPort ) portId.open( "SerialReader", timeout );
            inputStream = serialPort.getInputStream();
            serialPort.addEventListener( this );
            serialPort.notifyOnDataAvailable( true );
            serialPort.setSerialPortParams( rate, dataBits, stopBits, parity );
            
            isOpen = true;
        }
        catch ( PortInUseException e )
        {
           // 绔�?"+serialParams.get( PARAMS_PORT ).toString()+"宸茬粡琚崰鐢�";
        }
        catch ( TooManyListenersException e )
        {
           //"绔�?"+serialParams.get( PARAMS_PORT ).toString()+"鐩戝惉鑰呰繃澶�";
        }
        catch ( UnsupportedCommOperationException e )
        {
           //"绔彛鎿嶄綔鍛戒护涓嶆敮鎸�";
        }
        catch ( NoSuchPortException e )
        {
          //"绔�?"+serialParams.get( PARAMS_PORT ).toString()+"涓嶅瓨鍦�?";
        }
        catch ( IOException e )
        {
           //"鎵撳紑绔彛"+serialParams.get( PARAMS_PORT ).toString()+"澶辫�?";
        }
        serialParams.clear();
        Thread readThread = new Thread( this );
        readThread.start();
    }

     
    public void run()
    {
        try
        {
            Thread.sleep(50);
        }
        catch ( InterruptedException e )
        {
        }
    } 
    public void start(){
   	  try {  
      	outputStream = serialPort.getOutputStream();
   	     } 
   	catch (IOException e) {}
   	try{ 
   	    readThread = new Thread(this);
     	readThread.start();
   	} 
   	catch (Exception e) {  }
   }  //start() end


   public void run(String message) {
   	try { 
   		Thread.sleep(4); 
           } 
   	 catch (InterruptedException e) {  } 
   	 try {
   		 if(message!=null&&message.length()!=0)
   		 { 	 
   			 System.out.println("run message:"+message);
   	        outputStream.write(message.getBytes()); //寰�涓插彛鍙戦�佹暟鎹紝鏄弻鍚戦�氳鐨勩��?
   		 }
   	} catch (IOException e) {}
   } 
    

    public void close() 
    { 
        if (isOpen)
        {
            try
            {
            	serialPort.notifyOnDataAvailable(false);
            	serialPort.removeEventListener();
                inputStream.close();
                serialPort.close();
                isOpen = false;
            } catch (IOException ex)
            {
            //"鍏抽棴涓插彛澶辫�?";
            }
        }
    }
    
    public void serialEvent( SerialPortEvent event )
    {
        try
        {
            Thread.sleep( delayRead );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
        switch ( event.getEventType() )
        {
            case SerialPortEvent.BI: // 10
            case SerialPortEvent.OE: // 7
            case SerialPortEvent.FE: // 9
            case SerialPortEvent.PE: // 8
            case SerialPortEvent.CD: // 6
            case SerialPortEvent.CTS: // 3
            case SerialPortEvent.DSR: // 4
            case SerialPortEvent.RI: // 5
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
                break;
            case SerialPortEvent.DATA_AVAILABLE: // 1
                try
                {
                    // 澶氭璇诲彇,灏嗘墍鏈夋暟鎹鍏�?
                     while (inputStream.available() > 0) {
                     numBytes = inputStream.read(readBuffer);
                     }
                     
                     //鎵撳嵃鎺ユ敹鍒扮殑�?�楄妭鏁版嵁鐨凙SCII鐮�
                     for(int i=0;i<numBytes;i++){
                    	// System.out.println("msg[" + numBytes + "]: [" +readBuffer[i] + "]:"+(char)readBuffer[i]);
                     }
//                    numBytes = inputStream.read( readBuffer );
                    changeMessage( readBuffer, numBytes );
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    // 閫氳繃observer pattern灏嗘敹鍒扮殑鏁版嵁鍙戦�佺粰observer
    // 灏哹uffer涓殑绌哄瓧鑺傚垹闄ゅ悗鍐嶅彂閫佹洿鏂版秷鎭�?,閫氱煡瑙傚療鑰�
    public void changeMessage( byte[] message, int length )
    {
        setChanged();
        byte[] temp = new byte[length];
        System.arraycopy( message, 0, temp, 0, length );
        notifyObservers( temp );
    }

    static void listPorts()
    {
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() )
        {
            CommPortIdentifier portIdentifier = ( CommPortIdentifier ) portEnum
                .nextElement();
            
        }
    }
    
    
    public void openSerialPort(String message)
    {
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();  
        String port="COM1";
        String rate = "9600";
        String dataBit = ""+SerialPort.DATABITS_8;
        String stopBit = ""+SerialPort.STOPBITS_1;
        String parity = ""+SerialPort.PARITY_NONE;    
        int parityInt = SerialPort.PARITY_NONE; 
        params.put( SerialReader.PARAMS_PORT, port ); // 绔彛鍚嶇�?
        params.put( SerialReader.PARAMS_RATE, rate ); // 娉㈢壒鐜�?
        params.put( SerialReader.PARAMS_DATABITS,dataBit  ); // 鏁版嵁浣�?
        params.put( SerialReader.PARAMS_STOPBITS, stopBit ); // 鍋滄浣�?
        params.put( SerialReader.PARAMS_PARITY, parityInt ); // 鏃犲鍋舵牎楠�
        params.put( SerialReader.PARAMS_TIMEOUT, 100 ); // 璁惧瓒呮椂鏃堕�? 1绉�
        params.put( SerialReader.PARAMS_DELAY, 100 ); // 绔彛鏁版嵁鍑嗗鏃堕棿 1绉�
        try {
			open(params);//鎵撳紑涓插彛
			//LoginFrame cf=new LoginFrame();
			//addObserver(cf);
			//涔熷彲浠ュ儚涓婇潰涓�涓�氳繃LoginFrame鏉ョ粦�?�氫覆鍙ｇ殑閫氳杈撳嚭.
			if(message!=null&&message.length()!=0)
			 {
				String str="";
				for(int i=0;i<10;i++)
				{
					str+=message;
				}
				 start(); 
			     run(str);  
			 } 
		} catch (Exception e) { 
		}
    }

    static String getPortTypeName( int portType )
    {
        switch ( portType )
        {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }

     
    public  HashSet<CommPortIdentifier> getAvailableSerialPorts()//鏈潵static
    {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while ( thePorts.hasMoreElements() )
        {
            CommPortIdentifier com = ( CommPortIdentifier ) thePorts
                .nextElement();
            switch ( com.getPortType() )
            {
                case CommPortIdentifier.PORT_SERIAL:
                    try
                    {
                        CommPort thePort = com.open( "CommUtil", 50 );
                        thePort.close();
                        h.add( com );
                    }
                    catch ( PortInUseException e )
                    {
                        System.out.println( "Port, " + com.getName()
                            + ", is in use." );
                    }
                    catch ( Exception e )
                    {
                        System.out.println( "Failed to open port "
                            + com.getName() + e );
                    }
            }
        }
        return h;
    }
}
