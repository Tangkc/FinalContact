/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jc.android.template.presentation.mapper;

import com.jc.android.template.data.entity.ContactEntity;
import com.jc.android.template.presentation.model.ContactModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Mapper class used to transform {@link ContactEntity} (in the domain layer) to {@link ContactModel} in the
 * presentation layer.
 */
public class ContactModelDataMapper {

  public ContactModelDataMapper() {}

  /**
   * Transform a {@link ContactEntity} into an {@link ContactModel}.
   *
   * @param demo Object to be transformed.
   * @return {@link ContactModel}.
   */
  public ContactModel transformUser(ContactEntity demo) {
    if (demo == null) {
      throw new IllegalArgumentException("Cannot transform a null value");
    }
    ContactModel demoModel = new ContactModel();
    demoModel.setId(demo.getId());
    demoModel.setPhoto(demo.getPhoto());
    demoModel.setDeptId(demo.getDeptId());
    demoModel.setMobile(demo.getMobile());
    demoModel.setOrderNo(demo.getOrderNo());
    demoModel.setDisplayName(demo.getDisplayName());

    return demoModel;
  }

  /**
   * Transform a Collection of {@link ContactEntity} into a Collection of {@link ContactModel}.
   *
   * @param usersCollection Objects to be transformed.
   * @return List of {@link ContactModel}.
   */
  public Collection<ContactModel> transformUsers(Collection<ContactEntity> usersCollection) {
    Collection<ContactModel> demoModelsCollection;

    if (usersCollection != null && !usersCollection.isEmpty()) {
      demoModelsCollection = new ArrayList<>();
      for (ContactEntity demo : usersCollection) {
        demoModelsCollection.add(transformUser(demo));
      }
    } else {
      demoModelsCollection = Collections.emptyList();
    }

    return demoModelsCollection;
  }
}
