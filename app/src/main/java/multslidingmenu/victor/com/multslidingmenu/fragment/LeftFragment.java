/*
 * Copyright (C) 2012 victor
 */
package multslidingmenu.victor.com.multslidingmenu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import multslidingmenu.victor.com.multslidingmenu.R;

public class LeftFragment extends Fragment {
	
	private View view;
	private int screenWidth;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.left, null);
		if(screenWidth != 0){
			initViews(screenWidth, view);
		}
		return view;
	}

	private void initViews(int screenWidth, View view) {
		LinearLayout llContent = (LinearLayout) view.findViewById(R.id.llContent);
		LayoutParams layoutParams = (LayoutParams) llContent.getLayoutParams();
		layoutParams.width = screenWidth/2;
		llContent.setLayoutParams(layoutParams);
	}
	
	public void setScreenWidth(int screenWidth){
		if(view != null){
			initViews(screenWidth, view);
		}else{
			this.screenWidth = screenWidth;
		}
	}
}
