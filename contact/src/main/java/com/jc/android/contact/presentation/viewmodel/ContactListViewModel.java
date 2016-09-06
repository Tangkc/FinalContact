package com.jc.android.contact.presentation.viewmodel;


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
import android.widget.Toast;

import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.viewmodel.LoadingViewModel;
import com.jc.android.base.presentation.App;
import com.jc.android.logon.domain.interactor.GetUser;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.domain.interactor.GetContactList;
import com.jc.android.contact.presentation.mapper.ContactModelDataMapper;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.activity.ContactDetailsActivity;
import com.jc.android.contact.presentation.wigth.CharacterParser;
import com.jc.android.contact.presentation.wigth.ClearEditText;
import com.jc.android.contact.presentation.wigth.PinyinComparator;
import com.jc.android.contact.presentation.wigth.SideBar;
import com.jc.android.contact.presentation.view.adapter.SortGroupMemberAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rocko on 15-11-5.
 */
public class ContactListViewModel extends LoadingViewModel  {
    private final static String TAG = ContactListViewModel.class.getSimpleName();

    public final ObservableBoolean showContentList = new ObservableBoolean(false);
    public final ObservableField<SortGroupMemberAdapter> adapter = new ObservableField<>();
    private List<ContactModel> SourceDateList = new ArrayList<>();
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    public ListView sortListView;
    public LinearLayout titleLayout;
    private TextView title;
    GetContactList getUserList = new GetContactList(App.context());
    ContactModelDataMapper demoModelDataMapper = new ContactModelDataMapper();
    GetUser getUser = new GetUser(App.context());

    @Command
    public void loadUsersCommand(final ListView sortListView, TextView title, LinearLayout titleLayout) {
        this.sortListView = sortListView;
        this.title = title;
        this.titleLayout = titleLayout;
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        if (showLoading.get()) {
            return;
        }
        showLoading();
        getUserList.setId(getUser.buildUseCaseObservable().getId() + "");
        getUserList.execute(new ProcessErrorSubscriber<List<Contact>>(App.context()) {
            @Override
            public void onNext(List<Contact> demos) {
                List<ContactModel> demoModelsCollection = (List<ContactModel>) demoModelDataMapper.transformUsers(demos);
                SourceDateList = filledData(demoModelsCollection);
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter.set(new SortGroupMemberAdapter(App.context(), SourceDateList));
                sortListView.setAdapter(adapter.get());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showRetry();
            }

        });

    }

    /**
     * /设置右侧触摸监听
     *
     * @return
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
     *
     * @return
     */
    public ListView.OnItemClickListener sortListViewOnItemClick() {
        return new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = ContactDetailsActivity.getCallingIntent(App.instance().getCurrentActivity(), ((ContactModel) adapter.get().getItem(position)).getId());
                ActivityNavigator.to(ContactDetailsActivity.class,intent);
            }
        };
    }

    /**
     * ListView滑动事件
     *
     * @return
     */

    public ListView.OnScrollListener sortListViewOnScrollClick() {
        return new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0 && SourceDateList.size() > 0) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
                    params.topMargin = 0;
                    titleLayout.setLayoutParams(params);
                    title.setText(SourceDateList.get(getPositionForSection(getSectionForPosition(firstVisibleItem))).getSortLetters());
                }

                if (firstVisibleItem != 0) {
                    int section = getSectionForPosition(firstVisibleItem);
                    int nextSection = getSectionForPosition(firstVisibleItem + 1);
                    int nextSecPosition = getPositionForSection(+nextSection);

                    if (firstVisibleItem != lastFirstVisibleItem) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout.getLayoutParams();
                        params.topMargin = 0;
                        titleLayout.setLayoutParams(params);
                        title.setText(SourceDateList.get(getPositionForSection(section)).getSortLetters());
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
     * 输入框变化监听
     *
     * @return
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
                loadUsersCommand(sortListView, title, titleLayout);

            }
        };
    }


    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<ContactModel> filledData(List<ContactModel> date) {
        List<ContactModel> mSortList = new ArrayList<>();

        for (int i = 0; i < date.size(); i++) {
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getDisplayName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                date.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                date.get(i).setSortLetters("#");
            }

            mSortList.add(date.get(i));
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    public void filterData(String filterStr) {
        filterStr=filterStr.toLowerCase();
        List<ContactModel> filterDateList = new ArrayList<ContactModel>();

        if (TextUtils.isEmpty(filterStr)) {
            titleLayout.setVisibility(View.VISIBLE);
            filterDateList = SourceDateList;
            showContentList.set(false);
        } else {
            filterDateList.clear();
            for (ContactModel sortModel : SourceDateList) {
                String name = sortModel.getDisplayName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.get().updateListView(filterDateList);
        if (filterDateList.size() == 0) {
            showContentList.set(true);
        }
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }



    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }


}
