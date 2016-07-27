package com.jc.android.template.presentation.viewmodel;


import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import com.jc.android.base.presentation.viewmodel.LoadingViewModel;
import com.jc.android.base.domain.interactor.DefaultSubscriber;
import com.jc.android.base.domain.interactor.UseCase;
import com.jc.android.base.presentation.App;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.template.data.entity.DemoEntity;
import com.jc.android.template.domain.interactor.GetDemoList;
import com.jc.android.template.presentation.exception.ErrorMessageFactory;
import com.jc.android.template.presentation.mapper.DemoModelDataMapper;
import com.jc.android.template.presentation.model.DemoModel;
import com.jc.android.template.presentation.view.activity.DemoDetailsActivity;
import com.jc.android.template.presentation.view.adapter.DemosAdapter;

import java.util.Collection;
import java.util.List;

/**
 * Created by rocko on 15-11-5.
 */
public class DemoListViewModel extends LoadingViewModel {
	private final static String TAG = DemoListViewModel.class.getSimpleName();

	public final ObservableBoolean showContentList = new ObservableBoolean(false);
	public final ObservableField<DemosAdapter> usersListAdapter = new ObservableField<>();

	UseCase getUserList = new GetDemoList(App.context());
	DemoModelDataMapper demoModelDataMapper = new DemoModelDataMapper();


	@BindView
	@Override
	public void showLoading() {
		super.showLoading();
		showContentList.set(false);
	}

	@BindView
	@Override
	public void showRetry() {
		super.showRetry();
		showContentList.set(false);
	}

	@BindView
	public void showContentList(DemosAdapter demosAdapter) {
		showLoading.set(false);
		showRetry.set(false);
		showContentList.set(true);
		usersListAdapter.set(demosAdapter);
	}

	@BindView
	public void showMoreContent() {
		// userAdapter
	}

	@Command
	public void loadUsersCommand() {
		if (showLoading.get()) {
			return;
		}
		showLoading();
		getUserList.execute(new ProcessErrorSubscriber(App.context()) {

			public void onNext(List<DemoEntity> demos) {
				Collection<DemoModel> demoModelsCollection = demoModelDataMapper.transformUsers(demos);
				DemosAdapter demosAdapter = new DemosAdapter(App.context(), demoModelsCollection);
				demosAdapter.setOnItemClickListener(onUserItemClick());
				showContentList(demosAdapter);
			}

			@Override
			public void onError(Throwable e) {
				super.onError(e);
				showRetry();
//				String message=ErrorMessageFactory.create(App.context(), (Exception) e);
//				Toast.makeText(App.context(), message, Toast.LENGTH_SHORT).show();
			}

		});
	}

	@Override
	public View.OnClickListener onRetryClick() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadUsersCommand();

			}
		};
	}

	public DemosAdapter.OnItemClickListener onUserItemClick() {
		return new DemosAdapter.OnItemClickListener() {
			@Override
			public void onUserItemClicked(DemoModel demoModel) {
				Intent intent = DemoDetailsActivity.getCallingIntent(App.instance().getCurrentActivity(), demoModel.getUserId());
				ActivityNavigator.to(DemoDetailsActivity.class, intent);
			}
		};
	}

}
