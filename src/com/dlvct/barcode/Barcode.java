package com.dlvct.barcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;


public class Barcode extends Activity{
	
	private SurfaceView surfaceView;  
    private Camera camera; 
	
    private Button selectPic,inputCode;
    private ImageButton mycard,more;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        initView();
    }
    private void initView(){
    	surfaceView = (SurfaceView)findViewById(R.id.surface_camera);
    	selectPic = (Button)findViewById(R.id.select_pic);
    	inputCode = (Button)findViewById(R.id.input_code);
    	mycard = (ImageButton)findViewById(R.id.mycard);
    	more = (ImageButton)findViewById(R.id.more);
    	
    	selectPic.setOnClickListener(click);
    	inputCode.setOnClickListener(click);
    	mycard.setOnClickListener(click);
    	more.setOnClickListener(click);
    }
	
	View.OnClickListener click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.select_pic:
				
				break;
			case R.id.input_code:
				Intent input = new Intent(Barcode.this,InputCode.class);
				startActivity(input);
				break;
			case R.id.mycard:
				Intent mycard = new Intent(Barcode.this,Mycard.class);
				startActivity(mycard);
				break;
			case R.id.more:
				Intent more = new Intent(Barcode.this,MoreList.class);
				startActivity(more);
				break;
			default:
				break;
			}
		}
	};
}