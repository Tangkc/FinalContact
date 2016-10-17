package com.jc.android.contact.presentation.view.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jc.android.base.presentation.App;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.view.fragment.BaseFragment;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.data.entity.Dept;
import com.jc.android.contact.domain.interactor.GetDeptContactList;
import com.jc.android.contact.presentation.ContactTreeBinding;
import com.jc.android.contact.presentation.mapper.ContactModelDataMapper;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.activity.ContactCenterActivity;
import com.jc.android.contact.presentation.view.activity.ContactDetailsActivity;
import com.jc.android.contact.presentation.view.activity.ContentBuilder;
import com.jc.android.contact.presentation.view.holder.IconTreeItemHolder;
import com.jc.android.contact.presentation.view.holder.IconTreeItemHolder.IconTreeItem;
import com.jc.android.contact.presentation.view.holder.SelectableHeaderHolder;
import com.jc.android.contact.presentation.view.holder.SelectableItemHolder;
import com.jc.android.contact.presentation.view.interfaces.IContactInterface;
import com.jc.android.contact.presentation.viewmodel.ContactListViewModel;
import com.jc.android.logon.domain.interactor.GetUser;
import com.jc.android.module.contact.R;
import com.jc.android.widget.presentation.viewmodel.ProcessErrorSubscriber;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Fragment that shows a list of Users.
 */
public class ContactTreeFragment extends BaseFragment<ContactListViewModel, ContactTreeBinding>
        implements IContactInterface {

    public final static String TAG = ContactTreeFragment.class.getSimpleName();
    private AndroidTreeView tView;
    private TreeNode root;
    private boolean isSelect;                 //是否进行人员选择
    public static List<ContactModel> listContact = new ArrayList<>();
    List<TreeNode> list = new ArrayList<>();
    ContactModelDataMapper contactModelDataMapper = new ContactModelDataMapper();
    GetUser getUser = new GetUser(App.context());


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setViewModel(new ContactListViewModel(getActivity()));
        setBinding(DataBindingUtil.<ContactTreeBinding>inflate(inflater, R.layout.fragment_contact_tree, container, false));
        getBinding().setViewModel(getViewModel());
        View rootView = getBinding().getRoot();

        //是否进行人员选择
        if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE || ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE)
            isSelect = true;

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GetDeptContactList getDeptContactList = new GetDeptContactList(App.context());
        getDeptContactList.setId(ContactCenterActivity.isOrgSplit ? String.valueOf(getUser.buildUseCaseObservable().getId()) : "1");
        getDeptContactList.execute(new ProcessErrorSubscriber<List<Dept>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(List<Dept> deptList) {
                ViewGroup containerView = getBinding().container;
                root = TreeNode.root();

                assembleTreeWithMap(deptList, root);
                tView = new AndroidTreeView(getActivity(), root);

                tView.setDefaultAnimation(true);
                tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                tView.setDefaultViewHolder(IconTreeItemHolder.class);


                tView.setDefaultNodeClickListener(nodeClickListener);
                tView.setDefaultNodeLongClickListener(nodeLongClickListener);
                //TreeNode 是否能选择
                if (isSelect)
                    tView.setSelectionModeEnabled(true);
                containerView.addView(tView.getView());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    //默认点击事件
    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItem item = (IconTreeItem) value;
            if (item.type == 1 && !isSelect) {
                Intent intent = ContactDetailsActivity.getCallingIntent(App.instance().getCurrentActivity(), item.id);
                ActivityNavigator.to(ContactDetailsActivity.class, intent);
            }
        }
    };

    //选择时人的点击事件
    private TreeNode.TreeNodeClickListener personClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            boolean isCheck = node.isSelected();
            if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE) {
                if (isCheck) {
                    tView.selectNode(node, false);
                    setValueToMap(node, false);
                } else {
                    tView.selectNode(node, true);
                    setValueToMap(node, true);
                }
            }
            if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_SINGLE) {
                if (!isCheck) {
                    for (TreeNode treeNode : tView.getSelected()) {
                        tView.selectNode(treeNode, false);
                    }
                    ContactCenterActivity.selected.clear();
                }
                tView.selectNode(node, true);
                setValueToMap(node, true);
            }
        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            IconTreeItem item = (IconTreeItem) value;
