package cn.andrewlu.test.lzwchrome;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import cn.andrewlu.test.lzwchrome.entities.CustomTabsProperties;

/**
 * Created by Andrewlu.lzw on 2016/3/8.
 * 实现通过外部传入参数控制页面标题栏显示样式功能.
 */
public class ChromeActivity extends AppCompatActivity {
    private FrameLayout webViewContainer;
    private Toolbar toolBar;
    private CustomTabsProperties properties;
    private CustomTabsCallback callback;
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        webViewContainer = (FrameLayout) findViewById(R.id.webViewContainer);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        properties = CustomTabsProperties.getFromIntent(getIntent());
        if (properties.getToken() != null) {
            callback = properties.getToken().getCallback();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置返回按钮事件.
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置页面进入/退出时的动画效果.
                //overridePendingTransition(0,0);
                finish();
                callback.onNavigationEvent(CustomTabsCallback.NAVIGATION_ABORTED, new Bundle());
            }
        });

        setUpActionBar();

        //加载网页.
        if (properties.getData() != null) {
            final String url = properties.getData().toString();
            WebViewProvider.getInstance().createWebView(url, new WebViewProvider.WebViewCreator() {
                @Override
                public void onWebViewCreated(WebView webView) {
                    mWebView = webView;
                    boolean isShowTitle = properties.isShowTitle();
                    if (isShowTitle) {
                        //获取到网页的标题信息.
                        toolBar.setTitle(mWebView.getTitle());
                        toolBar.setSubtitle(properties.getData().toString());
                    } else {
                        toolBar.setTitle(properties.getData().toString());
                        toolBar.setSubtitle(null);
                    }
                    webViewContainer.addView(mWebView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                }
            });
        }
    }

    public void onDestroy() {
        super.onDestroy();
        WebViewProvider.getInstance().recycleWebView(mWebView);
    }


    //设置页面效果.
    private void setUpActionBar() {
        int color = properties.getToolbarColor();
        Bitmap closeBtnIcon = properties.getCloseButtonIcon();
        toolBar.setBackgroundColor(color);
        if (closeBtnIcon != null) {
            toolBar.setNavigationIcon(new BitmapDrawable(getResources(), closeBtnIcon));
        }

        //设置菜单.
        if (properties.getMenuItems() != null && !properties.getMenuItems().isEmpty()) {
            //toolBar.inflateMenu(R.menu.menu_more);
            toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(ChromeActivity.this, "xxxxx", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_more, menu);
        if (properties.getMenuItems() != null) {
            // int i = 0;
            for (Bundle bundle : properties.getMenuItems()) {
                MenuItem item = menu.add(bundle.getString(CustomTabsIntent.KEY_MENU_ITEM_TITLE));
//                PendingIntent intent = bundle.getParcelable(CustomTabsIntent.KEY_PENDING_INTENT);
//                item.setIntent(intent);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
