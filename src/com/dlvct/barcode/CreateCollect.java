package com.dlvct.barcode;

import java.util.ArrayList;
import java.util.Map;

import com.dlvct.utils.db.DataHelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCollect extends Activity {
	private DataHelper dataHelper;
	private Spinner spinner;
	private LinearLayout list;
	private Button saveBtn;
	
	private ArrayAdapter<String> spinnerAdapter;
	private Handler handler;
	private LayoutInflater inflater;
	
	private final int LOAD_TYPE_FINISH = 10;
	private final int LOAD_ATTRIBUTE_FINISH = 11;
	private ArrayList<Map<String,String>> type ;
	private ArrayList<String> typeData = new ArrayList<String>();
	private ArrayList<Map<String,String>> attributeData = new ArrayList<Map<String,String>>();
	private ArrayList<String> saveData = new ArrayList<String>();
	private String typeId = "1";
	private LoadAttributeThread loadAttribute;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_collect);
		initData();
		initView();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case LOAD_TYPE_FINISH:
					spinnerAdapter.notifyDataSetChanged();
					loadAttribute.start();
					break;
				case LOAD_ATTRIBUTE_FINISH:
					initList();
					break;
				default:
					break;
				}
			}
		};
		loadType.start();
	}
	
	private void initData(){
		inflater = getLayoutInflater();
		dataHelper = new DataHelper(this);
		loadAttribute = new LoadAttributeThread();
		spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,typeData);
		
	}
	
	private void initView(){
		spinner = (Spinner)findViewById(R.id.create_collect_spinner);
		list = (LinearLayout)findViewById(R.id.create_collect_list);
		saveBtn = (Button)findViewById(R.id.create_collect_save);
		
		
		spinner.setAdapter(spinnerAdapter);
		saveBtn.setOnClickListener(click);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				typeId = type.get(position).get("ID");
				loadAttribute = new LoadAttributeThread();
				loadAttribute.start();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initList(){
		list.removeAllViews();
		int size = attributeData.size();
		for(int i=0;i<size;i++){
			View item = inflater.inflate(R.layout.create_new_item, null);
			((TextView)(item.findViewById(R.id.create_new_item_name))).setText(attributeData.get(i).get("NAME"));
			((Button)(item.findViewById(R.id.create_new_item_icon))).setVisibility(View.GONE);
			list.addView(item, i);
			Log.i("initList",String.valueOf(i));
		}
	}
	
	View.OnClickListener click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.create_collect_save:
				int size = attributeData.size();
				saveData.clear();
				for(int i=0;i<size;i++){
//					saveData.add();
					attributeData.get(i).put("VALUE", ((EditText)list.getChildAt(i).findViewById(R.id.create_new_item_text)).getText().toString());
				}
				if(saveData.indexOf("")!=-1){
					Toast.makeText(CreateCollect.this, "你还有没填写的项目", Toast.LENGTH_SHORT).show();
					return;
				}
				dataHelper.saveCollect(attributeData);
				Toast.makeText(CreateCollect.this, "保存成功", Toast.LENGTH_SHORT).show();
				finish();
				break;

			default:
				break;
			}
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		dataHelper.Close();
	};
	
	Thread loadType = new Thread(){
		public void run() {
			type = dataHelper.getType();
			typeId = type.get(0).get("ID");
			int size = type.size();
			for(int i=0;i<size;i++){
				typeData.add(type.get(i).get("TYPE"));
			}
			handler.sendEmptyMessage(LOAD_TYPE_FINISH);
		};
	};
	class LoadAttributeThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			attributeData = dataHelper.getAttribute(typeId);
			handler.sendEmptyMessage(LOAD_ATTRIBUTE_FINISH);
		}
	}
	
}
