
import ClientDemo.HCNetSDK;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;

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
        HCNetSDK INSTANCE = (HCNetSDK) Native.loadLibrary("D:\\lib\\HCNetSDK.dll", HCNetSDK.class);
        //INSTANCE.NET_DVR_CaptureJPEGPicture(, NativeLong lChannel, HCNetSDK.NET_DVR_JPEGPARA lpJpegPara, String sPicFileName);
        System.out.println(INSTANCE);
    }
}
