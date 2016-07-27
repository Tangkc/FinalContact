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
package com.jc.android.template.domain.interactor.repository;

import android.content.Context;

import com.jc.android.template.data.datasource.DemoDataStore;
import com.jc.android.template.data.datasource.DemoDataStoreFactory;
import com.jc.android.template.data.entity.DemoEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * {@link UserRepository} for retrieving user data.
 */
public class UserDataRepository implements UserRepository {

	private DemoDataStoreFactory demoDataStoreFactory;

	public UserDataRepository(Context appContext) {
		this(new DemoDataStoreFactory(appContext));
	}

	/**
	 * Constructs a {@link UserRepository}.
	 *
	 * @param dataStoreFactory     A factory to construct different data source implementations.
	 */
	public UserDataRepository(DemoDataStoreFactory dataStoreFactory) {
		this.demoDataStoreFactory = dataStoreFactory;
	}

	public void setDemoDataStoreFactory(DemoDataStoreFactory demoDataStoreFactory) {
		this.demoDataStoreFactory = demoDataStoreFactory;
	}


	@SuppressWarnings("Convert2MethodRef")
	@Override
	public Observable<List<DemoEntity>> users() {
		//we always get all users from the cloud
		final DemoDataStore demoDataStore = this.demoDataStoreFactory.createCloudDataStore();

		return demoDataStore.userEntityList()
				.map(new Func1<List<DemoEntity>, List<DemoEntity>>() {
					@Override
					public List<DemoEntity> call(List<DemoEntity> userEntities) {
						return userEntities;
					}
				});
	}

	@SuppressWarnings("Convert2MethodRef")
	@Override
	public Observable<DemoEntity> user(int userId) {
		final DemoDataStore demoDataStore = this.demoDataStoreFactory.create(userId);
		return demoDataStore.userEntityDetails(userId)
				.map(new Func1<DemoEntity, DemoEntity>() {
					@Override
					public DemoEntity call(DemoEntity demoEntity) {
						return demoEntity;
					}
				});
	}
}
