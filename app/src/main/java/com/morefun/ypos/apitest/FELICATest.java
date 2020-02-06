package com.morefun.ypos.apitest;

import android.os.Bundle;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.card.industry.IndustryCardHandler;
import com.morefun.yapi.device.reader.icc.ICCSearchResult;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.device.reader.icc.IccReaderSlot;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.BaseApiTest;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;
import com.morefun.ypos.uitls.Utils;

import static com.morefun.ypos.uitls.Utils.byte2string;

public class FELICATest extends BaseApiTest {
    private static final String TAG = "FELICATest";

    public static void test_felica(final DeviceServiceEngine mSDKManager, final MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) {
        new SearchCardOrCardReaderTest(mSDKManager, alertDialogOnShowListener).searchRFCard(new String[]{IccCardType.FELICA}, new MainActivity.OnSearchListener() {
            @Override
            public void onSearchResult(int retCode, Bundle bundle) {
                if (ServiceResult.Success == retCode) {
                    String cardType = bundle.getString(ICCSearchResult.CARDTYPE);
                    Log.v(TAG, "cardType：" + cardType);
                    if (IccCardType.FELICA.equals(cardType)) {
                        felica(mSDKManager, alertDialogOnShowListener);
                    }
                } else {
                    alertDialogOnShowListener.showMessage(getString(R.string.msg_icorrfid));
                }
            }
        });
    }

    private static void felica(DeviceServiceEngine mSDKManager, MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) {
        try {
            final IndustryCardHandler cardHandler = mSDKManager.getIndustryCardHandler(
                    mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT));
            if (cardHandler == null) {
                alertDialogOnShowListener.showMessage(MainActivity.getStaticString(R.string.msg_readfail_retry));
                return;
            }

            cardHandler.setPowerOn(new byte[]{0x00, 0x00});

            byte[] result = new byte[256];
            byte[] ucPollCmd = new byte[]{0x6, 0x00, (byte) 0xFF, (byte) 0xFF, 0x0, 0x00};
            byte[] ucReadCmd = new byte[]{0x10, 0x06, 0x01, 0x2e, 0x45, 0x76, (byte) 0xba, (byte) 0xc5, 0x41, 0x2c, 0x01, 0x0b, 0x00, 0x01, (byte) 0x80, (byte) 0x82};

            Log.v(TAG, "ucPollcmd:\n" + byte2string(ucPollCmd));
            int ret = cardHandler.exchangeCmd(result, ucPollCmd, ucPollCmd.length);
            Log.v(TAG, "ucPollcmd ret = " + ret);

            String msg = String.format("%s%d\n", getString(R.string.label_result), ret);
            if (ret > 0 && result != null) {
                Log.v(TAG, "ucReadCmd:\n" + byte2string(result));

                int pos = 2;
                Utils.memcpy(ucReadCmd, 2, result, 2, 8);
                pos += 8;
                Utils.memcpy(ucReadCmd, pos, ucReadCmd, pos, ucReadCmd.length - pos);


                Log.v(TAG, "ucReadCmd:\n" + byte2string(ucReadCmd));
                ret = cardHandler.exchangeCmd(result, ucReadCmd, ucReadCmd.length);
                cardHandler.setPowerOff();
                Log.v(TAG, "ucPollcmd ret = " + ret);
                if (ret > 0) {
                    byte[] buffer = new byte[ret];
                    Utils.memcpy(buffer, 0, result, 0, ret);
                    Log.v(TAG, "data:\n" + byte2string(buffer));
                    msg = String.format("%s%d\n%s", getString(R.string.label_result)
                            , ret, byte2string(buffer));
                }
            }
            alertDialogOnShowListener.showMessage(msg);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
