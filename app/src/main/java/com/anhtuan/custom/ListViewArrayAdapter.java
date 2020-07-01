package com.anhtuan.custom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.anhtuan.lbtodolist.ListActivity;
import com.anhtuan.lbtodolist.R;
import com.anhtuan.pojo.TodoEntry;

import java.util.ArrayList;
import java.util.Arrays;

public class ListViewArrayAdapter extends ArrayAdapter<TodoEntry> {

    private ListActivity parentActivity;
    private ArrayList<TodoEntry> itemList;

    public ListViewArrayAdapter(int resource, ListActivity parentActivity) {
        super(parentActivity.getApplicationContext(), resource);
        this.itemList = new ArrayList<>();
        this.parentActivity=parentActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowListViewHolder holder;
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
                parentActivity.getDataHolder().deleteFromListRequestDAO(currentEntry);
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

    @Override
    public void add(TodoEntry entry) {
        itemList.add(entry);
        super.add(entry);
    }

    @Override
    public void remove(TodoEntry entry) {
        itemList.remove(entry);
        super.remove(entry);
    }
    @Override
    public void clear() {
        itemList.clear();
        super.clear();
    }

    @Override
    public void addAll(TodoEntry... items) {
        itemList.addAll(Arrays.asList(items));
        super.addAll(items);
    }

    public int findEntry(TodoEntry entry) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getValue().equals(entry.getValue())) {
                return i;
            }
        }
        return -1;
    }

    public void deleteItem(int position) {
        this.remove(this.getItem(position));
    }
}
