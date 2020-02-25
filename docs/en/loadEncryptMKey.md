# loadEncryptMKey

### Interface functions
> Load Master key

### Prototype

```java
int loadEncryptMKey(int mKeyIdx, in byte[] keyData, int keyDataLen, int decMKeyIdx, boolean isTmsKey);
```

- #### Parameter
| Parameter  | Type    | Description                          |
| :--------- | :------ | :----------------------------------- |
| mKeyIdx    | int     | Save master key index(0~98).         |
| keyData    | byte[]  | Ciphertext key.                      |
| keyDataLen | int     | Key data length.                     |
| decMKeyIdx | int     | KEK index for master key decryption. |
| isTmsKey   | boolean | Reserve.                             |


- #### Return
| Value | Description |
| :---- | :---------- |
| 0     | Succeed     |
| else  | Fail        |


### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)|[inputOnlinePin](inputOnlinePin.md)|[setSupportPinLen](setSupportPinLen.md)
