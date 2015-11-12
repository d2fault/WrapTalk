package com.wraptalk.wraptalk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.db.SQLiteUserHandler;
import com.wraptalk.wraptalk.models.UserInfo;
import com.wraptalk.wraptalk.services.ChattingService;
import com.wraptalk.wraptalk.services.TaskWatchService;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Game App");


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_applist);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_category);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_mychannel);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_setting);

        SQLiteUserHandler sqLiteUserHandler = SQLiteUserHandler.open(getApplicationContext());
        sqLiteUserHandler.insert(UserInfo.getInstance().token, UserInfo.getInstance().deviceId, UserInfo.getInstance().email, UserInfo.getInstance().gcmKey);

        Cursor c = sqLiteUserHandler.select();

        while(c.moveToNext()) {
            String token = c.getString(c.getColumnIndex("token"));
            String device_id = c.getString(c.getColumnIndex("device_id"));
            String user_id = c.getString(c.getColumnIndex("user_id"));
            String gcm_key = c.getString(c.getColumnIndex("gcm_key"));

            Log.e("DATA", "token:" + token + " / device_id:" + device_id + " / user_id:" + user_id + " / gcm_key:" + gcm_key);
        }
        sqLiteUserHandler.close();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, TaskWatchService.class));
                startService(new Intent(MainActivity.this, ChattingService.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getSupportActionBar().setTitle("Game App");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Category");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("My Channels");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Setting");
                        break;
                }
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new TabGameListFragment();
                case 1:
                    return new TabCategoryFragment();
                case 2:
                    return new TabMyChannelFragment();
                case 3:
                    return new TabSettingFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

}
