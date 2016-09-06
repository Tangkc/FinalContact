package com.jc.android.demo.presentation.viewmodel;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.jc.android.base.data.cache.persister.PrefrenceTool;
import com.jc.android.base.presentation.App;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.viewmodel.ViewModel;
import com.jc.android.component.config.data.datasource.ConfigDataStoreFactory;
import com.jc.android.component.config.data.dto.Config;
import com.jc.android.component.config.domain.interactor.GetConfig;
import com.jc.android.component.config.domain.interactor.PutConfig;
import com.jc.android.logon.presentation.view.activity.LoginActivity;
public class HomeViewModel extends ViewModel {

	@Command
	public View.OnClickListener onClickLoadData() {
		return new View.OnClickListener(){
			@Override
			public void onClick(View v) {

				GetConfig getConfig = new GetConfig(App.context());
				Config config=new Config();
				if (TextUtils.isEmpty(getConfig.buildUseCase().getBusinessServer())) {
					PutConfig putConfig = new PutConfig(App.context());
					putConfig.setBusinessServer("http://172.16.3.239:8180/goa/");
					putConfig.setUpdateServer("http://172.16.3.239:8089/");
					putConfig.buildUseCase(config);
				}

				//clear login cookie
				PrefrenceTool.removeValue(App.context().getPackageName(), config.getBusinessServer(), App.context());

				Intent intent = LoginActivity.getCallingIntent(App.instance().getCurrentActivity(),
						"com.jc.android.demo.presentation.view.activity.MenuActivity",
						"android/system/login4M.action");
				ActivityNavigator.to(LoginActivity.class, intent);
			}
		};
	}
}
