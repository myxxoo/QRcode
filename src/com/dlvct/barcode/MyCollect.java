package com.dlvct.barcode;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MyCollect extends TabActivity{
	private TabHost tabHost;
	private Resources resources;
	private LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_collect);
		resources = getResources();
		inflater = getLayoutInflater();
		initView();
	}
	
	private void initView(){
		tabHost = getTabHost();
		View item = inflater.inflate(R.layout.collect_top_item, null);
		TextView name = (TextView)item.findViewById(R.id.collect_top_item_text);
		
		TabSpec tab1 = tabHost.newTabSpec("tab1");
		name.setText(R.string.check_by_time);
		tab1.setIndicator(item);
		tab1.setContent(new Intent(MyCollect.this,CollectTime.class));
		tabHost.addTab(tab1);
		
		
		item = inflater.inflate(R.layout.collect_top_item, null);
		name = (TextView)item.findViewById(R.id.collect_top_item_text);
		name.setText(R.string.check_by_classify);
		TabSpec tab2 = tabHost.newTabSpec("tab2");
		tab2.setIndicator(item);
		tab2.setContent(new Intent(MyCollect.this,CollectClassify.class));
		tabHost.addTab(tab2);
		
//		tabHost.setBackgroundResource(R.drawable.bg_collect_top_select);
	}
}
