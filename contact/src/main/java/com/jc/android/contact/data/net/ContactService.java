package com.jc.android.contact.data.net;

import com.jc.android.contact.data.entity.Contact;

import java.util.List;
import java.util.Map;

import com.jc.android.base.data.entity.JCResponse;
import com.jc.android.contact.data.entity.Dept;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface ContactService {

    @GET("android/system/user/getUserById4M.action")
    Observable<JCResponse<Contact>> user(@Query("id") String id);

    @GET("android/system/user/queryDeptAndUserFor4MNew.action")
    Observable<JCResponse<List<Contact>>> userList(@Query("id") String id);

    @GET("android/system/user/queryDeptAndUserTreeFor4M.action")
    Observable<JCResponse<List<Dept>>> deptUserList(@Query("id") String id);
}
