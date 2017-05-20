package com.ncatz.babyguard;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.repository.Repository;

public class AboutNursery_Activity extends MaterialAboutActivity {

    private NurserySchool nursery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String idNursery = getIntent().getExtras().getString(Home_Parent_Activity.KID_NURSERY_KEY);
        nursery = Repository.getInstance().getNurserySchoolById(idNursery);
        ((Babyguard_Application)getApplicationContext()).addNurseryListener(new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                refreshActivity();
            }
        });
        super.onCreate(savedInstanceState);
    }

    private void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    @Override
    protected MaterialAboutList getMaterialAboutList(final Context c) {

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();

        authorCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(nursery.getName())
                .icon(R.drawable.ic_contact_title)
                .build());

        authorCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
                c.getResources().getDrawable(R.drawable.ic_contact_email),
                "Send an email",
                true,
                nursery.getEmail(),
                ""));
        authorCardBuilder.addItem(ConvenienceBuilder.createMapItem(c,
                c.getResources().getDrawable(R.drawable.ic_contact_map),
                "Visit us",
                nursery.getAddress(),
                nursery.getAddress()));

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Visit Website")
                .subText(nursery.getWeb())
                .icon(c.getResources().getDrawable(R.drawable.ic_contact_web))
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse(nursery.getWeb())))
                .build());

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title("Phones");

        int count = 1;
        for (String phone : nursery.getTelephone()){

            convenienceCardBuilder.addItem(ConvenienceBuilder.createPhoneItem(c,
                    c.getResources().getDrawable(R.drawable.ic_contact_phone),
                    "Phone "+count++,
                    true,
                    phone));
        }


        return new MaterialAboutList(authorCardBuilder.build(), convenienceCardBuilder.build());
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getResources().getString(R.string.item_contacto);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((Babyguard_Application)getApplicationContext()).removeNurseryListener();
    }
}
