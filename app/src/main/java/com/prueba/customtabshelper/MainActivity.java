package com.prueba.customtabshelper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.chromium.customtabsclient.CustomTabsActivityHelper;

import me.zhanghai.android.customtabshelper.CustomTabsHelperFragment;

public class MainActivity extends AppCompatActivity {

    private static final Uri PROJECT_URI = Uri.parse(
            "https://android.com");

    private final CustomTabsActivityHelper.CustomTabsFallback mCustomTabsFallback =
            new CustomTabsActivityHelper.CustomTabsFallback() {
                @Override
                public void openUri(Activity activity, Uri uri) {
                    Toast.makeText(activity, "Custom tab failed", Toast.LENGTH_SHORT)
                            .show();
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "Activity not found", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            };

    private CustomTabsHelperFragment mCustomTabsHelperFragment;
    private CustomTabsIntent mCustomTabsIntent;
    private Button btnURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnURL = (Button)findViewById(R.id.view_on_github);

        mCustomTabsHelperFragment = CustomTabsHelperFragment.attachTo(this);
        mCustomTabsIntent = new CustomTabsIntent.Builder()
                .enableUrlBarHiding()
                .setToolbarColor(getResources().getColor(R.color.colorPrimary))
                .setShowTitle(true)
                .build();

        mCustomTabsHelperFragment.setConnectionCallback(
                new CustomTabsActivityHelper.ConnectionCallback() {
                    @Override
                    public void onCustomTabsConnected() {
                        mCustomTabsHelperFragment.mayLaunchUrl(PROJECT_URI, null, null);
                    }
                    @Override
                    public void onCustomTabsDisconnected() {}
                });
        btnURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsHelperFragment.open(MainActivity.this, mCustomTabsIntent, PROJECT_URI,
                        mCustomTabsFallback);
            }
        });
    }
}

