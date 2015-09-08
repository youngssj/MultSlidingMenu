package multslidingmenu.victor.com.multslidingmenu.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import multslidingmenu.victor.com.multslidingmenu.R;

public class HintFragment extends Fragment {
	
	private TextView tv;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.hint_fragment, null);
		initViews(view);
		return view;
	}
	
	private void initViews(View view) {
		tv = (TextView) view.findViewById(R.id.tv);
	}
	
	public void setText(String text){
		tv.setText(text);
	}
}
