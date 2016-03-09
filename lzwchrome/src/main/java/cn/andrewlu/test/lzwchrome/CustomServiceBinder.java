package cn.andrewlu.test.lzwchrome;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.customtabs.ICustomTabsCallback;
import android.support.customtabs.ICustomTabsService;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2016/3/9.
 * 用于实际处理CustomTab业务的位置.
 */
public class CustomServiceBinder extends ICustomTabsService.Stub {

    @Override
    public boolean warmup(long flags) throws RemoteException {
        Log.e("CustomServiceBinder", "warmup");
        return false;
    }

    @Override
    public boolean newSession(ICustomTabsCallback callback) throws RemoteException {
        Log.e("CustomServiceBinder", "newSession");
        return false;
    }

    @Override
    public boolean mayLaunchUrl(ICustomTabsCallback callback, Uri url, Bundle extras, List<Bundle> otherLikelyBundles) throws RemoteException {
        Log.e("CustomServiceBinder", "mayLaunchUrl");
        return false;
    }

    @Override
    public Bundle extraCommand(String commandName, Bundle args) throws RemoteException {
        Log.e("CustomServiceBinder", "extraCommand");
        return null;
    }

    @Override
    public boolean updateVisuals(ICustomTabsCallback callback, Bundle bundle) throws RemoteException {
        Log.e("CustomServiceBinder", "updateVisuals");
        return false;
    }
}
