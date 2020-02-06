package com.morefun.ypos.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;
import com.morefun.ypos.SDKManager;
import com.morefun.ypos.apitest.EmvHandlerTest;
import com.morefun.ypos.interfaces.OnInputAmountCallBack;
import com.morefun.ypos.interfaces.OnIssuerVoiceReference;
import com.morefun.ypos.interfaces.OnSelectAccountType;
import com.morefun.ypos.interfaces.OnSelectAppCallBack;
import com.morefun.ypos.uitls.ActionItems;
import com.morefun.ypos.uitls.ViewAIDAndCAPK;
import com.morefun.ypos.widget.DefaultListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AIDAndCapkActivity extends AppCompatActivity {
    List<String> items = new ArrayList<>();
    DeviceServiceEngine mSDKManager;
    private static final String TAG = "AIDAndCapkActivity";
    ActionItems ac;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initItems();
        ac = new ActionItems(this);

        this.setTitle("More...");
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    test(items.get(position));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
 // mpos more then 20
    private void initItems() {
        items.add(API_NAME.GET_AID );
        items.add(API_NAME.SET_AID );
        items.add(API_NAME.GET_CAPK);
        items.add(API_NAME.SET_CAPK);
        items.add(API_NAME.VIEW_AID);
        items.add(API_NAME.VIEW_CAPK);
    }

    interface API_NAME{
        String GET_AID = "Get AID list";
        String SET_AID = "Set AID list";
        String GET_CAPK = "Get CAPK list";
        String SET_CAPK = "Set CAPK list";
        String VIEW_CAPK = "View CAPK list";
        String VIEW_AID = "View AID list";
    }

    public void test(String id) throws RemoteException {
        this.id = id;
        mSDKManager = SDKManager.getInstance().getDeviceServiceEngine();
        if (mSDKManager == null) {
            Log.e(TAG, "ServiceEngine is Null");
            return;
        }

        if (id.equals(API_NAME.GET_AID)) {
            EmvHandlerTest.getInstance().setEmvHandler(mSDKManager, mAlertDialogOnShowListener).getAidList();
        } else if (id.equals(API_NAME.SET_AID)) {
            EmvHandlerTest.getInstance().setEmvHandler(mSDKManager, mAlertDialogOnShowListener).setAIDList();
        } else if (id.equals(API_NAME.GET_CAPK)) {
            EmvHandlerTest.getInstance().setEmvHandler(mSDKManager, mAlertDialogOnShowListener).getCAPKList();
        } else if (id.equals(API_NAME.SET_CAPK)) {
            EmvHandlerTest.getInstance().setEmvHandler(mSDKManager, mAlertDialogOnShowListener).setCAPKList();
        }else if (id.equals(API_NAME.VIEW_AID)){
            ViewAIDAndCAPK aid = new ViewAIDAndCAPK(mSDKManager.getEmvHandler());
            final Map<String, String> aidMap = aid.getAidMap();
            final String[] data = aid.getStringArray(aidMap);
            final DefaultListFragment fragment =  new DefaultListFragment().setTitle("Pls choice a AID")
                    .setAdapter(new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1 , data));
            fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                    Log.d(TAG ,"item = " + index);
                    fragment.dismissAllowingStateLoss();
                    showmsg(aidMap.get(data[index]));
                }
            });
            fragment.show(AIDAndCapkActivity.this, "aid");
        }else if (id.equals(API_NAME.VIEW_CAPK)){
            ViewAIDAndCAPK capk = new ViewAIDAndCAPK(mSDKManager.getEmvHandler());
            ArrayList<String> capkList = capk.getCapkList();
            String[] data = capkList.toArray(new String[capkList.size()]);

            final DefaultListFragment fragment =  new DefaultListFragment().setTitle("Pls choice a app")
                    .setAdapter(new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1 , data));
            fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                    Log.d(TAG ,"item = " + index);
                    fragment.dismissAllowingStateLoss();
                }
            });
            fragment.show(AIDAndCapkActivity.this, "capk");
        }
    }


    private MainActivity.AlertDialogOnShowListener mAlertDialogOnShowListener = new MainActivity.AlertDialogOnShowListener() {
        @Override
        public void showMessage(String content) {
            showmsg(content);
        }

        @Override
        public void showProgress(final String content, final ActionItems.OnCancelCall onCall) {
            AIDAndCapkActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ac.showwait(id, content, onCall);
                }
            });
        }

        @Override
        public void dismissProgress() {
            if (ac != null) {
                ac.closewait();
            }
        }

        @Override
        public void onSelApp(List<String> appNameList, boolean isFirstSelect, OnSelectAppCallBack callBack) {

        }

        @Override
        public void onInputAmount(OnInputAmountCallBack callBack) {

        }

        @Override
        public void onSelectAccountType(List<String> accountTypes, OnSelectAccountType callBack) {

        }

        @Override
        public void onIssuerVoiceReference(String pan, OnIssuerVoiceReference callBack) {

        }

        @Override
        public void showPinPad(String pan) {

        }
    };

    private void showmsg(final String msg) {
        AIDAndCapkActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ac.blockmsg(id, msg);
            }
        });
    }
    public Context getContext() {
        return this;
    }
}
