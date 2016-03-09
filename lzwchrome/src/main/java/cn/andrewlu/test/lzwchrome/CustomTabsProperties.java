package cn.andrewlu.test.lzwchrome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.ColorInt;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/9.
 * 通过Intent提取可控参数内容.
 */
public class CustomTabsProperties {

    public static CustomTabsProperties getFromIntent(Intent intent) {
        return new CustomTabsProperties(intent);
    }

    private CustomTabsProperties(Intent intent) {
        buildProperties(intent);
    }

    public Bundle getSession() {
        return session;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public int getSecondaryToolbarColor() {
        return secondaryToolbarColor;
    }

    public boolean isEnableUrlBarHiding() {
        return enableUrlBarHiding;
    }

    public Bitmap getCloseButtonIcon() {
        return closeButtonIcon;
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public ArrayList<Bundle> getMenuItems() {
        return menuItems;
    }

    public Bundle getExitAnimationBundle() {
        return exitAnimationBundle;
    }

    public ArrayList<Bundle> getActionButtons() {
        return actionButtons;
    }

    public boolean isShowDefaultShareMenuItem() {
        return showDefaultShareMenuItem;
    }

    public boolean isTintActionBar() {
        return tintActionBar;
    }

    public Bundle getActionBarBundle() {
        return actionBarBundle;
    }

    public Uri getData() {
        return data;
    }


    private void buildProperties(Intent intent) {
        if (intent == null) return;
        this.data = intent.getData();
        this.session = intent.getBundleExtra(CustomTabsIntent.EXTRA_SESSION);
        this.toolbarColor = intent.getIntExtra(CustomTabsIntent.EXTRA_TOOLBAR_COLOR, Color.BLUE);
        this.secondaryToolbarColor = intent.getIntExtra(CustomTabsIntent.EXTRA_SECONDARY_TOOLBAR_COLOR, Color.WHITE);
        this.enableUrlBarHiding = intent.getBooleanExtra(CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING, false);
        this.closeButtonIcon = intent.getParcelableExtra(CustomTabsIntent.EXTRA_CLOSE_BUTTON_ICON);
        this.showTitle = intent.getIntExtra(CustomTabsIntent.EXTRA_TITLE_VISIBILITY_STATE, 0) == 1 ? true : false;
        this.menuItems = intent.getParcelableArrayListExtra(CustomTabsIntent.EXTRA_MENU_ITEMS);
        this.actionButtons = intent.getParcelableArrayListExtra(CustomTabsIntent.EXTRA_TOOLBAR_ITEMS);
        this.exitAnimationBundle = intent.getBundleExtra(CustomTabsIntent.EXTRA_EXIT_ANIMATION_BUNDLE);
        this.showDefaultShareMenuItem = intent.getBooleanExtra(CustomTabsIntent.EXTRA_DEFAULT_SHARE_MENU_ITEM, false);
        this.tintActionBar = intent.getBooleanExtra(CustomTabsIntent.EXTRA_TINT_ACTION_BUTTON, false);
        this.actionBarBundle = intent.getBundleExtra(CustomTabsIntent.EXTRA_ACTION_BUTTON_BUNDLE);
    }

    private Bundle session;
    private int toolbarColor;
    private int secondaryToolbarColor;
    private boolean enableUrlBarHiding;
    private Bitmap closeButtonIcon;
    private boolean showTitle;
    ArrayList<Bundle> menuItems;
    // private Bundle startAnimationBundle;
    private Bundle exitAnimationBundle;
    private ArrayList<Bundle> actionButtons;
    private boolean showDefaultShareMenuItem;
    private boolean tintActionBar;
    private Bundle actionBarBundle;
    private Uri data;

}
