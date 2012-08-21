package com.dlvct.barcode;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class InputCode extends Activity{
	private GridView grid;
	private String[] numList = {"7","8","9","4","5","6","1","2","3","978","0","删除"};
	private LayoutInflater inflater;
	private MyAdapter adapter;
	private EditText edit;
	private StringBuffer text = new StringBuffer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.input_code);
		adapter = new MyAdapter(this, R.layout.input_keybord_item, numList);
		inflater = getLayoutInflater();
		initView();
		
	}
	
	private void initView(){
		edit = (EditText)findViewById(R.id.edit_text);
		edit.setOnTouchListener(new OnTouchListener() {               
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				edit.setInputType(InputType.TYPE_NULL); // 隐藏软键盘        
				return false;
			}  
		  
		});
		grid = (GridView)findViewById(R.id.keybord);
		grid.setAdapter(adapter);
	}
	
	class MyAdapter extends ArrayAdapter<String>{
		private int id;
		public MyAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
			id = textViewResourceId;
			// TODO Auto-generated constructor stub
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = inflater.inflate(id, null);
			}
			((Button)convertView).setText(numList[position]);
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int location = edit.getSelectionStart();
					System.out.println("���λ��:"+location);
					if(position==numList.length-1){
						if(text.length()==0 || location==0)return;
						text.deleteCharAt(location-1);
						edit.setText(text.toString());
						edit.setSelection(location-1);
						return;
					}
					if(text.length()>=15){
						Toast.makeText(InputCode.this, "条码长度不能超过15", Toast.LENGTH_SHORT).show();
						return;
					}
					if(position==9&&text.length()>12){
						Toast.makeText(InputCode.this, "条码长度不能超过15", Toast.LENGTH_SHORT).show();
						return;
					}
					text = new StringBuffer(edit.getText().toString());
					text.insert(location,numList[position]);
					edit.setText(text.toString());
					if(position==9){
						edit.setSelection(location+3);
					}else{
						edit.setSelection(location+1);
					}
					
				}
			});
			return super.getView(position, convertView, parent);
		}
	}
}
