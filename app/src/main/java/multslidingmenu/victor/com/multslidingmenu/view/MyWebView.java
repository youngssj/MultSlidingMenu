package multslidingmenu.victor.com.multslidingmenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebView extends WebView {

	public MyWebView(Context context) {
		super(context);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private boolean canMove;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(canMove){
			return super.onTouchEvent(event);
		}
		return true;
	}
	
	public void setCanMove(boolean canMove){
		this.canMove = canMove;
	}
}
