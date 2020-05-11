package com.anhtuan.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.anhtuan.lbtodolist.R;

// TODO : https://stackoverflow.com/questions/11281952/listview-with-customized-row-layout-android
public class ListViewArrayAdapter extends ArrayAdapter<String> {


    public ListViewArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowListViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.todolist_entry, null, false);
            holder = new RowListViewHolder(convertView);
            convertView.setTag(holder);
        }
        // TODO Set this right.
        holder.getUpperText().setText(this.getItem(position));

    }
}
