# login

#### Function description
> Login ysdk service with bussinessId;

#### Function prototype

```java
int login(in Bundle bundle, String bussinessId);
```

#### Parameter
| Name        | Type   | Description          |
| :---------- | :----- | :------------------- |
| bundle      | Bundle | Reserve              |
| bussinessId | String | “**00000000**” MK/SK |


#### Return
| Value | Description |
| :---- | :---------- |
| 0     | succeed     |
| else  | fail        |

Constant value returned by login

```
public class ServiceResult {
	public final static int LOGIN_SUCCESS = 0;
	public final static int LOGIN_FAIL = 1;
}
```



### See also

[Home](../README.md) |[getDevInfo](getDevInfo.md)|[setSystemClock](setSystemClock.md)

