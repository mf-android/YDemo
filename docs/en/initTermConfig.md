# initTermConfig

### Function functions
> Initialize terminal parameters

### Prototype

```java
int initTermConfig(in Bundle cfg);
```

- #### Parameter
| Name | Type   | Description                                          |
| :--- | :----- | :--------------------------------------------------- |
| cfg  | Bundle | [EMVTermCfgConstrants](enum.md#EMVTermCfgConstrants) |


- #### Return
| Value | Description |
| :---- | :---------- |
| 0     | Succeed     |
| else  | Fail        |



### For example:

```java
public static Bundle getInitTermConfig() {
        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvTermCfgConstrants.TERMCAP, new byte[]{(byte) 0x20, (byte) 0x68, (byte) 0x08});
        bundle.putByteArray(EmvTermCfgConstrants.ADDTERMCAP, new byte[]{(byte) 0xF2, (byte) 0x00, (byte) 0xF0, (byte) 0xA0, (byte) 0x01});
        bundle.putByte(EmvTermCfgConstrants.TERMTYPE, (byte) 0x22);
        bundle.putByteArray(EmvTermCfgConstrants.COUNTRYCODE, new byte[]{(byte) 0x03, (byte) 0x56});
        bundle.putByteArray(EmvTermCfgConstrants.CURRENCYCODE, new byte[]{(byte) 0x03, (byte) 0x56});
        bundle.putByteArray(EmvTermCfgConstrants.TRANS_PROP_9F66, new byte[]{0x36, (byte) 0x00, (byte) 0xc0, (byte) 0x00});
        return bundle;
}
```



### See also

[Home](../README.md)|[emvProcess](emvProcess.md) |[readEmvData](readEmvData.md)|[initTermConfig](initTermConfig.md)

