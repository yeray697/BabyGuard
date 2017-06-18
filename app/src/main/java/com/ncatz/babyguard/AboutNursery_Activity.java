package com.ncatz.babyguard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.repository.Repository;

/**
 * Activity that shows nursery information
 */
public class AboutNursery_Activity extends MaterialAboutActivity {

    private NurserySchool nursery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String idNursery = getIntent().getExtras().getString(Home_Parent_Activity.KID_NURSERY_KEY);
        nursery = Repository.getInstance().getNurserySchoolById(idNursery);
        ((Babyguard_Application) getApplicationContext()).addNurseryListener(new Babyguard_Application.ActionEndListener() {
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
                getString(R.string.send_email_about),
                true,
                nursery.getEmail(),
                ""));
        authorCardBuilder.addItem(ConvenienceBuilder.createMapItem(c,
                c.getResources().getDrawable(R.drawable.ic_contact_map),
                getString(R.string.visit_us_about),
                nursery.getAddress(),
                nursery.getAddress()));

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.visit_web_about)
                .subText(nursery.getWeb())
                .icon(c.getResources().getDrawable(R.drawable.ic_contact_web))
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse(nursery.getWeb())))
                .build());

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title(getString(R.string.phones_about));

        int count = 1;
        for (String phone : nursery.getTelephone()) {

            convenienceCardBuilder.addItem(ConvenienceBuilder.createPhoneItem(c,
                    c.getResources().getDrawable(R.drawable.ic_contact_phone),
                    getString(R.string.phone_about) + " " + count++,
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
        ((Babyguard_Application) getApplicationContext()).removeNurseryListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Babyguard_Application.setCurrentActivity("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Babyguard_Application.setCurrentActivity("Login");
    }
}
