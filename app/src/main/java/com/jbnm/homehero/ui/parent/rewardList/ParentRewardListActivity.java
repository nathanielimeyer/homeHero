package com.jbnm.homehero.ui.parent.rewardList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.parent.rewardEdit.RewardEditorActivity;
import com.jbnm.homehero.ui.parent.ParentActivity;
import com.jbnm.homehero.util.SharedPrefManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nathanielmeyer on 8/7/17.
 */

public class ParentRewardListActivity extends BaseActivity implements ParentRewardListContract.MvpView, ParentRewardClickListener {
    @BindView(R.id.rewardListRecyclerView) RecyclerView rewardListRecyclerView;
    @BindView(R.id.addRewardButton) FloatingActionButton addRewardButton;

    List<Reward> rewards;
    String childId;
    private Context context = this;
    private ParentRewardListContract.Presenter presenter;

    public static Intent createIntent(Context context, String childId) {
        Intent intent = new Intent(context, ParentRewardListActivity.class);
        intent.putExtra(Constants.CHILD_INTENT_KEY, childId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_reward_list);
        ButterKnife.bind(this);

        childId = getIntent().getStringExtra(Constants.CHILD_INTENT_KEY);

        presenter = new ParentRewardListPresenter(this, SharedPrefManager.getInstance(this));
        presenter.loadRewards(childId);

        addRewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addRewardButtonClicked();
            }
        });

        rewardListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_INDICATOR_TOP && addRewardButton.isEnabled()) {
                    addRewardButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && addRewardButton.isShown()) {
                    addRewardButton.hide();
                } else if (dy < 0 && !addRewardButton.isShown() && addRewardButton.isEnabled()) {
                    addRewardButton.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        presenter.handleBackButtonPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void listRewards(List<Reward> rewards) {
        this.rewards = rewards;
        ParentRewardListAdapter parentRewardListAdapter = new ParentRewardListAdapter(rewards);
        parentRewardListAdapter.setParentRewardClickListener(this);
        rewardListRecyclerView.setAdapter(parentRewardListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rewardListRecyclerView.setLayoutManager(layoutManager);
        rewardListRecyclerView.setHasFixedSize(true);
        rewardListRecyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    @Override
    public void addRewardIntent(String childId) {
        startActivity(RewardEditorActivity.createIntent(this, childId, Constants.REWARD_NEW_INTENT_VALUE));
    }

    @Override
    public void onEditReward(String rewardId) {
        startActivity(RewardEditorActivity.createIntent(this, childId, rewardId));
    }

    @Override
    public void parentIntent(String childId) {
        startActivity(ParentActivity.createIntent(this, childId));
        finish();
    }

    @Override
    public void onDeleteReward(Reward reward) {
        presenter.checkAndNullCurrentReward(reward.getId());
        rewards.remove(reward);
        presenter.saveRewards(rewards);
    }

    @Override
    public void setAddRewardButtonEnabled(boolean enabled) {
        addRewardButton.setEnabled(enabled);
        if (enabled) {
            addRewardButton.setVisibility(View.VISIBLE);
        } else {
            addRewardButton.setVisibility(View.GONE);
        }
    }
}
