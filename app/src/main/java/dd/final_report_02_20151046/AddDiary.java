package dd.final_report_02_20151046;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDiary extends AppCompatActivity {

    boolean isPhotoCaptured;
    boolean isPhotoFileSaved;
    boolean isPhotoCanceled;
    ImageView photo;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.add);
        ab.setDisplayHomeAsUpEnabled(true);

        photo = (ImageView) findViewById(R.id.add_diary_image);

        myDBHelper = new MyDBHelper(this);
        etNewTitle = (EditText) findViewById(R.id.add_diary_title);
        etNewContext = (EditText) findViewById(R.id.add_diary_context);
        etNewDate = (TextView) findViewById(R.id.add_diary_date);
        etNewDate.setText(getTime());

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_diary_addbtn:

//                쓰기 가능 데이터베이스 가져오기
                SQLiteDatabase db = myDBHelper.getWritableDatabase();

//                테이블에 추가할 항목 설정 - 강의자료 참조
                ContentValues row = new ContentValues();
                row.put(MyDBHelper.COL_TITLE, etNewTitle.getText().toString());
                row.put(MyDBHelper.COL_CONTEXT, etNewContext.getText().toString());
                row.put(MyDBHelper.COL_DATE, etNewContext.getText().toString());

//                레코드 추가 - 레코드 추가를 성공할 경우 추가한 레코드 개수 반환
                long result = db.insert(MyDBHelper.TABLE_NAME, null, row);

                if (result > 0) Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show();

                //마무리 작업
                myDBHelper.close();
                finish();
                break;
            case R.id.add_diary_closebtn:
                finish();
                break;
        }
    }
}
/*
    @Override
    protected Dialog onCreateDialog(int id) {

        AlertDialog.Builder builder = null;

        switch (id) {
            case BasicInfo.CONTENT_PHOTO:
                builder = new AlertDialog.Builder(this);

                mSelectdContentArray = R.array.array_photo;
                builder.setTitle("선택하세요");
                builder.setSingleChoiceItems(mSelectdContentArray, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mChoicedArrayItem = whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mChoicedArrayItem == 0) {
                            showPhotoCaptureActivity();
                        } else if (mChoicedArrayItem == 1) {
                            showPhotoSelectionActivity();
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        Log.d(TAG, "whichButton3        ======        " + whichButton);
                    }
                });

                break;

            case BasicInfo.CONTENT_PHOTO_EX:
                builder = new AlertDialog.Builder(this);

                mSelectdContentArray = R.array.array_photo_ex;
                builder.setTitle("선택하세요");
                builder.setSingleChoiceItems(mSelectdContentArray, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mChoicedArrayItem = whichButton;
                    }
                });
                builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mChoicedArrayItem == 0) {
                            showPhotoCaptureActivity();
                        } else if (mChoicedArrayItem == 1) {
                            showPhotoSelectionActivity();
                        } else if (mChoicedArrayItem == 2) {
                            isPhotoCanceled = true;
                            isPhotoCaptured = false;

                            mPhoto.setImageResource(R.drawable.person_add);
                        }
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                break;

            default:
                break;
        }

        return builder.create();
    }

    public void showPhotoCaptureActivity() {
        Intent intent = new Intent(getApplicationContext(), PhotoCaptureActivity.class);
        startActivityForResult(intent, BasicInfo.REQ_PHOTO_CAPTURE_ACTIVITY);
    }

    public void showPhotoSelectionActivity() {
        Intent intent = new Intent(getApplicationContext(), PhotoSelectionActivity.class);
        startActivityForResult(intent, BasicInfo.REQ_PHOTO_SELECTION_ACTIVITY);
    }
    */
    /**
     * 앨범의 사진을 사진 폴더에 복사한 후, PICTURE 테이블에 사진 정보 추가
     * 이미지의 이름은 현재 시간을 기준으로 한 getTime() 값의 문자열 사용
     *
     * @return 새로 추가된 이미지의 이름
     */
