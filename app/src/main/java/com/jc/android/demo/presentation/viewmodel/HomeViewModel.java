package com.jc.android.demo.presentation.viewmodel;

import android.content.Intent;
import android.view.View;

import com.jc.android.base.presentation.App;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.viewmodel.ViewModel;
import com.jc.android.logon.presentation.view.activity.LoginActivity;

/**
 * Created by rocko on 15-11-5.
 */
public class HomeViewModel extends ViewModel {

	@Command
	public View.OnClickListener onClickLoadData() {
		return new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				Intent intent = LoginActivity.getCallingIntent(App.instance().getCurrentActivity(),
						"com.jc.android.mail.presentation.view.activity.MailListActivity",
						"android/system/login4M.action");
				ActivityNavigator.to(LoginActivity.class, intent);
			}
		};
	}
}
