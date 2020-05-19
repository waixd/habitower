package com.example.android.habitower;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.List;

public class CustomAdapter extends ArrayAdapter {

    Context context;
    List<String> modelItems;
    boolean[] checkBoxState;
    @SuppressWarnings("unchecked")

    public CustomAdapter(Context context, List<String> resource) {
        super(context , R.layout.fragment_todo, resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.modelItems = resource;
        checkBoxState=new boolean[resource.size()];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row_id, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        name.setText(modelItems.get(position));
        cb.setTag(new Integer(position));
        cb.setOnCheckedChangeListener(null);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Integer pos = (Integer)buttonView.getTag();
                if(isChecked){

                    checkBoxState[pos.intValue()]=true;
                }
                else{
                    checkBoxState[pos.intValue()]=false;
                    Log.e("checked","unchecked");

                }
            }
        });

        if(checkBoxState.length>0)
            cb.setChecked(checkBoxState[position]);

        return convertView;
    }
}
