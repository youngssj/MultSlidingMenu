package multslidingmenu.victor.com.multslidingmenu;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import multslidingmenu.victor.com.multslidingmenu.fragment.ContentFragment;
import multslidingmenu.victor.com.multslidingmenu.fragment.HintFragment;
import multslidingmenu.victor.com.multslidingmenu.fragment.LeftFragment;
import multslidingmenu.victor.com.multslidingmenu.fragment.LeftSecondFragment;
import multslidingmenu.victor.com.multslidingmenu.view.MultSlidingMenu;


public class MainActivity extends FragmentActivity implements MultSlidingMenu.OnPageEdgeListener{

    private MultSlidingMenu mSlidingMenu;
    private LeftFragment leftFragment;
    private HintFragment hintFragment;
    private LeftSecondFragment leftSecondFragment;
    private ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * 初始化slidingmenu，在其中加载各个界面
     */
    private void init() {
        mSlidingMenu = (MultSlidingMenu) findViewById(R.id.slidingMenu);
        boolean isVartical = false;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isVartical = false;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isVartical = true;
        }

        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        mSlidingMenu.setConfigChanged(screenWidth, screenHeight, isVartical);
		/* 导入左滑第二个菜单布局 */
        mSlidingMenu.setSecondLeftView(getLayoutInflater().inflate(
                R.layout.left_second_frame, null));
		/* 导入左滑第一个菜单布局 */
        mSlidingMenu.setLeftView(getLayoutInflater().inflate(
                R.layout.left_frame, null));
        /* 设置 */
        mSlidingMenu.setHintView(getLayoutInflater().inflate(
                R.layout.hint_frame, null));
        mSlidingMenu.setCenterView(getLayoutInflater().inflate(
                R.layout.center_frame, null));

        FragmentTransaction t = this.getSupportFragmentManager()
                .beginTransaction();

        leftSecondFragment = new LeftSecondFragment();
        t.replace(R.id.left_second_frame, leftSecondFragment);

        leftFragment = new LeftFragment();
        t.replace(R.id.left_frame, leftFragment);

        hintFragment = new HintFragment();
        t.replace(R.id.hint_frame, hintFragment);

        contentFragment = new ContentFragment();
        t.replace(R.id.center_frame, contentFragment);
        t.commit();

        contentFragment.setSlidingMenu(mSlidingMenu);

        leftFragment.setScreenWidth(screenWidth);

        mSlidingMenu.setContentFragment(contentFragment);

        mSlidingMenu.setOnScrollListener(new MultSlidingMenu.OnScrollListener() {
            @Override
            public void onScroll(boolean isShowContent) {
                if(isShowContent){
                    Log.i("log", "显示内容了");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSlidingMenu != null && !mSlidingMenu.isShowContent()) {
            mSlidingMenu.smoothScrollToRight();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onGoTop() {
        Toast.makeText(this, "向上跳转", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoBottom() {
        Toast.makeText(this, "向下跳转", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeTextForLast(int state) {
        switch (state) {
            case 0:
                hintFragment.setText("继续拖动跳到上一页");
                break;
            case 1:
                hintFragment.setText("松开手指跳到上一页");
                break;
        }
    }

    @Override
    public void changeTextForNext(int state) {
        switch (state) {
            case 0:
                hintFragment.setText("继续拖动跳到下一页");
                break;
            case 1:
                hintFragment.setText("松开手指跳到下一页");
                break;
        }
    }
}
