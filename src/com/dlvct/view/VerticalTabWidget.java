package com.dlvct.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabWidget;

public class VerticalTabWidget extends TabWidget{
	
	public VerticalTabWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addView(View child) {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = new LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 0, 0);
		child.setLayoutParams(lp);
		super.addView(child);
	}

}
