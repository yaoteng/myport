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
public class SerialPortTx {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Test test= new Test();
        test.send("HELLO WORLD!!!!");
    }
    
}
