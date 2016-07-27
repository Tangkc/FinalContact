package com.jc.android.demo.presentation.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;

import com.jc.android.app.contact.R;
import com.jc.android.base.presentation.view.activity.BaseActivity;
import com.jc.android.demo.presentation.HomeBinding;
import com.jc.android.demo.presentation.viewmodel.HomeViewModel;


/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends BaseActivity<HomeViewModel, HomeBinding> {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setViewModel(new HomeViewModel());
    setBinding(DataBindingUtil.<HomeBinding>setContentView(this, R.layout.home_activity));
    getBinding().setViewModel(getViewModel());

    initWidget();
  }

  private void initWidget() {
    getBinding().linkTv.setText(Html.fromHtml(getResources().getString(R.string.url)));
    getBinding().linkTv.setMovementMethod(LinkMovementMethod.getInstance());
  }

}
