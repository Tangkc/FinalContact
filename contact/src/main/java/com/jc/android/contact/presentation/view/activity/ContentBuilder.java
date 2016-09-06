package com.jc.android.contact.presentation.view.activity;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ContentBuilder {

    // RANGE
    public static final String PARAM_RANGE_MIN = "PARAM_RANGE_MIN";
    public static final String PARAM_RANGE_MAX = "PARAM_RANGE_MAX";

    // TYPE
    public static final String PARAM_VIEW_TYPE = "PARAM_VIEW_TYPE";

    // LIST
    public static final String PARAM_LIST_IDS = "PARAM_LIST_IDS";
    public static final String PARAM_LIST_NAMES = "PARAM_LIST_NAMES";
    public static final String PARAM_LIST_PHOTOS = "PARAM_LIST_PHOTOS";
    public static final String PARAM_LIST_DEPTS = "PARAM_LIST_DEPTS";
    public static final String PARAM_LIST_ORGS = "PARAM_LIST_ORGS";

    // SELECTED
    public static final String PARAM_SELECTED_IDS = "PARAM_SELECTED_IDS";

    // 页面类型
    public static final int PARAM_PAGE_USER_LIST = 0; // 人员列表
    public static final int PARAM_PAGE_USER_TREE = 1; // 人员树
    public static final int PARAM_PAGE_ORG_LIST = 2;  // 机构列表
    public static final int PARAM_PAGE_ORG_TREE = 3;  // 机构树

    // 选择类型
    public static final int VIEW_TYPE_SHOW = 0;       // 展示, 通讯录
    public static final int VIEW_TYPE_SINGLE = 1;     // 单选
    public static final int VIEW_TYPE_MULTIPLE = 2;   // 多选

    private final Intent intent;
    private final Context context;

    public ContentBuilder(Context context) {
        this.context = context;
        intent = new Intent(context, ContactListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public ContentBuilder page(int page) {
        switch (page) {
            case PARAM_PAGE_USER_LIST:
                intent.setClass(context, ContactListActivity.class);
                break;
            case PARAM_PAGE_USER_TREE:
                intent.setClass(context, ContactTreeActivity.class);
                break;
            case PARAM_PAGE_ORG_LIST:
                Log.e("error", "机构列表未实现", new NullPointerException("机构列表未实现"));
                break;
            case PARAM_PAGE_ORG_TREE:
                Log.e("error", "机构树未实现", new NullPointerException("机构树未实现"));
                break;
            default:
                intent.setClass(context, ContactListActivity.class);
                break;
        }
        return this;
    }

    public ContentBuilder range(int min, int max) {
        intent.putExtra(PARAM_RANGE_MIN, min);
        intent.putExtra(PARAM_RANGE_MAX, max);
        return this;
    }

    public ContentBuilder type(int type) {
        intent.putExtra(PARAM_VIEW_TYPE, type);
        return this;
    }

    public ContentBuilder list(String ids, String names) {
        intent.putExtra(PARAM_LIST_IDS, ids);
        intent.putExtra(PARAM_LIST_NAMES, names);
        return this;
    }

    public ContentBuilder list(String ids, String names, String photos) {
        intent.putExtra(PARAM_LIST_IDS, ids);
        intent.putExtra(PARAM_LIST_NAMES, names);
        intent.putExtra(PARAM_LIST_PHOTOS, photos);
        return this;
    }

    public ContentBuilder list(String ids, String names, String photos, String depts, String orgs) {
        intent.putExtra(PARAM_LIST_IDS, ids);
        intent.putExtra(PARAM_LIST_NAMES, names);
        intent.putExtra(PARAM_LIST_PHOTOS, photos);
        intent.putExtra(PARAM_LIST_DEPTS, depts);
        intent.putExtra(PARAM_LIST_ORGS, orgs);
        return this;
    }

    public ContentBuilder selected(String ids) {
        intent.putExtra(PARAM_LIST_IDS, ids);
        return this;
    }

    public Intent intent() {
        return intent;
    }


}
