# searchCard

### Function functions
> Search magnetic stripe Card.

### Prototype

```java
int searchCard(in OnSearchMagCardListener listener, int timeout ,in Bundle data);
```

- #### Parameter
| Name     | Type                                                | Description |
| :------- | :-------------------------------------------------- | :---------- |
| listener | [OnSearchMagCardListener](#OnSearchMagCardListener) |             |
| timeout  | int                                                 | time out.   |
| data     | Bundle                                              | With dukpt. |


- #### Return
| Value | Description |
| :---- | :---------- |
| 0     | Succeed     |
| else  | Fail        |

### OnSearchMagCardListener

```
void onSearchResult(int retCode, in MagCardInfoEntity mcie);
```

- #### Parameter
| Name    | Type                                    | Description |
| :------ | :-------------------------------------- | :---------- |
| retCode | int                                     | 0: succeed  |
| mcie    | [MagCardInfoEntity](#MagCardInfoEntity) |             |

  

- #### Return

  > void

- #### MagCardInfoEntity

```
	private String tk1; //track 1
	private String tk2; // track 2
	private String tk3; // track 3
	private String ksn; // ksn
	private String cardNo;
	private String cardholderName;
	private String expDate;
	private String serviceCode;
```





### See also

[Home](../README.md) 

