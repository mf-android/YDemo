### [Home](https://github.com/mf-android/YDemo)
#### Emv process

```
mSDKManager.getEmvHandler().emvProcess(inBundle, mOnEmvProcessListener);
```

#### inBundle

```
    /**
     * getEmvHandler().emvProcess(Bundle bundle,
     *
     * @param channelType
     * @param amount
     * @param cashBackAmt
     * @return
     */
    public static Bundle getInitBundleValue(int channelType, String amount, String cashBackAmt) {
        Bundle bundle = new Bundle();
        byte[] transDate = new byte[3];
        byte[] transTime = new byte[3];

        getDateTime(transDate, transTime);

        bundle.putInt(EmvTransDataConstrants.MKEYIDX, 1);
        bundle.putBoolean(EmvTransDataConstrants.ISSUPPORTEC, false);
        bundle.putInt(EmvTransDataConstrants.PROCTYPE, 0);
        bundle.putInt(EmvTransDataConstrants.ISQPBOCFORCEONLINE, 0);
        bundle.putInt(EmvTransDataConstrants.CHANNELTYPE, channelType);

        bundle.putByte(EmvTransDataConstrants.B9C, getTrans9C());
        bundle.putString(EmvTransDataConstrants.TRANSDATE, pubByteToHexString(transDate));
        bundle.putString(EmvTransDataConstrants.TRANSTIME, pubByteToHexString(transTime));
        bundle.putString(EmvTransDataConstrants.SEQNO, "00001");

        bundle.putString(EmvTransDataConstrants.TRANSAMT, amount);
        bundle.putString(EmvTransDataConstrants.CASHBACKAMT, cashBackAmt);

        bundle.putString(EmvTransDataConstrants.MERNAME, "MOREFUN");
        bundle.putString(EmvTransDataConstrants.MERID, "488923");
        bundle.putString(EmvTransDataConstrants.TERMID, "4999000");
        //Pin Free Amount for contactLess
        bundle.putString(EmvTransDataConstrants.CONTACTLESS_PIN_FREE_AMT, "200000");
        //TODO For online transactions, the terminal must force to enter the password,Please set true
//        bundle.putBoolean(EmvTransDataConstrants.FORCE_ONLINE_CALL_PIN, false);
        //some additional requirements, It's optional
        bundle.putStringArrayList(EmvTransDataConstrants.TERMINAL_TLVS, setTerminalParamByTlvs());
        return bundle;
    }
```

#### InitTermConfig

```
mSDKManager.getEmvHandler().initTermConfig(EmvProcessConfig.getInitTermConfig());
```

