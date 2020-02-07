

#### Search Card

```
 Bundle bundle = DukptConfigs.getTrackIPEKBundle();
mEngine.getMagCardReader().searchCard(listtener, timeOut, bundle);// second
```

#### OnSearchMagCardListener

```
OnSearchMagCardListener.Stub listtener = new OnSearchMagCardListener.Stub() {
            @Override
            public void onSearchResult(int retCode, MagCardInfoEntity magCardInfoEntity) throws RemoteException {
                if (retCode == ServiceResult.Success) {
                }
            }
        }, 60, bundle)
```

#### Stop search

```
mEngine.getMagCardReader().stopSearch();
```

