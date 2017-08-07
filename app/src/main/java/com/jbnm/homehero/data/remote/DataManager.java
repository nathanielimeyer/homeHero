package com.jbnm.homehero.data.remote;

import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;
import com.jbnm.homehero.data.model.Reward;
import com.jbnm.homehero.data.model.Task;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by janek on 7/24/17.
 */

public class DataManager {

    private final FirebaseService.FirebaseAPI firebaseService;

    // Placeholder until auth is implemented
    private String parentId = "ParentTestId2";

    public DataManager() {
        firebaseService = FirebaseService.createService(FirebaseService.FirebaseAPI.class);
    }

    public Observable<Parent> saveParent(Parent parent) {
        return firebaseService.saveParent(parent.getId(), parent);
    }

    public Observable<Parent> getParent() {
        return firebaseService.getParentById(parentId);
    }

    public Observable<Child> saveChild(Child child) {
        return Observable.combineLatest(firebaseService.saveChild(child.getId(), child),
                firebaseService.addChildToParent(parentId, child.getId()),
                new BiFunction<Child, String, Child>() {
                    @Override
                    public Child apply(Child child, String s) throws Exception {
                        return child;
                    }
                });
    }

    public Observable<Child> updateChild(Child child) {
        return firebaseService.saveChild(child.getId(), child);
    }

    public Observable<Child> getChild(String childId) {
        return firebaseService.getChildById(childId);
    }
}
