# desEncByWKey

### Interface functions
> DES encrypted MAC/track key

### Prototype

```java
int desEncByWKey(int mKeyIdx, int wKeyType, in byte[] data, int dataLen, int desType
                 ,out byte[] desResult);
```

- #### Parameter
| Parameter | Class  | Description          |
| :-------- | :----- | :------------------- |
| mKeyIdx   | int    | Work key index       |
| keyType   | int    | See [WorkKeyType](enum.md#WorkKeyType). |
| data      | byte[] | Source data          |
| dataLen   | int    | Source data length.  |

- #### Return
| Class | Description               |
| :---- | :------------------------ |
| int   | 0: succeed;   other: fail |

### For example:

```java
final byte[] block = new byte[16];
byte[] tdKeyBytes = hexStringToByte("6259960052855293D220620112762919");
final int ret = engine.getPinPad().desEncByWKey(0, WorkKeyType.TDKEY, tdKeyBytes, tdKeyBytes.length, DesAlgorithmType.TDES, block);
```



### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)

