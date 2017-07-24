package com.jbnm.homehero.data.remote;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by janek on 7/24/17.
 */

public class FirebaseService {

    public interface FirebaseAPI {
        @GET("parents/{parentId}/.json")
        Observable<Parent> getParentById(@Path("parentId") String parentId);

        @GET("children/{childId}/.json")
        Observable<Child> getChildById(@Path("childId") String childId);

        @GET("tasks/{taskId}/.json")
        Observable<Task> getTaskById(@Path("taskId") String taskId);

        @GET("rewards/{rewardId}/.json")
        Observable<Reward> getRewardById(@Path("rewardId") String rewardId);

        @PUT("parents/{parentId}/.json")
        Observable<Parent> saveParent(@Path("parentId") String parentId, @Body Parent parent);

        @PUT("parents/{parentId}/child.json")
        Observable<String> addChildToParent(@Path("parentId") String parentId, @Body String childId);

        @POST("children.json")
        Observable<Map<String, String>> createChild(@Body Child child);

        @PUT("children/{childId}/tasks/{taskId}.json")
        Observable<Boolean> addTaskToChild(@Path("childId") String childId, @Path("taskId") String taskId, @Body Boolean task);

        @PUT("children/{childId}/rewards/{rewardId}.json")
        Observable<Boolean> addRewardsToChild(@Path("childId") String childId, @Path("rewardId") String rewardId, @Body Boolean rewards);

        @POST("tasks.json")
        Observable<Map<String, String>> createTask(@Body Task task);

        @POST("rewards.json")
        Observable<Map<String, String>> createReward(@Body Reward reward);
    }

    private static final String FIREBASE_URL = "https://homehero-a7715.firebaseio.com/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(FIREBASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
