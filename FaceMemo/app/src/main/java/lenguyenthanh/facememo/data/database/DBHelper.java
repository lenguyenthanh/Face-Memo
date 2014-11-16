package lenguyenthanh.facememo.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lenguyenthanh.facememo.FaceMemoApplication;
import lenguyenthanh.facememo.data.dao.DaoMaster;
import lenguyenthanh.facememo.data.dao.DaoSession;

/**
 * Created by lenguyenthanh on 7/15/14.
 */

public class DBHelper {
    private static DBHelper instance;
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private SQLiteDatabase db;

    public synchronized static DBHelper getInstance() {
        if(instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    public DBHelper() {
        Context appContext = FaceMemoApplication.getInstance().getApplicationContext();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(appContext, "facememo.db", null);
        db = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}
