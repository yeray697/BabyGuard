package com.phile.babyguard.presenter;

import android.content.Context;

import com.phile.babyguard.interfaces.KidList_Presenter;
import com.phile.babyguard.interfaces.KidList_View;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.repository.Repository;

import java.util.List;

/**
 * Created by usuario on 19/12/16.
 */

public class KidListPresenterImpl implements KidList_Presenter {
    KidList_View view;
    KidList mCallback;
    public interface KidList{
        public void OnLoadedKidList();
    }

    public KidListPresenterImpl(final KidList_View view){
        this.view = view;
        mCallback = new KidList() {
            @Override
            public void OnLoadedKidList() {
                view.setKids();
            }
        };
        Repository.getInstance((Context) view, mCallback);
    }

}
