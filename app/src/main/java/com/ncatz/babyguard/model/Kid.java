package com.ncatz.babyguard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ncatz.babyguard.firebase.FirebaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Kid POJO class
 *
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Kid implements Parcelable {
    private String id;
    private String info;
    private String name;
    private String id_nursery;
    private String id_nursery_class;
    private String parent;
    private String img;
    private ArrayList<TrackingKid> tracking;
    private String fcmID;

    public Kid() {

    }

    protected Kid(Parcel in) {
        id = in.readString();
        info = in.readString();
        name = in.readString();
        id_nursery = in.readString();
        id_nursery_class = in.readString();
        parent = in.readString();
        img = in.readString();
        tracking = in.createTypedArrayList(TrackingKid.CREATOR);
        fcmID = in.readString();
    }

    public static final Creator<Kid> CREATOR = new Creator<Kid>() {
        @Override
        public Kid createFromParcel(Parcel in) {
            return new Kid(in);
        }

        @Override
        public Kid[] newArray(int size) {
            return new Kid[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(String id_nursery) {
        this.id_nursery = id_nursery;
    }

    public String getId_nursery_class() {
        return id_nursery_class;
    }

    public void setId_nursery_class(String id_nursery_class) {
        this.id_nursery_class = id_nursery_class;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<TrackingKid> getTracking() {
        return tracking;
    }

    public void setTracking(ArrayList<TrackingKid> tracking) {
        this.tracking = tracking;
    }

    public String getFcmID() {
        return fcmID;
    }

    public void setFcmID(String fcmID) {
        this.fcmID = fcmID;
    }

    public boolean removeTrackingItem(String kidId, String trackingId) {
        int count = 0;
        boolean result = false;
        for (TrackingKid trackAux : tracking) {
            if (trackAux.getId().equals(trackingId)) {
                tracking.remove(count);
                result = true;
                break;
            }
            count++;
        }
        return result;
    }

    public interface KidListener {
        void onKidGetListener(Kid kid);
    }

    public static HashMap<String, Kid> parseFromDataSnapshot(HashMap<String, HashMap<String, Object>> kids) {
        HashMap<String, Kid> kidsList = new HashMap<>();
        HashMap<String, Object> auxEntry;
        Kid kidAux;

        for (Map.Entry<String, HashMap<String, Object>> entry :
                kids.entrySet()) {
            auxEntry = entry.getValue();
            kidAux = new Kid();
            kidAux.id = entry.getKey();
            kidAux.info = (String) auxEntry.get("info");
            kidAux.name = (String) auxEntry.get("name");
            kidAux.id_nursery = (String) auxEntry.get("id_nursery");
            kidAux.id_nursery_class = (String) auxEntry.get("id_nursery_class");
            kidAux.parent = (String) auxEntry.get("id_parent");
            kidAux.img = (String) auxEntry.get("img");
            kidAux.fcmID = (String) auxEntry.get("fcmToken");
            kidAux.tracking = new ArrayList<TrackingKid>();
            FirebaseManager.getInstance().getTrackingKid(kidAux.getId());
            kidsList.put(kidAux.getId(), kidAux);
        }
        return kidsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(info);
        dest.writeString(name);
        dest.writeString(id_nursery);
        dest.writeString(id_nursery_class);
        dest.writeString(parent);
        dest.writeString(img);
        dest.writeTypedList(tracking);
        dest.writeString(fcmID);
    }
}
