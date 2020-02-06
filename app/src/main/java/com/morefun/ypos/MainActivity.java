package com.morefun.ypos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.activities.AIDAndCapkActivity;
import com.morefun.ypos.activities.SerialPortActivity;
import com.morefun.ypos.activities.SettingActivity;
import com.morefun.ypos.apitest.ApduCPUCardHandlerTest;
import com.morefun.ypos.apitest.BeepLEDTest;
import com.morefun.ypos.apitest.DevicesInfoTest;
import com.morefun.ypos.apitest.EmvHandlerTest;
import com.morefun.ypos.apitest.EmvPBOCTest;
import com.morefun.ypos.apitest.FELICATest;
import com.morefun.ypos.apitest.InstallApkTest;
import com.morefun.ypos.apitest.M1CardHandlerTest;
import com.morefun.ypos.apitest.PinPadTest;
import com.morefun.ypos.apitest.PrinterTest;
import com.morefun.ypos.apitest.ScannerApi;
import com.morefun.ypos.apitest.SearchCardOrCardReaderTest;
import com.morefun.ypos.apitest.ZxingTest;
import com.morefun.ypos.interfaces.OnInputAmountCallBack;
import com.morefun.ypos.interfaces.OnIssuerVoiceReference;
import com.morefun.ypos.interfaces.OnSelectAccountType;
import com.morefun.ypos.interfaces.OnSelectAppCallBack;
import com.morefun.ypos.uitls.ActionItems;
import com.morefun.ypos.uitls.LanguageUtils;
import com.morefun.ypos.uitls.Utils;
import com.morefun.ypos.widget.DefaultInputDialogFragment;
import com.morefun.ypos.widget.DefaultListFragment;

import java.util.ArrayList;
import java.util.List;

