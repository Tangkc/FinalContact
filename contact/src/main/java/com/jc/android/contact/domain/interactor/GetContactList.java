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
package com.jc.android.contact.domain.interactor;


import android.content.Context;

import com.jc.android.base.domain.interactor.UseCase;
import com.jc.android.contact.domain.interactor.repository.ContactDataRepository;
import com.jc.android.contact.domain.interactor.repository.ContactRepository;

import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 *
 */
public class GetContactList extends UseCase {
	private String id;
	ContactRepository userRepository;

	public GetContactList(Context appContext) {
		this(new ContactDataRepository(appContext));
	}

	public GetContactList(ContactRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setUserRepository(ContactRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Observable buildUseCaseObservable() {
		return this.userRepository.getPeoples(this.id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
