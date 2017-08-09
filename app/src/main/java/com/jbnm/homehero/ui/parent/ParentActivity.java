package com.jbnm.homehero.ui.parent;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.goal.GoalActivity;
import com.jbnm.homehero.ui.login.LoginActivity;
import com.jbnm.homehero.ui.parent.settings.SettingsFragment;
import com.jbnm.homehero.ui.parent.taskList.ParentTaskListActivity;
import com.jbnm.homehero.ui.parent.taskreview.TaskReviewFragment;
import com.jbnm.homehero.util.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentActivity extends BaseActivity implements ParentContract.MvpView{
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private ParentPagerAdapter parentPagerAdapter;
    private ParentContract.Presenter presenter;

    public static Intent createIntent(Context context, String childId) {
        Intent intent = new Intent(context, ParentActivity.class);
        intent.putExtra(Constants.CHILD_INTENT_KEY, childId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        ButterKnife.bind(this);
        String childId = getIntent().getStringExtra(Constants.CHILD_INTENT_KEY);

        presenter = new ParentPresenter(this, SharedPrefManager.getInstance(this));
        presenter.init(childId);

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.parent, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_parent_childNav_item) {
            presenter.handleChildNavButtonClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUpViewPager(String childId) {
        parentPagerAdapter = new ParentPagerAdapter(getSupportFragmentManager());
        parentPagerAdapter.addFragment(TaskReviewFragment.newInstance(childId));
        parentPagerAdapter.addFragment(SettingsFragment.newInstance(childId));

        viewPager.setAdapter(parentPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    public void taskListIntent(String childId) {
        startActivity(ParentTaskListActivity.createIntent(this, childId));
    }

    @Override
    public void goalListIntent(String childId) {
        Log.d("test", "goalList");
    }

    @Override
    public void goalIntent(String childId) {
        startActivity(GoalActivity.createIntent(this, childId));
        finish();
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
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
