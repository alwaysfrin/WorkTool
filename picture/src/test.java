
import ClientDemo.HCNetSDK;
import com.sun.jna.Native;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author HP
 */
public class test {
    public static void main(String[] args){
        System.out.println(System.getProperty("java.library.path"));
        HCNetSDK INSTANCE = (HCNetSDK) Native.loadLibrary("../lib/HCNetSDK.dll", HCNetSDK.class);
        System.out.println(INSTANCE);
    }
}
