package com.morefun.ypos.uitls;

import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.emv.EmvAidPara;
import com.morefun.yapi.emv.EmvCapk;
import com.morefun.yapi.emv.EmvHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAIDAndCAPK {
    private static final String TAG = "ViewAIDAndCAPK";
    EmvHandler mEmvHandler;

    public ViewAIDAndCAPK(EmvHandler mEmvHandler) {
        this.mEmvHandler = mEmvHandler;
    }

    public Map<String, String> getAidMap() throws RemoteException {
        Map<String, String> data = new HashMap<>();
        List<EmvAidPara> aidParaList = mEmvHandler.getAidParaList();
        StringBuilder builder = new StringBuilder();
        if (aidParaList != null && aidParaList.size() > 0) {
            Log.d(TAG, "aidParaList = " + aidParaList.size());
            for (EmvAidPara para : aidParaList) {
                builder.setLength(0);
                Map<String, String> map = new HashMap<>();
                byte[] bAid = para.getAID();
                addStringItem("AID:", bAid, 16, builder);
                addStringItem("TermAppVer(9F09):", para.getTermAppVer(), builder);
                addStringItem("TFL_Domestic(9F1B):", para.getTFL_Domestic(), builder);
                addStringItem("TAC_Default(DF11):", para.getTAC_Default(), builder);
                addStringItem("TAC_Online(DF12):", para.getTAC_Online(), builder);
                addStringItem("TAC_Denial(DF13):", para.getTAC_Denial(), builder);
                addStringItem("RFOfflineLimit(DF19):", para.getRFOfflineLimit(), builder);
                addStringItem("RFTransLimit(DF20):", para.getRFTransLimit(), builder);
                addStringItem("RFCVMLimit(DF21):", para.getRFCVMLimit(), builder);
                addStringItem("EC_TFL(9F7B):", para.getEC_TFL(), builder);
                data.put(Utils.hex2asc(bAid, bAid.length, 1), builder.toString());
            }
        }
        return data;
    }

    public String[] getStringArray(Map<String, String> map) {
        String[] data = new String[map.size()];
        int i = 0;
        for (String aid : map.keySet()) {
            data[i] = aid;
            i++;
        }
        return data;
    }

    private void addStringItem(String tagHead, byte[] data, int len, StringBuilder builder) {
        builder.append(tagHead + Utils.hex2asc(data, len, 1));
        builder.append("\n");
    }

    private void addStringItem(String tagHead, byte[] data, StringBuilder builder) {
        builder.append(tagHead + Utils.hex2asc(data, data.length * 2, 1));
        builder.append("\n");
    }

    private void addStringItem(String tagHead, byte data, StringBuilder builder) {
        builder.append(tagHead + data);
        builder.append("\n");
    }

    public ArrayList<String> getCapkList() throws RemoteException {
        ArrayList<String> data = new ArrayList<>();
        List<EmvCapk> capkList = mEmvHandler.getCapkList();
        StringBuilder builder = new StringBuilder();
        if (capkList != null && capkList.size() > 0) {
            Log.d(TAG, "capkList = " + capkList.size());
            for (EmvCapk para : capkList) {
                builder.setLength(0);
                builder.append("RID:" + Utils.hex2asc(para.getRID(), para.getRID().length * 2, 1));
                builder.append("\n");
                builder.append("CAPK_INDEK:" + para.getCA_PKIndex());
                builder.append("\n");
                data.add(builder.toString());
            }
            data.add(builder.toString());
        }
        return data;
    }
}
