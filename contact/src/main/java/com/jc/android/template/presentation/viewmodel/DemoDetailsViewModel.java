package com.jc.android.template.presentation.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.jc.android.base.presentation.viewmodel.LoadingViewModel;
import com.jc.android.base.domain.interactor.DefaultSubscriber;
import com.jc.android.base.presentation.App;
import com.jc.android.template.data.entity.DemoEntity;
import com.jc.android.template.domain.interactor.GetDemoDetails;
import com.jc.android.template.presentation.mapper.DemoModelDataMapper;
import com.jc.android.template.presentation.model.DemoModel;

/**
 * Created by rocko on 15-11-5.
 */
public class DemoDetailsViewModel extends LoadingViewModel {

	public final ObservableBoolean showUserDetails = new ObservableBoolean(true);
	public final ObservableField<DemoModel> userObs = new ObservableField<>();

	GetDemoDetails getDemoDetailsUseCase = new GetDemoDetails(App.context());
	DemoModelDataMapper demoModelDataMapper = new DemoModelDataMapper();

	@BindView
	@Override
	public void showLoading() {
//		super.showLoading(); // show Details
		showRetry.set(false);
		showLoading.set(true);
		showUserDetails.set(true);
	}

	@BindView
	@Override
	public void showRetry() {
		super.showRetry();
		showUserDetails.set(false);
	}

	@BindView
	public void showUserDetails(DemoModel demoModel) {
		showLoading.set(false);
		showRetry.set(false);
		showUserDetails.set(true);
		userObs.set(demoModel);
	}


	@Command
	public void loadUserDetailsCommand(int userId) {
		showLoading();
		getDemoDetailsUseCase.setUserId(userId);
		getDemoDetailsUseCase.execute(new DefaultSubscriber<DemoEntity>(){
			@Override
			public void onNext(DemoEntity demo) {
				showUserDetails(demoModelDataMapper.transformUser(demo));
			}

			@Override
			public void onError(Throwable e) {
				showRetry();
			}
		});
	}

	@Override
	public View.OnClickListener onRetryClick() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadUserDetailsCommand(userObs.get().getUserId());
			}
		};
	}
}
