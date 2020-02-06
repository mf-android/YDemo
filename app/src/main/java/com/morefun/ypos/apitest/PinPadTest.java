package com.morefun.ypos.apitest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.pinpad.DesAlgorithmType;
import com.morefun.yapi.device.pinpad.DesMode;
import com.morefun.yapi.device.pinpad.DispTextMode;
import com.morefun.yapi.device.pinpad.DukptKeyGid;
import com.morefun.yapi.device.pinpad.DukptKeyType;
import com.morefun.yapi.device.pinpad.MacAlgorithmType;
import com.morefun.yapi.device.pinpad.OnPinPadInputListener;
import com.morefun.yapi.device.pinpad.PinAlgorithmMode;
import com.morefun.yapi.device.pinpad.PinPad;
import com.morefun.yapi.device.pinpad.PinPadConstrants;
import com.morefun.yapi.device.pinpad.PinPadType;
import com.morefun.yapi.device.pinpad.WorkKeyType;
import com.morefun.yapi.emv.EmvHandler;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.BaseApiTest;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;
import com.morefun.ypos.config.DukptConfigs;
import com.morefun.ypos.uitls.ToastUtils;
import com.morefun.ypos.uitls.Utils;

import static com.morefun.ypos.uitls.Utils.byte2string;
import static com.morefun.ypos.uitls.Utils.string2byte;

public class PinPadTest extends BaseApiTest {
    private static final String TAG = "PinPadTest";

    private void LoadKek() {
        //KEK Key used by KEK to decode the master key ciphertext
        // default KEK value
        //0A08BB5CEE4956FA69E9233B96A5969F
    }

    public static int LoadMainKey(DeviceServiceEngine engine) throws RemoteException {
        PinPad pinPad = engine.getPinPad();
        //ciphertext 4B24C397E2D59A29A176FC37909A54E6       //plaintext 05959A878CEE8C0C6B70706483CB4C80
        //plaintext  05959A878CEE8C0C6B70706483CB4C80
        byte[] mkeyBytes = string2byte("4B24C397E2D59A29A176FC37909A54E6");
//       return pinPad.loadEncryptMKey(0, mkeyBytes, mkeyBytes.length, 0, true);
        return pinPad.loadPlainMKey(0, string2byte("05959A878CEE8C0C6B70706483CB4C80"), 16, true);
    }

    public static void LoadWorkKey(DeviceServiceEngine engine, MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) throws RemoteException {
        PinPad pinPad = engine.getPinPad();
        //8 + 4 bytes 16 + 4 bytes（KVC） 3F854952 BFD0D79D
        byte[] pkey12Bytes = string2byte("A26C78C550F023C53F854952");
        byte[] pkeyPlain8Bytes = string2byte("A37C78C550F023C5");
        byte[] pkeyPlain16Bytes = string2byte("A37C78C550F023C5A37C78C550F023C5");
        byte[] macBytes = string2byte("A26C78C550F023C56AFCF24D6A3BA2BB3410137B");
        byte[] trackBytes = string2byte("A26C78C550F023C56AFCF24D6A3BA2BB3410137B");
        StringBuilder sb = new StringBuilder();
//        int pkeyRet = pinPad.loadWKey(0, WorkKeyType.PINKEY, pkey12Bytes, pkey12Bytes.length);
        //Load Plain PinKey  8 bytes or 16 bytes
        //load PlainText wkey
        int pkeyRet = pinPad.loadPlainWKey(0, WorkKeyType.PINKEY, pkeyPlain8Bytes, pkeyPlain8Bytes.length);
        int mackeyRet = pinPad.loadWKey(0, WorkKeyType.MACKEY, macBytes, macBytes.length);
        int trackkeyRet = pinPad.loadWKey(0, WorkKeyType.TDKEY, trackBytes, trackBytes.length);

        sb.append(getString(R.string.label_loadpin) + (pkeyRet == ServiceResult.Success ? getString(R.string.msg_succ) : getString(R.string.msg_fail)) + "\n");
        sb.append(getString(R.string.label_loadmac) + (mackeyRet == ServiceResult.Success ? getString(R.string.msg_succ) : getString(R.string.msg_fail)) + "\n");
        sb.append(getString(R.string.label_loadtrack) + (trackkeyRet == ServiceResult.Success ? getString(R.string.msg_succ) : getString(R.string.msg_fail)) + "\n");
        alertDialogOnShowListener.showMessage(sb.toString());
    }

