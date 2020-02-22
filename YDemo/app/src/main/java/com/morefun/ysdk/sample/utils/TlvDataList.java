package com.morefun.ysdk.sample.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TlvDataList {
    private List<TlvData> data = new ArrayList<TlvData>();

    public static TlvDataList fromBinary(byte[] data) {
        TlvDataList l = new TlvDataList();
        int offset = 0;
        while (offset < data.length) {
            TlvData d = TlvData.fromRawData(data, offset);
            l.addTLV(d);
            offset += d.getRawData().length;
        }
        return l;
    }

    public static TlvDataList fromBinary(String data) {
        return fromBinary(HexUtil.hexStringToByte(data));
    }

    public int size() {
        return data.size();
    }

    public byte[] toBinary() {
        byte[][] allData = new byte[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            allData[i] = data.get(i).getRawData();
        }
        return HexUtil.merge(allData);
    }

    public boolean contains(String tag) {
        return null != getTLV(tag);
    }

    public TlvData getTLV(String tag) {
        for (TlvData d : data) {
            if (d.getTag().equals(tag)) {
                return d;
            }
        }
        return null;
    }

    public TlvDataList getTLVs(String... tags) {
        TlvDataList list = new TlvDataList();
        for (String tag : tags) {
            TlvData data = getTLV(tag);
            if (data != null) {
                list.addTLV(data);
            }
        }
        if (list.size() == 0) {
            return null;
        }
        return list;
    }

    public TlvData getTLV(int index) {
        return data.get(index);
    }

    public void addTLV(TlvData tlv) {
        if (tlv.isValid()) {
            data.add(tlv);
        } else {
            throw new IllegalArgumentException("tlv is not valid!");
        }
    }

    public void retainAll(String... tags) {
        List<String> tagList = Arrays.asList(tags);
        for (int i = 0; i < data.size(); ) {
            if (!tagList.contains(data.get(i).getTag())) {
                data.remove(i);
            } else {
                i++;
            }
        }
    }

    public void remove(String tag) {
        for (int i = 0; i < data.size(); ) {
            if (tag.equals(data.get(i).getTag())) {
                data.remove(i);
            } else {
                i++;
            }
        }
    }

    public void removeAll(String... tags) {
        List<String> tagList = Arrays.asList(tags);
        for (int i = 0; i < data.size(); ) {
            if (tagList.contains(data.get(i).getTag())) {
                data.remove(i);
            } else {
                i++;
            }
        }
    }

    @Override
    public String toString() {
        if (data.isEmpty()) {
            return super.toString();
        }
        return HexUtil.bytesToHexString(toBinary());
    }
}