package lenguyenthanh.facememo.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by lenguyenthanh on 11/14/14.
 */
public class StorageUtil {
    public static final String PHOTO_FOLDER = "/facememo";
    public static final String BLURR_PHOTO_FOLDER = "/facememo_blurr";
    private static final String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String getRootDirectory(Context aContext, String name) {
        File file = aContext.getExternalCacheDir();
        String folderName;
        if(file == null)
            folderName = LOCAL_PATH + name;
        else folderName = file.toString() + name;
        File folder = new File(folderName);
        if(!folder.exists()){
            folder.mkdir();
        }
        return folderName + "/";
    }

    public static String getNormalRootDirectory(Context aContext) {
        return getRootDirectory(aContext, PHOTO_FOLDER);
    }

    public static String getBlurrRootDirectory(Context aContext) {
        return getRootDirectory(aContext, BLURR_PHOTO_FOLDER);
    }

}
