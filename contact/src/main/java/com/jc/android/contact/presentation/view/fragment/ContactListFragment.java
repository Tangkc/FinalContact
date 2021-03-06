package com.jc.android.contact.presentation.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.android.base.presentation.view.fragment.BaseFragment;
import com.jc.android.contact.presentation.ContactListBinding;
import com.jc.android.contact.presentation.view.interfaces.IContactInterface;
import com.jc.android.contact.presentation.viewmodel.ContactListViewModel;
import com.jc.android.module.contact.R;

/**
 * Fragment that shows a list of Users.
 */
public class ContactListFragment extends BaseFragment<ContactListViewModel, ContactListBinding>
        implements IContactInterface {

    public final static String TAG = ContactListFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setViewModel(new ContactListViewModel(getActivity()));
        setBinding(DataBindingUtil.<ContactListBinding>inflate(inflater, R.layout.fragment_contact_list, container, false));
        getBinding().setViewModel(getViewModel());
        getBinding().sidebar.setTextView(getBinding().dialog);

        return getBinding().getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().loadContactsCommand(getBinding().countryLvcountry, getBinding().titleLayoutCatalog, getBinding().titleLayout);
        getViewModel().mClearEditTextClick(getBinding().filterEdit);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    @Override
    public void selectAll() {
        getViewModel().selectAll();
    }

    @Override
    public void cancelAll() {
        getViewModel().cancelAll();
    }
}
