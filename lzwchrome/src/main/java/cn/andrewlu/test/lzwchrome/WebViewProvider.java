package cn.andrewlu.test.lzwchrome;

import android.os.Looper;
import android.util.Log;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import org.xutils.common.TaskController;
import org.xutils.x;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Administrator on 2016/3/15.
 */
public class WebViewProvider {
    //保存经过预加载的网页内容.
    private Hashtable<String, WebView> cache = new Hashtable<>();
    //保存销毁不用的网页内容,以便预加载时直接取用view.而不用new一个.
    private Queue<WebView> draft = new LinkedList<>();
    private static WebViewProvider provider = new WebViewProvider();

    public static WebViewProvider getInstance() {
        return provider;
    }

    //提供预热功能.实际理解为提前创建好几个可用的WebView,减少在需要时才创建的时间消耗.
    public void warmup(long flag) {
        if (flag >= 5) flag = 5;
        if (flag <= 0) flag = 2;
        final long size = flag;
        if (draft.size() > 0) return;
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                for (int i = 0; i < size; i++) {
                    WebView webView = new WebView(App.getApplication());
                    setupWebView(webView);
                    draft.add(webView);
                }
                Log.i("Warmup", (System.currentTimeMillis() - time) + "ms");
            }
        });
    }

    //提供给服务用于预加载一个网页.并添加进预加载队列中去.
    public void mayLaunchUrl(final String url) {
        //如果已经预加载过了,就不再加载了.
        if (cache.get(url) != null) return;
        if (draft.size() > 0) {
            x.task().autoPost(new Runnable() {
                @Override
                public void run() {
                    WebView draftWebView = draft.poll();
                    draftWebView.loadUrl(url);
                    cache.put(url, draftWebView);
                }
            });
        } else {
            createWebViewInner(url, new WebViewCreator() {
                @Override
                public void onWebViewCreated(WebView webView) {
                    cache.put(url, webView);
                }
            });
        }
    }

    private void createWebViewInner(final String url, final WebViewCreator webViewCreator) {
        TaskController taskController = x.task();
        taskController.autoPost(new Runnable() {
            @Override
            public void run() {
                WebView webView = new WebView(App.getApplication());
                setupWebView(webView);
                webView.loadUrl(url);
                if (webViewCreator != null) webViewCreator.onWebViewCreated(webView);
            }
        });
    }

//    private boolean isUiThread() {
//        return Looper.myLooper() == Looper.getMainLooper();
//    }

    private void setupWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("WebView.mayLaunch_OK", url);
            }
        });
    }

    //用于给Activity想要使用WebView时调用.
    public void createWebView(final String url, WebViewCreator creator) {
        if (creator == null) return;
        //如果有预加载的内容,就直接使用,否则就新造一个WebView供使用.
        WebView webView = cache.get(url);
        if (webView != null) {
            cache.remove(url);
            creator.onWebViewCreated(webView);
            return;
        }
        createWebViewInner(url, creator);
    }

    //用于Activity销毁时回收不用的webView;
    public void recycleWebView(WebView webView) {
        if (webView == null) return;
        if (webView.getParent() != null) {
            ViewParent parent = webView.getParent();
            FrameLayout realParent = (FrameLayout) parent;
            realParent.removeView(webView);
        }
        draft.add(webView);
    }

    public interface WebViewCreator {
        void onWebViewCreated(WebView webView);
    }
}
