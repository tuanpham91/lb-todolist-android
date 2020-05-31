package com.anhtuan.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.anhtuan.lbtodolist.ListActivity;
import com.anhtuan.lbtodolist.R;
import com.anhtuan.pojo.TodoEntry;

// TODO : https://stackoverflow.com/questions/11281952/listview-with-customized-row-layout-android
public class ListViewArrayAdapter extends ArrayAdapter<TodoEntry> {

    private ListActivity parentActivity;

    public ListViewArrayAdapter(@NonNull Context context, int resource, ListActivity parentActivity) {
        super(context, resource);
        this.parentActivity=parentActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowListViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.todolist_entry, parent, false);
            holder = new RowListViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (RowListViewHolder) convertView.getTag();
        }
        // TODO Set this right.
        TodoEntry currentEntry = this.getItem(position);
        holder.getUpperText().setText(this.getItem(position).getValue());
        holder.getLowerText().setText(this.getItem(position).getAmount().toString());
        holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
                parentActivity.deleteFromListRequestDAO(currentEntry);
            }
        });

        holder.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.openUpdateDialog(currentEntry);
            }
        });
        return convertView;
    }

    public void deleteItem(int position) {
        this.remove(this.getItem(position));
    }
}
