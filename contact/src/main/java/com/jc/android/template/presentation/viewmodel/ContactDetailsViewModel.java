package com.jc.android.template.presentation.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.jc.android.base.presentation.viewmodel.LoadingViewModel;
import com.jc.android.base.domain.interactor.DefaultSubscriber;
import com.jc.android.base.presentation.App;
import com.jc.android.template.data.entity.ContactEntity;
import com.jc.android.template.domain.interactor.GetContactDetails;
import com.jc.android.template.presentation.mapper.ContactModelDataMapper;
import com.jc.android.template.presentation.model.ContactModel;

/**
 * Created by rocko on 15-11-5.
 */
public class ContactDetailsViewModel extends LoadingViewModel {

	public final ObservableBoolean showUserDetails = new ObservableBoolean(true);
	public final ObservableField<ContactModel> userObs = new ObservableField<>();

	GetContactDetails getDemoDetailsUseCase = new GetContactDetails(App.context());
	ContactModelDataMapper demoModelDataMapper = new ContactModelDataMapper();

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
	public void showUserDetails(ContactModel demoModel) {
		showLoading.set(false);
		showRetry.set(false);
		showUserDetails.set(true);
		userObs.set(demoModel);
	}


	@Command
	public void loadUserDetailsCommand(String userId) {
		showLoading();
		getDemoDetailsUseCase.setUserId(userId);
		getDemoDetailsUseCase.execute(new DefaultSubscriber<ContactEntity>(){
			@Override
			public void onNext(ContactEntity demo) {
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
				loadUserDetailsCommand(userObs.get().getId()+"");
			}
		};
	}
}
