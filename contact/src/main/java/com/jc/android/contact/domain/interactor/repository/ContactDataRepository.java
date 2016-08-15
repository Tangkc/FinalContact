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
package com.jc.android.contact.domain.interactor.repository;

import android.content.Context;

import com.jc.android.contact.data.datasource.ContactDataStore;
import com.jc.android.contact.data.datasource.ContactDataStoreFactory;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.data.entity.Dept;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * {@link ContactRepository} for retrieving user data.
 */
public class ContactDataRepository implements ContactRepository {

	private ContactDataStoreFactory demoDataStoreFactory;

	public ContactDataRepository(Context appContext) {
		this(new ContactDataStoreFactory(appContext));
	}

	/**
	 * Constructs a {@link ContactRepository}.
	 *
	 * @param dataStoreFactory     A factory to construct different data source implementations.
	 */
	public ContactDataRepository(ContactDataStoreFactory dataStoreFactory) {
		this.demoDataStoreFactory = dataStoreFactory;
	}

	public void setDemoDataStoreFactory(ContactDataStoreFactory demoDataStoreFactory) {
		this.demoDataStoreFactory = demoDataStoreFactory;
	}


	@Override
	public Observable<List<Contact>> getPeoples(String id) {
		//we always get all users from the cloud
		final ContactDataStore demoDataStore = this.demoDataStoreFactory.createCloudDataStore();

		return demoDataStore.userEntityList(id)
				.map(new Func1<List<Contact>, List<Contact>>() {
					@Override
					public List<Contact> call(List<Contact> userEntities) {
						return userEntities;
					}
				});
	}

	@SuppressWarnings("Convert2MethodRef")
	@Override
	public Observable<Contact> user(String userId) {
		final ContactDataStore demoDataStore = this.demoDataStoreFactory.create(userId);
		return demoDataStore.userEntityDetails(userId)
				.map(new Func1<Contact, Contact>() {
					@Override
					public Contact call(Contact demoEntity) {
						return demoEntity;
					}
				});
	}

	@Override
	public Observable<List<Dept>> getDeptUser(String id) {
		//we always get all users from the cloud
		final ContactDataStore demoDataStore = this.demoDataStoreFactory.createCloudDataStore();

		return demoDataStore.deptUserList(id);
	}
}
