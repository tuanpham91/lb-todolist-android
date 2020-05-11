package com.anhtuan.custom;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anhtuan.lbtodolist.R;

public class RowListViewHolder {
    private View row;
    private TextView upperText = null, lowerText = null;
    private ImageButton deleteButton = null , editButton = null;

    public RowListViewHolder(View row) {
        this.row = row;
    }

    public TextView getUpperText() {
        if (upperText == null) {
            this.upperText = (TextView) row.findViewById(R.id.tdl_entry_name);
        }
        return this.upperText;
    }

    public TextView getLowerText() {
        if (this.lowerText == null) {
            this.lowerText = (TextView) row.findViewById(R.id.tdl_entry_quantity);
        }
        return this.lowerText;
    }

    public ImageButton getDeleteButton() {
        if (this.deleteButton == null) {
            this.deleteButton = (ImageButton) row.findViewById(R.id.tdl_entry_delete_button);
        }
        return this.deleteButton;
    }

    public ImageButton getEditButton() {
        if (this.editButton == null) {
            this.editButton = (ImageButton) row.findViewById(R.id.tdl_entry_edit_button);
        }
        return this.editButton;
    }
}
