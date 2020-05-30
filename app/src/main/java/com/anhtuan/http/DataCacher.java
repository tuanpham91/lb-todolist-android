package com.anhtuan.http;

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
    private static String localListFileName = "todolist-cache";
    private static String localAllItemsFileName= "allitems-cache";

    public String readStringFromFile(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void saveStringToFile(File file, String content) {
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
        }
        return cacher;
    }

    public void cacheTodoListContent(String content) {
        saveStringToFile(localListFile, content);
    }

}
