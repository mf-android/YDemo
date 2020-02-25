
# inputOnlinePin

### Interface functions
> Input online pin

### Prototype

```java
int inputOnlinePin(in Bundle bundle,in byte[] panBlock, int mKeyId, int pinAlgMode, in OnPinPadInputListener listener);
```

#### Parameter
| Name  | Type                 | Description                   |
| :--------- | :-------------------- | :---------------------------- |
| bundle     | Bundle                | [PinPadConstrants](enum.md#PinPadConstrants) |
| panBlock   | byte[]                | pan                           |
| mKeyId     | int                   | work key index                |
| pinAlgMode | int |                               |
| listener   | OnPinPadInputListener | See **OnPinPadInputListener** |


#### Return
| Value | Description               |
| :---- | :------------------------ |
| 0     | succeed. |
| other |  fail. |


#### OnPinPadInputListener

```
void onInputResult(int retCode, in byte[] pin ,String ksn);
	
void onSendKey(byte keyCode);
```

- #### onInputResult

| Name    | Type   | Description               |
| ------- | ------ | ------------------------- |
| retCode | int    | 0: succeed;   other: fail |
| pin     | byte[] | Pin data.                 |
| ksn     | String | For dukpt.                |

- #### onSendKey

  | Name    | Type | Description            |
  | ------- | ---- | ---------------------- |
  | keyCode | byte | [ServiceResult](enum.md#ServiceResult) |

#### For example

```

```



### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)|[inputOnlinePin](inputOnlinePin.md)|[setSupportPinLen](setSupportPinLen.md)

