package com.jbnm.homehero.ui.parent;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.parent.settings.SettingsFragment;
import com.jbnm.homehero.ui.parent.taskreview.TaskReviewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentActivity extends BaseActivity {
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private ParentPagerAdapter parentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        parentPagerAdapter = new ParentPagerAdapter(getSupportFragmentManager());
        parentPagerAdapter.addFragment(TaskReviewFragment.newInstance());
        parentPagerAdapter.addFragment(SettingsFragment.newInstance());

        viewPager.setAdapter(parentPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }


    public class ParentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();

        public ParentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
