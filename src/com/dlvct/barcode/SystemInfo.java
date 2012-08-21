package com.dlvct.barcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SystemInfo extends Activity{
	private ListView listView;
	private LayoutInflater inflater;
	private String[] names = {"系统版本:","内存:","SD卡:"};
	private List<String> data = new ArrayList<String>();
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.system_info);
		inflater = getLayoutInflater();
		initView();
	}
	
	private void initView(){
		getData();
		adapter = new MyAdapter(this, R.layout.system_info_list_item, data);
		listView = (ListView)findViewById(R.id.system_info_listview);
		listView.setAdapter(adapter);
	}
	
	private void getData(){
		data.add("Android "+Build.VERSION.RELEASE);
		
		long a = getTotalMemory();
		if(a==0){
			data.add("获取内存信息异常！");
		}else{
			data.add(a/1024+"M");
		}
		
		long[] l = getSDCardMemory();
		data.add(formatSize(l[1])+"/"+formatSize(l[0]));
		
	}
	//获取内存信息
	public long getTotalMemory() {  
        String str1 = "/proc/meminfo";  
        String str2="";  
        try {  
            FileReader fr = new FileReader(str1);  
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);  
            while ((str2 = localBufferedReader.readLine()) != null) {  
                if(str2.indexOf("MemTotal")!=-1){
                	break;
                }
            }
//            str2.substring(str2.lastIndexOf(":")+1);
            String[] s = str2.split(" ");
            
            return Long.parseLong(s[9]);
        } catch (IOException e) { 
        }
        return 0;
    }
	//获取系统版本信息
	public String[] getVersion(){  
	    String[] version={"null","null","null","null"};  
	    String str1 = "/proc/version";  
	    String str2;  
	    String[] arrayOfString;  
	    try {  
	        FileReader localFileReader = new FileReader(str1);  
	        BufferedReader localBufferedReader = new BufferedReader(  
	                localFileReader, 8192);  
	        str2 = localBufferedReader.readLine();  
	        arrayOfString = str2.split("\\s+");  
	        version[0]=arrayOfString[2];//KernelVersion  
	        localBufferedReader.close();  
	    } catch (IOException e) {  
	    }  
	    version[1] = Build.VERSION.RELEASE;// firmware version  
	    version[2]=Build.MODEL;//model  
	    version[3]=Build.DISPLAY;//system version  
	    return version;  
	}
	
	//CPU信息
	public String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2="";
		String[] cpuInfo={"",""};
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return cpuInfo;
	}

	public long[] getSDCardMemory() {  
        long[] sdCardInfo=new long[2];  
        String state = Environment.getExternalStorageState();  
        if (Environment.MEDIA_MOUNTED.equals(state)) {  
            File sdcardDir = Environment.getExternalStorageDirectory();  
            StatFs sf = new StatFs(sdcardDir.getPath());  
            long bSize = sf.getBlockSize();  
            long bCount = sf.getBlockCount();  
            long availBlocks = sf.getAvailableBlocks();  
  
            sdCardInfo[0] = bSize * bCount;//总大小  
            sdCardInfo[1] = bSize * availBlocks;//可用大小  
        }  
        return sdCardInfo;  
    }
	
	
	public String formatSize(long size) {
		String suffix = null;
		float fSize=0;

		if (size >= 1024) {
			suffix = "KB";
			fSize=size / 1024;
			if (fSize >= 1024) {
				suffix = "MB";
				fSize /= 1024;
			}
			if (fSize >= 1024) {
				suffix = "GB";
				fSize /= 1024;
			}
		} else {
			fSize = size;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
		StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	
	private class MyAdapter extends ArrayAdapter<String>{
		private int id;
		public MyAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			id = textViewResourceId;
			// TODO Auto-generated constructor stub
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = inflater.inflate(id, null);
			}
			((TextView)convertView.findViewById(R.id.system_info_list_item_name)).setText(names[position]);
			((TextView)convertView.findViewById(R.id.system_info_list_item_value)).setText(data.get(position));
			return convertView;
		}
		
	}
}
