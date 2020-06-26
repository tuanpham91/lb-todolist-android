package com.anhtuan.custom;

import android.content.Context;
import android.view.View;

public class ModifyItemDialog extends TodoEntryDialog {

    public ModifyItemDialog(Context context,
                            String titel,
                            View.OnClickListener closeDialogListener,
                            View.OnClickListener updateButtonDialogListener) {
        super(context);
        this.setTitle(titel);
        this.getCloseUpdateDialogButton().setOnClickListener(closeDialogListener);
        this.getApplyUpdateDialogButton().setOnClickListener(updateButtonDialogListener);
    }

}
