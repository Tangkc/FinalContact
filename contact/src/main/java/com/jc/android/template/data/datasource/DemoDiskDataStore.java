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
package com.jc.android.template.data.datasource;

import com.jc.android.template.data.cache.DemoCache;
import com.jc.android.template.data.entity.DemoEntity;

import java.util.List;
import rx.Observable;

/**
 * {@link DemoDataStore} implementation based on file system data store.
 */
public class DemoDiskDataStore implements DemoDataStore {

  private final DemoCache demoCache;

  /**
   * Construct a {@link DemoDataStore} based file system data store.
   *
   * @param demoCache A {@link DemoCache} to cache data retrieved from the api.
   */
  public DemoDiskDataStore(DemoCache demoCache) {
    this.demoCache = demoCache;
  }

  @Override public Observable<List<DemoEntity>> userEntityList() {
    //TODO: implement simple cache for storing/retrieving collections of users.
    throw new UnsupportedOperationException("Operation is not available!!!");
  }

  @Override public Observable<DemoEntity> userEntityDetails(final int userId) {
     return this.demoCache.get(userId);
  }
}
