package com.jbnm.homehero.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jbnm.homehero.R;
import com.jbnm.homehero.ui.base.BaseActivity;
import com.jbnm.homehero.ui.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class LoginActivity extends BaseActivity implements LoginContract.MvpView {
    @BindView(R.id.loginEmailEditText) EditText emailEditText;
    @BindView(R.id.loginPasswordEditText) EditText passwordEditText;
    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.signUpLinkButton) Button signUpLinkButton;
    private LoginContract.Presenter presenter;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        presenter.init();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleLoginButtonClick(emailEditText.getText().toString().trim()
                        , passwordEditText.getText().toString().trim());
            }
        });

        signUpLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.handleSignUpLinkClick();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.addAuthListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.removeAuthListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void enableLoginButton() {
        loginButton.setEnabled(true);
    }

    @Override
    public void disableLoginButton() {
        loginButton.setEnabled(false);
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
    public void signUpIntent() {
        startActivity(SignUpActivity.createIntent(this));
    }
}
