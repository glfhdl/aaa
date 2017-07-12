package dd.final_report_02_20151046;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryDetailpage extends AppCompatActivity {

    private MyDBHelper myDBHelper;
    private EditText etNewTitle;
    private EditText etNewContext;
    private TextView etNewDate;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detailpage);


        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.add);
        ab.setDisplayHomeAsUpEnabled(true);


        myDBHelper = new MyDBHelper(this);
        etNewTitle = (EditText) findViewById(R.id.detailpage_diary_title);
        etNewContext = (EditText) findViewById(R.id.detailpage_diary_context);
        etNewDate = (TextView) findViewById(R.id.detailpage_diary_date);
        etNewDate.setText(getTime());
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.detailpage_diary_addbtn:
                //        전달 받은 intent를 가져옴
                Intent receivedIntent = getIntent();
                int id = -1;
                //        intent에 putExtra() 를 사용하여 저장한 값을 추출
                String _id = String.valueOf(receivedIntent.getIntExtra("_id", id));

                //                쓰기 가능 데이터베이스 가져오기
                SQLiteDatabase db = myDBHelper.getWritableDatabase();

//                수정할 레코드 항목 정보 설정
                ContentValues row = new ContentValues();
                row.put(MyDBHelper.COL_TITLE, etNewTitle.getText().toString());
                row.put(MyDBHelper.COL_CONTEXT, etNewContext.getText().toString());
                row.put(MyDBHelper.COL_DATE, etNewDate.getText().toString());
//                수정을 위한 where 절 구성
                String whereClause = MyDBHelper.COL_ID + "=?";
                String[] whereArgs = {_id };

//                레코드 수정 수행 - 수정 성공 시 수정한 레코드 개수 반환
                long result = db.update(MyDBHelper.TABLE_NAME, row, whereClause, whereArgs);

                if (result > 0) Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show();

//                마무리 작업
                myDBHelper.close();

                break;
            case R.id.detailpage_diary_closebtn:
                finish();
                break;
        }
    }
}