/*
    private String insertPhoto() {
        String photoName = null;

        if (isPhotoCaptured) { // captured Bitmap
            try {
                if (mMemoMode != null && mMemoMode.equals(BasicInfo.MODE_MODIFY)) {
                    Log.d(TAG, "previous photo is newly created for modify mode.");

                    String SQL = "delete from " + MemoDatabase.TABLE_PHOTO +
                            " where _ID = '" + mMediaPhotoId + "'";
                    Log.d(TAG, "SQL : " + SQL);
                    if (MultiMemoActivity.mDatabase != null) {
                        MultiMemoActivity.mDatabase.execSQL(SQL);
                    }

                    File previousFile = new File(BasicInfo.FOLDER_PHOTO + mMediaPhotoUri);
                    if (previousFile.exists()) {
                        previousFile.delete();
                    }
                }


                File photoFolder = new File(BasicInfo.FOLDER_PHOTO);

                //폴더가 없다면 폴더를 생성한다.
                if(!photoFolder.isDirectory()){
                    Log.d(TAG, "creating photo folder : " + photoFolder);
                    photoFolder.mkdirs();
                }

                // Temporary Hash for photo file name
                photoName = createFilename();

                FileOutputStream outstream = new FileOutputStream(BasicInfo.FOLDER_PHOTO + photoName);
                resultPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
                outstream.close();


                if (photoName != null) {
                    Log.d(TAG, "isCaptured            : " +isPhotoCaptured);

                    // INSERT PICTURE INFO
                    String SQL = "insert into " + MemoDatabase.TABLE_PHOTO + "(URI) values(" + "'" + photoName + "')";
                    if (MultiMemoActivity.mDatabase != null) {
                        MultiMemoActivity.mDatabase.execSQL(SQL);
                    }
                }

            } catch (IOException ex) {
                Log.d(TAG, "Exception in copying photo : " + ex.toString());
            }


        }
        return photoName;
    }

    /**
     * 다른 액티비티로부터의 응답 처리
     */
/*
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) {
            case BasicInfo.REQ_PHOTO_CAPTURE_ACTIVITY:  // 사진 찍는 경우
                Log.d(TAG, "onActivityResult() for REQ_PHOTO_CAPTURE_ACTIVITY.");

                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "resultCode : " + resultCode);

                    boolean isPhotoExists = checkCapturedPhotoFile();
                    if (isPhotoExists) {
                        Log.d(TAG, "image file exists : " + BasicInfo.FOLDER_PHOTO + "captured");

                        resultPhotoBitmap = BitmapFactory.decodeFile(BasicInfo.FOLDER_PHOTO + "captured");

                        tempPhotoUri = "captured";

                        mPhoto.setImageBitmap(resultPhotoBitmap);
                        isPhotoCaptured = true;

                        mPhoto.invalidate();
                    } else {
                        Log.d(TAG, "image file doesn't exists : " + BasicInfo.FOLDER_PHOTO + "captured");
                    }
                }

                break;

            case BasicInfo.REQ_PHOTO_SELECTION_ACTIVITY:  // 사진을 앨범에서 선택하는 경우
                Log.d(TAG, "onActivityResult() for REQ_PHOTO_LOADING_ACTIVITY.");

                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "resultCode : " + resultCode);

                    Uri getPhotoUri = intent.getParcelableExtra(BasicInfo.KEY_URI_PHOTO);
                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

                        resultPhotoBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(getPhotoUri), null, options);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    mPhoto.setImageBitmap(resultPhotoBitmap);
                    isPhotoCaptured = true;

                    mPhoto.invalidate();
                }

                break;

        }
    }


    /**
     * 저장된 사진 파일 확인
     */
/*
    private boolean checkCapturedPhotoFile() {
        File file = new File(BasicInfo.FOLDER_PHOTO + "captured");
        if(file.exists()) {
            return true;
        }

        return false;
    }

}
*/