package com.dlvct.barcode;

import java.util.ArrayList;
import java.util.Map;

import com.dlvct.utils.db.DataHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CollectTmp extends Activity{
	private LinearLayout listView;
	private MyAdapter adapter;
	private LayoutInflater inflater;
	private ArrayList<Map<String, String>> data = new ArrayList<Map<String,String>>();
	private Intent intent;
	private Handler handler;
	private String typeId;
	private DataHelper dataHelper;
	private ArrayList<Integer> breakIndexs = new ArrayList<Integer>();
	private final int LOAD_DATA_FINISH = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collect_right);
		intent = getIntent();
		typeId = intent.getStringExtra("ID");
		inflater = getLayoutInflater();
		dataHelper = new DataHelper(this);
		initView();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case LOAD_DATA_FINISH:
//					adapter.notifyDataSetChanged();
					initList();
					break;

				default:
					break;
				}
			}
		};
		loadData.start();
	}
	
	private void initView(){
		listView = (LinearLayout)findViewById(R.id.collect_right_list);
		adapter = new MyAdapter();
//		listView.setAdapter(adapter);
	}
	
	private void initList(){
		listView.removeAllViews();
		int size = data.size();
		int j = 0;
		for(int i=0;i<size;i++){
			if(breakIndexs.indexOf(i)!=-1){
				listView.addView(inflater.inflate(R.layout.null_textview, null),j);
				j++;
			}
			View item = inflater.inflate(R.layout.collect_right_item, null);
			TextView name = (TextView) item.findViewById(R.id.collect_right_item_name);
			TextView value = (TextView) item.findViewById(R.id.collect_right_item_value);
			name.setText(data.get(i).get("ATTRIBUTE"));
			value.setText(data.get(i).get("VALUE"));
			listView.addView(item, j);
			j++;
		}
	}
	
	Thread loadData = new Thread(){
		public void run() {
			data = dataHelper.getCollectByType(typeId);
			handler.sendEmptyMessage(LOAD_DATA_FINISH);
			int size = data.size();
			if(size!=0){
				String time = data.get(0).get("TIME");
				for(int i=0;i<size;i++){
					if(!time.equals(data.get(i).get("TIME"))){
						time = data.get(i).get("TIME");
						breakIndexs.add(i);
					}
				}
			}
		};
	};
	
	
	protected void onDestroy() {
		super.onDestroy();
		dataHelper.Close();
	};
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = inflater.inflate(R.layout.collect_right_item, null);
				TextView name = (TextView) convertView.findViewById(R.id.collect_right_item_name);
				TextView value = (TextView) convertView.findViewById(R.id.collect_right_item_value);
				name.setText(data.get(position).get("ATTRIBUTE"));
				value.setText(data.get(position).get("VALUE"));
			}
			
			return convertView;
		}
		
	}
	
}