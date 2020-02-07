package com.morefun.ypos.apitest;

import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.emv.EmvAidPara;
import com.morefun.yapi.emv.EmvCapk;
import com.morefun.yapi.emv.EmvHandler;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.BaseApiTest;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;

import java.util.List;

import static com.morefun.ypos.uitls.Utils.string2byte;

public class EmvHandlerTest extends BaseApiTest {
    private static final String TAG = "EmvHandlerTest";
    DeviceServiceEngine mSDKManager;
    EmvHandler mEmvHandler;
    MainActivity.AlertDialogOnShowListener mAlertDialogOnShowListener;
    static EmvHandlerTest mEmvHandlerTest;

    public static EmvHandlerTest getInstance() {
        if (mEmvHandlerTest == null) {
            synchronized (EmvHandlerTest.class) {
                if (mEmvHandlerTest == null) {
                    mEmvHandlerTest = new EmvHandlerTest();
                }
            }
        }
        return mEmvHandlerTest;
    }

    private EmvHandlerTest() {
    }

    public void setAIDList() throws RemoteException {
        List<EmvAidPara> aidParaList = mSDKManager.getEmvHandler().getAidParaList();
        if (aidParaList != null && aidParaList.size() > 0) {
            Log.d(TAG, "aid size =" + aidParaList.size());
        } else {
            Log.d(TAG, "clear success. AID num 0.");
            return;
        }
        int ret = mSDKManager.getEmvHandler().setAidParaList(aidParaList);
        Log.d(TAG, "setAidParaList= " + ret);
        mAlertDialogOnShowListener.showMessage("setAidParaList = " + (ret == 0));
    }

    public void getCAPKList() throws RemoteException {
        List<EmvCapk> capkList = mSDKManager.getEmvHandler().getCapkList();
        if (capkList != null && capkList.size() > 0) {
            Log.d(TAG, "Capk size =" + capkList.size());
            mAlertDialogOnShowListener.showMessage("CAPK size = " + capkList.size());
        } else {
            Log.d(TAG, "clear success. CAPK num 0.");
            mAlertDialogOnShowListener.showMessage("CAPK size = 0");
        }
    }

    public void setCAPKList() throws RemoteException {
        List<EmvCapk> capkList = mSDKManager.getEmvHandler().getCapkList();
        if (capkList != null && capkList.size() > 0) {
            Log.d(TAG, "Capk size =" + capkList.size());
        } else {
            Log.d(TAG, "clear success. CAPK num 0.");
        }
        int ret = mSDKManager.getEmvHandler().setCAPKList(capkList);
        Log.d(TAG, "setCAPKList ret = " + ret);
        mAlertDialogOnShowListener.showMessage("setCAPKList = " + (ret == 0));
    }


