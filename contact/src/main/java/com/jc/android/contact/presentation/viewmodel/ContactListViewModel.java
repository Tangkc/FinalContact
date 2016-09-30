package com.jc.android.contact.presentation.viewmodel;


import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.viewmodel.LoadingViewModel;
import com.jc.android.base.presentation.App;
import com.jc.android.contact.presentation.view.activity.ContactCenterActivity;
import com.jc.android.contact.presentation.view.activity.ContentBuilder;
import com.jc.android.logon.domain.interactor.GetUser;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.domain.interactor.GetContactList;
import com.jc.android.contact.presentation.mapper.ContactModelDataMapper;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.activity.ContactDetailsActivity;
import com.jc.android.contact.presentation.widget.ClearEditText;
import com.jc.android.contact.presentation.widget.SideBar;
import com.jc.android.contact.presentation.view.adapter.SortGroupMemberAdapter;
import com.jc.android.widget.presentation.viewmodel.ProcessErrorSubscriber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContactListViewModel extends LoadingViewModel {
    private final static String TAG = ContactListViewModel.class.getSimpleName();

    public final ObservableBoolean showContentList = new ObservableBoolean(false);
    public final ObservableField<SortGroupMemberAdapter> adapter = new ObservableField<>();
    private List<ContactModel> sourceDateList = new ArrayList<>();

    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;


    public ListView sortListView;
    public LinearLayout titleLayout;
    private TextView title;

    GetContactList getUserList = new GetContactList(App.context());
    ContactModelDataMapper contactModelDataMapper = new ContactModelDataMapper();
    GetUser getUser = new GetUser(App.context());

    private Activity activity;
    public ContactListViewModel(Activity activity) {
        this.activity = activity;
    }

    @Command
    public void loadContactsCommand(final ListView sortListView, TextView title, LinearLayout titleLayout) {
        this.sortListView = sortListView;
        this.title = title;
        this.titleLayout = titleLayout;

        if (TextUtils.isEmpty(ContactCenterActivity.listIds)) {
            loadContactsCloud();
        } else {
            loadContactsWithList();
        }
    }



    private void loadContactsCloud() {

        if (showLoading.get()) {
            return;
        }

        showLoading();
        getUserList.setId(ContactCenterActivity.isOrgSplit ? String.valueOf(getUser.buildUseCaseObservable().getId()) : "1");
        getUserList.execute(new ProcessErrorSubscriber<List<Contact>>() {
            @Override
            public void onNext(List<Contact> contacts) {
                initDate(contactModelDataMapper.transformUsersWithLetter(contacts));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showRetry();
            }

        });

    }

    private void loadContactsWithList() {

        List<Contact> contacts = new ArrayList<>();

        String[] ids = ContactCenterActivity.listIds.split(",");
        String[] names = ContactCenterActivity.listNames.split(",");
        int length = Math.min(ids.length, names.length);
        for (int i=0; i<length; ++i) {
            if (TextUtils.isEmpty(ids[i]) || TextUtils.isEmpty(names[i])) {
                continue;
            }

            try {
                Contact model = new Contact();
                model.setId(Long.valueOf(ids[i]));
                model.setDisplayName(names[i]);
                contacts.add(model);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initDate(contactModelDataMapper.transformUsersWithLetter(contacts));
    }

    public void initDate(List<ContactModel> contacts) {

        // TODO 可以优化
        // 初始化选中
        if (!ContactCenterActivity.isInitSelected) {
            String[] selectedIds = ContactCenterActivity.selectedIds.split(",");
            for (String id : selectedIds) {
                if (TextUtils.isEmpty(id)) {
                    continue;
                }

                for (ContactModel model:contacts) {
                    if (id.equals(String.valueOf(model.getId()))) {
                        if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE && ContactCenterActivity.selected.size()>0) {
                            break;
                        }
                        ContactCenterActivity.selected.put(model.getId(), model);
                    }
                }

            }
            ContactCenterActivity.isInitSelected = true;

        } else {
            // 切换视图的选中初始化
            Collection<ContactModel> selectedModels = ContactCenterActivity.selected.values();
            ContactCenterActivity.selected.clear();

            for (ContactModel selected:selectedModels) {
                for (ContactModel model:contacts) {
                    if (selected.getId() == model.getId()) {
                        if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE && ContactCenterActivity.selected.size()>0) {
                            break;
                        }
                        ContactCenterActivity.selected.put(model.getId(), model);
                    }

                }
            }

        }

        // 初始化数据
        sourceDateList.clear();
        sourceDateList.addAll(contacts);

        // 展示页面
        adapter.set(new SortGroupMemberAdapter(App.context(), sourceDateList));
        sortListView.setAdapter(adapter.get());
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return sourceDateList.get(position).getSortLetters().charAt(0);
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < sourceDateList.size(); i++) {
            String sortStr = sourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 设置右侧触摸监听
     */
    public SideBar.OnTouchingLetterChangedListener sideOnTouchingLetterChange() {
        return new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.get().getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        };
    }

    /**
     * ListView单项点击事件
     */
    public ListView.OnItemClickListener sortListViewOnItemClick() {
        return new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = ContactDetailsActivity.getCallingIntent(App.instance().getCurrentActivity(), ((ContactModel) adapter.get().getItem(position)).getId());
//                ActivityNavigator.to(ContactDetailsActivity.class, intent);

                Log.i("data11", "onItemClick");
                if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SHOW) {
                    Intent intent = ContactDetailsActivity.getCallingIntent(App.instance().getCurrentActivity(), ((ContactModel) adapter.get().getItem(position)).getId());
                    ActivityNavigator.to(ContactDetailsActivity.class, intent);
                } else if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE || ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE) {
                    adapter.get().onItemSelected(position);
                }
            }
        };
    }

    /**
     * ListView滑动事件
     */

    public ListView.OnScrollListener sortListViewOnScrollClick() {
        return new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0 && sourceDateList.size() > 0) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
                    params.topMargin = 0;
                    titleLayout.setLayoutParams(params);
                    title.setText(sourceDateList.get(getPositionForSection(getSectionForPosition(firstVisibleItem))).getSortLetters());
                }

                if (firstVisibleItem != 0) {
                    int section = getSectionForPosition(firstVisibleItem);
                    int nextSection = getSectionForPosition(firstVisibleItem + 1);
                    int nextSecPosition = getPositionForSection(+nextSection);

                    if (firstVisibleItem != lastFirstVisibleItem) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
                        params.topMargin = 0;
                        titleLayout.setLayoutParams(params);
                        title.setText(sourceDateList.get(getPositionForSection(section)).getSortLetters());
                    }

                    if (nextSecPosition == firstVisibleItem + 1) {
                        View childView = view.getChildAt(0);
                        if (childView != null) {
                            int titleHeight = titleLayout.getHeight();
                            int bottom = childView.getBottom();
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();

                            if (bottom < titleHeight) {
                                float pushedDistance = bottom - titleHeight;
                                params.topMargin = (int) pushedDistance;
                                titleLayout.setLayoutParams(params);
                            } else {
                                if (params.topMargin != 0) {
                                    params.topMargin = 0;
                                    titleLayout.setLayoutParams(params);
                                }
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        }

                ;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    public void filterData(String filterStr) {

        if (TextUtils.isEmpty(filterStr)) {
            titleLayout.setVisibility(View.VISIBLE);
        }

        adapter.get().updateListView(contactModelDataMapper.transformUsersWithFilter(sourceDateList, filterStr));

        showContentList.set(sourceDateList.size() == 0);
    }

    /**
     * 输入框变化监听
     */

    public void mClearEditTextClick(ClearEditText mClearEditText) {

        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                // 这个时候不需要挤压效果 就把他隐藏掉
                titleLayout.setVisibility(View.GONE);
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public View.OnClickListener onRetryClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadContactsCommand(sortListView, title, titleLayout);
            }
        };
    }


    public void selectAll() {
        for (ContactModel model:sourceDateList) {
            ContactCenterActivity.selected.put(model.getId(), model);
        }
        adapter.get().notifyDataSetChanged();
    }

    public void cancelAll() {
        ContactCenterActivity.selected.clear();
        adapter.get().notifyDataSetChanged();
    }

}
