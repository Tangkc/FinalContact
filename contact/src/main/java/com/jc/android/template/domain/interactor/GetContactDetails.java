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
package com.jc.android.template.domain.interactor;


import android.content.Context;

import com.jc.android.base.domain.interactor.UseCase;
import com.jc.android.template.domain.interactor.repository.ContactDataRepository;
import com.jc.android.template.domain.interactor.repository.ContactRepository;

import rx.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving data related to an specific {@link .data.dto.User}.
 */
public class GetContactDetails extends UseCase {

	String userId;
	ContactRepository userRepository;

	public GetContactDetails(Context appContext) {
		this(null, new ContactDataRepository(appContext));
	}

	public GetContactDetails(String userId, Context appContext) {
		this(userId, new ContactDataRepository(appContext));
	}

	public GetContactDetails(String userId, ContactRepository userRepository) {
		this.userId = userId;
		this.userRepository = userRepository;
	}

	public void setUserRepository(ContactRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	protected Observable buildUseCaseObservable() {
		return this.userRepository.user(this.userId);
	}
}
