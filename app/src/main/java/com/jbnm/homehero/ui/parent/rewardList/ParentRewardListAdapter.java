package com.jbnm.homehero.ui.parent.rewardList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbnm.homehero.R;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Reward;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nathanielmeyer on 8/8/17.
 */

class ParentRewardListAdapter extends RecyclerView.Adapter<ParentRewardListAdapter.ParentRewardListViewHolder> {
    private List<Reward> rewards = new ArrayList<>();
    private ParentRewardClickListener parentRewardClickListener;

    public ParentRewardListAdapter(List<Reward> rewards) {
        this.rewards = rewards;
    }

    @Override
    public ParentRewardListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_reward_list_item, parent, false);
        ParentRewardListViewHolder viewHolder = new ParentRewardListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ParentRewardListViewHolder holder, int position) {
        holder.bindReward((Reward) rewards.get(position), position);
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    public void setParentRewardClickListener(ParentRewardClickListener parentRewardClickListener) {
        this.parentRewardClickListener = parentRewardClickListener;
    }

    public class ParentRewardListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rewardImage)
        ImageView rewardImage;
        @BindView(R.id.rewardDescriptionTextView)
        TextView rewardDescriptionTextView;
        @BindView(R.id.rewardEditButton)
        ImageButton rewardEditButton;
        @BindView(R.id.rewardDeleteButton)
        ImageButton rewardDeleteButton;

        private Context context;
        private Reward reward;

        public ParentRewardListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void bindReward(Reward reward, int position) {
            this.reward = reward;
            if (reward.getRewardImage() != null && !reward.getRewardImage().equals("")) {
                rewardImage.setImageResource(context.getResources().getIdentifier(reward.getRewardImage(), "drawable", context.getPackageName()));
            }
            rewardDescriptionTextView.setText(reward.getDescription());
            rewardEditButton.setVisibility(View.VISIBLE);
        }

        @OnClick(R.id.rewardEditButton)
        public void rewardEditClick() {
            parentRewardClickListener.onEditReward(reward.getId());
        }

        @OnClick(R.id.rewardDeleteButton)
        public void rewardDeleteClick() {
            parentRewardClickListener.onDeleteReward(reward);
        }
    }

}