    public static void LoadDesBykey(DeviceServiceEngine engine) throws RemoteException  {
        PinPad pinPad = engine.getPinPad();
//        pinPad.desEncByWKey()
    }
    public static void format(PinPad pinPad) {
        try {
            pinPad.format();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void getMac(DeviceServiceEngine engine, MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) throws RemoteException {
        String strmac = "00000000005010016222620910029130840241205100100367FD3414057DB801BE18A309A544C5174CC777525974CBD467BCC56EA16629F3B016488A6C314921485C75F57066D4682FEDC1F910C5C8136A201279B590898B40D7098461D345168810CCFEBC61204B3E6F364A95175EF54C7EBAAEC2A6AEE44D9783747124D313B78A3F754C5ECC611533C4957377DD2067DF927C80461C4E4C20A8A4CC57EF1CCE2BC1AEEA442431256F66A25AB855912BA82FB8AD308F0EDE358CDDDEA63C95401B8335C8689E5735E0FB96733426FD71A7248E140A95CB4B4313AC0DBDA1E70EA8800000000000";
        int keyId = 0;
        String ksn = null;
        if (DukptConfigs.isDukpt){
            keyId = -1;
        }
        byte[] inputData = Utils.checkInputData(string2byte(strmac));
        byte[] macBlock = engine.getPinPad().getMac(keyId, MacAlgorithmType.ECB, DesAlgorithmType.TDES, inputData, DukptConfigs.getMacIPEKBundle());
//        System.arraycopy(block, block.length - 8, macBlock, 0, 8);
        alertDialogOnShowListener.showMessage(getString(R.string.msg_calresult) + byte2string(macBlock));
    }

    public static void CalData(DeviceServiceEngine engine, MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) throws RemoteException {
        String src = "6222620910029130840D2412220822043945";
        int len = src.length();
        byte[] tsrc = string2byte(src);
        int groupcount = (tsrc.length % 8) == 0 ? tsrc.length / 8 : tsrc.length / 8 + 1;
        int keyId = 0;
        if (DukptConfigs.isDukpt){
            keyId = -1;
        }
        byte[] tdst = engine.getPinPad().getMac(keyId, MacAlgorithmType.ECB, DesAlgorithmType.TDES, tsrc, DukptConfigs.getMacIPEKBundle());
        String s = String.format("%04d", len) + byte2string(tdst);
        alertDialogOnShowListener.showMessage(getString(R.string.label_result) + s);
    }

    public static void CalTrack(DeviceServiceEngine engine, MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) throws RemoteException {
        final byte[] block = new byte[16];
        byte[] tdKeyBytes = string2byte("6259960052855293D220620112762919");
        //nt desEncByWKey(int mKeyIdx,int wKeyType,in byte[]  data,int dataLen,int desType,out byte[] desResult)
        final int ret = engine.getPinPad().desEncByWKey(0, WorkKeyType.TDKEY, tdKeyBytes, tdKeyBytes.length, DesAlgorithmType.TDES, block);
        if (ret == ServiceResult.Success) {
            alertDialogOnShowListener.showMessage(getString(R.string.msg_calresult) + byte2string(block));
        } else {
            alertDialogOnShowListener.showMessage(getString(R.string.msg_fail));
        }
    }

    public static void showPinKey(DeviceServiceEngine mSDKManager, String pan, String typefontPath ,boolean isOffLine,final MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) throws RemoteException {
        if (isOffLine){
            showOffLinePinKey(mSDKManager, null,alertDialogOnShowListener);
        }else {
            showOnlinePinPanKeyK(mSDKManager,null, pan, typefontPath,  alertDialogOnShowListener);
        }
    }
    /**
     * Offline PinPad Demo
     * @throws RemoteException
     */
    public static void showOffLinePinKey(DeviceServiceEngine mSDKManager, final EmvHandler mEmvHandler, final MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) throws RemoteException {
        int textMode = DispTextMode.PASSWORD;
//        int textMode = DispTextMode.PLAINTEXT;
        //check permission for PinPad dialog.
        int pinResult = mSDKManager.getPinPad().initPinPad(PinPadType.INTERNAL);
        if (checkPinPadNeedAllowPermission(mEmvHandler, pinResult)){
            Log.d(TAG, "checkPinPadNeedAllowPermission =  true");
            return;
        }
        //setSupportPinLen is Config set Pin Len . if you need input the  12 Len Pin .. you can set minLength 12 and maxLength 12.
        int minLength = 4;
        int maxLength = 12;
        mSDKManager.getPinPad().setSupportPinLen(new int[]{minLength, maxLength});
        Bundle bundle = new Bundle();
        bundle.putBoolean(PinPadConstrants.IS_SHOW_PASSWORD_BOX ,true);
        bundle.putBoolean(PinPadConstrants.IS_SHOW_TITLE_HEAD ,true);
        bundle.putString(PinPadConstrants.TITLE_HEAD_CONTENT ,"Please input offline Pin\n");
//        bundle.putString(PinPadConstrants.COMMON_TYPEFACE_PATH , path);
        pinResult = mSDKManager.getPinPad().inputText(bundle, new OnPinPadInputListener.Stub() {
            @Override
            public void onInputResult(int ret, byte[] pin, String ksn) throws RemoteException {
                Log.d(TAG ,"ret= " + ret + ",bytes = " + pin);
                if (mEmvHandler != null && ret == ServiceResult.Success){
                    mEmvHandler.onSetCardHolderInputPin(pin);
                }else {
                    alertDialogOnShowListener.showMessage("offline pin : " + new String(pin));
                }
            }

            @Override
            public void onSendKey(byte keyCode) throws RemoteException {
                Log.d(TAG ,"keyCode = " + keyCode);
                if (keyCode == (byte)ServiceResult.PinPad_Input_Cancel){
                    if (mEmvHandler != null){
                        mEmvHandler.onSetCardHolderInputPin(null);
                    }
                    alertDialogOnShowListener.showMessage("Pin Pad is cancel.");
                }else if (keyCode ==  (byte) ServiceResult.PinPad_Input_OK ){
                    Log.d(TAG ,"keyCode =  PinPad_Input_OK");
                }else if (keyCode ==  (byte) ServiceResult.PinPad_Input_Clear ){
                    Log.d(TAG ,"keyCode =  PinPad_Input_Clear");
                }else if (keyCode ==  (byte) ServiceResult.PinPad_Input_Num ){
                    Log.d(TAG ,"keyCode =  PinPad_Input_Num");
                }
            }
        },textMode);
        checkPinPadNeedAllowPermission(mEmvHandler, pinResult);
    }
    public static void showOnlinePinPanKeyK(DeviceServiceEngine mSDKManager, final EmvHandler mEmvHandler , String pan, String path, final MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) throws RemoteException {
        mSDKManager.getPinPad().initPinPad(PinPadType.EXTERNAL);
        Bundle bundle = DukptConfigs.getPinIPEKBundle();
        if (TextUtils.isEmpty(pan)){
            //pan
            pan = EmvPBOCTest.getTagByEmv("5A", mSDKManager.getEmvHandler(), bundle);
        }
        byte[] panBlock = pan.getBytes();
        Log.d(TAG,"pan =" + pan);
        int minLength = 4;
        int maxLength = 4;
        mSDKManager.getPinPad().setSupportPinLen(new int[]{minLength, maxLength});
        int pinResult = mSDKManager.getPinPad().initPinPad(PinPadType.INTERNAL);
        if (checkPinPadNeedAllowPermission(mEmvHandler, pinResult)){
            Log.d(TAG, "checkPinPadNeedAllowPermission =  true");
            return;
        }

        //default is true
        bundle.putBoolean(PinPadConstrants.IS_SHOW_PASSWORD_BOX ,true);
        bundle.putBoolean(PinPadConstrants.IS_SHOW_TITLE_HEAD ,true);
        bundle.putString(PinPadConstrants.TITLE_HEAD_CONTENT ,"Please input online Pin");
        bundle.putString(PinPadConstrants.COMMON_TYPEFACE_PATH , path);
        //TODO if you need change pinpad KEY background color or text color ,you can follow this.
//        int[] testColor = new int[]{Color.BLACK , Color.BLUE , Color.WHITE };
//        int[] bgColor = new int[]{Color.RED , Color.YELLOW , Color.GREEN };
//        bundle.putIntArray(PinPadConstrants.COMMON_TEXT_COLOR , testColor);
//        bundle.putIntArray(PinPadConstrants.COMMON_BG_COLOR , bgColor);
        //need 10 color.
        int[] testColor = new int[]{Color.BLACK , Color.BLUE , Color.WHITE ,Color.YELLOW , Color.BLUE , Color.WHITE, Color.BLACK , Color.BLUE , Color.RED , Color.WHITE};
//        bundle.putIntArray(PinPadConstrants.NUMBER_TEXT_COLOR , testColor);
        Log.d(TAG, "inputOnlinePin  before");
        pinResult = mSDKManager.getPinPad().inputOnlinePin(bundle, panBlock, 0, PinAlgorithmMode.ISO9564FMT1, new OnPinPadInputListener.Stub() {
            @Override
            public void onInputResult(int ret, byte[] pinBlock,String pinKsn) throws RemoteException {
                Log.d(TAG, "onInputResult =  " + byte2string(pinBlock));
                Log.d(TAG, "onInputResult pinKsn=  " + pinKsn);
                boolean isByPass = "000000000000".equals(byte2string(pinBlock));
                Log.d(TAG, "onInputResult =  " + isByPass);
                alertDialogOnShowListener.showMessage("online pin : " + byte2string(pinBlock) + "\n pinKsn = "+ pinKsn);

                if (mEmvHandler != null && ret == ServiceResult.Success){
                    mEmvHandler.onSetCardHolderInputPin(isByPass ? new byte[0] : Utils.getByteArray(pinBlock,0 , 8));
                }
            }

            @Override
            public void onSendKey(byte keyCode) throws RemoteException {
                Log.d(TAG ,"keyCode = " + keyCode);
                if (keyCode == (byte) ServiceResult.PinPad_Input_Cancel){
                    if (mEmvHandler != null){
                        mEmvHandler.onSetCardHolderInputPin(null);
                    }
//                    alertDialogOnShowListener.showMessage("Pin Pad is cancel.");
                }else if (keyCode ==  (byte) ServiceResult.PinPad_Input_OK ){
                    Log.d(TAG ,"keyCode =  PinPad_Input_OK");
                }else if (keyCode ==  (byte) ServiceResult.PinPad_Input_Clear ){
                    Log.d(TAG ,"keyCode =  PinPad_Input_Clear");
                }else if (keyCode ==  (byte) ServiceResult.PinPad_Input_Num ){
                    Log.d(TAG ,"keyCode =  PinPad_Input_Num");
                }
            }
        });
        Log.d(TAG, "inputOnlinePin  after" + pinResult);
        checkPinPadNeedAllowPermission(mEmvHandler, pinResult);
    }

    private static boolean checkPinPadNeedAllowPermission(EmvHandler mEmvHandler, int pinResult) throws RemoteException {
        if (pinResult == ServiceResult.PinPad_Need_Permissions_Draw_over_other_apps) {
            Log.d(TAG, " =  " + pinResult);
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            MainActivity.getContext().startActivity(intent);
            Log.d(TAG, " =   Please allow YSDK App permission");
            ToastUtils.show(MainActivity.getContext(), "Please allow YSDK App permission");
            if (mEmvHandler != null) {
                mEmvHandler.onSetCardHolderInputPin(null);
            }
            return true;
        }
        return false;
    }

    public static void initDukptBDKAndKsn(DeviceServiceEngine engine, final MainActivity.AlertDialogOnShowListener alertDialogOnShowListener)throws RemoteException{
        //BDK 32 length String.
        String BDK = "C1D0F8FB4958670DBA40AB1F3752EF0D";
        //KSN must be 20 length String. 95A627000210210 00000
        String ksn = "FFFF9876543210" + "000000";
        int ret = engine.getPinPad().initDukptBDKAndKsn(KeyIndex, BDK, ksn,true, "00000");
        alertDialogOnShowListener.showMessage("initDukptBDKAndKsn ret :" +( ret == ServiceResult.Success));
    }
    //support 0~5;
    static int KeyIndex = 0;
    public static void initDukptIPEKAndKsn(DeviceServiceEngine engine, final MainActivity.AlertDialogOnShowListener alertDialogOnShowListener)throws RemoteException{
        //IPEK 32 length String.
        String IPEK = "C1D0F8FB4958670DBA40AB1F3752EF0D";
        //KSN must be 20 length String. 95A627000210210 00000
        String ksn = "FFFF9876543210" + "000000";
        int ret = engine.getPinPad().initDukptIPEKAndKsn(KeyIndex, IPEK, ksn,true, "00000");//its a open function for ipek keys
        alertDialogOnShowListener.showMessage("initDukptIPEKAndKsn ret :" +( ret == ServiceResult.Success));
    }

    public static void dukptCalculation(DeviceServiceEngine engine, final MainActivity.AlertDialogOnShowListener alertDialogOnShowListener)throws RemoteException{
        String ksn = engine.getPinPad().increaseKSN(KeyIndex, new Bundle());
        // data length should be is multiple of 8.
         byte[] inputData = Utils.str2Bcd("04953DFFFF9D9D7B".trim());
		 byte[] data = Utils.checkInputData(inputData);
        byte keyType = DukptKeyType.MF_DUKPT_DES_KEY_PIN;
        //only support TDES.
        int desAlgorithmType = DesAlgorithmType.TDES_CBC;
        int desMode = DesMode.ENCRYPT; // DesMode.ENCRYPT DesMode.DECRYPT
//        String dukptCalculation(int keyIndex, byte keyType, int desAlgorithmType, byte[] data, int dataLen, int desMode, Bundle bundle)
        String calculationData = engine.getPinPad().dukptCalculation(DukptKeyGid.GID_GROUP_EMV_IPEK, keyType, desAlgorithmType ,data, data.length , desMode, new Bundle());
        Log.d(TAG,"calculationData = " + calculationData);
        Log.d(TAG,"ksn = " + ksn);
        alertDialogOnShowListener.showMessage(
                "multiple of 8 = " + (data.length / 8 ==0)
                +"\n dukptCalculation ksn :" +(ksn)
                + "\n" + " calculationData :" + calculationData);
    }
}
