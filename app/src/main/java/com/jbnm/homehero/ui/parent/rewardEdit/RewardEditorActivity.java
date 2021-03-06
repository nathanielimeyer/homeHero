package com.jbnm.homehero.ui.parent.rewardEdit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.parent.rewardList.ParentRewardListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RewardEditorActivity extends BaseActivity implements RewardEditorContract.MvpView {
    @BindView(R.id.edit_text_description) EditText descriptionEditText;
    @BindView(R.id.reward_image_view) ImageView reward_image_view;
    @BindView(R.id.reward_value_spinner) Spinner valueSpinner;

    private Context context = this;
    ListAdapter iconAdapter;

    private RewardEditorContract.Presenter presenter;

    public static Intent createIntent(Context context, String childId, String rewardId) {
        Intent intent = new Intent(context, RewardEditorActivity.class);
        intent.putExtra(Constants.CHILD_INTENT_KEY, childId);
        intent.putExtra(Constants.REWARD_INTENT_KEY, rewardId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_editor);
        ButterKnife.bind(this);

        String childId = getIntent().getStringExtra(Constants.CHILD_INTENT_KEY);
        String rewardId = getIntent().getStringExtra(Constants.REWARD_INTENT_KEY);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
        setSpinner();

        presenter = new RewardEditorPresenter(this);
        presenter.loadChildAndReward(childId, rewardId);

        reward_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIconPickerDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reward_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_reward_edit_save) {
            presenter.saveChildData(descriptionEditText.getText().toString().trim(), (Integer)valueSpinner.getSelectedItem());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    public void showIconPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.reward_icon_picker_title)
                .setAdapter(iconAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.setRewardIcon(i);
                    }
                });
        builder.create().show();
    }

    @Override
    public void buildIconPickerDialog() {
        List<String> dialogItemList = presenter.getIconList();
        final String[] dialogItems = new String[dialogItemList.size()];
        dialogItemList.toArray(dialogItems);

        iconAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                dialogItems){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                //Put the image on the TextView

                tv.setCompoundDrawablesWithIntrinsicBounds((getResources().getIdentifier(dialogItems[position], "drawable", getPackageName())), 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };
    }

    @Override
    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            descriptionEditText.setText(description);
        }
    }

    private void setSpinner() {
        Integer[] spinnerValues = new Integer[100];
        for(int i = 0; i < spinnerValues.length; i++) {
            spinnerValues[i] = i + 1;
        }
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        valueSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void setValue(int value) {
        if (value != 0) {
            valueSpinner.setSelection(value-1);
        } else {
            valueSpinner.setSelection(0);
        }
    }

    @Override
    public void loadIcon(String icon) {
        reward_image_view.setImageResource(getResources().getIdentifier(icon, "drawable", getPackageName()));
    }

    @Override
    public void parentRewardListIntent(String childId) {
        startActivity(ParentRewardListActivity.createIntent(this, childId));
        finish();
    }

    @Override
    public void showDescriptionError() {
        descriptionEditText.setError(getString(R.string.reward_edit_description_error));
    }
}
