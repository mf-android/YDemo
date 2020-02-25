
# emvProcess

### Interface functions
> Emv process

### Prototype

```java
int emvProcess(in Bundle data, in OnEmvProcessListener listener);
```

- #### Parameter
| Name     | Type                                          | Description  |
| :------- | :-------------------------------------------- | :----------- |
| data     | Bundle                                        |              |
| listener | [OnEmvProcessListener](#OnEmvProcessListener) | Emv Callback |
| mode     | int                                           | Reserve      |

- #### Return
| Type | Description |
| :--- | :---------- |
| 0    | Succeed     |
| else | Fail        |

### OnEmvProcessListener

```java
	void onSelApp(in List<String> appNameList, boolean isFirstSelect);
	
	void onConfirmCardNo(String cardNo);
	
	void onCardHolderInputPin(boolean isOnlinePin,int leftTimes);
	
	void onOnlineProc(in Bundle data);

    void onContactlessOnlinePlaceCardMode(int mode);

	void onFinish(int retCode,in Bundle data);
```

- #### onSelApp

  > On Select App CallBack

| Name          | Type    | Description            |
| ------------- | ------- | ---------------------- |
| appNameList   | List    | App list               |
| isFirstSelect | boolean | true: mean first time. |

- #### onConfirmCardNo

  > Confirm CardNum

  | Name   | Type   | Description |
  | ------ | ------ | ----------- |
  | cardNo | String | Card number |

- #### onCardHolderInputPin

  > Inform user  enter password.
  
  | Name        | Type    | Description                         |
  | ----------- | ------- | ----------------------------------- |
  | isOnlinePin | boolean | true: online trans, false: offline. |
  | leftTimes   | int     |                                     |
  
- #### onOnlineProc

  > Work online here and import online results.

  | Name | Type   | Description |
  | ---- | ------ | ----------- |
  | data | Bundle |             |

- #### onContactlessOnlinePlaceCardMode

  > Rupay tow tap callback.
  
  | Name | Type | Description |
  | ---- | ---- | ----------- |
  | mode | int  | rupay       |


- #### onFinish

  > Final callback, inform transaction results, transaction error information.
  
  | Name    | Type   | Description |
  | ------- | ------ | ----------- |
  | retCode | String |             |
  | data    | Bundle |             |



#### For example

```java

```


### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)|[inputOnlinePin](inputOnlinePin.md)|[setSupportPinLen](setSupportPinLen.md)

