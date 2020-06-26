package com.anhtuan.custom;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.anhtuan.global.dataholder.AllItemListDataHolder;
import com.anhtuan.lbtodolist.R;
import com.anhtuan.pojo.TodoEntry;

public class TodoEntryDialog extends Dialog {
    private Button closeDialogButton;
    private Button applyDialogButton;
    private AutoCompleteTextView nameET;
    private Spinner categorySpinner;
    private EditText amountET;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private ArrayAdapter<String> itemSuggestionListAdapter;

    public TodoEntryDialog(Context context) {
        super(context);
        this.setContentView(R.layout.update_dialog);
        closeDialogButton = (Button) this.findViewById(R.id.update_dialog_cancel_button);
        applyDialogButton = (Button) this.findViewById(R.id.update_dialog_update_button);
        nameET = (AutoCompleteTextView) this.findViewById(R.id.update_dialog_et_1);
        categorySpinner = (Spinner) this.findViewById(R.id.update_dialog_et_2);
        amountET = (EditText) this.findViewById(R.id.update_dialog_et_3);
        spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.category_array, R.layout.spinner_item);
        categorySpinner.setAdapter(spinnerAdapter);
        itemSuggestionListAdapter =  new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, AllItemListDataHolder.getAllUniqueItemList());
        nameET.setAdapter(itemSuggestionListAdapter);
    }

    public void setEntry(TodoEntry entry) {
        nameET.setText(entry.getValue());
        amountET.setText(entry.getAmount().toString());
        categorySpinner.setSelection(spinnerAdapter.getPosition(entry.getKeywordCategory()));
        show();
    }

    public TodoEntry getRawEntry() {
        return new TodoEntry(nameET.getText().toString(),
                0L,
                "",
                categorySpinner.getSelectedItem().toString(),
                Long.valueOf(amountET.getText().toString()),"" , "");
    }

    public void setDefault() {
        nameET.setText("");
        amountET.setText("1");
        categorySpinner.setSelection(0);
    }

    public Button getApplyUpdateDialogButton() {
        return applyDialogButton;
    }

    public AutoCompleteTextView getUdNameET() {
        return nameET;
    }

    public Spinner getCategorySpinner() {
        return categorySpinner;
    }

    public EditText getAmountET() {
        return amountET;
    }

    public Button getCloseUpdateDialogButton() {
        return closeDialogButton;
    }

}
