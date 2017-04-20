package com.ncatz.babyguard.log;

import android.content.Context;
import android.util.Log;

import com.ncatz.babyguard.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yeray697 on 9/04/17.
 */

public class Logger {
    private static final String SEPARATOR = ";";
    private static final String NEW_LINE = "\n";
    private static Logger logger;
    private final Context context;
    private AppType appType = AppType.DEBUG;
    private String today;
    private FileOutputStream outputStream;

    enum AppType{
        DEBUG, RELEASE
    }
    enum MessageType{
        Debug,Error
    }

    public Logger(Context context) {
        this.context = context;
        this.today = "";
        createLogFile();
    }

    public static Logger getInstance(Context context) {
        if (logger==null)
            logger = new Logger(context);
        return logger;
    }

    public void log(MessageType type, String tag,String message){
        if (appType==AppType.DEBUG){
            if (type==MessageType.Debug)
                Log.d(tag,message);
            else if (type==MessageType.Error)
                Log.e(tag,message);
        } else {
            writeToFile(type,tag,message);
        }
    }

    private static final String FILE_NAME = "Log";
    private static final String FILE_EXTENSION = ".txt";

    private void writeToFile(MessageType type,String tag, String message) {
        String time = Utils.getTimeNow();
        String line = type.toString()+SEPARATOR+time+SEPARATOR+tag+SEPARATOR+message+SEPARATOR+NEW_LINE;
        createLogFile();
        try {
            outputStream.write(line.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int createLogFile(){
        int result = -1;
        if (appType==AppType.RELEASE){
            if (!today.equals(Utils.getTodayDate())){
                if (outputStream != null)
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                today = Utils.getTodayDate();
                String fileName = FILE_NAME+"_"+today+"_"+FILE_EXTENSION;
                File file = new File(context.getFilesDir(),fileName);
                result = 0;
                try {
                    outputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
                    writeToFile(MessageType.Debug,"Logger","Log file created");
                } catch (FileNotFoundException e) {
                    result = 1;
                }
            }
        }
        return result;
    }


}
