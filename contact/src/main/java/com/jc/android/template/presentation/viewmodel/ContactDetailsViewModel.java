package com.jc.android.template.presentation.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

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
	public final ObservableField<ContactModel> contactObs = new ObservableField<>();


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
		contactObs.set(demoModel);
	}


	@Command
	public void loadUserDetailsCommand(String displayName, String mobile, String photo) {
		showLoading();
		ContactModel contactModel=new ContactModel();
		contactModel.setDisplayName(displayName==null?"":displayName);
		contactModel.setMobile(mobile==null?"":mobile);
		contactModel.setPhoto(photo==null?"":photo);
		contactObs.set(contactModel);
		showLoading.set(false);
	}

	@Override
	public View.OnClickListener onRetryClick() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		};
	}

	public View.OnClickListener onSentMail() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(contactObs.get().getMobile()!=null&&contactObs.get().getMobile().length()>0) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setAction("android.intent.action.SENDTO");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setData(Uri.parse("sms:" + contactObs.get().getMobile()));
					App.instance().getCurrentActivity().startActivity(intent);
				}else {
					Toast.makeText(App.instance().getCurrentActivity(),"暂无联系人电话",Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	public View.OnClickListener onCallPhone() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(contactObs.get().getMobile()!=null&&contactObs.get().getMobile().length()>0) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + contactObs.get().getMobile()));
					App.instance().getCurrentActivity().startActivity(intent);
				}else {
					Toast.makeText(App.instance().getCurrentActivity(),"暂无联系人电话",Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
}
