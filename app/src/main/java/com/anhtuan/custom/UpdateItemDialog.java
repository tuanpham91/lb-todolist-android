package com.anhtuan.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.anhtuan.lbtodolist.R;
// Deprecated
public class UpdateItemDialog extends Dialog {
    private Button closeUpdateDialogButton;
    private Button applyUpdateDialogButton;
    private AutoCompleteTextView udNameET;
    private Spinner udCategorySpinner;
    private EditText udAmountET;

    public UpdateItemDialog(Context context,
                            ArrayAdapter<CharSequence> spinnerAdapter,
                            ArrayAdapter<String> itemSuggestionListAdapter,
                            View.OnClickListener closeDialogListener,
                            View.OnClickListener updateButtonDialogListener)
    {
        super(context);
        this.setContentView(R.layout.update_dialog);
        this.setTitle("Update entry");

        closeUpdateDialogButton = (Button) this.findViewById(R.id.update_dialog_cancel_button);
        applyUpdateDialogButton = (Button) this.findViewById(R.id.update_dialog_update_button);
        udNameET = (AutoCompleteTextView) this.findViewById(R.id.update_dialog_et_1);
        udCategorySpinner = (Spinner) this.findViewById(R.id.update_dialog_et_2);
        udAmountET = (EditText) this.findViewById(R.id.update_dialog_et_3);

        udNameET.setAdapter(itemSuggestionListAdapter);
        udCategorySpinner.setAdapter(spinnerAdapter);

        closeUpdateDialogButton.setOnClickListener(closeDialogListener);
        applyUpdateDialogButton.setOnClickListener(updateButtonDialogListener);

    }

    public Button getApplyUpdateDialogButton() {
        return applyUpdateDialogButton;
    }

    public AutoCompleteTextView getUdNameET() {
        return udNameET;
    }

    public Spinner getUdCategorySpinner() {
        return udCategorySpinner;
    }

    public EditText getUdAmountET() {
        return udAmountET;
    }

    public Button getCloseUpdateDialogButton() {
        return closeUpdateDialogButton;
    }
}
