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

import com.jc.android.base.presentation.App;
import com.jc.android.component.config.domain.interactor.GetConfig;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.presentation.model.ContactModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Mapper class used to transform {@link Contact} (in the domain layer) to {@link ContactModel} in the
 * presentation layer.
 */
public class ContactModelDataMapper {

    public ContactModelDataMapper() {
    }

    /**
     * Transform a {@link Contact} into an {@link ContactModel}.
     *
     * @param contact Object to be transformed.
     * @return {@link ContactModel}.
     */
    public ContactModel transformUser(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }
        GetConfig getConfig = new GetConfig(App.context());
        String businessServer = getConfig.buildUseCase().getBusinessServer();
        ContactModel contactModel = new ContactModel();
        contactModel.setId(contact.getId());
        contactModel.setPhoto(contact.getPhoto().length() > 0 ? businessServer + contact.getPhoto() : null);
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

    /**
     * Transform a Collection of {@link Contact} into a Collection of {@link ContactModel}.
     *
     * @param usersCollection Objects to be transformed.
     * @return List of {@link ContactModel}.
     */
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
}
