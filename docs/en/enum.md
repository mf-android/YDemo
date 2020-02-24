#### WorkKeyType

| Name   | Value | Description |
| ------ | ----- | ----------- |
| MACKEY | 1     | mac key     |
| TDKEY  | 2     | track key   |

#### DeviceInfoConstrants

```
public class DeviceInfoConstrants {
    public static final String COMMOM_VENDOR = "vendor";
    public static final String COMMOM_MODEL = "model";
    public static final String COMMOM_OS_VER = "os_ver";
    public static final String COMMOM_SN = "sn";
    public static final String COMMON_SERVICE_VER = "service_ver";
    public static final String IS_COMM_SIG = "is_common_sig";
    public static final String COMMOM_HARDWARE = "hardware";
}
```

#### **MacAlgorithmType**

| Name | Value | Description |
| ---- | ----- | ----------- |
| ECB  | 0     | mac key     |
| CBC  | 1     | track key   |

#### PinPadConstrants

| Name                 | Value   | Description                      |
| -------------------- | ------- | -------------------------------- |
| IS_SHOW_TITLE_HEAD   | boolean | Password layout title head show. |
| TITLE_HEAD_CONTENT   | String  | Password layout content.         |
| IS_SHOW_PASSWORD_BOX | boolean | Password layout password view.   |

ServiceResult

```
public class ServiceResult {
	public final static int LOGIN_SUCCESS = 0;
	public static final int Param_In_Invalid = -2;
	public static final int PinPad_Dstkey_Idx_Error = -7012;
	public static final int PinPad_Key_Len_Error = -7014;
	public static final int PinPad_No_Key_Error = -7001;
}
```

#### ServiceResult

| Parameter           | Value | Description   |
| ------------------- | ----- | ------------- |
| PinPad_Input_Cancel | -7006 | Cancel button |
| PinPad_Input_OK     | -7044 | Okay button   |
| PinPad_Input_Clear  | -7035 | Clear button  |
| PinPad_Input_Num    | -7045 | Number        |

Constant value returned by loadWKey

```
public class ServiceResult {
	public static final int LOGIN_SUCCESS = 0;
	public static final int Param_In_Invalid = -2;
	public static final int PinPad_Dstkey_Idx_Error = -7012;
	public static final int PinPad_Key_Len_Error = -7014;
	public static final int PinPad_No_Key_Error = -7001;
}
```

#### WorkKeyType

| Name   | Value | Description |
| ------ | ----- | ----------- |
| PINKEY | 0     | Pin key.    |
| MACKEY | 1     | Mac key.    |
| TDKEY  | 2     | Track key.  |

