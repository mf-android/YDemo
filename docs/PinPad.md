

#### offline PinPad

```
mSDKManager.getPinPad().inputText(bundle,listener,DispTextMode.PASSWORD);
```

#### online PinPad

```
//only support ISO-format0
int pinAlgMode = PinAlgorithmMode.ISO9564FMT1;
int mKeyId = mkskKeyIndex; 
mSDKManager.getPinPad().inputOnlinePin(bundle, panBlock,mKeyId,pinAlgMode,listener);
```

#### Listener

```
new OnPinPadInputListener.Stub() {
            @Override
            public void onInputResult(int ret, byte[] pinBlock, String pinKsn) throws RemoteException {

            }

            @Override
            public void onSendKey(byte keyCode) throws RemoteException {
                Log.d(TAG, "keyCode = " + keyCode);
                if (keyCode == (byte) ServiceResult.PinPad_Input_Cancel) {
                    if (mEmvHandler != null) {
                        mEmvHandler.onSetCardHolderInputPin(null);
                    }
//                    alertDialogOnShowListener.showMessage("Pin Pad is cancel.");
                } else if (keyCode == (byte) ServiceResult.PinPad_Input_OK) {
                    Log.d(TAG, "keyCode =  PinPad_Input_OK");
                } else if (keyCode == (byte) ServiceResult.PinPad_Input_Clear) {
                    Log.d(TAG, "keyCode =  PinPad_Input_Clear");
                } else if (keyCode == (byte) ServiceResult.PinPad_Input_Num) {
                    Log.d(TAG, "keyCode =  PinPad_Input_Num");
                }
            }
        }
```