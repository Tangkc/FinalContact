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

import com.jc.android.base.data.entity.JCResponse;
import com.jc.android.base.data.exception.BusinessException;
import com.jc.android.base.data.net.RetrofitFactory;
import com.jc.android.component.config.data.datasource.ConfigDataStoreFactory;
import com.jc.android.component.config.data.dto.Config;
import com.jc.android.template.data.cache.DemoCache;
import com.jc.android.template.data.entity.DemoEntity;
import com.jc.android.template.data.net.WebService;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * {@link DemoDataStore} implementation based on connections to the api (Cloud).
 */
public class DemoCloudDataStore implements DemoDataStore {

    private final Context context;
    private final WebService webService;


    /**
     * Construct a {@link DemoDataStore} based on connections to the api (Cloud)
     *
     * @param context
     */
    public DemoCloudDataStore(Context context) {
        this.context = context;

        ConfigDataStoreFactory configDataStoreFactory = new ConfigDataStoreFactory(context);

        Config config = configDataStoreFactory.create().readConfig();
        webService = RetrofitFactory.getRetrofit(config.getBusinessServer()).create(WebService.class);
    }

    @Override
    public Observable<List<DemoEntity>> userEntityList() {
        return this.webService.demoList()
                .flatMap(new Func1<JCResponse<List<DemoEntity>>, Observable<List<DemoEntity>>>() {
                    @Override
                    public Observable<List<DemoEntity>> call(JCResponse<List<DemoEntity>> listJCResponse) {
                        if (!"000000".equals(listJCResponse.getCode())) {
                            return Observable.error(new BusinessException(listJCResponse.getErrormsg()));
                        }
                        return Observable.just(listJCResponse.getBody());
                    }
                });
    }

    @Override
    public Observable<DemoEntity> userEntityDetails(final int id) {
        return this.webService.demo(String.valueOf(id))
                .flatMap(new Func1<JCResponse<DemoEntity>, Observable<DemoEntity>>() {
                    @Override
                    public Observable<DemoEntity> call(JCResponse<DemoEntity> listJCResponse) {
                        if (!"000000".equals(listJCResponse.getCode())) {
                            return Observable.error(new BusinessException(listJCResponse.getErrormsg()));
                        }
                        return Observable.just(listJCResponse.getBody());
                    }
                });

    }
}
