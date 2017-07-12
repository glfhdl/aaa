package dd.final_report_02_20151046;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NaviAdapter extends BaseAdapter {
    private Context context;
    List<String> menus = null;
    private LayoutInflater layoutInflater;



    public NaviAdapter(Context context, List<String> menus){
        this.context = context;
        this.menus = menus;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {

        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {

        return menus.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.navigation_menu_list_item, viewGroup, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(menus.get(position));


        return convertView;
    }



}
