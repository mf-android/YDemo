package com.morefun.ypos.widget;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.morefun.ypos.R;


public class DefaultInputDialogFragment extends BaseDialogFragment {

    private int mLines = 1;

    private Button mBtnLeft;
    private Button mBtnRight;
    private TextView mTvTitle;
    private EditText mEtContent;
    private OnClickListener mOnClickListener;

    private String mTitle;
    private String mTextHint;
    private String mLeftButtonText;
    private String mRightButtonText;
    private String mContent;
    private int mRawInputType = - 1;
    private KeyListener mKeyListener;
    @LayoutRes
    private int mLayoutId = R.layout.fragment_default_input;

    public void setLayoutId(int mLayoutId) {
        this.mLayoutId = mLayoutId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(mLayoutId, container, false);
        findView(view);
        initView();
        setEvent();
        return view;
    }

    private void findView(View view) {
        mBtnLeft = (Button) view.findViewById(R.id.btn_left);
        mBtnRight = (Button) view.findViewById(R.id.btn_right);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mEtContent = (EditText) view.findViewById(R.id.et_content);
    }

    private void initView() {
        mTvTitle.setText(mTitle);
        mEtContent.setHint(mTextHint);
        mBtnLeft.setText(mLeftButtonText);
        mBtnRight.setText(mRightButtonText);
        mEtContent.setLines(mLines);
        if (mKeyListener != null) {
            mEtContent.setKeyListener(mKeyListener);
        }
        mEtContent.setText("2");
        mEtContent.setSelection(1);
    }

    private void setEvent() {
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtContent.getText().toString();
                if (mOnClickListener != null) {
                    mOnClickListener.onLeftClick(content);
                }
            }
        });

        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtContent.getText().toString();
                if (mOnClickListener != null) {
                    mOnClickListener.onRightClick(content);
                }
            }
        });
    }
    public void setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
        mTitle = title;
    }

    public void setTextHint(String hint) {
        if (mEtContent != null) {
            mEtContent.setHint(hint);
        }
        mTextHint = hint;
    }

    public void setLines(int lines) {
        if (mEtContent != null) {
            mEtContent.setLines(lines);
        }
        mLines = lines;
    }

    public DefaultInputDialogFragment setContent(String content) {
        if (mEtContent != null) {
//            ViewUtils.setEditTextContent(mEtContent, mContent);
            mEtContent.setText(content);
            mEtContent.setSelection(content.length());
        }
        mContent = content;
        return this;
    }

    public DefaultInputDialogFragment setContent(String content, int rawType, KeyListener keyListener) {
        if (mEtContent != null) {
            mEtContent.setInputType(rawType);
            mEtContent.setKeyListener(keyListener);
        }
        mContent = content;
        mRawInputType = rawType;
        mKeyListener = keyListener;
        return this;
    }


    public DefaultInputDialogFragment setText(String title, String hint, int lines, String leftButtonText, String rightButtonText) {
        setTitle(title);
        setTextHint(hint);
        setLines(lines);
        setLeftButton(leftButtonText);
        setRightButton(rightButtonText);
        return this;
    }

    public void setLeftButton(String title) {
        if (mBtnLeft != null) {
            mBtnLeft.setText(title);
        }
        mLeftButtonText = title;
    }

    public void setRightButton(String content) {
        if (mBtnRight != null) {
            mBtnRight.setText(content);
        }
        mRightButtonText = content;
    }

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public interface OnClickListener {
        void onLeftClick(String content);
        void onRightClick(String content);
    }
}
