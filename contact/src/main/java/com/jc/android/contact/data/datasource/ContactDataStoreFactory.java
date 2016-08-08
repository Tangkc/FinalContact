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
package com.jc.android.contact.data.datasource;

import android.content.Context;

import com.jc.android.contact.data.cache.ContactCache;
import com.jc.android.contact.data.cache.ContactCacheImpl;


/**
 * Factory that creates different implementations of {@link ContactDataStore}.
 */
public class ContactDataStoreFactory {

	private final Context context ;
	private ContactCache demoCache;

	public ContactDataStoreFactory(Context applicationContext) {
		this(applicationContext, new ContactCacheImpl(applicationContext));
	}

	public ContactDataStoreFactory(Context context, ContactCache demoCache) {
		if (context == null || demoCache == null) {
			throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
		}
		this.context = context.getApplicationContext();
		this.demoCache = demoCache;
	}

	public void setDemoCache(ContactCache demoCache) {
		this.demoCache = demoCache;
	}

	/**
	 * Create {@link ContactDataStore} from a user id.
	 */
	public ContactDataStore create(String userId) {
		ContactDataStore demoDataStore;

		if (!this.demoCache.isExpired() && this.demoCache.isCached(userId)) {
			demoDataStore = new ContactDiskDataStore(this.demoCache);
		} else {
			demoDataStore = createCloudDataStore();
		}

		return demoDataStore;
	}

	/**
	 * Create {@link ContactDataStore} to retrieve data from the Cloud.
	 */
	public ContactDataStore createCloudDataStore() {

		return new ContactCloudDataStore(context);
	}
}
