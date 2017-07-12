// 과제명: 다이어리앱
// 분반: 02 분반
// 학번: 20151046 성명: 진슬기
// 제출일: 2017년6월XX일


package dd.final_report_02_20151046;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //    Log 를 위한 TAG
    public final static String TAG = "MainActivity";
    private MyDBHelper myDBHelper;      // SQLiteOpenHelper 상속 클래스

    private DrawerLayout drawerLayout;
    private ConstraintLayout constraintLayout;
    List<String> navi_data = new ArrayList<>(); //네비게이션 속 메뉴
    ListView memoListView;//다이어리 리스트뷰
    MyDiaryAdapter myDiaryAdapter;//다이어리 리스트 어댑터
    ArrayList<MyDiaryData> myDiaryData;//다이어리 리스트 데이터
    EditText edSearch;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        View v = menu.findItem(R.id.menu_search).getActionView();
        if (v != null) {
            edSearch = (EditText) v.findViewById(R.id.edSearch);

            if (edSearch != null) {
                edSearch.setOnEditorActionListener(onSearchListener);
            }
        } else {
            Toast.makeText(getApplicationContext(), "ActionView is null.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    private TextView.OnEditorActionListener onSearchListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
                // 검색 메소드 호출
                search();

                // 키패드 닫기
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            return (true);
        }
    };
    private void search() {
        String searchString = edSearch.getEditableText().toString();
        Toast.makeText(this, "검색어 : " + searchString, Toast.LENGTH_SHORT).show();

        //        읽기 가능 데이터베이스 가져오기
        SQLiteDatabase db = myDBHelper.getReadableDatabase();

//                검색을 위한 where 절 구성
        String[] cols = {MyDBHelper.COL_ID, MyDBHelper.COL_TITLE, MyDBHelper.COL_CONTEXT, MyDBHelper.COL_DATE};
        String whereClause = MyDBHelper.COL_TITLE + "=?";
        String[] whereArgs = {edSearch.getText().toString() };

        Cursor cursor = db.query(MyDBHelper.TABLE_NAME, cols, whereClause, whereArgs, null, null, null, null);

        int flag = 0;
        while (cursor.moveToNext()) {
            if(edSearch.getText().toString().equals(cursor.getString(1))){
                flag = 1;
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DiaryDetailpage.class);
                intent.putExtra("_id", Integer.valueOf(cursor.getString(0)));
                startActivity(intent);

                break;}
        }

        if(flag ==0)
            Toast.makeText(this, "검색결과는 없습니다.", Toast.LENGTH_SHORT).show();

//                마무리 작업
        cursor.close();
        myDBHelper.close();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawerLayout = (DrawerLayout) findViewById(R.id.navi_drawable);
        switch (item.getItemId()) {
            /*
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
               */
            case R.id.menu0://새 항목 추가
                Intent intent = new Intent(MainActivity.this, AddDiary.class);
                startActivity(intent);
                break;
            case R.id.menu1://개발자 소개
                Intent intent1 = new Intent(MainActivity.this, Introduce.class);
                startActivity(intent1);
                break;
            case R.id.menu2://종료료
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("종료 확인");
                builder.setMessage("종료하시겠습니까?");
                builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("취소", null);
                // 미리 객체를 만들어 놓기때문에 대화상자를 여러번 사용할때 편리
                Dialog dlg = builder.create();
                dlg.setCanceledOnTouchOutside(false);
                dlg.show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        viewTable();    // 테이블 전체의 내용을 Log에 출력하는 메소드 호출
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        myDBHelper = new MyDBHelper(this);
        myDiaryData = new ArrayList<MyDiaryData>();//다이어리 리스트 데이터 itemList =


        //data를 전달하여 새로운 Adapter를 생성
        myDiaryAdapter= new MyDiaryAdapter(this, myDiaryData);
        //리스트뷰 레이아웃을 불러옵니다.
        memoListView = (ListView)findViewById(R.id.main_diary_list);myDiaryAdapter.notifyDataSetChanged();
        //불러온 리스트뷰에 생성한 Adater 넣음
        memoListView.setAdapter(myDiaryAdapter);
        memoListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, DiaryDetailpage.class);
                intent.putExtra("_id", myDiaryData.get(position).get_id());
                startActivity(intent);
            }
        });
        memoListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final int pos = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("삭제 확인");
            builder.setMessage("삭제하시겠습니까?");
            builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //                쓰기 가능 데이터베이스 가져오기
                    SQLiteDatabase db = myDBHelper.getWritableDatabase();

