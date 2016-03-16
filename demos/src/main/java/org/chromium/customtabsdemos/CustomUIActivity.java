// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.chromium.customtabsdemos;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.chromium.customtabsclient.shared.CustomTabActivityHelper;

/**
 * Opens Chrome Custom Tabs with a customized UI.
 */
public class CustomUIActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CustChromeTabActivity";
    private static final int TOOLBAR_ITEM_ID = 1;

    private EditText mUrlEditText;
    private EditText mCustomTabColorEditText;
    private EditText mCustomTabSecondaryColorEditText;
    private CheckBox mShowActionButtonCheckbox;
    private CheckBox mAddMenusCheckbox;
    private CheckBox mShowTitleCheckBox;
    private CheckBox mCustomBackButtonCheckBox;
    private CheckBox mAutoHideAppBarCheckbox;
    private CheckBox mAddDefaultShareCheckbox;
    private CheckBox mToolbarItemCheckbox;
    private CustomTabActivityHelper mCustomTabActivityHelper;
    private CustomTabsCallback mCallback = new CustomTabsCallback() {
        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            Log.i("CustomTabsCallback", navigationEvent + ":" + extras.toString());
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void extraCallback(String callbackName, Bundle args) {

            Log.i("CustomTabsCallback", callbackName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui);

        mCustomTabActivityHelper = new CustomTabActivityHelper();
        findViewById(R.id.start_custom_tab).setOnClickListener(this);

        mUrlEditText = (EditText) findViewById(R.id.url);
        mCustomTabColorEditText = (EditText) findViewById(R.id.custom_toolbar_color);
        mCustomTabSecondaryColorEditText =
                (EditText) findViewById(R.id.custom_toolbar_secondary_color);
        mShowActionButtonCheckbox = (CheckBox) findViewById(R.id.custom_show_action_button);
        mAddMenusCheckbox = (CheckBox) findViewById(R.id.custom_add_menus);
        mShowTitleCheckBox = (CheckBox) findViewById(R.id.show_title);
        mCustomBackButtonCheckBox = (CheckBox) findViewById(R.id.custom_back_button);
        mAutoHideAppBarCheckbox = (CheckBox) findViewById(R.id.auto_hide_checkbox);
        mAddDefaultShareCheckbox = (CheckBox) findViewById(R.id.add_default_share);
        mToolbarItemCheckbox = (CheckBox) findViewById(R.id.add_toolbar_item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.start_custom_tab:
                openCustomTab();
                break;
            default:
                //Unknown View Clicked
        }
    }

    private int getColor(EditText editText) {
        try {
            return Color.parseColor(editText.getText().toString());
        } catch (NumberFormatException ex) {
            Log.i(TAG, "Unable to parse Color: " + editText.getText());
            return Color.LTGRAY;
        }
    }

    private void openCustomTab() {
        String url = mUrlEditText.getText().toString();

        int color = getColor(mCustomTabColorEditText);
        int secondaryColor = getColor(mCustomTabSecondaryColorEditText);
        CustomTabsSession session = mCustomTabActivityHelper.createSession(mCallback);
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder(session);

        intentBuilder.setToolbarColor(color);
        intentBuilder.setSecondaryToolbarColor(secondaryColor);

        if (mShowActionButtonCheckbox.isChecked()) {
            //Generally you do not want to decode bitmaps in the UI thread. Decoding it in the
            //UI thread to keep the example short.
            String actionLabel = getString(R.string.label_action);
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    android.R.drawable.ic_menu_share);
            PendingIntent pendingIntent =
                    createPendingIntent(ActionBroadcastReceiver.ACTION_ACTION_BUTTON);
            intentBuilder.setActionButton(icon, actionLabel, pendingIntent);
        }

        if (mAddMenusCheckbox.isChecked()) {
            for (int i = 0; i < 5; i++) {
                String menuItemTitle = getString(R.string.menu_item_title) + i;
                PendingIntent menuItemPendingIntent =
                        createPendingIntent(ActionBroadcastReceiver.ACTION_MENU_ITEM);
                intentBuilder.addMenuItem(menuItemTitle, menuItemPendingIntent);
            }

        }

        if (mAddDefaultShareCheckbox.isChecked()) {
            intentBuilder.addDefaultShareMenuItem();
        }

        if (mToolbarItemCheckbox.isChecked()) {
            //Generally you do not want to decode bitmaps in the UI thread. Decoding it in the
            //UI thread to keep the example short.
            String actionLabel = getString(R.string.label_action);
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    android.R.drawable.ic_menu_share);
            PendingIntent pendingIntent =
                    createPendingIntent(ActionBroadcastReceiver.ACTION_TOOLBAR);
            intentBuilder.addToolbarItem(TOOLBAR_ITEM_ID, icon, actionLabel, pendingIntent);
        }

        intentBuilder.setShowTitle(mShowTitleCheckBox.isChecked());

        if (mAutoHideAppBarCheckbox.isChecked()) {
            intentBuilder.enableUrlBarHiding();
        }

        if (mCustomBackButtonCheckBox.isChecked()) {
            intentBuilder.setCloseButtonIcon(
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
        }

        //intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        CustomTabActivityHelper.openCustomTab(
                this, intentBuilder.build(), Uri.parse(url), null);
    }

    private PendingIntent createPendingIntent(int actionSourceId) {
        Intent actionIntent = new Intent(
                this.getApplicationContext(), ActionBroadcastReceiver.class);
        actionIntent.putExtra(ActionBroadcastReceiver.KEY_ACTION_SOURCE, actionSourceId);
        actionIntent.setData(Uri.parse("http://www.uc.cn"));
        return PendingIntent.getBroadcast(
                getApplicationContext(), actionSourceId, actionIntent, 0);
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(CustomUIActivity.this, "ChromeActivity向你发送一个回调", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
