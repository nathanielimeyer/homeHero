package com.jbnm.homehero.ui.reauth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.jbnm.homehero.Constants;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.parent.ParentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by janek on 8/10/17.
 */

public class ReAuthDialogFragment extends DialogFragment implements ReAuthContract.MvpView{
    @BindView(R.id.passwordEditText) EditText passwordEditText;
    @BindView(R.id.cancelButton) Button cancelButton;
    @BindView(R.id.okButton) Button okButton;
    @BindView(R.id.dialogContent) LinearLayout content;
    @BindView(R.id.dialogLoading) ProgressBar loading;
    private Unbinder unbinder;
    private ReAuthContract.Presenter presenter;
    private long animationDuration;

    public static ReAuthDialogFragment newInstance(String childId) {
        ReAuthDialogFragment fragment = new ReAuthDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.CHILD_INTENT_KEY, childId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);
        animationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_reauth, container, false);
        getDialog().setCanceledOnTouchOutside(false);

        unbinder = ButterKnife.bind(this, view);
        String childId = getArguments().getString(Constants.CHILD_INTENT_KEY);
        presenter = new ReAuthPresenter(this, childId);
        presenter.init();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleCancelButtonClick();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleOkButtonClick(passwordEditText.getText().toString().trim());
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void navigateToParent(String childId) {
        startActivity(ParentActivity.createIntent(getActivity(), childId));
        getActivity().finish();
        dismiss();
    }

    @Override
    public void showPasswordError() {
        passwordEditText.setError("Incorrect Password. Please Try Again.");
    }

    @Override
    public void hidePasswordError() {
        passwordEditText.setError(null);
    }

    @Override
    public void showLoading() {
        content.setVisibility(View.INVISIBLE);
        loading.setAlpha(1f);
        loading.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        content.setAlpha(0f);
        content.setVisibility(View.VISIBLE);

        content.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        loading.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (loading != null) {
                            loading.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }
}
