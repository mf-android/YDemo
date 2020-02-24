# loadWKey

### Function functions
> Load work key

### Prototype

```java
int loadWKey(int mKeyIdx, int keyType, in byte[] keyData, int keyDataLen);
```

- #### Parameter
| Parameter  | Class  | Description                  |
| :--------- | :----- | :--------------------------- |
| mKeyIdx    | int    | Save master key index(0~98). |
| keyType    | int    | See **WorkKeyType**.         |
| keyData    | byte[] | Ciphertext key.              |
| keyDataLen | int    | Key data length.             |


- #### Return
| Class | Description               |
| :---- | :------------------------ |
| int   | 0: succeed;   other: fail |




### See also
[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)

