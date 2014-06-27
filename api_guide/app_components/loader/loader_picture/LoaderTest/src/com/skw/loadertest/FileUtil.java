package com.skw.loadertest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileUtil {
    public FileUtil() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * InputStream to byte
     * @param inStream
     * @return
     * @throws Exception
     */
    public byte[] readInputStream(InputStream inStream) throws Exception { 
        byte[] buffer = new byte[1024]; 
        int len = -1; 
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        
        while ((len = inStream.read(buffer)) != -1) { 
            outStream.write(buffer, 0, len); 
        }
        
        byte[] data = outStream.toByteArray(); 
        outStream.close(); 
        inStream.close();
        
        return data; 
   } 
    
   /**
    * Byte to bitmap
    * @param bytes
    * @param opts
    * @return
    */
   public Bitmap getBitmapFromBytes(byte[] bytes, BitmapFactory.Options opts) {
       if (bytes != null){
           if (opts != null){ 
               return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts); 
           }
           else{
               return BitmapFactory.decodeByteArray(bytes, 0, bytes.length); 
           }
       }
       
       return null; 
   } 
}
