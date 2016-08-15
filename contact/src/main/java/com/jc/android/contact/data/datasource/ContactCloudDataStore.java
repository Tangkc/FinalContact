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

import com.jc.android.base.data.entity.JCResponse;
import com.jc.android.base.data.exception.BusinessException;
import com.jc.android.base.data.exception.NoConnectionException;
import com.jc.android.base.data.net.RetrofitFactory;
import com.jc.android.base.data.net.utils.NetUtil;
import com.jc.android.component.config.data.datasource.ConfigDataStoreFactory;
import com.jc.android.component.config.data.dto.Config;
import com.jc.android.contact.data.entity.Contact;
import com.jc.android.contact.data.entity.Dept;
import com.jc.android.contact.data.net.ContactService;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * {@link ContactDataStore} implementation based on connections to the api (Cloud).
 */
public class ContactCloudDataStore implements ContactDataStore {

    private final Context context;
    private final ContactService webService;


    /**
     * Construct a {@link ContactDataStore} based on connections to the api (Cloud)
     *
     * @param context
     */
    public ContactCloudDataStore(Context context) {
        this.context = context;

        ConfigDataStoreFactory configDataStoreFactory = new ConfigDataStoreFactory(context);
        Config config = configDataStoreFactory.create().readConfig();
        webService = RetrofitFactory.getRetrofit(config.getBusinessServer()).create(ContactService.class);
    }

    @Override
    public Observable<List<Contact>> userEntityList(String id) {
        if (!NetUtil.isThereInternetConnection(context))
            return Observable.error(new NoConnectionException());
        return this.webService.userList(id)
                .flatMap(new Func1<JCResponse<List<Contact>>, Observable<List<Contact>>>() {
                    @Override
                    public Observable<List<Contact>> call(JCResponse<List<Contact>> listJCResponse) {
                        if (!"000000".equals(listJCResponse.getCode())) {
                            return Observable.error(new BusinessException(listJCResponse.getErrormsg()));
                        }
                        return Observable.just(listJCResponse.getBody());
                    }
                });
    }

    @Override
    public Observable<Contact> userEntityDetails(final String id) {
        return this.webService.user(String.valueOf(id))
                .flatMap(new Func1<JCResponse<Contact>, Observable<Contact>>() {
                    @Override
                    public Observable<Contact> call(JCResponse<Contact> jcResponse) {
                        if (!"000000".equals(jcResponse.getCode())) {
                            return Observable.error(new BusinessException(jcResponse.getErrormsg()));
                        }
                        return Observable.just(jcResponse.getBody());
                    }
                });

    }

    @Override
    public Observable<List<Dept>> deptUserList(String id) {
        if (!NetUtil.isThereInternetConnection(context))
            return Observable.error(new NoConnectionException());
        return this.webService.deptUserList(id)
                .flatMap(new Func1<JCResponse<List<Dept>>, Observable<List<Dept>>>() {
                    @Override
                    public Observable<List<Dept>> call(JCResponse<List<Dept>> listJCResponse) {
                        if (!"000000".equals(listJCResponse.getCode())) {
                            return Observable.error(new BusinessException(listJCResponse.getErrormsg()));
                        }
                        return Observable.just(listJCResponse.getBody());
                    }
                });
    }
}
