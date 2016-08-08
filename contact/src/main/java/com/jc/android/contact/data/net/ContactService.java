package com.jc.android.contact.data.net;

import com.jc.android.contact.data.entity.ContactEntity;

import java.util.List;
import com.jc.android.base.data.entity.JCResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 16/7/15.
 */
public interface ContactService {

//    @GET("user_111.json")
    @GET("{user}")
    Observable<JCResponse<ContactEntity>> demo(@Path("user") String user);

    @GET("android/system/user/queryDeptAndUserFor4M.action")
    Observable<JCResponse<List<ContactEntity>>> demoList(@Query("id") String id);

}