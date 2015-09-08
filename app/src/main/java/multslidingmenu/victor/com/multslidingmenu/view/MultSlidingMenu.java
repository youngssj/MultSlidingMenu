/*
 * Copyright (C) 2014 victor
 */
package multslidingmenu.victor.com.multslidingmenu.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import multslidingmenu.victor.com.multslidingmenu.R;
import multslidingmenu.victor.com.multslidingmenu.fragment.ContentFragment;

public class MultSlidingMenu extends RelativeLayout {

	private View mSlidingView;
	private View mMenuView;
	private View mSecondMenuView;
	private View mHintView;
	private ContentFragment contentFragment;
	private ImageView iv1;
	private ImageView iv2;
	private int screenWidth;
	private int screenHeight;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private final int VELOCITY = 50;
	private boolean mIsBeingDragged = true;
	private boolean mIsVartical = false;
	private int mSecondMenuWidth;
	private int mMenuWidth;
	private boolean isVartical = true;
	private boolean isShowContent = true;
	private boolean canDragVartical = true;
	private boolean isToLefting = true;
	private int count = 0;
	private int DRAGDISTANCE = 100;
	private OnPageEdgeListener mCallback;

	public MultSlidingMenu(Context context) {
		super(context);
		init();
		initCallback(context);
	}