//            Toast.makeText(getActivity(), "Long click: " + item.text, Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    private void assembleTreeWithMap(@NonNull List<Dept> deptList, @NonNull TreeNode root) {

        Map<Long, List<Dept>> map = new HashMap<>();
        for (Dept dept : deptList) {
            List<Dept> list = map.get(dept.getParentId());
            if (list == null) {
                list = new LinkedList<>();
                map.put(dept.getParentId(), list);
            }

            list.add(dept);
        }

        fillMap(root, map, 0);
    }

    /**
     * 填充树
     *
     * @param parent
     * @param map
     * @param parentId
     */
    private void fillMap(TreeNode parent, Map<Long, List<Dept>> map, long parentId) {
        List<Dept> list = map.get(parentId);
        if (list != null) {
            for (Dept dept : list) {
                TreeNode child = new TreeNode(new IconTreeItem(dept.getDeptType() == 1 ? R.string.ic_hotel : R.string.ic_flag, dept.getName(), dept.getId(), 0));
                //进行人员选择时给TreeNode setViewHolder
                if (isSelect)
                    child.setViewHolder(new SelectableHeaderHolder(getActivity()));
                parent.addChild(child);

                fillMap(child, map, dept.getId());

                //add person
                if (dept.getUserList() != null) {
                    for (Contact contact : dept.getUserList()) {
                        TreeNode person = new TreeNode(new IconTreeItem(R.string.ic_person, contact.getDisplayName(), contact.getId(), 1));
                        //进行人员选择时给TreeNode setViewHolder
                        if (isSelect) {
                            // TODO: 2016/10/14 解决匹配问题 
                            listContact.addAll(contactModelDataMapper.transformUsers(dept.getUserList()));
                            person.setViewHolder(new SelectableItemHolder(getActivity()));
                            for (Long key : ContactCenterActivity.selected.keySet()) {
                                if (contact.getId() == ContactCenterActivity.selected.get(key).getId())
                                    person.setSelected(true);
                            }
                            person.setClickListener(personClickListener);
                        }
                        child.addChild(person);
                    }
                }
            }
        }
    }


    public void setValueToMap(TreeNode node, boolean isChecked) {
        for (int j = 0; j < ContactTreeFragment.listContact.size(); j++) {
            if (((IconTreeItemHolder.IconTreeItem) node.getValue()).id == ContactTreeFragment.listContact.get(j).getId()) {
                if (isChecked)
                    ContactCenterActivity.selected.put(ContactTreeFragment.listContact.get(j).getId(), ContactTreeFragment.listContact.get(j));
                else
                    ContactCenterActivity.selected.remove(ContactTreeFragment.listContact.get(j).getId());
            }
        }
    }

    @Override
    public void selectAll() {
        getViewModel().selectAll();
    }

    @Override
    public void cancelAll() {
        getViewModel().cancelAll();
    }

    /**
     * 覆盖activity的菜单栏
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.fragment_save == item.getItemId()) {
            StringBuilder stringIds = new StringBuilder();
            StringBuilder stringNames = new StringBuilder();
            list = tView.getSelected();
            //防止选择根节点时，下面没有人员，会把管理员选上
            if (0 != list.size() && !list.get(0).getChildren().isEmpty())
                list.remove(0);
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < listContact.size(); j++) {
                    if (((IconTreeItem) list.get(i).getValue()).id == listContact.get(j).getId()) {
                        ContactCenterActivity.selected.put(listContact.get(j).getId(), listContact.get(j));
                    }
                }
            }

            if (ContactCenterActivity.rangeMin > 0 && ContactCenterActivity.rangeMin > ContactCenterActivity.selected.size()) {
                Toast.makeText(getActivity(), "最少选择" + ContactCenterActivity.rangeMin + "条数据", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (ContactCenterActivity.rangeMax > 0 && ContactCenterActivity.rangeMax < ContactCenterActivity.selected.size()) {
                Toast.makeText(getActivity(), "最多选择" + ContactCenterActivity.rangeMax + "条数据", Toast.LENGTH_SHORT).show();
                return true;
            }
            for (ContactModel model : ContactCenterActivity.selected.values()) {

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
            intent.putExtra(ContactCenterActivity.SELECTED_IDS, stringIds.toString());
            intent.putExtra(ContactCenterActivity.SELECTED_NAMES, stringNames.toString());
            getActivity().setResult(ContactCenterActivity.SELECTED_CONFIRM, intent);
            ContactCenterActivity.selected.clear();
            getActivity().finish();
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.unselect_all).setVisible(false);
        menu.findItem(R.id.select_all).setVisible(false);
        menu.findItem(R.id.save).setVisible(false);
        menu.findItem(R.id.show_flatten).setVisible(true);
        menu.findItem(R.id.show_tree).setVisible(false);
        if (isSelect)
            inflater.inflate(R.menu.menu_treeselect_center, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
