package com.morefun.ysdk.sample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.morefun.ysdk.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InputDialog extends Dialog {

    @BindView(R.id.btnPositive)
    Button btnPositive;

    @BindView(R.id.btnNegtive)
    Button btnNegtive;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.etInput)
    EditText etInput;

    private String inputString;
    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;


    public InputDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_input);
        ButterKnife.bind(this);

        etInput.addTextChangedListener(new AmountTextWatcher(etInput, "0.00"));
    }

    @OnClick({R.id.btnNegtive, R.id.btnPositive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPositive:
                if (onClickBottomListener != null) {
                    inputString = etInput.getText().toString();
                    onClickBottomListener.onPositiveClick(inputString);
                }
                break;
            case R.id.btnNegtive:
                if (onClickBottomListener != null) {
                    onClickBottomListener.onNegtiveClick();
                }
                break;
        }

    }
    public InputDialog setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        return this;
    }

    public InputDialog setTextHint(String hint) {
        if (etInput != null) {
            etInput.setHint(hint);
        }
        return this;
    }

    public InputDialog setPositiveButton(String title) {
        if (btnPositive != null) {
            btnPositive.setText(title);
        }
        return this;
    }

    public InputDialog setNegtiveButton(String content) {
        if (btnNegtive != null) {
            btnNegtive.setText(content);
        }
        return this;
    }


    public InputDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }
    public interface OnClickBottomListener{
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick(String content);
        /**
         * 点击取消按钮事件
         */
        public void onNegtiveClick();
    }

}
