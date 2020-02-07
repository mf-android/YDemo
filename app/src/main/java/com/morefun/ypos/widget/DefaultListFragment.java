package com.morefun.ypos.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.morefun.ypos.R;


public class DefaultListFragment extends BaseDialogFragment {

    private TextView mTvTitle;
    private ListView mListView;
    private String mTitle;
    private ArrayAdapter<String> mAdapter;
    AdapterView.OnItemClickListener mOnItemClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_default_list, container, false);
        findView(view);
        initView();
        return view;
    }

    private void findView(View view) {
        mTvTitle = view.findViewById(R.id.tv_title);
        mListView = view.findViewById(R.id.listView);
    }

    private void initView() {
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }
        if (mOnItemClickListener != null) {
            mListView.setOnItemClickListener(mOnItemClickListener);
        }
    }

    public DefaultListFragment setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
        mTitle = title;
        return this;
    }

    public DefaultListFragment setAdapter(ArrayAdapter adapter) {
        if (mListView != null) {
            mListView.setAdapter(adapter);
        }
        mAdapter = adapter;
        return this;
    }

    public DefaultListFragment setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        if (mListView != null) {
            mListView.setOnItemClickListener(listener);
        }
        mOnItemClickListener = listener;
        return this;
    }
}
