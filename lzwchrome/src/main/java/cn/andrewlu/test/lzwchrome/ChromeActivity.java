package cn.andrewlu.test.lzwchrome;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Andrewlu.lzw on 2016/3/8.
 * 实现通过外部传入参数控制页面标题栏显示样式功能.
 */
public class ChromeActivity extends AppCompatActivity {
    private SwipeRefreshLayout refreshLayout;
    private WebView webView;
    private Toolbar toolBar;
    private CustomTabsProperties properties;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        webView = (WebView) findViewById(R.id.webView);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        setSupportActionBar(toolBar);
        properties = CustomTabsProperties.getFromIntent(getIntent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                refreshLayout.setRefreshing(false);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                //设置是否显示标题内容.
                boolean isShowTitle = properties.isShowTitle();
                if (isShowTitle) {
                    //获取到网页的标题信息.
                    toolBar.setTitle(title);
                    toolBar.setSubtitle(properties.getData().toString());
                } else {
                    toolBar.setTitle(properties.getData().toString());
                    toolBar.setSubtitle(null);
                }
            }
        });

        //设置返回按钮事件.
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置页面进入/退出时的动画效果.
                //overridePendingTransition(0,0);
                finish();
            }
        });

        //设置下拉刷新webView.
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUrl();
            }
        });
        loadUrl();
        setUpActionBar();
    }


    //加载网页.
    private void loadUrl() {
        if (properties.getData() != null) {
            String url = properties.getData().toString();
            webView.loadUrl(url);
        }
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
