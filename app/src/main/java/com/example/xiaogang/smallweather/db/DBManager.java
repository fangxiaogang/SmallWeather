package com.example.xiaogang.smallweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.example.xiaogang.smallweather.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hugo on 2015/9/30 0030.
 * 数据库管理类
 */
public class DBManager {

    private static String TAG = DBManager.class.getSimpleName();
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "china_city.db"; //数据库名字
    public static final String PACKAGE_NAME = "com.example.xiaogang.smallweather";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
        PACKAGE_NAME;  //在手机里存放数据库的位置(/data/data/com.xiecc.seeWeather/china_city.db)
    private SQLiteDatabase database;

    private Context mcontext;
    public DBManager(Context mcontext) {
        this.mcontext = mcontext;
    }


//    private DBManager() {
//
//    }
//
//    public static DBManager getInstance() {
//        return DBManagerHolder.sInstance;
//    }
//
//    private static final class DBManagerHolder {
//        public static final DBManager sInstance = new DBManager();
//    }

    public SQLiteDatabase getDatabase() {
        return database;
    }


    public void openDatabase() {
        //PLog.e(TAG, DB_PATH + "/" + DB_NAME);
        System.out.println("TAG, DB_PATH + \"/\" + DB_NAME");
        System.out.println(DB_PATH+"2"+DB_NAME);
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    @Nullable
    private SQLiteDatabase openDatabase(String dbfile) {

        try {
            if (!(new File(dbfile).exists())) {
                //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is =  mcontext.getResources().openRawResource(R.raw.china_city); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            return SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
//            PLog.e("IO exception");
            System.out.println("\"IO exception\"");
            e.printStackTrace();
        }

        return null;
    }

    public void closeDatabase() {
        if (this.database != null) {
            this.database.close();
        }
    }
}


