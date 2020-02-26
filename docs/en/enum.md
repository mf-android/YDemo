
### DeviceInfoConstrants

| Name               | Value           | Description               |
| ------------------ | --------------- | ------------------------- |
| COMMOM_VENDOR      | "vendor"        | morefun                   |
| COMMOM_MODEL       | "model"         | Model                     |
| COMMOM_OS_VER      | "os_ver"        | System version            |
| COMMOM_SN          | "sn"            | Serial No                 |
| COMMON_SERVICE_VER | "service_ver"   | SDK  version              |
| IS_COMM_SIG        | "is_common_sig" | "true": apk need to sign. |
| COMMOM_HARDWARE    | "hardware"      | Basechip version.         |


### MacAlgorithmType

| Name | Value | Description |
| ---- | ----- | ----------- |
| ECB  | 0     | mac key     |
| CBC  | 1     | track key   |



### PinPadConstrants

| Name                 | Value   | Description                      |
| -------------------- | ------- | -------------------------------- |
| IS_SHOW_TITLE_HEAD   | boolean | Password layout title head show. |
| TITLE_HEAD_CONTENT   | String  | Password layout content.         |
| IS_SHOW_PASSWORD_BOX | boolean | Password layout password view.   |


### ServiceResult

| Parameter           | Value | Description                |
| ------------------- | ----- | -------------------------- |
| PinPad_Input_Cancel | -7006 | PinPad Input Cancel button |
| PinPad_Input_OK     | -7044 | PinPad Input Okay button   |
| PinPad_Input_Clear  | -7035 | PinPad Input Clear button  |
| PinPad_Input_Num    | -7045 | PinPad Input Number        |
| Param_In_Invalid    | -2 | Method Param Invalid        |
| PinPad_Dstkey_Idx_Error    | -7012 | Load key Index error     |
| PinPad_Key_Len_Error    | -7014 | Load key Index error        |
| PinPad_No_Key_Error    | -7001 | Master key is empty         |


### WorkKeyType

| Name   | Value | Description |
| ------ | ----- | ----------- |
| PINKEY | 0     | Pin key.    |
| MACKEY | 1     | Mac key.    |
| TDKEY  | 2     | Track key.  |

### EMVTermCfgConstrants

| Name            | Value             | Description                                                  |
| --------------- | ----------------- | ------------------------------------------------------------ |
| TERMCAP         | "termCap"         | Indicates the card data input, CVM, and security capabilities of the Terminal. |
| TRANS_PROP_9F66 | "trans_prop_9F66" | Terminal Transaction Qualifiers.                             |
| TERMTYPE        | "termType"        | Terminal type.                                               |
| COUNTRYCODE     | "countryCode"     | Country code.                                                |
| CURRENCYCODE    | "curCode"         | Currency code.                                               |



### ICCSearchResult

| Name      | Value       | Description              |
| --------- | ----------- | ------------------------ |
| CARDTYPE  | "CardType"  | Card type                |
| M1SN      | “M1_sn"     | M1 card sn               |
| CARDOTHER | "CardOther" | Contact:1; Contactless:7 |

### IccCardType

| Name      | Value       | Description |
| --------- | ----------- | ----------- |
| CPUCARD   | "CPUCARD"   | CPU card    |
| AT24CXX   | "AT24CXX"   | CPU card    |
| AT88SC102 | "AT88SC102" | CPU card    |
| M1CARD    | "M1CARD"    | M1 Card     |
| PSAM      | "PSAM"      | PSAM        |

