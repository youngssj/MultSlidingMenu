package multslidingmenu.victor.com.multslidingmenu.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import multslidingmenu.victor.com.multslidingmenu.view.MyWebView;
import multslidingmenu.victor.com.multslidingmenu.R;
import multslidingmenu.victor.com.multslidingmenu.view.MultSlidingMenu;

public class ContentFragment extends Fragment implements View.OnClickListener{

	private RelativeLayout llWVs;
	private MyWebView mWebView;
	private Button btnOpenMenu;
	private MultSlidingMenu mSlidingMenu;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.content_fragment, null);
		initViews(view);
		return view;
	}
	
	public MyWebView getWebView(){
		return mWebView;
	}

	private void initViews(View view) {
		llWVs = (RelativeLayout) view.findViewById(R.id.llWVs);
		btnOpenMenu = (Button) view.findViewById(R.id.btnOpenMenu);
		btnOpenMenu.setOnClickListener(this);
		addWebView();
	}
	
	/**
	 * 给页面添加新的webview
	 */
	public void addWebView() {
		if(mWebView != null){
			mWebView.destroy();
		}
		mWebView = new MyWebView(getActivity());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		mWebView.setLayoutParams(lp);
		llWVs.addView(mWebView, lp);
		setWebView(mWebView);
		mWebView.loadUrl("http://www.baidu.com");
	}

	/**
	 * 为webview设置参数
	 * @param mWebView
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void setWebView(final MyWebView mWebView){
		mWebView.setInitialScale(100);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setDomStorageEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(final WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});
	}
	
	@Override
	public void onResume() {
		mWebView.onResume();
		super.onResume();
	}
	
	@Override
	public void onPause() {
		mWebView.onPause();
		super.onPause();
	}

	public void setSlidingMenu(MultSlidingMenu mSlidingMenu){
		this.mSlidingMenu = mSlidingMenu;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnOpenMenu:
				mSlidingMenu.smoothScrollToLeft();
				break;
		}
	}
}