    public void clearAIDAndRID() {
        try {
            mSDKManager.getEmvHandler().clearAIDParam();
            mSDKManager.getEmvHandler().clearCAPKParam();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mAlertDialogOnShowListener.showMessage("clear AID & CAPK success = ");
    }

    public void getAidList() throws RemoteException {
        List<EmvAidPara> aidParaList = mSDKManager.getEmvHandler().getAidParaList();
        if (aidParaList != null && aidParaList.size() > 0) {
            Log.d(TAG, "aid size =" + aidParaList.size());
            mAlertDialogOnShowListener.showMessage("aid size = " + aidParaList.size());
        } else {
            Log.d(TAG, "clear success. AID num 0.");
            mAlertDialogOnShowListener.showMessage("aid size = 0");
            return;
        }
    }

    public void ICAidManage() {
        /**
         * DF19 Contactless Floor Limit       -----If the limit is exceeded, the transaction may request online
         * DF20 Contactless Transaction Limit  -----If the limit is exceeded, the transaction will fail
         * DF21 Contactless CVM Limit         ---- If the limit is exceeded, the transaction will request CVM method
         *
         * 9F06(07)    <T> Terminal Application Identifier
         * 9F09(02)   <T> Application Version Number
         * DF11(05)   <T> terminal Action Code-Default
         * DF12(05)  <T> terminal Action Code-Online
         * DF13(05)  <T> terminal Action Code-Denial
         * 9F1B(04)  <T> Terminal Floor Limit
         * 5F2A(02)  <T> Transaction Currency Code
         * 5F36(01)  <T> Transaction Currency Exponent
         * DF19(06)  <T>  Contactless Floor Limit
         * DF20(06)  <T> Contactless Transaction Limit
         * DF21(06)  <T> Contactless CVM Limit
         * DF17(01)  <T> Target Percentage for Random Selection
         * DF16(01)  <T> Maximum Target Percentage for Random Selection
         * DF15(04)  <T> Threshold Value for Biased Random Selection
         * 50(16)   <T>Application Label, e.g.(0xA0 0x00 0x00 0x00 0x03 0x10 0x10 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00)
         * DF01(01)  <T> Application selection indicator,0 or 1, e.g.(0x00)
         * DF18(01)  <T> Terminal online pin capability, 0x30 or 0x31 , e.g.(0x31)
         * DF14(252)  <T> Default DDOL(Hex), e.g.(0x9F 0x37 0x04 0x00 ...)
         * 9F7B(06)  <T> EC Terminal Transaction Limit, e.g.(0x00 0x00 0x00 0x00 0x20 0x00)--for UnionPay cards
         */
        String[] keys = new String[]{
                "9F0608A000000524010101DF0101009F08020030DF11050000000000DF12050000000000DF130500000000009F1B04000186A0DF150400000000DF160100DF170100DF14039F3704DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF2106000000004000",
                "9F0607A0000003241010DF0101009F08020001DF11050000000000DF12050000000000DF130500000000009F1B0400001388DF150400000000DF160150DF170120DF14039F3704DF1801319F7B06000000200000DF1906000000200000DF2006000002000000DF2106000000100000",
                "9F0607A00000052410109F08020002DF11050000000000DF12050000000000DF130500000000009F1B04000186A05F2A0203565F360102DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF170100DF160105DF1504000001F4",
                "9F0607A00000052410119F08020002DF11050000000000DF12050000000000DF130500000000009F1B04000186A05F2A0203565F360102DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF170100DF160105DF1504000001F4",

        };
        //9F0608A000000333010101
        int ret = -1;
        try {
            for (int j = 0; j < keys.length; j++) {
                String key = keys[j];
                ret = mSDKManager.getEmvHandler().addAidParam(string2byte(key));
                Log.d(TAG, "result = " + (ret == ServiceResult.Success));
                if (ret != ServiceResult.Success) {
                    break;
                }
            }
            mAlertDialogOnShowListener.showMessage(getString(R.string.msg_done) + ":" + (ret == ServiceResult.Success));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void ICPublicKeyManage() {
        /**
         * 9F06	Application Identifier (AID) – terminal
         * 9F22	Certification Authority Public Key Index
         * DF05
         * DF06
         * DF07
         * DF02
         * DF04
         * DF03
         */
        String[] keys = new String[]{
                "9F0605A0000003339F220102DF050420211231DF060101DF070101DF028190A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57DF040103DF031403BB335A8549A03B87AB089D006F60852E4B8060",
                "9F0605A0000003339F220103DF050420221231DF060101DF070101DF0281B0B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33DDF040103DF031487F0CD7C0E86F38F89A66F8C47071A8B88586F26",
                "9F0605A0000003339F220104DF050420221231DF060101DF070101DF0281F8BC853E6B5365E89E7EE9317C94B02D0ABB0DBD91C05A224A2554AA29ED9FCB9D86EB9CCBB322A57811F86188AAC7351C72BD9EF196C5A01ACEF7A4EB0D2AD63D9E6AC2E7836547CB1595C68BCBAFD0F6728760F3A7CA7B97301B7E0220184EFC4F653008D93CE098C0D93B45201096D1ADFF4CF1F9FC02AF759DA27CD6DFD6D789B099F16F378B6100334E63F3D35F3251A5EC78693731F5233519CDB380F5AB8C0F02728E91D469ABD0EAE0D93B1CC66CE127B29C7D77441A49D09FCA5D6D9762FC74C31BB506C8BAE3C79AD6C2578775B95956B5370D1D0519E37906B384736233251E8F09AD79DFBE2C6ABFADAC8E4D8624318C27DAF1DF040103DF0314F527081CF371DD7E1FD4FA414A665036E0F5E6E5"
        };
        int ret = -1;
        try {
            for (int j = 0; j < keys.length; j++) {
                String key = keys[j];
                ret = mSDKManager.getEmvHandler().addCAPKParam(string2byte(key));
                Log.d(TAG, "result = " + (ret == ServiceResult.Success));
                if (ret != ServiceResult.Success) {
                    break;
                }
            }
            mAlertDialogOnShowListener.showMessage(getString(R.string.msg_done) + ":" + (ret == ServiceResult.Success));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public EmvHandlerTest setEmvHandler(DeviceServiceEngine engine, MainActivity.AlertDialogOnShowListener listener) {
        try {
            mSDKManager = engine;
            mEmvHandler = engine.getEmvHandler();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mAlertDialogOnShowListener = listener;
        return this;
    }
}
