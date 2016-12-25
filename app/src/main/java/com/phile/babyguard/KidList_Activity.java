package com.phile.babyguard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.phile.babyguard.adapter.KidList_Adapter;
import com.phile.babyguard.interfaces.KidList_Presenter;
import com.phile.babyguard.interfaces.KidList_View;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.presenter.KidListPresenterImpl;
import com.phile.babyguard.utils.Utils;

/**
 * KidList view
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class KidList_Activity extends AppCompatActivity implements KidList_View{

    ListView lvKids;
    public static final String KID_EXTRA = "kid_id";
    KidList_Presenter presenter;
    KidList_Adapter adapter;
    ImageView ivExpandedImage;
    private boolean zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_list);
        zoom = false;
        ivExpandedImage = (ImageView) findViewById(R.id.ivExpanded_KidList);
        presenter = new KidListPresenterImpl(this);
        lvKids = (ListView) findViewById(R.id.lvKidList);
        lvKids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent intent = new Intent(KidList_Activity.this, Tracing_Fragment.class);
                Intent intent = new Intent(KidList_Activity.this, Home_Activity.class);
                intent.putExtra(KID_EXTRA,((Kid)lvKids.getItemAtPosition(i)).getIdKid());
                startActivity(intent);
            }
        });
    }

    @Override
    public void setKids() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter = new KidList_Adapter(KidList_Activity.this, new KidList_Adapter.OnImageClickListener() {
                    @Override
                    public void clicked(final View view, Drawable drawable) {
                        zoom = true;
                        Utils.zoomImageFromThumb(view.getContext(),R.id.activity_kid_list, view, ivExpandedImage, drawable, new Utils.OnAnimationEnded() {
                            @Override
                            public void finishing() {
                                zoom = false;
                            }

                            @Override
                            public void finished() {
                            }
                        });
                    }
                });
                lvKids.setAdapter(adapter);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (!zoom)
            super.onBackPressed();
        else
            Utils.cancelZoomedImage(ivExpandedImage);
    }
}
