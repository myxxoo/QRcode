package com.dlvct.barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dlvct.utils.constant.Constant;
import com.dlvct.utils.db.DataHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateAttribute extends Activity {
	private LinearLayout listView;
	private Button saveBtn;
	private Button tmpBtn;
	private Spinner spinner;
	private LayoutInflater inflater;
	private AlertDialog dialog;
	private GridView gridView; 
	
	private ArrayList<Map<String,String>> data = new ArrayList<Map<String,String>>();
	private ArrayAdapter<String> spinnerAdapter;
	private MyAdapter gridViewAdapter;
	private ArrayList<Map<String,String>> type = new ArrayList<Map<String,String>>();
	private ArrayList<String> typeList = new ArrayList<String>();
	private ArrayList<String> iconSelected = new ArrayList<String>();
	private String typeId;
	private DataHelper dataHelper;
	private Handler handler;
	private AssetManager asset;
	private String[] iconList;
	
	private final int LOAD_TYPE_FINISH = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_new_attribute);
		initData();
		initView();
		loadType.start();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case LOAD_TYPE_FINISH:
					spinnerAdapter.notifyDataSetChanged();
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
		try {
			iconList = asset.list(Constant.ICON_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initView() {
		spinnerAdapter = new ArrayAdapter<String>(CreateAttribute.this,R.layout.spinner_item,typeList);
		gridViewAdapter = new MyAdapter(this, R.layout.imageview, iconList);
		
		
		listView = (LinearLayout) findViewById(R.id.create_new_attribute_list);
		View item = inflater.inflate(R.layout.create_new_item, null);
		item.findViewById(R.id.create_new_item_icon).setOnClickListener(click);
		listView.addView(item, 0);
		saveBtn = (Button)findViewById(R.id.create_new_attribute_save);
		spinner = (Spinner)findViewById(R.id.create_new_attribute_spinner);
		gridView = (GridView) inflater.inflate(R.layout.gridview, null);
		
		gridView.setAdapter(gridViewAdapter);
		spinner.setAdapter(spinnerAdapter);
		
		spinner.setOnItemSelectedListener(adapterItemClick);
		gridView.setOnItemClickListener(itemClick);
		saveBtn.setOnClickListener(click);
		buildDialog();
	}
	
	
	Thread loadType = new Thread(){
		public void run() {
			type = dataHelper.getType();
			int size = type.size();
			for(int i=0;i<size;i++){
				typeList.add(type.get(i).get("TYPE"));
			}
			typeId = type.get(0).get("ID");
			handler.sendEmptyMessage(LOAD_TYPE_FINISH);
		};
	};
	
	View.OnClickListener click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.create_new_attribute_save:
				int count = listView.getChildCount();
				data.clear();
				for(int i=0;i<count;i++){
					String s = ((EditText)listView.getChildAt(i).findViewById(R.id.create_new_item_text)).getText().toString();
					Object icon = listView.getChildAt(i).findViewById(R.id.create_new_item_icon).getTag();
					if(s!=null&&!"".equals(s)){
						if(icon==null){
							Toast.makeText(CreateAttribute.this, "你还没有为"+s+"选择图标", Toast.LENGTH_SHORT).show();
							return;
						}
						HashMap<String,String> map = new HashMap<String, String>();
						map.put("ICON", icon.toString());
						map.put("NAME", s);
						data.add(map);
					}
				}
				if(data.size()==0){
					Toast.makeText(CreateAttribute.this, "你什么都没输入", Toast.LENGTH_SHORT).show();
					return;
				}
				dataHelper.saveAttribute(typeId, data);
				dataHelper.Close();
				Toast.makeText(CreateAttribute.this, "保存成功", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case R.id.create_new_item_icon:
				tmpBtn = (Button) v;
				dialog.show();
				break;
			default:
				break;
			}
		}
	};
	
	AdapterView.OnItemSelectedListener adapterItemClick = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View v, int position,
				long arg3) {
			// TODO Auto-generated method stub
				typeId = type.get(position).get("ID");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			try {
				LayoutParams p = tmpBtn.getLayoutParams();
				p.height = 48;
				p.width = 48;
				tmpBtn.setLayoutParams(p);
				tmpBtn.setText("");
				tmpBtn.setBackgroundDrawable(BitmapDrawable.createFromStream(asset.open(Constant.ICON_PATH+"/"+iconList[position]), null));
				tmpBtn.setTag(iconList[position]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dialog.dismiss();
		}
	};
	
	private void buildDialog(){
		Builder b = new Builder(this);
		b.setTitle(R.string.select_icon);
		b.setView(gridView);
		dialog = b.create();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(R.string.add);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.create_new_item, null);
		v.findViewById(R.id.create_new_item_icon).setOnClickListener(click);
		listView.addView(v, listView.getChildCount());
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dataHelper.Close();
	}
	
	private class MyAdapter extends ArrayAdapter<String>{
		
		public MyAdapter(Context context,int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = inflater.inflate(R.layout.imageview, null);
			}
			try {
				Bitmap img = BitmapFactory.decodeStream(asset.open(Constant.ICON_PATH+"/"+iconList[position]));
				((ImageView)convertView).setImageBitmap(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}
		
	}

}
