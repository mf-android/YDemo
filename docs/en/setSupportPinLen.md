# setSupportPinLen

### Interface functions
> Set Password Length

### Prototype

```java
setSupportPinLen(in int[] pinLen);
```

- #### Parameter
| Name   | Type  | Description |
| :----- | :---- | :---------- |
| pinLen | int[] | {max, min}  |

- #### Return
> void

### For example:

```java
//need to set 4 digits password length.        
int minLength = 4;
int maxLength = 4;
DevicesHelper.getPinPad().setSupportPinLen(new int[]{minLength, maxLength});
```



### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)|[inputOnlinePin](inputOnlinePin.md)|[setSupportPinLen](setSupportPinLen.md)

