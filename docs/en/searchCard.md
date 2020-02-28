# searchCard

### Function functions
> Search CPU Card.

### Prototype

```java
int searchCard(in OnSearchIccCardListener listener, int timeout, in String[] cardType);
```

- #### Parameter
| Name     | Type                                                | Description                        |
| :------- | :-------------------------------------------------- | :--------------------------------- |
| listener | [OnSearchIccCardListener](#OnSearchMagCardListener) |                                    |
| timeout  | int                                                 | time out.                          |
| cardType | String[]                                            | [IccCardType](enum.md#IccCardType) |


- #### Return
| Value | Description |
| :---- | :---------- |
| 0     | Succeed     |
| else  | Fail        |

### OnSearchIccCardListener

```
void onSearchResult(int retCode,in Bundle bundle);
```

- #### Parameter
| Name    | Type   | Description                                |
| :------ | :----- | :----------------------------------------- |
| retCode | int    | 0: succeed                                 |
| bundle  | Bundle | [ICCSearchResult](enum.md#ICCSearchResult) |

- #### Return

  > void




### See also

[Home](../README.md) 

