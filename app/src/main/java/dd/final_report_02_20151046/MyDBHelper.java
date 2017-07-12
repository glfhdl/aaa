package dd.final_report_02_20151046;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jin on 2017-06-26.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "my_db";

    //    테이블 명 및 컬럼 명은 외부에서 많이 참조하므로 상수로 선언
    public static final String TABLE_NAME = "my_table";

    public static final String COL_ID = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTEXT  = "context";
   // public static final String COL_WhICH = "which";
   // public static final String COL_PHOTO = "photo";
    public static final String COL_DATE = "date";

    public MyDBHelper(Context  context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "create table if not exists " + TABLE_NAME +
                "( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " TEXT, " + COL_CONTEXT + " TEXT," + COL_DATE + " TEXT)";
        db.execSQL(createTable);

//        필요에 따라 샘플 데이터 추가


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        테이블 업그레이드가 필요할 경우 업그레이드 내용 작성
    }
}
