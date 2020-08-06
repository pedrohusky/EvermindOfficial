package com.example.Evermind;

import android.content.Context;
import android.graphics.ColorSpace;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.adapters.AbsListViewOverScrollDecorAdapter;


//TODO SETUP ADAPTER CGECKBOX \/

//   ArrayList arrayList = new ArrayList<Checkboxlist_model>();

//  arrayList.add(new Checkboxlist_model(1, "text"));
//arrayList.add(new Checkboxlist_model(2, "text"));
//   arrayList.add(new Checkboxlist_model(3, "text"));
//   arrayList.add(new Checkboxlist_model(4, "text"));

//    ListView listView = getActivity().findViewById(R.id.listview_checkbox);
//   adapter_checkbox = new CheckboxAdapter(this.getActivity(), arrayList);
//   listView.setAdapter(adapter_checkbox);










public class CheckboxAdapter  extends BaseAdapter {

    private Context context;
    public static ArrayList<Checkboxlist_model> modelArrayList;


    public CheckboxAdapter(Context context, ArrayList<Checkboxlist_model> modelArrayList) {

        this.context = context;
        this.modelArrayList = modelArrayList;

    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.note_checkboxlist, null, true);

            holder.checkBox = convertView.findViewById(R.id.checkBox_note_editor);
            holder.content = convertView.findViewById(R.id.checkbox_text);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }


        holder.checkBox.setText("Checkbox "+position);


        holder.checkBox.setTag( position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer)  holder.checkBox.getTag();
                Toast.makeText(context, "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();

            }
        });

        return convertView;
    }

    private class ViewHolder {

        protected CheckBox checkBox;
        private TextView content;

    }

}