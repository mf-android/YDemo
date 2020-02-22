# login

#### Function description
> Login ysdk service

#### Function prototype

```objective-c
int login(in Bundle bundle, String bussinessId);
```

#### Parameter
| Parameter   | Class  | Description                                    |
| :---------- | :----- | :--------------------------------------------- |
| bundle      | Bundle | Reserve                                        |
| bussinessId | String | “**09000000**” DUKPT      “**00000000**” MK/SK |


#### Return
| Class | Description           |
| :---- | :-------------------- |
| int   | 0: succeed    1: fail |

Constant value returned by login

```
public class ServiceResult {
	public final static int LOGIN_SUCCESS = 0;
	public final static int LOGIN_FAIL = 1;
}
```



### Reference related functions

[Function table](../README.md) | [Quickstart](Quickstart.md)

