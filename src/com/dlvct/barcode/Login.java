package com.dlvct.barcode;

import java.util.Map;

import com.dlvct.utils.db.DataHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private Button submitBtn,regBtn;
	private EditText username,password;
	private CheckBox autoLogin;
	private boolean isChecked = false;
	private DataHelper dataHelper;
	private Handler handler;
	private Map<String,String> setting;
	private Map<String,String> user;
	
	private final int LOAD_SETTING = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		dataHelper = new DataHelper(this);
		initView();
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case LOAD_SETTING:
					if ("1".equals(setting.get("AUTO_LOGIN"))) {
						isChecked = true;
						user = dataHelper.getUser(setting.get("USERID"));
						username.setText(user.get("USERNAME"));
						password.setText(user.get("PASSWORD"));
					}
					autoLogin.setChecked(isChecked);
					break;
				default:
					break;
				}
			}
		};
		loadPreferences.start();
	}
	
	private void initView(){
		submitBtn = (Button) findViewById(R.id.login_submit);
		regBtn = (Button) findViewById(R.id.login_reg);
		username = (EditText) findViewById(R.id.login_username);
		password = (EditText) findViewById(R.id.login_password);
		autoLogin = (CheckBox) findViewById(R.id.login_auto_checkbox);
		

		submitBtn.setOnClickListener(click);
		autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Login.this.isChecked = isChecked;
			}
		});
	}
	
	View.OnClickListener click = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.login_submit:
				String name = username.getText().toString();
				String pass = password.getText().toString();
				if ("".equals(name)) {
					Toast.makeText(Login.this, "请输入用户名", Toast.LENGTH_SHORT)
							.show();
					return;
				} else if ("".equals(pass)) {
					Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				int userid = dataHelper.checkLogin(name, pass);
				if (userid < 1) {
					Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
				dataHelper.saveSetting(userid, isChecked ? 1 : 0);
				dataHelper.Close();
				Intent submit = new Intent(Login.this, Barcode.class);
				startActivity(submit);
				finish();
				break;

			default:
				break;
			}
		}
	};
	
	Thread loadPreferences = new Thread(){
		public void run() {
			setting = dataHelper.getStetting();
			handler.sendEmptyMessage(LOAD_SETTING);
		};
	};
	
	protected void onDestroy() {
		super.onDestroy();
		dataHelper.Close();
	};
}
