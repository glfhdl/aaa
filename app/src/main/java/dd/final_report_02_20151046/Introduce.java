package dd.final_report_02_20151046;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Introduce extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.Introduce_close :
                finish();

        }

    }
}