import static com.morefun.ypos.uitls.Utils.byte2string;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static boolean DLreturn = false;
    SharedPreferences config = null;
    ActionItems ac;
    List<String> items = new ArrayList<>();
    DeviceServiceEngine mSDKManager;
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SDKManager.getInstance().bindService(getApplicationContext());
        mContext = this;
        LanguageUtils.setLanguage();
        Log.d(TAG, "onCreate" + Build.MODEL);
        setContentView(R.layout.activity_main);
        config = getSharedPreferences("config", Context.MODE_PRIVATE);

        intiitems();
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

        refrushcsid(config.getInt("CSID", 0));
        // PBOC();
        EmvPBOCTest.getInstance().initEmvListener();
        //init Path
        mTypeFacePath = PrinterTest.initTypeFacePath();
    }

    void refrushcsid(int csid) {
        ac = new ActionItems(this);
        this.setTitle(getString(R.string.activity_title_mid) + "  V" + Utils.getVersion(this.getApplicationContext()));
    }

    private static final String QR_CODE = "Generates a QR code";
    private static final String BAR_CODE = "Generates a Barcode";

    private void intiitems() {
        items.add(getString(R.string.login_devices));
        items.add(getString(R.string.menu_pboc2));
        items.add(getString(R.string.menu_apdu));
        items.add(getString(R.string.menu_m1card));
        items.add(getString(R.string.menu_felica));
//        items.add(getString(R.string.menu_eleccash));

        items.add(getString(R.string.init_dukpt));
        items.add(getString(R.string.initIPEK));
        items.add(getString(R.string.dukpt_calculation));
//        items.add(getString(R.string.menu_carddetail));
//        items.add(getString(R.string.menu_loadkek));
//        items.add(getString(R.string.menu_loadmasterkey));
//        items.add(getString(R.string.menu_loadworkkey));
        items.add(getString(R.string.menu_calmac));
//        items.add(getString(R.string.menu_caldata));
        items.add(getString(R.string.menu_capk));
        items.add(getString(R.string.menu_aid));

        items.add(getString(R.string.clear_capk_aid));
        items.add(getString(R.string.menu_beep));
        items.add(getString(R.string.menu_mag_card_reader));
        items.add(getString(R.string.menu_ic_card_reader));
        items.add(getString(R.string.menu_led));
        items.add(getString(R.string.menu_scanner));
        items.add(getString(R.string.menu_serialport));
        items.add(getString(R.string.menu_print));
        items.add(getString(R.string.menu_pinkeybord));
        items.add(getString(R.string.menu_sign));
//        items.add(QR_CODE);
//        items.add(BAR_CODE);
        items.add(getString(R.string.menu_chooselan));
        items.add(getString(R.string.device_info));
        items.add(getString(R.string.install_app));
        items.add("More");
//        items.add("uninstall");
    }

    private void test(final String id) throws RemoteException {
        this.id = id;
        if (id.equals(QR_CODE)) {
            qrCode();
            return;
        } else if (id.equals(BAR_CODE)) {
            barcode();
            return;
        }
        mSDKManager = SDKManager.getInstance().getDeviceServiceEngine();
        if (mSDKManager == null) {
            Log.e(TAG, "ServiceEngine is Null");
            return;
        }
        /*if (!TextUtils.equals(getString(R.string.login_devices), id) && !isLogin) {
            ac.blockmsg("Prompt", "Please Login Devices first");
            return;
        }*/
        if (id.equals(getString(R.string.login_devices))) {
            Bundle bundle = new Bundle();
            int ret = mSDKManager.login(bundle, "09000000");
            isLogin = (ret == 0);
            ac.blockmsg(id, ret == ServiceResult.Success ? getString(R.string.msg_succ) : getString(R.string.msg_fail));
        } else if (id.equals(getString(R.string.menu_pboc2))) {
            PBOC2();
        } else if (id.equals(getString(R.string.menu_apdu))) {
            // 读卡ic/非接卡sn号
            // "\x00\xA4\x04\x00\x0E\x32\x50\x41\x59\x2E\x53\x59\x53\x2E\x44\x44\x46\x30\x31\x00"
            testApdu("00A40400", "325041592E5359532E4444463031", (byte) 0x00);
        } else if (id.equals(getString(R.string.menu_m1card))) {
            // 读卡ic/非接卡sn号
            testM1card();
        } else if (id.equals(getString(R.string.menu_felica))) {
            // 读卡ic/非接卡sn号
            testFelica();
        } else if (id.equals(getString(R.string.init_dukpt))) {
            PinPadTest.initDukptBDKAndKsn(mSDKManager, mAlertDialogOnShowListener);
        }else if (id.equals(getString(R.string.initIPEK))) {
            PinPadTest.initDukptIPEKAndKsn(mSDKManager, mAlertDialogOnShowListener);
        }else if (id.equals(getString(R.string.dukpt_calculation))) {
            PinPadTest.dukptCalculation(mSDKManager, mAlertDialogOnShowListener);
        } else if (id.equals(getString(R.string.menu_loadkek))) {
            LoadKek();
//        } else if (id.equals(getString(R.string.menu_loadmasterkey))) {
//            LoadMainKey();
        } else if (id.equals(getString(R.string.menu_calmac))) {
            CalMac();
        } else if (id.equals(getString(R.string.menu_capk))) {
            EmvHandlerTest.getInstance().setEmvHandler(mSDKManager, mAlertDialogOnShowListener).ICPublicKeyManage();
        } else if (id.equals(getString(R.string.menu_aid))) {
            EmvHandlerTest.getInstance().setEmvHandler(mSDKManager, mAlertDialogOnShowListener).ICAidManage();
        } else if (id.equals(getString(R.string.clear_capk_aid))) {
            EmvHandlerTest.getInstance().setEmvHandler(mSDKManager, mAlertDialogOnShowListener).clearAIDAndRID();
        } else if (id.equals(getString(R.string.menu_beep))) {
            Beep();
        } else if (id.equals(getString(R.string.menu_ic_card_reader))) {
            ICCardReaderApi();
        } else if (id.equals(getString(R.string.menu_mag_card_reader))) {
            MagCardReaderApi();
        } else if (id.equals(getString(R.string.menu_led))) {
            LED();
        } else if (id.equals(getString(R.string.menu_scanner))) {
            ScannerApi();
        } else if (id.equals(getString(R.string.menu_serialport))) {
            SerialPort();
        } else if (id.equals(getString(R.string.menu_print))) {
            Print();
        } else if (id.equals(getString(R.string.menu_pinkeybord))) {
            PinPadTest.showOffLinePinKey(mSDKManager,null,  mAlertDialogOnShowListener);
//            PinPadTest.showOnlinePinPanKeyK(mSDKManager, null, pan, mTypeFacePath, mAlertDialogOnShowListener);
//            PinPadTest.showPinKey(mSDKManager, pan, false ,mAlertDialogOnShowListener);
        } else if (id.equals(getString(R.string.menu_pinkeybord2))) {

        } else if (id.equals(getString(R.string.menu_sign))) {
            Intent i = new Intent();
            i.setClass(MainActivity.this, SignActivity.class);
            startActivity(i);
        } else if (id.equals(getString(R.string.menu_caldata))) {
            CalData();
        } else if (id.equals(getString(R.string.menu_chooselan))) {
            int count = 2;
            CharSequence[] items = new CharSequence[count];
            items[0] = "English";
            items[1] = "简体中文";

            AlertDialog.Builder listDia = new AlertDialog.Builder(this);
            listDia.setTitle(id);
            listDia.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which >= 0) {
                        //保存设置的语言
                        LanguageUtils.saveLanguage(which + 1);
                        ac.blockmsg(getString(R.string.msg_title), getString(R.string.msg_rest));
                    }
                }
            });
            listDia.setCancelable(true);
            listDia.create().show();
        } else if (id.equals(getString(R.string.menu_randomnumber))) {
            byte[] random = mSDKManager.getPinPad().getRandom();
            ac.blockmsg(id, byte2string(random));
        } else if (id.equals(getString(R.string.device_info))) {
            getDeviceInfo();
        } else if (id.equals(getString(R.string.install_app))) {
            InstallApp();
        } else if (id.equals("More")) {
            startActivity(new Intent(this, AIDAndCapkActivity.class));
        }
    }

    private void qrCode() {
        Bitmap bitQrCode = new ZxingTest().qrCode();
        if (bitQrCode != null) {
            showView(bitQrCode);
        }
    }

    private void barcode() {
        Bitmap bitQrCode = new ZxingTest().barcode();
        if (bitQrCode != null) {
            showView(bitQrCode);
        }
    }

    private void showView(Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.activity_alert_dialog, null);
        ImageView ivContent = v.findViewById(R.id.iv_content);
        Button btOk = v.findViewById(R.id.btnOk);
        btOk.setTextColor(Color.WHITE);
        ivContent.setImageBitmap(bitmap);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        dialog.getWindow().setGravity(Gravity.CENTER);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void ICCardReaderApi() throws RemoteException {
        //RF ICCCard
        new SearchCardOrCardReaderTest(mSDKManager, mAlertDialogOnShowListener).ICCardReaderApi();
    }

    private void MagCardReaderApi() throws RemoteException {
        new SearchCardOrCardReaderTest(mSDKManager, mAlertDialogOnShowListener).MagCardReaderApi();
    }

    /**
     * Before the call, you must first install the serial port driver
     * on the computer Window system.
     * COM & LPT: Qualcomm HS-USB Android GPS 9020
     */
    private void SerialPort() {
        //call before must install driver in PC.
        //Qualcomm HS-USB Android GPS 9020
        startActivity(new Intent(this, SerialPortActivity.class));
//        SerialPortTest.SerialPort(mSDKManager,mAlertDialogOnShowListener);
    }

    private void ScannerApi() {
        ScannerApi.ScannerApi(mSDKManager, mAlertDialogOnShowListener);
    }

    boolean isLightLed = false;

    private void LED() {
        isLightLed = !isLightLed;
        BeepLEDTest.setLed(mSDKManager, isLightLed);
    }


    static String mTypeFacePath;

    private void Print() throws RemoteException {
        //打印输出
        ac.showwait(getString(R.string.msg_title), getString(R.string.msg_printing));
        PrinterTest.Print(mSDKManager, getContext(), mTypeFacePath, mAlertDialogOnShowListener);
    }

    private void Beep() throws RemoteException {
        BeepLEDTest.beep(mSDKManager);
    }


    private void ICPublicKeyManage() {
        EmvHandlerTest.getInstance().setEmvHandler(mSDKManager, mAlertDialogOnShowListener).ICPublicKeyManage();
    }

    private void CalMac() throws RemoteException {
        PinPadTest.getMac(mSDKManager, mAlertDialogOnShowListener);
    }

    private void CalPin() throws RemoteException {
//        ac.blockmsg(id, getString(R.string.msg_calresult) + byte2string(block));
    }

    private void CalTrack() throws RemoteException {
        PinPadTest.CalTrack(mSDKManager, mAlertDialogOnShowListener);
    }

    public interface OnSearchListener {
        void onSearchResult(int retCode, Bundle bundle);
    }

    String id = "";
    String pan = "123456789012345678";


    private void PBOC2() {
        EmvPBOCTest.PBOC(mSDKManager, mAlertDialogOnShowListener);
    }

    private void LoadKek() {
        //KEK Key used by KEK to decode the master key ciphertext
        //0A08BB5CEE4956FA69E9233B96A5969F
        ac.blockmsg(id, getString(R.string.msg_succ));
    }

    // 主密钥下载 MainKey
    private void LoadMainKey() throws RemoteException {
        int ret = PinPadTest.LoadMainKey(mSDKManager);
        ac.blockmsg(id, ret == ServiceResult.Success ? getString(R.string.msg_succ) : getString(R.string.msg_fail));
    }

    // 工作密钥(PIN/MAC/TRACK)下载
    private void LoadWorkKey() throws RemoteException {
        PinPadTest.LoadWorkKey(mSDKManager, mAlertDialogOnShowListener);
    }

    private void CalData() throws RemoteException {
        PinPadTest.CalData(mSDKManager, mAlertDialogOnShowListener);
    }

    private void testFelica() {
        FELICATest.test_felica(mSDKManager, mAlertDialogOnShowListener);
    }

    private void testM1card() {
        M1CardHandlerTest.test_m1card(mSDKManager, mAlertDialogOnShowListener);
    }

    // 演示发送apdu指令
    // "\x00\xA4\x04\x00\x0E\x32\x50\x41\x59\x2E\x53\x59\x53\x2E\x44\x44\x46\x30\x31\x00"
    private void testApdu(final String cmd, final String data, final byte le) throws RemoteException {
        new ApduCPUCardHandlerTest(mSDKManager, mAlertDialogOnShowListener).testApdu(cmd, data, le);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle b = data.getExtras();  //data为B中回传的Intent
            if (requestCode == 1) {
                String str = b.getString("result");
                ac.blockmsg(id, str);
                Log.v("tag", "code ret:" + str);
            }
        }
    }

    private void getDeviceInfo() throws RemoteException {
        DevicesInfoTest.getDeviceInfo(mSDKManager, mAlertDialogOnShowListener);
    }

    private void InstallApp() {
//        InstallApkTest.InstallApp(mSDKManager);
        InstallApkTest.InstallApp();
    }

    private AlertDialogOnShowListener mAlertDialogOnShowListener = new AlertDialogOnShowListener() {
        @Override
        public void showMessage(String content) {
            showmsg(content);
        }

        @Override
        public void showProgress(final String content, final ActionItems.OnCancelCall onCall) {
            MainActivity.this.runOnUiThread(new Runnable() {
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
        public void onSelApp(List<String> appNameList, boolean isFirstSelect, final OnSelectAppCallBack callBack) {
            String[] data = appNameList.toArray(new String[appNameList.size()]);
            //crash reseason
            final DefaultListFragment fragment = new DefaultListFragment().setTitle("Pls choice a app")
                    .setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, data));
            fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                    Log.d(TAG, "item = " + index);
                    callBack.onSetSelAppResponse(index);
                    fragment.dismissAllowingStateLoss();
                }
            });
            fragment.show(MainActivity.this, "amount");
        }

        @Override
        public void onInputAmount(final OnInputAmountCallBack callBack) {
            final DefaultInputDialogFragment fragment = new DefaultInputDialogFragment().setText("Please input amount", "", 1, "OK", "Cancel");
            fragment.setContent("1");
            fragment.setOnClickListener(new DefaultInputDialogFragment.OnClickListener() {
                @Override
                public void onLeftClick(String content) {
                    fragment.dismissAllowingStateLoss();
                    long amount = 0;
                    try {
                        amount = Long.parseLong(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "input amount = " + amount);
                    if (amount == 0) {
                        callBack.onInputAmount(null);
                    } else {
                        callBack.onInputAmount(String.valueOf(amount));
                    }
                }

                @Override
                public void onRightClick(String content) {
                    fragment.dismissAllowingStateLoss();
                    callBack.onInputAmount(null);
                }
            });
            fragment.show(MainActivity.this, "amount");
        }

        @Override
        public void onSelectAccountType(List<String> accountTypes, final OnSelectAccountType callBack) {
            String[] data = accountTypes.toArray(new String[accountTypes.size()]);

            final DefaultListFragment fragment = new DefaultListFragment().setTitle("Pls choice a app")
                    .setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, data));
            fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                    Log.d(TAG, "item = " + index);
                    callBack.onSetSelectAccountResponse(index);
                    fragment.dismissAllowingStateLoss();
                }
            });
            fragment.show(MainActivity.this, "account");
        }

        @Override
        public void onIssuerVoiceReference(String pan, final OnIssuerVoiceReference callBack) {
            final DefaultInputDialogFragment fragment = new DefaultInputDialogFragment().setText("Accept or Refund", "", 1, "Accept", "Refund");
            fragment.setOnClickListener(new DefaultInputDialogFragment.OnClickListener() {
                @Override
                public void onLeftClick(String content) {
                    fragment.dismissAllowingStateLoss();
                    callBack.onSetIssuerVoiceReferenceResponse(0);
                }

                @Override
                public void onRightClick(String content) {
                    fragment.dismissAllowingStateLoss();
                    callBack.onSetIssuerVoiceReferenceResponse(-1);
                }
            });
            fragment.show(MainActivity.this, "issvoice");
        }

        @Override
        public void showPinPad(final String pan) {
            try {
                PinPadTest.showPinKey(mSDKManager, pan, mTypeFacePath, false, mAlertDialogOnShowListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private void showmsg(final String msg) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ac.blockmsg(id, msg);
            }
        });
    }

    public interface AlertDialogOnShowListener {
        void showMessage(String content);

        void showProgress(String content, ActionItems.OnCancelCall onCall);

        void dismissProgress();

        void onSelApp(List<String> appNameList, boolean isFirstSelect, OnSelectAppCallBack callBack);

        void onInputAmount(OnInputAmountCallBack callBack);

        void onSelectAccountType(List<String> accountTypes, OnSelectAccountType callBack);

        void onIssuerVoiceReference(String pan, OnIssuerVoiceReference callBack);

        void showPinPad(String pan);
    }

    static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static String getStaticString(int resId) {
        return mContext.getString(resId);
    }

    public static String getTypeFacePath() {
        return mTypeFacePath;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        LogReader.startCatchLog("*");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LogReader.stopCatchLog();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
