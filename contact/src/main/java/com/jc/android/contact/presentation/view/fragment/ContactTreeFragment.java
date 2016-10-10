package com.jc.android.contact.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnkil.print.PrintView;
import com.jc.android.base.presentation.App;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.view.fragment.BaseFragment;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.data.entity.Dept;
import com.jc.android.contact.domain.interactor.GetDeptContactList;
import com.jc.android.contact.presentation.ContactTreeBinding;
import com.jc.android.contact.presentation.view.activity.ContactCenterActivity;
import com.jc.android.contact.presentation.view.activity.ContactDetailsActivity;
import com.jc.android.contact.presentation.view.activity.ContentBuilder;
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


    GetUser getUser = new GetUser(App.context());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setViewModel(new ContactListViewModel(getActivity()));
        setBinding(DataBindingUtil.<ContactTreeBinding>inflate(inflater, R.layout.fragment_contact_tree, container, false));
        getBinding().setViewModel(getViewModel());
        View rootView = getBinding().getRoot();
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


                containerView.addView(tView.getView());
            }
        });
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            if (item.type == 1 && !(ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE)) {
                Intent intent = ContactDetailsActivity.getCallingIntent(App.instance().getCurrentActivity(), item.id);
                ActivityNavigator.to(ContactDetailsActivity.class, intent);
            }
        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
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

    private void fillMap(TreeNode parent, Map<Long, List<Dept>> map, long parentId) {
        List<Dept> list = map.get(parentId);
        if (list != null) {
            for (Dept dept : list) {
                TreeNode child = new TreeNode(new IconTreeItemHolder.IconTreeItem(dept.getDeptType() == 1 ? R.string.ic_hotel : R.string.ic_flag, dept.getName(), dept.getId(), 0));
                parent.addChild(child);

                fillMap(child, map, dept.getId());

                //add person
                if (dept.getUserList() != null) {
                    for (Contact contact : dept.getUserList()) {
                        TreeNode person = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, contact.getDisplayName(), contact.getId(), 1));
                        child.addChild(person);
                    }
                }
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
//////////////////////////////////////////////////////华丽分割线///////////////////////////////////////////////////////////////


    public static class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
        private TextView tvValue;
        private PrintView arrowView;
        private CheckBox checkBox;
        List<TreeNode> treeNodes = new ArrayList<>();

        public IconTreeItemHolder(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(final TreeNode node, IconTreeItem value) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.layout_icon_node, null, false);


            tvValue = (TextView) view.findViewById(R.id.node_value);
            tvValue.setText(value.text);
            // 控制选择框的显隐
            if (ContactCenterActivity.viewType == ContentBuilder.VIEW_TYPE_MULTIPLE) {
                checkBox = (CheckBox) view.findViewById(R.id.icon_check_box);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!node.isLeaf()) {
                            treeNodes = node.getChildren();
                            for (TreeNode treeNode : treeNodes) {
                                // TODO: 2016/10/10 更新状态
// 获取子节点视图中的CheckBox设置为被选中状态

                                treeNode.setSelected(true);
                                updateCheckBox(treeNode);
                            }
                        } else if (node.isLeaf()) {
                            updateCheckBox(node);
                        }
                    }
                });
            }
            final PrintView iconView = (PrintView) view.findViewById(R.id.icon);
            iconView.setIconText(context.getResources().getString(value.icon));

            arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
            if (node.isLeaf()) {
                arrowView.setVisibility(View.INVISIBLE);
            }
            return view;
        }

        //更新选择框状态
        public void updateCheckBox(TreeNode node) {
            if (node.isSelected()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }

        @Override
        public void toggle(boolean active) {
            arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
        }

        public static class IconTreeItem {
            public int icon;
            public String text;
            public long id;
            public int type;

            public IconTreeItem(int icon, String text, long id, int type) {
                this.icon = icon;
                this.text = text;
                this.id = id;
                this.type = type;
            }
        }
    }

}