//                삭제를 위한 where 절 구성
                    String whereClause = MyDBHelper.COL_ID + "=?";
                    String[] whereArgs = {String.valueOf(Integer.valueOf(myDiaryData.get(pos).get_id()))};
//                삭제 수행 - 성공 시 삭제된 레코드 개수를 반환
                    long result = db.delete(MyDBHelper.TABLE_NAME, whereClause, whereArgs);


                    if (result > 0) {
                        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                    } else Toast.makeText(MainActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
                    myDBHelper.close();
                    viewTable();
                }
            });
            builder.setNegativeButton("취소", null);
            // 미리 객체를 만들어 놓기때문에 대화상자를 여러번 사용할때 편리
            Dialog dlg = builder.create();
            dlg.setCanceledOnTouchOutside(false);
            dlg.show();


            return true;

        }
        });
/*
        //맨 위 햄버거 버튼 설정
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.menubutton);
        ab.setDisplayHomeAsUpEnabled(true);

        /*
        //네비게이션 메뉴 인플레이트
        constraintLayout = (ConstraintLayout) findViewById(R.id.main);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_navigation_menu, constraintLayout);

        //네비게이션 메뉴 항목
        navi_data.add("새 항목 추가");
        navi_data.add("개발자 소개");
        navi_data.add("종료");
        */

/*
        //data를 전달하여 새로운 Adapter를 생성
        NaviAdapter naviAdapter= new NaviAdapter(this, navi_data);
        //리스트뷰 레이아웃을 불러옵니다.
        ListView navigationMenuListView = (ListView) findViewById(R.id.navi_listview);
        //불러온 리스트뷰에 생성한 Adater 넣음
        navigationMenuListView.setAdapter(naviAdapter);
        navigationMenuListView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0://새 항목 추가
                        Intent intent = new Intent(MainActivity.this, AddDiary.class);
                        startActivity(intent);
                        break;
                    case 1://개발자 소개
                        Intent intent1 = new Intent(MainActivity.this, Introduce.class);
                        startActivity(intent1);
                        break;
                    case 2://종료료
                       AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setTitle("종료 확인");
                        builder.setMessage("종료하시겠습니까?");
                        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setNegativeButton("취소", null);
                           // 미리 객체를 만들어 놓기때문에 대화상자를 여러번 사용할때 편리
                        Dialog dlg = builder.create();
                        dlg.setCanceledOnTouchOutside(false);
                        dlg.show();
                        break;
                }
            }
        });
        */

    }

    //    테이블 전체의 내용을 Log에 출력하는 메소드
    private void viewTable() {
//        레코드를 MyItem에 저장한 후 보관할 리스트를 초기화
        myDiaryData.clear();

//        읽기 가능 데이터베이스 가져오기
        SQLiteDatabase db = myDBHelper.getReadableDatabase();

        String[] cols = null;
        String whereClause = null;
        String[] whereArgs = null;
//        아래 부분을 사용할 경우 특정 이름을 갖는 레코드만 출력
//        String[] cols = {MyDBHelper.COL_ID, MyDBHelper.COL_NAME, MyDBHelper.COL_PHONE};
//        String whereClause = MyDBHelper.COL_NAME + "=?";
//        String[] whereArgs = {"홍길동"};

        Cursor cursor = db.query(MyDBHelper.TABLE_NAME, cols, whereClause, whereArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            int  _id = cursor.getInt(0);
            String title = cursor.getString(1);
            String data = cursor.getString(2);
            String context = cursor.getString(3);
//            레코드에서 읽어들인 값을 MyItem 객체에 저장
            MyDiaryData newItem = new MyDiaryData(_id, title, data, context);

//            MyItem 객체를 리스트에 저장
            myDiaryData.add(newItem);
        }

//        리스트의 내용을 로그에 출력
        for (MyDiaryData item : myDiaryData) {
            Log.i(TAG, item.toString());
        }

//        마무리 작업
        cursor.close();
        myDBHelper.close();

//        원본 리스트(itemList)를 새로 읽어왔으므로 어댑터에게 변경 통지
        myDiaryAdapter.notifyDataSetChanged();
    }

}
