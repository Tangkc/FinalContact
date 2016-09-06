package com.jc.android.contact.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jc.android.base.presentation.App;
import com.jc.android.base.presentation.navigation.ActivityNavigator;
import com.jc.android.base.presentation.view.fragment.BaseFragment;
import com.jc.android.base.presentation.viewmodel.ViewModel;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.data.entity.Dept;
import com.jc.android.contact.domain.interactor.GetDeptContactList;
import com.jc.android.contact.presentation.ContactTreeBinding;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.view.activity.ContactDetailsActivity;
import com.jc.android.contact.presentation.view.holder.IconTreeItemHolder;
import com.jc.android.contact.presentation.viewmodel.ContactListViewModel;
import com.jc.android.contact.presentation.viewmodel.ProcessErrorSubscriber;
import com.jc.android.module.contact.R;
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
public class ContactTreeFragment extends BaseFragment<ViewModel, ContactTreeBinding> {

    public final static String TAG = ContactTreeFragment.class.getSimpleName();
    private AndroidTreeView tView;
    private TreeNode root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setBinding(DataBindingUtil.<ContactTreeBinding>inflate(inflater, R.layout.fragment_contact_tree, container, true));

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
        getDeptContactList.setId("1");
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
            if (item.type == 1) {
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
        for (Dept dept:deptList) {
            List<Dept> list = map.get(dept.getParentId());
            if (list==null) {
                list = new LinkedList<>();
                map.put(dept.getParentId(), list);
            }

            list.add(dept);
        }

        fillMap(root, map, 0);
    }

    private void fillMap(TreeNode parent, Map<Long, List<Dept>> map, long parentId) {
        List<Dept> list = map.get(parentId);
        if (list!=null) {
            for (Dept dept:list) {
                TreeNode child = new TreeNode(new IconTreeItemHolder.IconTreeItem(dept.getDeptType() == 1 ? R.string.ic_hotel : R.string.ic_flag, dept.getName(), dept.getId(),0));
                parent.addChild(child);

                fillMap(child, map, dept.getId());

                //add person
                if(dept.getUserList()!=null){
                    for (Contact contact:dept.getUserList()) {
                        TreeNode person = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, contact.getDisplayName(), contact.getId(), 1));
                        child.addChild(person);
                    }
                }
            }
        }
    }
}
