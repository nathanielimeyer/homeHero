package com.jbnm.homehero.ui.base;

/**
 * Created by nathanielmeyer on 7/23/17.
 */

public interface BaseMvpView {
    boolean showLoading();
    void hideLoading();
    boolean showError(String errorMessage);
    void hideError();
}
