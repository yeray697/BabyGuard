package com.phile.babyguard.presenter;

import android.content.Context;

import com.phile.babyguard.interfaces.KidList_Presenter;
import com.phile.babyguard.interfaces.KidList_View;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.repository.Repository;

import java.util.List;

/**
 * KidList presenter
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class KidListPresenterImpl implements KidList_Presenter {
    KidList_View view;
    KidList mCallback;
    
    public interface KidList{
        /**
         * Show kids in the list when it finish
         */
        public void OnLoadedKidList();
    }

    public KidListPresenterImpl(final KidList_View view){
        this.view = view;
        mCallback = new KidList() {
            @Override
            public void OnLoadedKidList() {
                //TODO si no hay internet, mostrar un botón para actualizar.
                view.setKids();
            }
        };
        Repository.getInstance((Context) view, mCallback);
    }

}
