package com.jc.android.contact.presentation.view.activity;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ContentBuilder {

    // PARAM_TITLE
    public static final String PARAM_TITLE = "PARAM_TITLE";

    // RAGE
    public static final String PARAM_PAGE_TYPE = "PARAM_PAGE_TYPE";

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

    // 机构隔离
    public static final String PARAM_ORG_SPLIT = "PARAM_ORG_SPLIT";

    // 页面类型
    public static final int PAGE_USER_LIST = 0; // 人员列表
    public static final int PAGE_USER_TREE = 1; // 人员树
    public static final int PAGE_ORG_LIST = 2;  // 机构列表
    public static final int PAGE_ORG_TREE = 3;  // 机构树

    // 选择类型
    public static final int VIEW_TYPE_SHOW = 0;       // 展示, 通讯录
    public static final int VIEW_TYPE_SINGLE = 1;     // 单选
    public static final int VIEW_TYPE_MULTIPLE = 2;   // 多选

    private final Intent intent;
    private final Context context;

    public ContentBuilder(Context context) {
        this.context = context;
        intent = new Intent(context, ContactCenterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    /**
     * 页面类型
     */
    public ContentBuilder page(int page) {
        intent.putExtra(PARAM_PAGE_TYPE, page);
        return this;
    }

    /**
     * 选择数量的范围
     *
     * @param min 最小数量
     * @param max 最大数量
     */
    public ContentBuilder range(int min, int max) {
        intent.putExtra(PARAM_RANGE_MIN, min);
        intent.putExtra(PARAM_RANGE_MAX, max);
        return this;
    }

    /**
     * 选择类型
     */
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
        intent.putExtra(PARAM_SELECTED_IDS, ids);
        return this;
    }

    public ContentBuilder title(String title) {
        intent.putExtra(PARAM_TITLE, title);
        return this;
    }

    public ContentBuilder orgSplit(boolean isOrgSplit) {
        intent.putExtra(PARAM_ORG_SPLIT, isOrgSplit);
        return this;
    }

    public Intent intent() {
        return intent;
    }


}