	public MultSlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		initCallback(context);
	}

	public MultSlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		initCallback(context);
	}

	public void setContentFragment(ContentFragment contentFragment){
		this.contentFragment = contentFragment;
	}

	/**
	 * 初始化触摸事件的回调函数
	 * @param context
	 */
	private void initCallback(Context context) {
		try {
			mCallback = (OnPageEdgeListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	private void init() {
		changeShowState(true);
		canDragVartical = true;
		mScroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	private void changeShowState(boolean isShowContent){
		this.isShowContent = isShowContent;
		if(onScrollListener != null) {
			onScrollListener.onScroll(isShowContent);
		}
	}

	/**
	 * 设置左边滑出的第一个界面
	 * @param view
	 */
	public void setLeftView(View view) {
		mMenuWidth = screenWidth/2;
		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(view, behindParams);
		mMenuView = view;

		// 设置界面上方的黑色遮挡层
		iv2 = new ImageView(getContext());
		iv2.setBackgroundColor(getContext().getResources().getColor(R.color.pure_black));
		LayoutParams bgParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		iv2.setLayoutParams(bgParams);
		addView(iv2, bgParams);
	}

	/**
	 * 设置提示界面
	 * @param view
	 */
	public void setHintView(View view){
		LayoutParams bgParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(view, bgParams);
		mHintView = view;
	}

	/**
	 * 设置左边滑出的第二个界面
	 * @param view
	 */
	public void setSecondLeftView(View view){
		LayoutParams behind2Params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mSecondMenuWidth = screenWidth/2;
		behind2Params.width = mSecondMenuWidth;
		addView(view, behind2Params);
		mSecondMenuView = view;

		// 设置界面上方的黑色遮挡层
		iv1 = new ImageView(getContext());
		iv1.setBackgroundColor(getContext().getResources().getColor(R.color.pure_black));
		LayoutParams bgParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		iv1.setLayoutParams(bgParams);
		addView(iv1, bgParams);
	}

	/**
	 * 设置中间内容显示的界面
	 * @param view
	 */
	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		addView(view, aboveParams);
		mSlidingView = view;
		mSlidingView.bringToFront();
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	private boolean canScroll = true;

	/**
	 * 拦截touch事件
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				if(!mScroller.computeScrollOffset()){
					mLastMotionX = x;
					mLastMotionY = y;
					mIsBeingDragged = false;
					mIsVartical = true;
					count = 0;
					mMenuView.setVisibility(View.VISIBLE);
					mSecondMenuView.setVisibility(View.VISIBLE);
					canScroll = true;
				}else{
					canScroll = false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(!mScroller.computeScrollOffset()){
					final float dx = x - mLastMotionX;
					final float xDiff = Math.abs(dx);
					final float yDiff = Math.abs(y - mLastMotionY);
					if (xDiff > mTouchSlop && xDiff > yDiff) {
						float oldScrollX = mSlidingView.getScrollX();
						if (oldScrollX < 0) {
							if(count == 0){
								mIsBeingDragged = true;
								mIsVartical = false;
								count ++;
							}
							mLastMotionX = x;
						} else {
							if (dx > 0) {
								if(count == 0){
									mIsBeingDragged = true;
									mIsVartical = false;
									count ++;
								}
								mLastMotionX = x;
							}
						}
					}else if(yDiff > mTouchSlop && yDiff > xDiff){
						// TODO 网页上下移动
						if(count == 0){
							mIsBeingDragged = false;
							mIsVartical = true;
							count ++;
						}
						if(mIsVartical && canDragVartical){
							final float deltaY = mLastMotionY - y;//手指偏移量
							mLastMotionY = y;
							float oldScrollY = mSlidingView.getScrollY();
							float scrollY = oldScrollY + deltaY/2;

							WebView mWebView = contentFragment.getWebView();
							if(scrollY < 0){
								if (mWebView.getScrollY() == 0) {
									// 滑动到顶部，你要做的事····
									contentFragment.getWebView().setCanMove(false);
									int distanceY = Math.abs((int) scrollY);
									if(distanceY < DRAGDISTANCE){
										mCallback.changeTextForLast(0);
									}else{
										mCallback.changeTextForLast(1);
									}
									mHintView.setVisibility(View.VISIBLE);
									LayoutParams hintParams = new LayoutParams(isVartical?screenWidth:screenHeight, distanceY+2);
									hintParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
									mHintView.setLayoutParams(hintParams);
									mSlidingView.scrollTo(mSlidingView.getScrollX(), (int) scrollY);
								}
							}else{
								if ((int) (mWebView.getContentHeight() * mWebView.getScale()) <= (mWebView.getHeight() + mWebView.getScrollY())) {
									// 滑动到底部，你要做的事·····
									contentFragment.getWebView().setCanMove(false);
									int distanceY = Math.abs((int) scrollY);
									if(distanceY < DRAGDISTANCE){
										mCallback.changeTextForNext(0);
									}else{
										mCallback.changeTextForNext(1);
									}
									mHintView.setVisibility(View.VISIBLE);
									LayoutParams hintParams = new LayoutParams(isVartical?screenWidth:screenHeight, distanceY+2);
									hintParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
									mHintView.setLayoutParams(hintParams);
									mSlidingView.scrollTo(mSlidingView.getScrollX(), (int) scrollY);
								}
							}
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if(!mScroller.computeScrollOffset()){
					contentFragment.getWebView().setCanMove(true);
					int scrollY = mSlidingView.getScrollY();
					if(mIsVartical && canDragVartical){
						smoothScrollTo(-scrollY);
					}
					if(scrollY > DRAGDISTANCE){
						mCallback.onGoBottom();
					}else if(scrollY < -DRAGDISTANCE) {
						mCallback.onGoTop();
					}
				}
				break;
		}
		return mIsBeingDragged;
	}

	private OnScrollListener onScrollListener;

	public interface OnPageEdgeListener {
		void onGoTop();
		void onGoBottom();
		void changeTextForLast(int state);
		void changeTextForNext(int state);
	}

	public interface OnScrollListener{
		void onScroll(boolean isShowContent);
	}

	/**
	 * 设置滑动状态，检测是否是显示内容页状态
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener){
		this.onScrollListener = onScrollListener;
	}

	/*处理拦截后的touch事件*/
	@SuppressLint("Recycle")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				if(!mScroller.computeScrollOffset()){
					mLastMotionX = x;
					mLastMotionY = y;
					if (mSlidingView.getScrollX() == -screenWidth
							&& mLastMotionX < screenWidth) {
						return false;
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(!mScroller.computeScrollOffset() && canScroll){
					if (mIsBeingDragged) {
						mHintView.setVisibility(View.GONE);
						final float deltaX = mLastMotionX - x;//手指偏移量
						mLastMotionX = x;
						float oldScrollX = mSlidingView.getScrollX();
						float scrollX = oldScrollX + deltaX;
						if (scrollX > 0)
							scrollX = 0;
						if (deltaX < 0 && oldScrollX < 0) { // left view
							final float leftBound = 0;
							final float rightBound = -screenWidth;
							if (scrollX > leftBound) {
								scrollX = leftBound;
							} else if (scrollX < rightBound) {
								scrollX = rightBound;
							}
						}
						if (mSlidingView != null) {
							mSlidingView.scrollTo((int) scrollX,
									mSlidingView.getScrollY());

							// 左二透明度变化
							float percent1 = 0f;
							if(Math.abs(scrollX) >= Math.abs((screenWidth) - mSecondMenuWidth)){
								percent1 = Math.abs((Math.abs(scrollX) - (screenWidth)))/mSecondMenuWidth;
							}else {
								percent1 = 1;
							}
							setAlpha(iv1, percent1);

							float percent2 = 0f;
							if(Math.abs(scrollX) > 0 && Math.abs(scrollX) <= mMenuWidth){
								percent2 = (mMenuWidth - Math.abs(scrollX))/mMenuWidth;
							}
							setAlpha(iv2, percent2);

							if(Math.abs(scrollX) > mMenuWidth){
								mMenuView.scrollTo((int)scrollX + mMenuWidth, mSlidingView.getScrollY());
							}else{
								mMenuView.scrollTo(0, mSlidingView.getScrollY());
							}
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if(!mScroller.computeScrollOffset() && canScroll){
					if (mIsBeingDragged) {
						//速度跟踪
						final VelocityTracker velocityTracker = mVelocityTracker;
						velocityTracker.computeCurrentVelocity(100);
						float xVelocity = velocityTracker.getXVelocity();// 滑动的速度
						int oldScrollX = mSlidingView.getScrollX();

						if (oldScrollX <= 0) {
							if (xVelocity > VELOCITY) {
								canDragVartical = false;
								if(oldScrollX > (-(getMenuViewWidth() - mSecondMenuWidth))){
									dx = -(getMenuViewWidth() - mSecondMenuWidth) - oldScrollX;// noshow
								}else{
									dx = -getMenuViewWidth() - oldScrollX;// noshow
								}
							} else if (xVelocity < -VELOCITY) {
								if(oldScrollX < (-(getMenuViewWidth() - mSecondMenuWidth))){
									canDragVartical = false;
									dx = -(oldScrollX + (getMenuViewWidth() - mSecondMenuWidth));// noshow
								}else{
									canDragVartical = true;
									dx = -oldScrollX; // show
								}
							} else if (oldScrollX < (-getMenuViewWidth()) / 2) {
								canDragVartical = false;
								if(oldScrollX > (-(getMenuViewWidth() - mSecondMenuWidth))){
									dx = -(getMenuViewWidth() - mSecondMenuWidth) - oldScrollX;// noshow
								}else{
									dx = -getMenuViewWidth() - oldScrollX;// noshow
								}
							} else if (oldScrollX >= (-getMenuViewWidth()) / 2) {
								if(oldScrollX < (-(getMenuViewWidth() - mSecondMenuWidth))){
									canDragVartical = false;
									dx = -(oldScrollX + (getMenuViewWidth() - mSecondMenuWidth));// noshow
								}else{
									canDragVartical = true;
									dx = -oldScrollX; // show
								}
							}
						}
						smoothScrollTo(dx);
					}
				}
				break;
		}
		return true;
	}

	/**
	 * 设置遮挡的透明度
	 * @param view
	 * @param percent
	 */
	private void setAlpha(ImageView view, float percent){
		int alpha = (int)(255*percent);
		if(alpha > 255){
			alpha = 255;
		}
		if(alpha < 0){
			alpha = 0;
		}
		view.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
	}

	/**
	 * 获取左边第一栏的宽度
	 * @return
	 */
	private int getMenuViewWidth() {
		if (mMenuView == null) {
			return 0;
		}
		int width = 0;
		if(isVartical){
			width = mMenuView.getWidth();
		}else{
			width = mMenuView.getWidth();
		}
		return width;
	}

	int dx = 0;

	/**
	 * 自动滑动到目标位置
	 * @param delta
	 */
	public void smoothScrollTo(int delta) {
		int duration = 300;
		int oldScrollX = mSlidingView.getScrollX();
		int oldScrollY = mSlidingView.getScrollY();
		if(!mScroller.computeScrollOffset() || isToLefting){
			isToLefting = false;
			if(mIsVartical && canDragVartical){
				mScroller.startScroll(oldScrollX, oldScrollY, oldScrollX,
						delta, duration);
			}else{
				mScroller.startScroll(oldScrollX, oldScrollY, delta,
						oldScrollY, duration);
			}
			invalidate();
		}
	}

	/**
	 * 自动将页面滑动到最右边
	 */
	public void smoothScrollToRight(){
		mIsVartical = false;
		changeShowState(true);
		canDragVartical = true;
		int oldX = mSlidingView.getScrollX();
		if(oldX == -(getMenuViewWidth() - mSecondMenuWidth)){
			dx = getMenuViewWidth() - mSecondMenuWidth;
		}else{
			dx = screenWidth;
		}
		smoothScrollTo(dx);
	}

	/**
	 * 自动将页面滑动到最左边
	 */
	public void smoothScrollToLeft(){
		changeShowState(false);
		canDragVartical = false;
		isToLefting = true;
		mHintView.setVisibility(View.GONE);
		int oldX = mSlidingView.getScrollX();
		if(oldX == -(getMenuViewWidth() - mSecondMenuWidth)){
			dx = -mSecondMenuWidth;
		}else if(oldX == -(getMenuViewWidth() - screenWidth)){
			dx = -screenWidth;
		}else{
			dx = screenWidth;
		}
		smoothScrollTo(dx);
	}

	/**
	 * 是否在显示网页内容 
	 * @return
	 */
	public boolean isShowContent(){
		return isShowContent;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldX = mSlidingView.getScrollX();
			int oldY = mSlidingView.getScrollY();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			if (oldX != x || oldY != y) {
				if (mSlidingView != null) {
					mSlidingView.scrollTo(x, y);

					float percent1 = 0f;
					if(Math.abs(x) > Math.abs((screenWidth) - mSecondMenuWidth)){
						// 左二透明度变化
						percent1 = Math.abs(((float) Math.abs(x) - (screenWidth)))/mSecondMenuWidth;
					}else {
						percent1 = 1;
					}
					setAlpha(iv1, percent1);

					float percent2 = 0f;
					if(Math.abs(x) > 0 && Math.abs(x) <= mMenuWidth){
						percent2 = (mMenuWidth - (float) Math.abs(x))/mMenuWidth;
					}
					setAlpha(iv2, percent2);

					int oldScrollX = mSlidingView.getScrollX();
					if(dx < 0){
						if(oldScrollX < (-(getMenuViewWidth() - mSecondMenuWidth))){
							changeShowState(false);
							if(x < (-1 * (mSecondMenuView.getWidth()-5))){
								mMenuView.scrollTo(x+mMenuWidth, y);
							}
						}else{
							changeShowState(false);
						}
					}else if(dx > 0){
						if(oldScrollX < (-(getMenuViewWidth() - mSecondMenuWidth))){
							changeShowState(false);
							mMenuView.scrollTo(x + (getMenuViewWidth() - mSecondMenuWidth), y);
						}else if(oldScrollX != (-(getMenuViewWidth() - mSecondMenuWidth))){
							changeShowState(true);
						}
					}
				}
			}
			invalidate();
		}
	}

	/**
	 * 设置当前屏幕状态
	 * @param screenWidth
	 * @param screenHeight
	 * @param isVartical
	 */
	public void setConfigChanged(int screenWidth, int screenHeight, boolean isVartical){
		this.isVartical = isVartical;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
}