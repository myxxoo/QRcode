package com.dlvct.barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.dlvct.utils.constant.Constant;
import com.dlvct.utils.db.DataHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CollectTime extends Activity{
	private TextView searchText;
	private Button searchBtn;
	private LinearLayout listView;
	private LayoutInflater inflater;
	private Handler handler;
	private DatePickerDialog picker;
	
	private Date now = new Date();
	private boolean timeSelected = false;
	private Calendar calendar = Calendar.getInstance();
	private ArrayList<Map<String,String>> data;
	private ArrayList<Integer> breakIndexs = new ArrayList<Integer>();
	private DataHelper dataHelper;
	private LoadData loadData;
	private AssetManager asset;
	private long time;
	private final int DATA_LOAD_FINISH = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collect_time);
		initData();
		initView();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case DATA_LOAD_FINISH:
					initList();
					break;
					
				default:
					break;
				}
			}
		};
	}
	
	private void initData(){
		inflater = getLayoutInflater();
		dataHelper = new DataHelper(this);
		asset = getAssets();
	}
	
	private void initView(){
		searchText = (TextView)findViewById(R.id.collect_time_searchbox);
		searchBtn = (Button)findViewById(R.id.collect_time_search_button);
		listView = (LinearLayout)findViewById(R.id.collect_time_list);
		calendar.setTime(now);
		picker = new DatePickerDialog(this,pickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
		
		
		searchBtn.setOnClickListener(click);
		searchText.setOnClickListener(click);
	}
	
	private void initList(){
		listView.removeAllViews();
		int size = data.size();
		if(size!=0){
			String time = data.get(0).get("TIME");
			breakIndexs.clear();
			for(int i=0;i<size;i++){
				if(!time.equals(data.get(i).get("TIME"))){
					time = data.get(i).get("TIME");
					breakIndexs.add(i);
				}
			}
			
			int j = 0;
			for(int i=0;i<size;i++){
				if(breakIndexs.indexOf(i)!=-1){
					listView.addView(inflater.inflate(R.layout.null_textview, null),j);
					j++;
				}
				View item = inflater.inflate(R.layout.collect_right_item, null);
				TextView name = (TextView) item.findViewById(R.id.collect_right_item_name);
				TextView value = (TextView) item.findViewById(R.id.collect_right_item_value);
				ImageView icon = (ImageView)item.findViewById(R.id.collect_right_item_icon);
				name.setText(data.get(i).get("ATTRIBUTE"));
				value.setText(data.get(i).get("VALUE"));
				try {
					icon.setImageBitmap(BitmapFactory.decodeStream(asset.open(Constant.ICON_PATH+"/"+data.get(i).get("ICON"))));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listView.addView(item, j);
				j++;
			}
		}
	}
	
	View.OnClickListener click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.collect_time_search_button:
				if(!timeSelected){
					Toast.makeText(CollectTime.this, "请选择时间", Toast.LENGTH_SHORT).show();
					return;
				}
				loadData = new LoadData();
				loadData.start();
				break;
			case R.id.collect_time_searchbox:
				picker.show();
				break;
			default:
				break;
			}
		}
	};
	
	 DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			calendar.set(year, monthOfYear, dayOfMonth);
			time = calendar.getTimeInMillis();
			searchText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
			timeSelected = true;
		}
	};
	
	class LoadData extends Thread{
		public void run() {
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			String start = String.valueOf(calendar.getTimeInMillis());
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			String end = String.valueOf(calendar.getTimeInMillis());
			calendar.setTimeInMillis(time);
			data = dataHelper.getCollectByTime(start, end);
			handler.sendEmptyMessage(DATA_LOAD_FINISH);
		};
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dataHelper.Close();
	}
}
