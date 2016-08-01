/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.jc.android.template.presentation.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.android.base.presentation.view.fragment.BaseFragment;
import com.jc.android.module.contact.R;
import com.jc.android.template.presentation.DemoDetailsBinding;
import com.jc.android.template.presentation.viewmodel.ContactDetailsViewModel;


/**
 * Fragment that shows details of a certain user.
 */
public class ContactDetailsFragment extends BaseFragment<ContactDetailsViewModel, DemoDetailsBinding> {

	public final static String TAG = ContactDetailsFragment.class.getSimpleName();

	private static final String ARGUMENT_KEY_DISPLAYNAME = "org.android10.ARGUMENT_DISPLAYNAME";
	private static final String ARGUMENT_KEY_MOBILE = "org.android10.ARGUMENT_MOBILE";
	private static final String ARGUMENT_KEY_PHOTO = "org.android10.ARGUMENT_PHOTO";

	private String displayName;
	private String mobile;
	private String photo;

	public ContactDetailsFragment() {
		super();

	}

	public static ContactDetailsFragment newInstance(String displayName,String mobile,String photo) {
		ContactDetailsFragment demoDetailsFragment = new ContactDetailsFragment();

		Bundle argumentsBundle = new Bundle();
		argumentsBundle.putString(ARGUMENT_KEY_DISPLAYNAME, displayName);
		argumentsBundle.putString(ARGUMENT_KEY_MOBILE, mobile);
		argumentsBundle.putString(ARGUMENT_KEY_PHOTO, photo);
		demoDetailsFragment.setArguments(argumentsBundle);

		return demoDetailsFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		setViewModel(new ContactDetailsViewModel());
		setBinding(DataBindingUtil.<DemoDetailsBinding>inflate(inflater, R.layout.fragment_contact_details, container, false));
		getBinding().setViewModel(getViewModel());

		return getBinding().getRoot();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.initialize();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initialize() {
		this.displayName = getArguments().getString(ARGUMENT_KEY_DISPLAYNAME);
		this.mobile = getArguments().getString(ARGUMENT_KEY_MOBILE);
		this.photo = getArguments().getString(ARGUMENT_KEY_PHOTO);


		getViewModel().loadUserDetailsCommand(displayName,mobile,photo);
	}

	@Override
	public Context getContext() {
		return getActivity().getApplicationContext();
	}
}
