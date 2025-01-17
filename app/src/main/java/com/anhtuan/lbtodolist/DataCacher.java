package com.anhtuan.lbtodolist;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataCacher {

    private static DataCacher cacher;
    private Context context;
    public File localListFile;
    public File localAllItemsFile;
    public File basicAuthFile;
    public File userInfoFile;

    private static String localListFileName = "todolist-cache";
    private static String localAllItemsFileName= "allitems-cache";
    private static String basicAuthFileName = "basicAuth-cache";
    private static String userInfoFileName = "userInfo-cache";

    public String readStringFromFile(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            return "";
        }
    }

    public void saveStringToFile(File file, String content) {
        try (FileOutputStream fos = context.openFileOutput(file.getName(), Context.MODE_PRIVATE)){
            fos.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataCacher(Context context) {
        this.context = context;
    }

    public static DataCacher getCacher(Context context) {
        if (cacher == null) {
            cacher = new DataCacher(context);
            cacher.localListFile = new File(context.getFilesDir(), localListFileName);
            cacher.localAllItemsFile = new File(context.getFilesDir(), localAllItemsFileName);
            cacher.basicAuthFile = new File(context.getFilesDir(), basicAuthFileName);
            cacher.userInfoFile = new File(context.getFilesDir(), userInfoFileName);
        }
        return cacher;
    }

    public void cacheTodoListContent(String content) {
        saveStringToFile(localListFile, content);
    }

}
