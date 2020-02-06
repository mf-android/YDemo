package com.morefun.ypos.apitest;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.card.cpu.APDUCmd;
import com.morefun.yapi.card.cpu.CPUCardHandler;
import com.morefun.yapi.device.reader.icc.ICCSearchResult;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.BaseApiTest;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;
import com.morefun.ypos.uitls.Utils;

import static com.morefun.ypos.uitls.Utils.hex2asc;
import static com.morefun.ypos.uitls.Utils.string2byte;

public class ApduCPUCardHandlerTest extends BaseApiTest {
    private static final String TAG = "ApduCPUCardHandlerTest";
    DeviceServiceEngine mSDKManager;
    MainActivity.AlertDialogOnShowListener mAlertDialogOnShowListener;

    public ApduCPUCardHandlerTest(DeviceServiceEngine mSDKManager, MainActivity.AlertDialogOnShowListener mAlertDialogOnShowListener) {
        this.mSDKManager = mSDKManager;
        this.mAlertDialogOnShowListener = mAlertDialogOnShowListener;
    }

    private void searchCPUCard(final String[] cardtype, final MainActivity.OnSearchListener onSearchListener) {
        new SearchCardOrCardReaderTest(mSDKManager, mAlertDialogOnShowListener).searchCPUCard(cardtype, onSearchListener);
    }

    public void testApdu(final String cmd, final String data, final byte le) throws RemoteException {
        searchCPUCard(new String[]{IccCardType.CPUCARD}, new MainActivity.OnSearchListener() {
            @Override
            public void onSearchResult(int retCode, Bundle bundle) {
                if (ServiceResult.Success == retCode) {
                    String cardType = bundle.getString(ICCSearchResult.CARDTYPE);
                    int slot = bundle.getInt(ICCSearchResult.CARDOTHER);
                    Log.v(TAG, "cardType:" + cardType + ", slot:" + slot);
                    if (cardType.equals(IccCardType.CPUCARD)) {
                        try {
                            testApdu(slot, cmd, data, le);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    mAlertDialogOnShowListener.showMessage(getString(R.string.msg_icorrfid));
                }
            }
        });
    }

    /**
     * "\x00\xA4\x04\x00\x0E\x32\x50\x41\x59\x2E\x53\x59\x53\x2E\x44\x44\x46\x30\x31\x00"
     *
     * @throws RemoteException
     */
    private void testApdu(int slot, final String cmd, final String data, final byte le) throws RemoteException {
        try {
            IccCardReader cardReader = mSDKManager.getIccCardReader(slot);
            CPUCardHandler cpuCardHandler = mSDKManager.getCPUCardHandler(cardReader);
            if (cpuCardHandler == null) {
                mAlertDialogOnShowListener.showMessage(getString(R.string.msg_readfail_retry));
                return;
            }
            // atr
            if (!cpuCardHandler.setPowerOn(new byte[]{0x00, 0x00})) {
                Log.e(TAG, "CPUCard poweron fail.");
                mAlertDialogOnShowListener.showMessage(getString(R.string.msg_readfail_retry));
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
                    mAlertDialogOnShowListener.showMessage(hex2asc(apduCmd.getDataOut(), 0, apduCmd.getDataOutLen() * 2, 1));
                } else {
                    mAlertDialogOnShowListener.showMessage(getString(R.string.msg_succ));
                }
            } else {
                mAlertDialogOnShowListener.showMessage(getString(R.string.msg_readfail_retry));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
