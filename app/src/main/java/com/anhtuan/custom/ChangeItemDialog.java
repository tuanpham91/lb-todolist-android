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

public class ChangeItemDialog extends Dialog{

    private Button closeDiaglogButton;
    private Button createDiaglogButton;
    private AutoCompleteTextView cdNameET;
    private Spinner cdCategorySpinner;

    private EditText cdAmountET;
    private ArrayAdapter<String> itemSuggestionListAdapter;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    public ChangeItemDialog(String titel,
                            Context context,
                            View.OnClickListener closeDialogListener,
                            View.OnClickListener applyDialogListener)
    {
        super(context);
        this.setContentView(R.layout.add_item_dialog);
        this.setCustomTitle(titel);
        createDiaglogButton = (Button) this.findViewById(R.id.create_dialog_create_button);
        closeDiaglogButton = (Button) this.findViewById(R.id.create_dialog_cancel_button);
        cdNameET = (AutoCompleteTextView) this.findViewById(R.id.create_dialog_et_1);
        cdCategorySpinner = (Spinner) this.findViewById(R.id.create_dialog_et_3);
        cdAmountET = (EditText) this.findViewById(R.id.create_dialog_et_4);
        spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.category_array, R.layout.spinner_item);
        cdCategorySpinner.setAdapter(spinnerAdapter);
        itemSuggestionListAdapter =  new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, AllItemListDataHolder.getAllUniqueItemList());
        cdNameET.setAdapter(itemSuggestionListAdapter);

        closeDiaglogButton.setOnClickListener(closeDialogListener);
        createDiaglogButton.setOnClickListener(applyDialogListener);
    }

    public void setCustomTitle(String title) {
        this.setTitle(title);
    }

    public void setDefault() {
        cdNameET.setText("");
        cdAmountET.setText("1");
        cdCategorySpinner.setSelection(0);
    }

    public Button getCloseDiaglogButton() {
        return closeDiaglogButton;
    }

    public Button getCreateDiaglogButton() {
        return createDiaglogButton;
    }

    public AutoCompleteTextView getCdNameET() {
        return cdNameET;
    }

    public Spinner getCdCategorySpinner() {
        return cdCategorySpinner;
    }

    public EditText getCdAmountET() {
        return cdAmountET;
    }
}
