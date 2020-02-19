package com.morefun.ysdk.sample.dialog;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;


public class AmountTextWatcher implements TextWatcher {

    private String lastAmount = "0.00";
    private EditText editText;
    private String hint;

    public AmountTextWatcher(EditText editText) {
        this(editText, null);
    }

    public AmountTextWatcher(EditText editText, String hint) {
        this.editText = editText;
        this.hint = hint;

        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > '9' || s.charAt(i) < '0') {
            } else {
                text += s.charAt(i);
            }
        }

        if (text.length() > 12) {
            text = lastAmount;
        } else {
            text = getReadableAmount(text);
            lastAmount = text;
        }

        editText.removeTextChangedListener(this);
        if ("0.00".equals(text) && hint != null) {
            editText.setText(null);
            editText.setHint(hint);
        } else {
            editText.setText(text);
            editText.setSelection(text.length());
        }
        editText.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private String getReadableAmount(String amount) {
        if (amount != null && !amount.isEmpty()) {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(Double.parseDouble(amount) / 100.0D);
        } else {
            return "0.00";
        }
    }

}
