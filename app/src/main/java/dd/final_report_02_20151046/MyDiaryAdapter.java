package dd.final_report_02_20151046;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jin on 2017-06-26.
 */

public class MyDiaryAdapter extends BaseAdapter{
    private Context context;
    ArrayList<MyDiaryData> myDiaryData;
    private LayoutInflater layoutInflater;



    public MyDiaryAdapter(Context context, ArrayList<MyDiaryData> myDiaryData){
        this.context = context;
        this.myDiaryData = myDiaryData;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myDiaryData.size();
    }

    @Override
    public Object getItem(int position) {
        return myDiaryData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return myDiaryData.indexOf(position);
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        final int position = pos;
        if(view == null){
            view = layoutInflater.inflate(R.layout.mydiary_data_list_item, viewGroup, false);
        }

        TextView textTitle = (TextView) view.findViewById(R.id.list_diary_title);
        TextView textDate = (TextView) view.findViewById(R.id.list_diary_date);
        TextView textContext = (TextView) view.findViewById(R.id.list_diary_context);

        textTitle.setText(myDiaryData.get(pos).getTitle());
        textDate.setText(myDiaryData.get(pos).getDate());
        textContext.setText(myDiaryData.get(pos).getContext());



        return view;
    }

}
