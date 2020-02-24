# getMac

### Interface functions
> Calculate MAC

### Prototype

```java
byte[] getMac(int keyIndex, int mode,int desType, in byte[] data 
              , in Bundle bundle);
```

- #### Parameter
| Parameter | Class  | Description               |
| :-------- | :----- | :------------------------ |
| keyIndex  | int    | Work key index.           |
| mode      | int    | See **MacAlgorithmType**. |
| desType   | int    | Reserve.                  |
| data      | byte[] | Source data.              |
| bundle    | Bundle | Reserve.                  |

- #### Return
| Class  | Description                    |
| :----- | :----------------------------- |
| byte[] | Calculate the result from mac. |

### For example:

```java
 byte[] tsrc = hexStringToByte("6222620910029130840D2412220822043945");
 byte[] macResult = engine.getPinPad().getMac(0, MacAlgorithmType.ECB, 0, tsrc, new Bundle());
```



### See also

[Function table](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)

