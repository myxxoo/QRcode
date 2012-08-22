package com.dlvct.utils;

import java.io.UnsupportedEncodingException;

public class MessyCode {
	public static String toNormal(String s){
		String UTF_Str="";  
        String GB_Str="";  
        boolean is_cN=false;  
        try {  
            System.out.println("------------"+s);  
            UTF_Str=new String(s.getBytes("ISO-8859-1"),"UTF-8");  
            System.out.println("这是转了UTF-8的"+UTF_Str);  
            is_cN=isChineseCharacter(UTF_Str);  
            //防止有人特意使用乱码来生成二维码来判断的情况  
            boolean b=isSpecialCharacter(s);  
            if(b){  
                is_cN=true;
                s = UTF_Str;
            }  
            System.out.println("是中文:"+is_cN);  
            if(!is_cN){  
                GB_Str=new String(s.getBytes("ISO-8859-1"),"GB2312");
                s = GB_Str;
                System.out.println("这是转了GB2312的"+GB_Str);  
            }  
        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
		return s;
	}
	
		
    private static final boolean isChineseCharacter(String chineseStr) {  
        char[] charArray = chineseStr.toCharArray();  
        for (int i = 0; i < charArray.length; i++) {  	
        	//是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
            if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')||((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {  
                continue;
            }
            else{
            	return false;
            }
        }  
        return true;  
    }  
    
    private static final boolean isSpecialCharacter(String str){
    	//是"�"这个特殊字符的乱码情况
    	if(str.contains("ï¿½")){
    		return true;
    	}
    	return false;
    }
}
