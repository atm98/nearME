package com.agnt45.nearme;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.agnt45.nearme.Adapters.ViewPagerTabAdapter;

public class HomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Toolbar mToolbar;
    private ViewPagerTabAdapter mAdaptertab;
    private TabLayout mtabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mToolbar = (Toolbar)findViewById(R.id.Home_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        mAdaptertab =  new ViewPagerTabAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.tabVpagger);
        viewPager.setAdapter(mAdaptertab);
        mtabLayout = (TabLayout) findViewById(R.id.home_Tab);
        mtabLayout.setupWithViewPager(viewPager);
    }

}
