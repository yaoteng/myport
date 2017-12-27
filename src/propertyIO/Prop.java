package propertyIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

/**
 * Prop. Prop can load properties file from CLASSPATH or File object.
 */
public class Prop {
    
    private static Properties properties = null;
    private static String fileName;
    
    /**
     * Prop constructor.
     * @see #Prop(String, String)
     */
    public Prop(String fileName) {
        this(fileName, "UTF-8");
    }
    
    /**
     * Prop constructor
     * <p>
     * Example:<br>
     * Prop prop = new Prop("my_config.txt", "UTF-8");<br>
     * String userName = prop.get("userName");<br><br>
     * 
     * prop = new Prop("com/jfinal/file_in_sub_path_of_classpath.txt", "UTF-8");<br>
     * String value = prop.get("key");
     * 
     * @param fileName the properties file's name in classpath or the sub directory of classpath
     * @param encoding the encoding
     */
    public Prop(String fileName, String encoding) {
    	this.fileName=fileName;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);        // properties.load(Prop.class.getResourceAsStream(fileName));
            if (inputStream == null)
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        }
        finally {
            if (inputStream != null) try {inputStream.close();} catch (IOException e) {e.printStackTrace();}
        }
    }
    
    /**
     * Prop constructor.
     * @see #Prop(File, String)
     */
    public Prop(File file) {
        this(file, "UTF-8");
    }
    
    /**
     * Prop constructor
     * <p>
     * Example:<br>
     * Prop prop = new Prop(new File("/var/config/my_config.txt"), "UTF-8");<br>
     * String userName = prop.get("userName");
     * 
     * @param file the properties File object
     * @param encoding the encoding
     */
    public Prop(File file, String encoding) {
        if (file == null)
            throw new IllegalArgumentException("File can not be null.");
        if (file.isFile() == false)
            throw new IllegalArgumentException("Not a file : " + file.getName());
        this.fileName=file.getName();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        }
        finally {
            if (inputStream != null) try {inputStream.close();} catch (IOException e) {e.printStackTrace();}
        }
    }
    
    public String get(String key) {
        return properties.getProperty(key);
    }
    
    public String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null) ? value : defaultValue;
    }
    
    public Integer getInt(String key) {
        String value = get(key);
        return (value != null) ? Integer.parseInt(value) : null;
    }
    
    public Integer getInt(String key, Integer defaultValue) {
        String value = get(key);
        return (value != null) ? Integer.parseInt(value) : defaultValue;
    }
    
    public Long getLong(String key) {
        String value = get(key);
        return (value != null) ? Long.parseLong(value) : null;
    }
    
    public Long getLong(String key, Long defaultValue) {
        String value = get(key);
        return (value != null) ? Long.parseLong(value) : defaultValue;
    }
    
    public Boolean getBoolean(String key) {
        String value = get(key);
        return (value != null) ? Boolean.parseBoolean(value) : null;
    }
    
    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = get(key);
        return (value != null) ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    
    
    public static void writeProperties(String keyname,String keyvalue) {          
        try {   
            // ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�   
            // ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����   
            OutputStream fos = new FileOutputStream(fileName);   
            properties.setProperty(keyname, keyvalue);   
            // ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��   
            // ���� Properties ���е������б�����Ԫ�ضԣ�д�������   
            properties.store(fos, "Update '" + keyname + "' value");   
        } catch (IOException e) {   
            System.err.println("�����ļ����´���");   
        }   
    }   
  
    /**  
    * ����properties�ļ��ļ�ֵ��  
    * ����������Ѿ����ڣ����¸�������ֵ��  
    * ��������������ڣ�����һ�Լ�ֵ��  
    * @param keyname ����  
    * @param keyvalue ��ֵ  
    */   
    public void updateProperties(String keyname,String keyvalue) {   
        try {   
        	properties.load(new FileInputStream(new File(fileName)));   
            // ���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�   
            // ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����   
            OutputStream fos = new FileOutputStream(new File(fileName));              
            properties.setProperty(keyname, keyvalue);   
            // ���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��   
            // ���� Properties ���е������б�����Ԫ�ضԣ�д�������   
            properties.store(fos, "Update '" + keyname + "' value");   
        } catch (IOException e) {   
            System.err.println("�����ļ����´���");   
        }   
    }   
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }
    
    public Properties getProperties() {
        return properties;
    }
}