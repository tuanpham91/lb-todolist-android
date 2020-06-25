package com.anhtuan.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.anhtuan.global.dataholder.AllItemListDataHolder;
import com.anhtuan.lbtodolist.R;
import com.anhtuan.pojo.TodoEntry;
// Deprecated

public class UpdateItemDialog extends Dialog {
    private Button closeUpdateDialogButton;
    private Button applyUpdateDialogButton;
    private AutoCompleteTextView udNameET;
    private Spinner udCategorySpinner;
    private EditText udAmountET;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private ArrayAdapter<String> itemSuggestionListAdapter;


    public UpdateItemDialog(Context context,
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
        spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.category_array, R.layout.spinner_item);

        udCategorySpinner.setAdapter(spinnerAdapter);

        itemSuggestionListAdapter =  new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, AllItemListDataHolder.getAllUniqueItemList());
        udNameET.setAdapter(itemSuggestionListAdapter);

        closeUpdateDialogButton.setOnClickListener(closeDialogListener);
        applyUpdateDialogButton.setOnClickListener(updateButtonDialogListener);

    }

    public void showEntry(TodoEntry entry) {
        udNameET.setText(entry.getValue());
        udAmountET.setText(entry.getAmount().toString());
        udCategorySpinner.setSelection(spinnerAdapter.getPosition(entry.getKeywordCategory()));
        show();
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
