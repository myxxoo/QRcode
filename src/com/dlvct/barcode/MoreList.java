package com.dlvct.barcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MoreList extends Activity{
	private ListView listView;
//	private LayoutInflater inflater;
	private ArrayAdapter<String> adapter;
	private String[] list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_list);
		list = getResources().getStringArray(R.array.more_list);
		initView();
	}
	
	private void initView(){
		adapter = new ArrayAdapter<String>(this, R.layout.textview_white_item, list);
		listView = (ListView)findViewById(R.id.more_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position == 0){
					Intent one = new Intent(MoreList.this,CreateAttribute.class);
					startActivity(one);
				}else if(position == 1){
					Intent two = new Intent(MoreList.this,CreateCollect.class);
					startActivity(two);
				}
			}
		});
	}
	
}
