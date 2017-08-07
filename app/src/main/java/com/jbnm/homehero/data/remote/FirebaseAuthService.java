package com.jbnm.homehero.data.remote;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jbnm.homehero.data.model.Child;
import com.jbnm.homehero.data.model.Parent;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by janek on 8/6/17.
 */

public class FirebaseAuthService {

    private static FirebaseAuthService firebaseAuthService;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    private FirebaseAuthService() {
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAuthService getInstance() {
        if (firebaseAuthService == null) {
            firebaseAuthService = new FirebaseAuthService();
        }
        return firebaseAuthService;
    }

    public Maybe<AuthResult> login(String email, String password) {
        return RxFirebaseAuth.signInWithEmailAndPassword(auth, email, password);
    }

//    public Observable<Child> loginAndGetChild(String email, String password) {
//        return RxFirebaseAuth.signInWithEmailAndPassword(auth, email, password)
//                .flatMapObservable(new Function<AuthResult, ObservableSource<Child>>() {
//                    @Override public ObservableSource<Child> apply(AuthResult authResult) throws Exception {
//                        return getChild(authResult.getUser().getUid());
//                    }
//                });
//    }

    public Observable<Child> getChild(String parentId) {
        return RxFirebaseDatabase.observeValueEvent(rootRef.child("parents").child(parentId), Parent.class)
                .flatMap(new Function<Parent, Publisher<Child>>() {
                    @Override public Publisher<Child> apply(Parent parent) throws Exception {
                        return RxFirebaseDatabase.observeValueEvent(rootRef.child("children").child(parent.getChild()), Child.class);
                    }
                }).toObservable();
    }

    public Flowable<Child> getFlowChild(String parentId) {
        return RxFirebaseDatabase.observeValueEvent(rootRef.child("parents").child(parentId), Parent.class)
                .flatMap(new Function<Parent, Publisher<Child>>() {
                    @Override public Publisher<Child> apply(Parent parent) throws Exception {
                        return RxFirebaseDatabase.observeValueEvent(rootRef.child("children").child(parent.getChild()), Child.class);
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }

    public Observable<FirebaseAuth> getAuthState() {
        return RxFirebaseAuth.observeAuthState(auth);
    }

    public Completable createUser(String email, String password) {
        return RxFirebaseAuth.createUserWithEmailAndPassword(auth, email, password)
                .flatMapCompletable(new Function<AuthResult, CompletableSource>() {
                    @Override
                    public CompletableSource apply(AuthResult authResult) throws Exception {
                        return RxFirebaseDatabase.updateChildren(FirebaseDatabase.getInstance().getReference(), createNewUser(authResult.getUser()));
                    }
                });
    }

    private Map<String, Object> createNewUser(FirebaseUser user) {
        Child child = Child.newInstance();
        Parent parent = new Parent(user.getUid(), user.getEmail());
        parent.setChild(child.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put(String.format("parents/%s", parent.getId()), parent);
        updates.put(String.format("children/%s", child.getId()), child);
        return updates;
    }

}
