package com.jc.android.template.data.net;

import com.jc.android.template.data.entity.DemoEntity;

import java.util.List;
import com.jc.android.base.data.entity.JCResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by admin on 16/7/15.
 */
public interface WebService {

//    @GET("user_111.json")
    @GET("{user}")
    Observable<JCResponse<DemoEntity>> demo(@Path("user") String user);

    @GET("users.json")
    Observable<JCResponse<List<DemoEntity>>> demoList();

}
