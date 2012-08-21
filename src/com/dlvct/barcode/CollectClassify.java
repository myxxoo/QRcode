package com.dlvct.barcode;


import java.util.ArrayList;
import java.util.Map;

import com.dlvct.utils.db.DataHelper;

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

public class CollectClassify extends TabActivity{
	private TabHost tabHost;
	private Resources resources;
	private LayoutInflater inflater;
	private DataHelper dataHelper;
	private ArrayList<Map<String,String>> type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collect_classify);
		resources = getResources();
		inflater = getLayoutInflater();
		dataHelper = new DataHelper(this);
		type = dataHelper.getType();
		initView();
	}
	
	private void initView(){
		tabHost = getTabHost();
		int size = type.size();
//		for(int j=0;j<4;j++){
			for(int i=0;i<size;i++){
				View item = inflater.inflate(R.layout.collect_left_item, null);
				TextView name = (TextView)item.findViewById(R.id.collect_left_item_text);
				ImageView icon = (ImageView)item.findViewById(R.id.collect_left_item_img);
				
				TabSpec tab = tabHost.newTabSpec(type.get(i).get("TYPE"));
				name.setText(type.get(i).get("TYPE"));
				icon.setImageResource(R.drawable.card_history_left);
				tab.setIndicator(item);
				Intent one = new Intent(CollectClassify.this,CollectClassifyRight.class);
				one.putExtra("ID", type.get(i).get("ID"));
				one.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				tab.setContent(one);
				tabHost.addTab(tab);
			}
//		}
		
//		
//		TabSpec tab1 = tabHost.newTabSpec("tab1");
//		name.setText("火车票");
//		icon.setImageResource(R.drawable.card_history_left);
//		tab1.setIndicator(item);
//		Intent one = new Intent(CollectClassify.this,CollectTmp.class);
//		one.putExtra("ID", "1");
//		one.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		tab1.setContent(one);
//		tabHost.addTab(tab1);
//		
//		
//		item = inflater.inflate(R.layout.collect_left_item, null);
//		name = (TextView)item.findViewById(R.id.collect_left_item_text);
//		icon = (ImageView)item.findViewById(R.id.collect_left_item_img);
//		TabSpec tab2 = tabHost.newTabSpec("tab2");
//		name.setText("网站");
//		icon.setImageResource(R.drawable.net_history_left);
//		tab2.setIndicator(item);
//		Intent two = new Intent(CollectClassify.this,CollectTmp.class);
//		two.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		two.putExtra("ID", "2");
//		tab2.setContent(two);
//		tabHost.addTab(tab2);
//		
//		item = inflater.inflate(R.layout.collect_left_item, null);
//		name = (TextView)item.findViewById(R.id.collect_left_item_text);
//		icon = (ImageView)item.findViewById(R.id.collect_left_item_img);
//		TabSpec tab3 = tabHost.newTabSpec("tab2");
//		name.setText("商品");
//		icon.setImageResource(R.drawable.product_history_left);
//		tab3.setIndicator(item);
//		Intent three = new Intent(CollectClassify.this,CollectTmp.class);
//		three.putExtra("ID", "3");
//		three.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		tab3.setContent(three);
//		tabHost.addTab(tab3);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dataHelper.Close();
	}
}
