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

import com.jc.android.template.data.entity.DemoEntity;
import com.jc.android.template.presentation.model.DemoModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Mapper class used to transform {@link DemoEntity} (in the domain layer) to {@link DemoModel} in the
 * presentation layer.
 */
public class DemoModelDataMapper {

  public DemoModelDataMapper() {}

  /**
   * Transform a {@link DemoEntity} into an {@link DemoModel}.
   *
   * @param demo Object to be transformed.
   * @return {@link DemoModel}.
   */
  public DemoModel transformUser(DemoEntity demo) {
    if (demo == null) {
      throw new IllegalArgumentException("Cannot transform a null value");
    }
    DemoModel demoModel = new DemoModel(demo.getUserId());
    demoModel.setCoverUrl(demo.getCoverUrl());
    demoModel.setFullName(demo.getFullName());
    demoModel.setEmail(demo.getEmail());
    demoModel.setDescription(demo.getDescription());
    demoModel.setFollowers(demo.getFollowers());

    return demoModel;
  }

  /**
   * Transform a Collection of {@link DemoEntity} into a Collection of {@link DemoModel}.
   *
   * @param usersCollection Objects to be transformed.
   * @return List of {@link DemoModel}.
   */
  public Collection<DemoModel> transformUsers(Collection<DemoEntity> usersCollection) {
    Collection<DemoModel> demoModelsCollection;

    if (usersCollection != null && !usersCollection.isEmpty()) {
      demoModelsCollection = new ArrayList<>();
      for (DemoEntity demo : usersCollection) {
        demoModelsCollection.add(transformUser(demo));
      }
    } else {
      demoModelsCollection = Collections.emptyList();
    }

    return demoModelsCollection;
  }
}
