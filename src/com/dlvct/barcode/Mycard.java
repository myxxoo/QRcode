package com.dlvct.barcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class Mycard extends Activity{
	private ImageButton circle,collect,system;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_card);
		initView();
	}
	
	private void initView(){
		circle = (ImageButton)findViewById(R.id.cirtle);
		collect = (ImageButton)findViewById(R.id.my_collect);
		system = (ImageButton)findViewById(R.id.system_info);
		
		circle.setOnClickListener(click);
		collect.setOnClickListener(click);
		system.setOnClickListener(click);
	}
	
	View.OnClickListener click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.cirtle:
//				Intent circle = new Intent(Mycard.this,Barcode.class);
//				startActivity(circle);
				finish();
				break;
			case R.id.my_collect:
				Intent collect = new Intent(Mycard.this,MyCollect.class);
				startActivity(collect);
				break;
			case R.id.system_info:
				Intent info = new Intent(Mycard.this,SystemInfo.class);
				startActivity(info);
				break;
			default:
				break;
			}
		}
	};
}
