package com.example.sachoffline.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SachTruyenSqlite extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private static String DB_Name = "sachtruyenOffline.db";
    private SQLiteDatabase mDatabase;
    private static String DB_PATH = "";
    private final Context mContext;

    public SachTruyenSqlite(Context context) {
        super(context, DB_Name, null, 1);
        if (Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() {
        boolean mDataBaseExit = checkDataBase();
        if (!mDataBaseExit) {
            this.getReadableDatabase();
            this.close();
            try {
                {
                    copyDataBase();
                    Log.e(TAG, "createDatabase database created");
                }
            } catch (IOException mIoException) {
                throw new Error("ErrorCopyingDatabase");
            }
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_Name);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_Name);
        String outFileName = DB_Name + DB_Name;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLenght;
        while ((mLenght = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLenght);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }


    public Boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_Name;
        mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDatabase != null;
    }


    public synchronized void close() {
        if (mDatabase != null)
            mDatabase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
