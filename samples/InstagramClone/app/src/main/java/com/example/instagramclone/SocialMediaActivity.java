package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class SocialMediaActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private tabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        // Initialize UI components to variables
        setTitle("InstaClone App!");
        toolbar = findViewById(R.id.mytoolbar);
        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new tabAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(tabAdapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager, false);

    }
}