package com.morefun.ypos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.card.cpu.APDUCmd;
import com.morefun.yapi.card.cpu.CPUCardHandler;
import com.morefun.yapi.card.mifare.M1CardHandler;
import com.morefun.yapi.card.mifare.M1KeyTypeConstrants;
import com.morefun.yapi.device.beeper.BeepModeConstrants;
import com.morefun.yapi.device.reader.icc.ICCSearchResult;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.device.reader.icc.IccReaderSlot;
import com.morefun.yapi.device.reader.icc.OnSearchIccCardListener;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.uitls.ActionItems;
import com.morefun.ypos.uitls.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.morefun.ypos.uitls.Utils.hex2asc;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static boolean DLreturn = false;
    SharedPreferences config = null;
    ActionItems ac;
    List<String> items = new ArrayList<>();
    DeviceServiceEngine mSDKManager;
    boolean isLogin = false;
    private String id = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKManager.getInstance().bindService(getApplicationContext());

        //setLanguage();
        Log.d(TAG, "onCreate" + Thread.currentThread().getId());
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
    }

    void refrushcsid(int csid) {
        ac = new ActionItems(this);
        this.setTitle(getString(R.string.activity_title_mid) + "(" + csid + ")");
    }

    private static final String QR_CODE = "Generates a QR code";
    private static final String BAR_CODE = "Generates a Barcode";

    private void intiitems() {
        items.add(getString(R.string.login_devices));
        items.add(getString(R.string.menu_m1card));
    }

    private void test(final String id) throws RemoteException {
        this.id = id;
        mSDKManager = SDKManager.getInstance().getDeviceServiceEngine();
        if (mSDKManager == null) {
            Log.e(TAG, "ServiceEngine is Null");
            return;
        }
        if (!TextUtils.equals(getString(R.string.login_devices), id) && !isLogin) {
            ac.blockmsg("Prompt", "Please Login Devices first");
            return;
        }
        if (id.equals(getString(R.string.login_devices))) {
            Bundle bundle = new Bundle();
            int ret = mSDKManager.login(bundle, "09000000");
            isLogin = (ret == 0);
            ac.blockmsg(id, ret == ServiceResult.Success ? getString(R.string.msg_succ) : getString(R.string.msg_fail));
        } else if (id.equals(getString(R.string.menu_apdu))) {
            test_m1card2();
        } else if (id.equals(getString(R.string.menu_m1card))) {
            // 读卡ic/非接卡sn号
            testM1card();
        }
    }

    private void Beep() throws RemoteException {
        mSDKManager.getBeeper().beep(BeepModeConstrants.NORMAL);
    }


    String byte2string(byte[] b) {
        if (b == null || b.length == 0) {
            return "NULL";
        }
        String asci = new String();
        for (int i = 0; i < b.length; ++i) {
            asci += String.format("%02X", b[i]);
        }
        return asci;
    }

    byte[] string2byte(String key) {
        if (key == null) {
            return null;
        }

        int keyLen = key.length();
        //转为bcd码，和62域一致
        if (keyLen % 2 == 1) {            // 长度奇数
            key = key + "0";        //左对齐
        }
        int bcdLen = (keyLen + 1) / 2;
        byte[] bcdByte = new byte[bcdLen];
        for (int i = 0; i < keyLen; i += 2) {
            try {
                bcdByte[i / 2] = (byte) Integer.decode("0x" + key.substring(i, i + 2)).intValue();
            } catch (Exception e) {
                Log.v("error", "asc2hex error");
            }
        }
        return bcdByte;
    }


    public interface OnSearchListener {
        void onSearchResult(int retCode, Bundle bundle);
    }


    private void showmsg(final String msg) {
        TestActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ac.blockmsg(id, msg);
            }
        });
    }


    public void searchRFCard(final String[] cardtype, final OnSearchListener onSearchListener) {

        try {
            final IccCardReader rfReader = mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT);

            ac.showwait(id, getString(R.string.msg_rfcard), new ActionItems.OnCancelCall() {
                @Override
                public void onCancel(DialogInterface dialog) {
//                    try {
//                        Log.v(TAG, "rfReader.stopSearch();");
//                        rfReader.stopSearch();
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
                }
            });
            OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                @Override
                public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
//                    ICCSearchResult.CARDTYPE
                    if (retCode == 0) {
                        Log.d(TAG, "retCode= " + retCode);
                        Log.d(TAG, "cardType:" + bundle.getString(ICCSearchResult.CARDTYPE));
//                        rfReader.stopSearch();

                        mSDKManager.getBeeper().beep(retCode == ServiceResult.Success ? BeepModeConstrants.SUCCESS : BeepModeConstrants.FAIL);

                        onSearchListener.onSearchResult(retCode, bundle);
                    } else {
                        ac.closewait();
                    }

                }
            };
            rfReader.searchCard(listener, 30, cardtype);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void testM1card() throws RemoteException {
        searchRFCard(new String[]{IccCardType.M1CARD}, new OnSearchListener() {
            @Override
            public void onSearchResult(int retCode, Bundle bundle) {
                if (ServiceResult.Success == retCode) {
                    String cardType = bundle.getString(ICCSearchResult.CARDTYPE);
                    if (cardType.equals(IccCardType.M1CARD)) {
                        test_m1card();
                        //showmsg(getString(R.string.msg_succ));
                    }
                } else {
                    showmsg(getString(R.string.msg_icorrfid));
                }
            }
        });
        /*
        final IccCardReader m1CardReader = mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT);
        if (!m1CardReader.isCardExists()) {
            ac.blockmsg(id, getString(R.string.msg_icorrfid));
            return;
        }*/
    }

    M1CardHandler m1CardHandler;

    private void test_m1card2() {
        final IccCardReader m1CardReader;
        try {
//            m1CardReader = mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT);
            byte key[] = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
//           m1CardHandler = mSDKManager.getM1CardHandler(m1CardReader);
            if (m1CardHandler == null) {
                return;
            }
            int ret = m1CardHandler.authority(M1KeyTypeConstrants.KEYTYPE_A, 1, key, new byte[12]);
            if (ret != ServiceResult.Success) {
                showmsg(getString(R.string.label_result) + Integer.toString(ret));
                return;
            }
            byte buf[] = new byte[16];
            String s = String.format("M1Card Test\n");
            ret = m1CardHandler.readBlock(0, buf);
            s += String.format("readBlock0(%d):", ret) + byte2string(buf) + "\n";
            Log.v("tag", "read block:" + s);
            for (int i = 0; i < 20; i++) {
                ret = m1CardHandler.readBlock(i, buf);
                s += String.format("readBlock" + i + "(%d):", ret) + byte2string(buf) + "\n";
            }
            showmsg(s);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void test_m1card() {
        try {
            final IccCardReader m1CardReader = mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT);
            byte key[] = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            m1CardHandler = mSDKManager.getM1CardHandler(m1CardReader);
            if (m1CardHandler == null) {
                showmsg(getString(R.string.msg_readfail_retry));
                return;
            }
//            int ret = 0;
            byte[] uid = new byte[32];
            int ret = m1CardHandler.authority(M1KeyTypeConstrants.KEYTYPE_A, 1, key, uid);
            if (ret != ServiceResult.Success) {
                showmsg(getString(R.string.label_result) + Integer.toString(ret));
                return;
            }

            int len = 16;
            byte buf[] = new byte[len];
            String s = String.format("M1Card Test\n");
            int block = 5;
            ret = m1CardHandler.readBlock(block, buf);
            s += String.format("readBlock" + block + "(%d):", ret) + byte2string(buf) + "\n";

            showmsg(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void searchCPUCard(final String[] cardtype, final OnSearchListener onSearchListener) {

        try {
            final IccCardReader icReader = mSDKManager.getIccCardReader(IccReaderSlot.ICSlOT1);
            final IccCardReader rfReader = mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT);

            ac.showwait(id, getString(R.string.msg_icorrfid), new ActionItems.OnCancelCall() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    try {
                        icReader.stopSearch();
                        rfReader.stopSearch();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
            OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                @Override
                public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                    Log.d(TAG, "retCode:" + retCode
                            + ", cardType:" + bundle.getString(ICCSearchResult.CARDTYPE)
                            + ", cardOther:" + bundle.getInt(ICCSearchResult.CARDOTHER));
                    icReader.stopSearch();
                    rfReader.stopSearch();

                    mSDKManager.getBeeper().beep(retCode == ServiceResult.Success ? BeepModeConstrants.SUCCESS : BeepModeConstrants.FAIL);

                    onSearchListener.onSearchResult(retCode, bundle);
                }
            };
            icReader.searchCard(listener, 30, cardtype);
            rfReader.searchCard(listener, 30, cardtype);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 演示发送apdu指令
    // "\x00\xA4\x04\x00\x0E\x32\x50\x41\x59\x2E\x53\x59\x53\x2E\x44\x44\x46\x30\x31\x00"
    private void testApdu(final String cmd, final String data, final byte le) throws RemoteException {

        searchCPUCard(new String[]{IccCardType.CPUCARD}, new OnSearchListener() {
            @Override
            public void onSearchResult(int retCode, Bundle bundle) {
                if (ServiceResult.Success == retCode) {
                    String cardType = bundle.getString(ICCSearchResult.CARDTYPE);
                    int slot = bundle.getInt(ICCSearchResult.CARDOTHER);
                    Log.v(TAG, "cardType:" + cardType + ", slot:" + slot);
                    if (cardType.equals(IccCardType.CPUCARD)) {
                        //showmsg(getString(R.string.msg_succ));
                        test_apdu(slot, cmd, data, le);

                    }
                } else {
                    showmsg(getString(R.string.msg_icorrfid));
                }
            }
        });
    }

    private void test_apdu(int slot, String cmd, String data, byte le) {
        try {
            IccCardReader cardReader = mSDKManager.getIccCardReader(slot);
            if (cardReader == null) {
                showmsg(getString(R.string.msg_readfail_retry));
                return;
            }
            CPUCardHandler cpuCardHandler = mSDKManager.getCPUCardHandler(cardReader);
            if (cpuCardHandler == null) {
                showmsg(getString(R.string.msg_readfail_retry));
                return;
            }
            // atr
            if (!cpuCardHandler.setPowerOn(new byte[]{0x00, 0x00})) {
                Log.e(TAG, "CPUCard poweron fail.");
                showmsg(getString(R.string.msg_readfail_retry));
                return;
            }

            byte[] cmdBytes = string2byte(cmd);
            APDUCmd apduCmd = new APDUCmd();
            apduCmd.setCla(cmdBytes[0]);
            apduCmd.setIns(cmdBytes[1]);
            apduCmd.setP1(cmdBytes[2]);
            apduCmd.setP2(cmdBytes[3]);

            byte[] dataArray = string2byte(data);
            byte[] tmp = new byte[256];
            if (dataArray != null) {
                apduCmd.setLc(dataArray.length);
                Utils.memcpy(tmp, 0, dataArray, 0, dataArray.length);
            } else {
                apduCmd.setLc(0);
            }
            apduCmd.setDataIn(tmp);

            apduCmd.setLe(le);

            int ret = cpuCardHandler.exchangeAPDUCmd(apduCmd);
            cpuCardHandler.setPowerOff();
            if (ret == ServiceResult.Success) {
                if (apduCmd.getDataOutLen() > 0) {
                    showmsg(hex2asc(apduCmd.getDataOut(), 0, apduCmd.getDataOutLen() * 2, 1));
                } else {
                    showmsg(getString(R.string.msg_succ));
                }
            } else {
                showmsg(getString(R.string.msg_readfail_retry));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
