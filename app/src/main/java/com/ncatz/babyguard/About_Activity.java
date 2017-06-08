package com.ncatz.babyguard;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickListener;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

public class About_Activity extends MaterialAboutActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected MaterialAboutList getMaterialAboutList(final Context c) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();


        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("Material About Library")
                .icon(R.mipmap.ic_launcher)
                .build());

        try {
            appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                    c.getResources().getDrawable(R.drawable.bubble_receiver),
                    "Version",
                    false));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Changelog")
                .icon(c.getResources().getDrawable(R.drawable.bubble_receiver))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Releases", "https://github.com/daniel-stoneuk/material-about-library/releases", true, false))
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Licenses")
                .icon(c.getResources().getDrawable(R.drawable.bubble_receiver))
                .setOnClickListener(new MaterialAboutItemOnClickListener() {
                    @Override
                    public void onClick(boolean longClick) {

                    }
                })
                .build());

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Nursery");

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Daniel Stone")
                .subText("United Kingdom")
                .icon(c.getResources().getDrawable(R.drawable.bubble_receiver))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Fork on GitHub")
                .icon(c.getResources().getDrawable(R.drawable.bubble_receiver))
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(c, Uri.parse("https://github.com/daniel-stoneuk")))
                .build());

        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title("Convenience Builder");

        try {
            convenienceCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                    c.getResources().getDrawable(R.drawable.bubble_receiver),
                    "Version",
                    false));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
                c.getResources().getDrawable(R.drawable.bubble_receiver),
                "Visit Website",
                true,
                Uri.parse("https://babyguard.ncatz.com")));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(c,
                c.getResources().getDrawable(R.drawable.bubble_receiver),
                "Rate this app",
                null
        ));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
                c.getResources().getDrawable(R.drawable.bubble_receiver),
                "Send an email",
                true,
                "babyguard@ncatz.com",
                ""));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createPhoneItem(c,
                c.getResources().getDrawable(R.drawable.bubble_receiver),
                "Call me",
                true,
                "+44 12 3456 7890"));
        return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), convenienceCardBuilder.build());
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return "asgasg";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
