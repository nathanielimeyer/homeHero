package com.jbnm.homehero.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class SignUpActivity extends BaseActivity implements SignUpContract.MvpView {
    @BindView(R.id.signUpEmailEditText) EditText emailEditText;
    @BindView(R.id.signUpPasswordEditText) EditText passwordEditText;
    @BindView(R.id.signUpConfirmEditText) EditText confirmEditText;
    @BindView(R.id.signUpButton) Button signUpButton;
    @BindView(R.id.loginLinkButton) Button loginLinkButton;
    private SignUpContract.Presenter presenter;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        presenter = new SignUpPresenter(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleSignUpButtonClick();
            }
        });

        loginLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleLoginLinkClick();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void enableSignUpButton() {
        signUpButton.setEnabled(true);
    }

    @Override
    public void disableSignUpButton() {
        signUpButton.setEnabled(false);
    }

    @Override
    public Observable<CharSequence> getEmailText() {
        return RxTextView.textChanges(emailEditText).skipInitialValue();
    }

    @Override
    public Observable<CharSequence> getPasswordText() {
        return RxTextView.textChanges(passwordEditText).skipInitialValue();
    }

    @Override
    public Observable<CharSequence> getConfirmPasswordText() {
        return RxTextView.textChanges(confirmEditText).skipInitialValue();
    }

    @Override
    public void showEmailError() {
        emailEditText.setError(getString(R.string.valid_email_error));
    }

    @Override
    public void hideEmailError() {
        emailEditText.setError(null);
    }

    @Override
    public void showPasswordError() {
        passwordEditText.setError(getString(R.string.valid_password_error));
    }

    @Override
    public void hidePasswordError() {
        passwordEditText.setError(null);
    }

    @Override
    public void showConfirmError() {
        confirmEditText.setError(getString(R.string.valid_confirm_error));
    }

    @Override
    public void hideConfirmError() {
        confirmEditText.setError(null);
    }

    @Override
    public void loginIntent() {
        startActivity(LoginActivity.createIntent(this));
    }
}
