package com.jc.android.contact.presentation.view.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jc.android.base.presentation.App;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.fragment.ContactListFragment;
import com.jc.android.contact.presentation.view.fragment.ContactTreeFragment;
import com.jc.android.module.contact.R;
import com.jc.android.widget.presentation.view.activity.BackActivity;
import com.jc.android.widget.presentation.view.widget.ActivityUtils;

import java.util.HashMap;
import java.util.Map;

public class ContactCenterActivity extends BackActivity {

    private Map<Long, ContactModel> selected = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_contact_center);

        changeFragment(R.id.show_flatten);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_center, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.save == item.getItemId()) {
            Toast.makeText(App.context(), "保存", Toast.LENGTH_SHORT).show();
        } else if (R.id.select_all == item.getItemId()) {

        } else if (R.id.unselect_all == item.getItemId()) {

        } else if (R.id.show_flatten == item.getItemId()) {
            changeFragment(R.id.show_flatten);
        } else if (R.id.show_tree == item.getItemId()) {
            changeFragment(R.id.show_tree);
        }  else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private int curPageId = 0;
    private Fragment curFragment;
    public void changeFragment(@IdRes int id) {
        if (curPageId==0) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            curFragment = new ContactListFragment();
            transaction.add(R.id.container, new ContactListFragment());
            transaction.commit();

        } else if (curPageId != id) {

            ((FrameLayout)findViewById(R.id.container)).removeAllViews();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(curFragment);
            if (id == R.id.show_flatten) {
                curFragment = new ContactListFragment();
            } else {
                curFragment = new ContactTreeFragment();
            }
            transaction.add(R.id.container, curFragment);
            transaction.commit();

        }

        curPageId = id;
    }
}
