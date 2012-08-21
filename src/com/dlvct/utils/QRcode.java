package com.dlvct.utils;

import java.io.File;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.android.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRcode {
	/**
	 * 解码
	 * @param b
	 * @return
	 */
    public String decode(Bitmap b) { 
    	String r = null;
        try {  
            try {  
                if (b == null) {  
                    System.out.println("Could not decode image");
                    return null;
                }  
                LuminanceSource source = new RGBLuminanceSource(b);  
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(  
                        source));  
                Result result;  
                Hashtable hints = new Hashtable();  
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");  
                result = new MultiFormatReader().decode(bitmap, hints);  
                r = result.getText();  
            } catch (ReaderException re) {  
                System.out.println(re.toString());  
            }  
  
        } catch (Exception ex) {  
  
        }  
        return r;
    } 
    
    /**
	 * 用字符串生成二维码
	 * @param str
	 * @author zhouzhe@lenovo-cw.com
	 * @return
	 * @throws WriterException
	 */
	public Bitmap Create2DCode(String str) throws WriterException {
		//生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		//二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(matrix.get(x, y)){
					pixels[y * width + x] = 0xff000000;
				}
				
			}
		}
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		//通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
