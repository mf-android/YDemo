# loadWKey

### Function functions
> Load work key

### Prototype

```java
int loadWKey(int mKeyIdx, int keyType, in byte[] keyData, int keyDataLen);
```

- #### Parameter
| Parameter  | Type   | Description                                                  |
| :--------- | :----- | :----------------------------------------------------------- |
| mKeyIdx    | int    | Save master key index(0~98).                                 |
| keyType    | int    | [WorkKeyType](enum.md#WorkKeyType)                           |
| keyData    | byte[] | Ciphertext key.(12 digits:Key[8 digits] + KVC(4 digits))or(20 digits:Key[16 digits] + KVC[4 digtis]) |
| keyDataLen | int    | Key data length.                                             |


- #### Return
| Value | Description |
| :---- | :---------- |
| 0     | Succeed     |
| else  | Fail        |


### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)|[inputOnlinePin](inputOnlinePin.md)|[setSupportPinLen](setSupportPinLen.md)

