package com.jc.android.contact.presentation.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.jc.android.base.presentation.viewmodel.LoadingViewModel;
import com.jc.android.base.presentation.App;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.domain.interactor.GetContactDetails;
import com.jc.android.contact.presentation.mapper.ContactModelDataMapper;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.widget.presentation.viewmodel.ProcessErrorSubscriber;

/**
 * Created by rocko on 15-11-5.
 */
public class ContactDetailsViewModel extends LoadingViewModel {

    public final ObservableBoolean showUserDetails = new ObservableBoolean(true);
    public final ObservableField<ContactModel> contactObs = new ObservableField<>();

    GetContactDetails getContactDetail = new GetContactDetails(App.context());
    ContactModelDataMapper mapper = new ContactModelDataMapper();

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
    public void loadUserDetailsCommand(long id) {
        showLoading();

        getContactDetail.setUserId(String.valueOf(id));
        getContactDetail.execute(new ProcessErrorSubscriber<Contact>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(Contact o) {
                showUserDetails(mapper.transformUser(o));
            }
        });


    }

    @Override
    public View.OnClickListener onRetryClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }


    public void onSendSMSClick(View v,int i) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setAction("android.intent.action.SENDTO");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("smsto:" + (i==1?contactObs.get().getMobile():contactObs.get().getOfficeTel())));
        App.instance().getCurrentActivity().startActivity(intent);
    }

    public void onCallClick(View v,int i) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + (i==1?contactObs.get().getMobile():contactObs.get().getOfficeTel())));
        App.instance().getCurrentActivity().startActivity(intent);

    }
}
