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
package com.jc.android.template.data.datasource;

import android.content.Context;

import com.jc.android.template.data.cache.DemoCache;
import com.jc.android.template.data.cache.DemoCacheImpl;


/**
 * Factory that creates different implementations of {@link DemoDataStore}.
 */
public class DemoDataStoreFactory {

	private final Context context ;
	private DemoCache demoCache;

	public DemoDataStoreFactory(Context applicationContext) {
		this(applicationContext, new DemoCacheImpl(applicationContext));
	}

	public DemoDataStoreFactory(Context context, DemoCache demoCache) {
		if (context == null || demoCache == null) {
			throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
		}
		this.context = context.getApplicationContext();
		this.demoCache = demoCache;
	}

	public void setDemoCache(DemoCache demoCache) {
		this.demoCache = demoCache;
	}

	/**
	 * Create {@link DemoDataStore} from a user id.
	 */
	public DemoDataStore create(int userId) {
		DemoDataStore demoDataStore;

		if (!this.demoCache.isExpired() && this.demoCache.isCached(userId)) {
			demoDataStore = new DemoDiskDataStore(this.demoCache);
		} else {
			demoDataStore = createCloudDataStore();
		}

		return demoDataStore;
	}

	/**
	 * Create {@link DemoDataStore} to retrieve data from the Cloud.
	 */
	public DemoDataStore createCloudDataStore() {

		return new DemoCloudDataStore(context);
	}
}
