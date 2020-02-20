### [Home](https://github.com/mf-android/YDemo)

#### contact check card

```
final IccCardReader iccCardReader = mSDKManager.getIccCardReader(IccReaderSlot.ICSlOT1);
iccCardReader.searchCard(listener, timeout, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
```



#### contactless check card

```
final IccCardReader rfReader = mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT);
rfReader.searchCard(listener, timeout, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
```

#### OnSearchIccCardListener.Stub

```
OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
            @Override
            public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                Log.d(TAG, "retCode= " + retCode);
                int cardother = bundle.getInt(ICCSearchResult.CARDOTHER);
                Log.d(TAG, "contactless = " + (cardother == IccReaderSlot.RFSlOT));
                Log.d(TAG, "contact =" + (cardother == IccReaderSlot.ICSlOT1));
            }
        };
```

#### stop search

```
rfReader.stopSearch();
iccCardReader.stopSearch();
```

