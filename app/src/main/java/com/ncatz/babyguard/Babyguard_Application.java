package com.ncatz.babyguard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ncatz.babyguard.database.DatabaseHelper;
import com.ncatz.babyguard.firebase.FirebaseListeners;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatKeyMap;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.NurseryClass;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.model.User;
import com.ncatz.babyguard.model.UserCredentials;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.yeray.calendarview.BuildConfig;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Context application
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Babyguard_Application extends Application {

    private final static String FILE_PREFERENCE = "credentials";
    private final static String USER_PREFERENCE = "userCredentials";
    private final static String PASS_PREFERENCE = "pass";
    private final static String TYPE_PREFERENCE = "type";

    private UserCredentials userCredentials;
    private SharedPreferences pref;
    private static Context context;

    private ActionEndListener loginListener;
    private ActionEndListener nurseryListener;
    private ActionEndListener kidListListener;
    private ActionEndListener trackingListener;
    private ActionEndListener calendarListener;
    private ActionEndListener chatListener;
    private ActionEndListener homeTeacherListener;

    boolean nurseryAndChatsLoadedFirstTime;
    boolean kidsInfoLoadedFirstTime;

    private boolean databaseLoaded;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (final Thread thread, final Throwable e)
            {
                /*new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        handleUncaughtException (thread, e);
                    }
                });*/
            }
        });
        pref = getApplicationContext().getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE);
        FirebaseManager.getInstance().setListeners(firebaseListeners);
        FormatStrategy formatStrategyLog = PrettyFormatStrategy.newBuilder()
                .methodCount(5)
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("Babyguard")
                .build();
        AndroidLogAdapter adapterLog = new AndroidLogAdapter(formatStrategyLog) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        };

        Logger.addLogAdapter(adapterLog);
        FormatStrategy formatStrategyFile = CsvFormatStrategy.newBuilder()
                .logStrategy(new LogStrategy() {

                    @Override
                    public void log(int priority, String tag, String message) {

                    }
                })
                .build();
        AndroidLogAdapter adapterFile = new AndroidLogAdapter(formatStrategyLog) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return !BuildConfig.DEBUG;
            }
        };
        Logger.addLogAdapter(new DiskLogAdapter());
        Logger.d("Hello");
        Logger.e("darkness");
        Logger.i("my");
        Logger.w("old");
    }

    private void handleUncaughtException(Thread thread, Throwable e) {
        /*String body =  "Cause:\n" + e.getCause() + "\n==================================================\n"
                + "Stack trace:\n" + Arrays.toString(e.getStackTrace()) + "\n==================================================\n"
                + "Throwable to string:\n" + e.toString();
        Log.d("ERRORRRRR",body);*/

        /*
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"yeray.1997.yr@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "BabyGuard Exception");
        i.putExtra(Intent.EXTRA_TEXT   , "Cause:\n" + e.getCause() + "\n==================================================\n"
                + "Stack trace:\n" + Arrays.toString(e.getStackTrace()) + "\n==================================================\n"
                + "Throwable to string:\n" + e.toString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }*/
    }

    public static Context getContext(){
        return context;
    }

    /**
     * Set userCredentials credentials.
     * If userCredentials is null, it cleans preferences
     * @param userCredentials
     */
    public void setUserCredentials(UserCredentials userCredentials){
        this.userCredentials = userCredentials;
        if (userCredentials != null) {
            pref.edit().putString(USER_PREFERENCE, userCredentials.getUser()).apply();
            pref.edit().putString(PASS_PREFERENCE, userCredentials.getPass()).apply();
            pref.edit().putInt(TYPE_PREFERENCE, userCredentials.getType()).apply();
        } else {
            pref.edit().clear().apply();
        }
    }

    public void signOff() {
        try {
            DatabaseHelper.getInstance().closeDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FirebaseManager.getInstance().close();
        Repository.getInstance().signOff();
        setUserCredentials(null);
        kidsInfoLoadedFirstTime = false;
        nurseryAndChatsLoadedFirstTime = false;
        databaseLoaded = false;
    }

    /**
     * Get userCredentials's credentials
     * @return Return the userCredentials
     */
    public UserCredentials getUserCredentials(){
        String name;
        String pass;
        int type;
        if (this.userCredentials == null) {
            name = getUserIfExists();
            if (!TextUtils.isEmpty(name)) {
                pass = getPassIfExists();
                if (!TextUtils.isEmpty(pass)) {
                    type = getTypeIfExists();
                    this.userCredentials = new UserCredentials(name, pass, type);
                }
            }
        }
        return this.userCredentials;
    }

    /**
     * Get the userCredentials's name is exists
     * @return Return the userCredentials's name
     */
    private String getUserIfExists() {
        return pref.getString("userCredentials",null);
    }

    /**
     * Get the userCredentials's pass is exists
     * @return Return the userCredentials's pass
     */
    private String getPassIfExists() {
        return pref.getString("pass",null);
    }

    /**
     * Get the userCredentials's pass is exists
     * @return Return the userCredentials's pass
     */
    private int getTypeIfExists() {
        return pref.getInt("type",-1);
    }

    public static boolean isTeacher() {
        return ((Babyguard_Application)Babyguard_Application.getContext()).getUserCredentials().getType() == UserCredentials.TYPE_TEACHER;
    }

    public void addLoginListener(ActionEndListener loginListener) {
        this.loginListener = loginListener;
    }

    public void removeLoginListener() {
        loginListener = null;
    }

    public void addNurseryListener(ActionEndListener nurseryListener) {
        this.nurseryListener = nurseryListener;
    }

    public void removeNurseryListener() {
        nurseryListener = null;
    }

    public void addChatListener(ActionEndListener chatListener) {
        this.chatListener = chatListener;
    }

    public void removeChatListener() {
        chatListener = null;
    }

    public void addKidListListener(ActionEndListener kidListListener) {
        this.kidListListener = kidListListener;
    }

    public void removeKidListListener() {
        kidListListener = null;
    }

    public interface ActionEndListener {
        void onEnd();
    }

    public void addTrackingListener(ActionEndListener trackingListener) {
        this.trackingListener = trackingListener;
    }

    public void removeTrackingListener() {
        trackingListener = null;
    }

    public void addCalendarListener(ActionEndListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    public void removeCalendarListener() {
        calendarListener = null;
    }

    public void addHomeTeacherListener(ActionEndListener homeTeacherListener) {
        this.homeTeacherListener = homeTeacherListener;
    }

    public void removeHomeTeacherListener() {
        homeTeacherListener = null;
    }
    private FirebaseListeners firebaseListeners = new FirebaseListeners() {

        @Override
        public void onUserModified(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                User user = User.parseFromDataSnapshot(dataSnapshot);
                DatabaseHelper.getInstance(user.getId()).setPassword(user.getDbPass());
                Repository.getInstance().setUser(user);
                userCredentials.setType(user.getUser_type());
                if(isTeacher()) {
                    FirebaseManager.getInstance().getNursery(user.getId_nursery());
                }
                //if (!kidsInfoLoadedFirstTime) {
                    FirebaseManager.getInstance().getKidsInfo(user);
                    kidsInfoLoadedFirstTime = true;
                //}
                if (loginListener != null)
                    loginListener.onEnd();
                if (nurseryListener != null)
                    nurseryListener.onEnd();
                if (kidListListener != null)
                    kidListListener.onEnd();
                if (trackingListener != null)
                    trackingListener.onEnd();
                if (calendarListener != null)
                    calendarListener.onEnd();
                if (chatListener != null)
                    chatListener.onEnd();
            }
        }

        @Override
        public void onUserCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onKidModified(DataSnapshot dataSnapshot) {if (dataSnapshot.exists()){
                HashMap<String, HashMap<String, Object>> value = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
                HashMap<String, Kid> kids = Kid.parseFromDataSnapshot(value);
                List<String> aux;
                boolean isTeacher = Babyguard_Application.isTeacher();
                if (kids.size() > 0) {
                    if (kids.size() > 1){
                        HashMap<String,List<String>> nurseryClasses = new HashMap<>();
                        String id_nursery;
                        String id_class;
                        for (Map.Entry<String, Kid> kidAux : kids.entrySet()) {
                            id_nursery = kidAux.getValue().getId_nursery();
                            id_class = kidAux.getValue().getId_nursery_class();
                            if (nurseryClasses.containsKey(id_nursery)){
                                aux = nurseryClasses.get(id_nursery);
                                if (!aux.contains(id_class)){
                                    aux.add(id_class);
                                    nurseryClasses.put(id_nursery,aux);
                                }
                            } else {
                                aux = new ArrayList<>();
                                aux.add(id_class);
                                nurseryClasses.put(id_nursery,aux);
                            }
                        }

                        for (HashMap.Entry<String,List<String>> entry:nurseryClasses.entrySet()){
                            if (!isTeacher) {
                                FirebaseManager.getInstance().getNursery(entry.getKey());
                            }
                            for (String classId: entry.getValue()){
                                FirebaseManager.getInstance().getNurseryClass(entry.getKey(),classId);
                                if (isTeacher) {
                                    FirebaseManager.getInstance().getChatInfoTeacher(classId);
                                }
                            }
                        }
                        if (!isTeacher) {
                            for (Map.Entry<String, Kid> kidAux : kids.entrySet()) {
                                FirebaseManager.getInstance().getChatInfoParent(kidAux.getValue().getId_nursery(),kidAux.getValue().getId_nursery_class(),kidAux.getKey());
                                FirebaseManager.getInstance().getChat(kidAux.getKey());
                            }
                        } else {
                            FirebaseManager.getInstance().getChat(Repository.getInstance().getUser().getId());
                        }
                    } else {
                        Kid kidAux = kids.entrySet().iterator().next().getValue();
                        String idNursery,
                                idNurseryClass,
                                idChat;
                        idNursery = kidAux.getId_nursery();
                        idNurseryClass = kidAux.getId_nursery_class();
                        if (!isTeacher) {
                            idChat = kidAux.getId();
                            FirebaseManager.getInstance().getNursery(idNursery);
                        } else {
                            idChat = Repository.getInstance().getUser().getId();
                        }
                        FirebaseManager.getInstance().getNurseryClass(idNursery, idNurseryClass);
                        if (isTeacher) {
                            FirebaseManager.getInstance().getChatInfoTeacher(idNurseryClass);
                        } else {
                            FirebaseManager.getInstance().getChatInfoParent(idNursery,idNurseryClass,kidAux.getId());
                        }
                        FirebaseManager.getInstance().getChat(idChat);
                    }
                }
                Repository.getInstance().getUser().setKids(kids);
                if (kidListListener != null)
                    kidListListener.onEnd();
                if (trackingListener != null)
                    trackingListener.onEnd();
            }
        }

        @Override
        public void onKidCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onNurseryModified(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Repository.getInstance().setNursery(NurserySchool.parseFromDataSnapshot(dataSnapshot));
                if (nurseryListener != null)
                    nurseryListener.onEnd();
            }
        }

        @Override
        public void onNurseryCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onNurseryClassModified(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                ArrayList<DiaryCalendarEvent> calendarListAux = new ArrayList<>();
                String name = (String) ((HashMap<String, Object>)dataSnapshot.getValue()).get("name");
                String nursery_id = dataSnapshot.getRef().getParent().getKey();
                String nursery_class = dataSnapshot.getKey();
                HashMap<String,HashMap<String,String>> calendar = (HashMap<String,HashMap<String, String>>) ((HashMap<String, Object>)dataSnapshot.getValue()).get("calendar");
                DiaryCalendarEvent calendarAux = null;
                if (calendar != null) {
                    Integer day,year,month;
                    String unixTime;
                    for (Map.Entry<String, HashMap<String, String>> entry : calendar.entrySet()) {
                        unixTime = entry.getValue().get("datetime");
                        Calendar aux = Calendar.getInstance();
                        aux.setTimeInMillis(Long.parseLong(unixTime));
                        day = aux.get(Calendar.DAY_OF_MONTH);
                        year = aux.get(Calendar.YEAR);
                        month = aux.get(Calendar.MONTH);
                        calendarAux = new DiaryCalendarEvent(entry.getKey(),
                                entry.getValue().get("title"),
                                year,
                                month,
                                day,
                                entry.getValue().get("description"));
                        calendarListAux.add(calendarAux);
                    }
                }
                NurseryClass nurseryClass = new NurseryClass();
                nurseryClass.setId(nursery_class);
                nurseryClass.setCalendar(calendarListAux);
                nurseryClass.setName(name);
                Repository.getInstance().setNurseryClass(nursery_id,nursery_class,nurseryClass);
                if (loginListener != null)
                    loginListener.onEnd();
                if (nurseryListener != null)
                    nurseryListener.onEnd();
                if (homeTeacherListener != null)
                    homeTeacherListener.onEnd();
                if (calendarListener != null)
                    calendarListener.onEnd();
            }
        }

        @Override
        public void onNurseryClassCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onTrackingModified(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                ArrayList<TrackingKid> trackingList = new ArrayList<>();
                TrackingKid trackingAux;
                if (dataSnapshot.getValue() != null) {
                    ArrayList<HashMap<String, String>> kids = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                    String idKid = dataSnapshot.getKey();
                    for (HashMap<String, String> kid : kids) {
                        trackingAux = new TrackingKid("", kid.get("title"),
                                kid.get("datetime"),
                                TrackingKid.parseIntToType(Integer.parseInt(kid.get("type"))),
                                kid.get("description"));
                        trackingList.add(trackingAux);
                    }
                    Repository.getInstance().addTracking(idKid, trackingList);
                    if (trackingListener != null)
                        trackingListener.onEnd();
                }
            }
        }

        @Override
        public void onTrackingCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onChatAdded(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                try {
                    String id = dataSnapshot.getRef().getParent().getKey();
                    ChatMessage chatMessage = ChatMessage.parseFromDataSnapshot(dataSnapshot);
                    if (Repository.getInstance().addMessage(chatMessage)) {
                        FirebaseManager.getInstance().deleteMessage(id,chatMessage.getKey());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chatListener != null)
                    chatListener.onEnd();
            }
        }

        @Override
        public void onChatCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onChatNameChanged(DataSnapshot dataSnapshot) {
            Chat chat;
            if (dataSnapshot.exists()) {
                if (isTeacher()) {
                    for (HashMap.Entry<String, HashMap<String, Object>> chatData : ((HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue()).entrySet()) {
                        chat = Chat.parseFromDataSnapshot(chatData.getValue());
                        String teacherId = Repository.getInstance().getUser().getId();
                        Repository.getInstance().addChat(new ChatKeyMap(teacherId,chat.getId()),chat);
                    }
                } else {
                    chat = Chat.parseFromDataSnapshot((HashMap<String,Object>) dataSnapshot.getValue());
                    Repository.getInstance().addChat(chat);
                }
                try {
                    DatabaseHelper.getInstance().loadChatMessages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chatListener != null)
                    chatListener.onEnd();
            }
        }

        @Override
        public void onChatNameError(DatabaseError databaseError) {

        }
    };

    public boolean isDatabaseLoaded() {
        return databaseLoaded;
    }

    public void setDatabaseLoaded(boolean databaseLoaded) {
        this.databaseLoaded = databaseLoaded;
    }
}
