package com.jbnm.homehero.data.remote;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by janek on 7/24/17.
 */

public class FirebaseService {

    public interface FirebaseAPI {
        @GET("parents/{parentId}/.json")
        Observable<Parent> getParentById(@Path("parentId") String parentId);

        @PUT("parents/{parentId}/.json")
        Observable<Parent> saveParent(@Path("parentId") String parentId, @Body Parent parent);

        @PUT("parents/{parentId}/child.json")
        Observable<String> addChildToParent(@Path("parentId") String parentId, @Body String childId);

        @GET("children/{childId}/.json")
        Observable<Child> getChildById(@Path("childId") String childId);

        @PUT("children/{childId}/.json")
        Observable<Child> saveChild(@Path("childId") String childId, @Body Child child);

    }

    private static final String FIREBASE_URL = "https://homehero-a7715.firebaseio.com/";

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(FIREBASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(httpClient)
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
