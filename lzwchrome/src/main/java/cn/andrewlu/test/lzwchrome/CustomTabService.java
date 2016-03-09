package cn.andrewlu.test.lzwchrome;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.customtabs.ICustomTabsCallback;
import android.support.customtabs.ICustomTabsService;
import android.util.Log;

import java.util.List;

/**
 * @author andrewlu
 *         用于处理CustomTab需要的远程服务处理类.
 *         实际任务交给CustomServiceBinder类来处理.
 */

public class CustomTabService extends Service {

    private CustomServiceBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("CustomTabService", "onCreate");
        binder = new CustomServiceBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("CustomTabService", "onCreate");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        Log.e("CustomTabService", "onCreate");
        return false;

    }
}
