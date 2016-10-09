/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jc.android.contact.presentation.mapper;

import android.text.TextUtils;

import com.jc.android.base.presentation.App;
import com.jc.android.component.config.domain.interactor.GetConfig;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.presentation.model.ContactModel;
import com.jc.android.contact.presentation.widget.CharacterParser;
import com.jc.android.contact.presentation.widget.PinyinComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Mapper class used to transform {@link Contact} (in the domain layer) to {@link ContactModel} in the
 * presentation layer.
 */
public class ContactModelDataMapper {

    /**
     * 汉字转拼音
     */
    private CharacterParser characterParser = CharacterParser.getInstance();

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator = new PinyinComparator();

    public ContactModel transformUser(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        GetConfig getConfig = new GetConfig(App.context());
        String businessServer = getConfig.buildUseCase().getBusinessServer();
        ContactModel contactModel = new ContactModel();
        contactModel.setId(contact.getId());
        contactModel.setPhoto(contact.getPhoto()!=null&&contact.getPhoto().length() > 0 ? businessServer + contact.getPhoto() : null);
        contactModel.setDeptId(contact.getDeptId());
        contactModel.setMobile(contact.getMobile());
        contactModel.setOrderNo(contact.getOrderNo());
        contactModel.setDisplayName(contact.getDisplayName());

        contactModel.setLoginName(contact.getLoginName());
        contactModel.setUserName(contact.getUserName());
        contactModel.setEmail(contact.getEmail());
        contactModel.setDutyId(contact.getDutyId());
        contactModel.setDeptName(contact.getDeptName());
        contactModel.setOrgId(contact.getOrgId());
        contactModel.setOrgName(contact.getOrgName());
        contactModel.setOfficeTel(contact.getOfficeTel());/*办公室电话*/
        contactModel.setDutyIdValue(contact.getDutyIdValue());/*职务*/

        return contactModel;
    }

    public Collection<ContactModel> transformUsers(Collection<Contact> usersCollection) {
        Collection<ContactModel> demoModelsCollection;

        if (usersCollection != null && !usersCollection.isEmpty()) {
            demoModelsCollection = new ArrayList<>();
            for (Contact demo : usersCollection) {
                demoModelsCollection.add(transformUser(demo));
            }
        } else {
            demoModelsCollection = Collections.emptyList();
        }

        return demoModelsCollection;
    }

    /**
     * 汉字列表添加拼音
     */
    public List<ContactModel> transformUsersWithLetter(List<Contact> entities) {
        List<ContactModel> models;

        if (entities != null && !entities.isEmpty()) {
            models = new ArrayList<>();
            for (Contact demo : entities) {
                models.add(transformUser(demo));
            }

            filledData(models);

            // 根据a-z进行排序源数据
            Collections.sort(models, pinyinComparator);

        } else {
            models = Collections.emptyList();
        }

        return models;
    }

    /**
     * 汉字列表添加拼音
     */
    public List<ContactModel> transformUsersWithFilter(List<ContactModel> srcModels, String filterStr) {
        List<ContactModel> distModels = new ArrayList<>();

        if (srcModels != null && !srcModels.isEmpty()) {

            for (ContactModel model : srcModels) {
                String name = model.getDisplayName();
                if (TextUtils.isEmpty(filterStr) || name.contains(filterStr) || characterParser.getSelling(name).startsWith(filterStr)) {
                    distModels.add(model);
                }
            }

            // 根据a-z进行排序源数据
            Collections.sort(distModels, pinyinComparator);
        }

        return distModels;
    }

    /**
     * 为ListView填充数据
     */
    private void filledData(Collection<ContactModel> models) {
        for (ContactModel model:models) {
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(model.getDisplayName());

            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                model.setSortLetters(sortString.toUpperCase());
            } else {
                model.setSortLetters("#");
            }

        }

    }
}
