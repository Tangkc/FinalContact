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
import com.jc.android.template.presentation.DemoListBinding;
import com.jc.android.base.presentation.view.adapter.DefaultLayoutManager;
import com.jc.android.template.presentation.viewmodel.ContactListViewModel;

/**
 * Fragment that shows a list of Users.
 */
public class ContactListFragment extends BaseFragment<ContactListViewModel, DemoListBinding> {

    public final static String TAG = ContactListFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setViewModel(new ContactListViewModel());
        setBinding(DataBindingUtil.<DemoListBinding>inflate(inflater, R.layout.fragment_contact_list, container, true));
        getBinding().setViewModel(getViewModel());
        getBinding().sidrbar.setTextView(getBinding().dialog);


        return getBinding().getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().mClearEditTextClick(getBinding().filterEdit);
        getViewModel().loadUsersCommand(getBinding().countryLvcountry, getBinding().titleLayoutCatalog, getBinding().titleLayout);
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }
}
