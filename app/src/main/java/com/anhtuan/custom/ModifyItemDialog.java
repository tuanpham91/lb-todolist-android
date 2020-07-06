package com.anhtuan.custom;

import android.content.Context;
import android.view.View;

public class ModifyItemDialog extends TodoEntryDialog {

    public ModifyItemDialog(Context context,
                            String titel,
                            View.OnClickListener updateButtonDialogListener) {
        super(context);
        this.setTitle(titel);
        this.getCloseUpdateDialogButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.getApplyUpdateDialogButton().setOnClickListener(updateButtonDialogListener);
    }

}
