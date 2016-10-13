package com.jc.android.contact.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jc.android.base.presentation.App;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.fragment.ContactListFragment;
import com.jc.android.contact.presentation.view.fragment.ContactTreeFragment;
import com.jc.android.contact.presentation.view.interfaces.IContactInterface;
import com.jc.android.module.contact.R;
import com.jc.android.widget.presentation.view.activity.BackActivity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContactCenterActivity extends BackActivity {

    public static final int SELECTED_CONFIRM = 1001;
    public static final int SELECTED_CANCEL = 1002;

    public static final String SELECTED_IDS = "SELECTED_IDS";
    public static final String SELECTED_NAMES = "SELECTED_NAMES";

    public static boolean isInitSelected = false;
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

    public static String selectedIds = "";

    public static boolean isOrgSplit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_contact_center);

        isInitSelected = false;
        selected.clear();

        // 页面
        pageType = getIntent().getIntExtra(ContentBuilder.PARAM_PAGE_TYPE, ContentBuilder.PAGE_USER_LIST);
        // 选择
        viewType = getIntent().getIntExtra(ContentBuilder.PARAM_VIEW_TYPE, ContentBuilder.VIEW_TYPE_SHOW);

        // 选择数量范围
        rangeMin = getIntent().getIntExtra(ContentBuilder.PARAM_RANGE_MIN, 1);
        rangeMax = getIntent().getIntExtra(ContentBuilder.PARAM_RANGE_MAX, -1);

        // 备选列表
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

        // 已选中列表
        selectedIds = getIntent().getStringExtra(ContentBuilder.PARAM_SELECTED_IDS);
        if (TextUtils.isEmpty(selectedIds)) {
            selectedIds = "";
        }

        // 机构隔离
        isOrgSplit = getIntent().getBooleanExtra(ContentBuilder.PARAM_ORG_SPLIT, true);

        // 标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String title = getIntent().getStringExtra(ContentBuilder.PARAM_TITLE);

            if (TextUtils.isEmpty(title)) {
                if (viewType == ContentBuilder.VIEW_TYPE_SHOW) {
                    title = "通讯录";
                } else if (viewType == ContentBuilder.VIEW_TYPE_SINGLE) {
                    title = "通讯录-单选";
                } else if (viewType == ContentBuilder.VIEW_TYPE_MULTIPLE) {
                    title = "通讯录-多选";
                } else {
                    title = "通讯录";
                }
            }

            actionBar.setTitle(title);
        }

        changeFragment(pageType);
    }

    private Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_contact_center, menu);

        // 全选分组
        mMenu.setGroupVisible(R.id.multiple, viewType == ContentBuilder.VIEW_TYPE_MULTIPLE);

        //机构选择按钮
        mMenu.findItem(R.id.show_tree).setVisible(viewType == ContentBuilder.VIEW_TYPE_MULTIPLE || viewType == ContentBuilder.VIEW_TYPE_SINGLE);
        mMenu.findItem(R.id.show_flatten).setVisible(false);

        // 保存按钮
        mMenu.findItem(R.id.save).setVisible(viewType != ContentBuilder.VIEW_TYPE_SHOW);

        // 排序分组
        if (viewType != ContentBuilder.VIEW_TYPE_MULTIPLE && viewType != ContentBuilder.VIEW_TYPE_SINGLE)
            changeMenu();


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.save == item.getItemId()) {

            if (rangeMin > 0 && rangeMin > selected.size()) {
                Toast.makeText(this, "最少选择" + rangeMin + "条数据", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (rangeMax > 0 && rangeMax < selected.size()) {
                Toast.makeText(this, "最多选择" + rangeMax + "条数据", Toast.LENGTH_SHORT).show();
                return true;
            }

            StringBuilder stringIds = new StringBuilder();
            StringBuilder stringNames = new StringBuilder();
            for (ContactModel model : selected.values()) {

                if (stringIds.length() > 0) {
                    stringIds.append(",");
                }
                stringIds.append(model.getId());

                if (stringNames.length() > 0) {
                    stringNames.append(",");
                }
                stringNames.append(model.getDisplayName());
            }

            Intent intent = new Intent();
            intent.putExtra(SELECTED_IDS, stringIds.toString());
            intent.putExtra(SELECTED_NAMES, stringNames.toString());
            setResult(SELECTED_CONFIRM, intent);
            ContactCenterActivity.selected.clear();
            finish();

        } else if (R.id.select_all == item.getItemId()) {
            getContactInterface().selectAll();

        } else if (R.id.unselect_all == item.getItemId()) {
            getContactInterface().cancelAll();

        } else if (R.id.show_flatten == item.getItemId()) {
            ContactCenterActivity.selected.clear();
            changeFragment(ContentBuilder.PAGE_USER_LIST);
            mMenu.findItem(R.id.show_tree).setVisible(true);
            mMenu.findItem(R.id.show_flatten).setVisible(false);

        } else if (R.id.show_tree == item.getItemId()) {
            ContactCenterActivity.selected.clear();
            changeFragment(ContentBuilder.PAGE_USER_TREE);
            mMenu.findItem(R.id.show_tree).setVisible(false);
            mMenu.findItem(R.id.show_flatten).setVisible(true);

        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private IContactInterface getContactInterface() {
        if (curFragment != null && curFragment instanceof IContactInterface) {
            return (IContactInterface) curFragment;
        }

        return new IContactInterface() {
            @Override
            public void selectAll() {

            }

            @Override
            public void cancelAll() {

            }
        };
    }

    // 改变当前fragment页
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
            if (viewType != ContentBuilder.VIEW_TYPE_MULTIPLE && viewType != ContentBuilder.VIEW_TYPE_SINGLE)
                changeMenu();
        }
    }

    // 改变排序菜单的显示和隐藏
    private void changeMenu() {
        if (mMenu != null) {
            mMenu.findItem(R.id.show_flatten).setVisible(viewType == ContentBuilder.VIEW_TYPE_SHOW && curPageId != ContentBuilder.PAGE_USER_LIST);
            mMenu.findItem(R.id.show_tree).setVisible(viewType == ContentBuilder.VIEW_TYPE_SHOW && curPageId != ContentBuilder.PAGE_USER_TREE);
        }
    }

    @Override
    public void onBack() {
        setResult(SELECTED_CANCEL, null);
        super.onBack();
    }
}
