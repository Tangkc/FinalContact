package com.jc.android.contact.presentation.view.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import java.util.concurrent.ConcurrentHashMap;

public class ContactCenterActivity extends BackActivity {

    public static final Map<Long, ContactModel> selected = new ConcurrentHashMap<>();

    public static int viewType = ContentBuilder.VIEW_TYPE_SHOW;
    public static int rangeMin = 1;
    public static int rangeMax = -1;
    public static int pageType = ContentBuilder.PAGE_USER_LIST;

    public static String listIds = "";
    public static String listNames = "";
    public static String listPhotos = "";
    public static String listDepts = "";
    public static String listOrgs = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_contact_center);

        selected.clear();

        // TODO 初始化数据
        pageType = getIntent().getIntExtra(ContentBuilder.PARAM_PAGE_TYPE, ContentBuilder.PAGE_USER_LIST);
        viewType = getIntent().getIntExtra(ContentBuilder.PARAM_VIEW_TYPE, ContentBuilder.VIEW_TYPE_SHOW);

        rangeMin = getIntent().getIntExtra(ContentBuilder.PARAM_RANGE_MIN, 1);
        rangeMax = getIntent().getIntExtra(ContentBuilder.PARAM_RANGE_MAX, -1);

        listIds = getIntent().getStringExtra(ContentBuilder.PARAM_LIST_IDS);
        if (TextUtils.isEmpty(listIds)) {
            listIds = "";
        }

        listNames = getIntent().getStringExtra(ContentBuilder.PARAM_LIST_NAMES);
        if (TextUtils.isEmpty(listNames)) {
            listNames = "";
        }

        listPhotos = getIntent().getStringExtra(ContentBuilder.PARAM_LIST_PHOTOS);
        if (TextUtils.isEmpty(listPhotos)) {
            listPhotos = "";
        }

        listDepts = getIntent().getStringExtra(ContentBuilder.PARAM_LIST_DEPTS);
        if (TextUtils.isEmpty(listDepts)) {
            listDepts = "";
        }

        listOrgs = getIntent().getStringExtra(ContentBuilder.PARAM_LIST_ORGS);
        if (TextUtils.isEmpty(listOrgs)) {
            listOrgs = "";
        }

        changeFragment(pageType);
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
            changeFragment(ContentBuilder.PAGE_USER_LIST);

        } else if (R.id.show_tree == item.getItemId()) {
            changeFragment(ContentBuilder.PAGE_USER_TREE);

        }  else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }


    private int curPageId = -1;
    private Fragment curFragment;
    public void changeFragment(int id) {

        if (curPageId != id) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (curPageId >= 0) {
                transaction.remove(curFragment);
            }
            if (id == ContentBuilder.PAGE_USER_LIST) {
                curFragment = new ContactListFragment();
            } else {
                curFragment = new ContactTreeFragment();
            }
            transaction.add(R.id.container, curFragment);
            transaction.commit();

            curPageId = id;
        }
    }
}
