package com.jbnm.homehero.ui.parent;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;
import com.jbnm.homehero.data.remote.DataManager;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.parent.settings.SettingsFragment;
import com.jbnm.homehero.ui.parent.taskreview.TaskReviewFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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

//        setup();
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

    public void setup() {
        Parent parent = new Parent("ParentTestId3", "email@email.com");
        Child child = Child.newInstance();
        List<String> instructions = Arrays.asList("change bed", "vacuum walls");
        for (int i = 0; i<10; i++) {
            Task task = Task.newInstance("clean your room " + i, "down_arrow", instructions);
            child.addTask(task);
        }
        for (int i =0; i<2; i++) {
            Reward reward = Reward.newInstance("Disneyland", 20, "castle.jpg");
            child.addReward(reward);
        }

        DataManager dataManager = new DataManager();
        dataManager.saveParent(parent).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Parent>() {
            @Override public void onSubscribe(Disposable d) {}
            @Override public void onNext(Parent parent) {}
            @Override public void onError(Throwable e) {}
            @Override public void onComplete() {
                Log.d("test", "parent saved");
            }
        });

        dataManager.saveChild(child).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Child>() {
            @Override public void onSubscribe(Disposable d) {}
            @Override public void onNext(Child child) {}
            @Override public void onError(Throwable e) {}
            @Override public void onComplete() {
                Log.d("test", "child saved");
            }
        });
    }
}
