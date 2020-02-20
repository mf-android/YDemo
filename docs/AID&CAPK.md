### [Home](https://github.com/mf-android/YDemo)

# AID

#### Add AID

```
String key = "9F0608A000000524010101DF0101009F08020030DF11050000000000DF12050000000000DF130500000000009F1B04000186A0DF150400000000DF160100DF170100DF14039F3704DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF2106000000004000";
int ret = mSDKManager.getEmvHandler().addAidParam(string2byte(key));
```



#### Function  getAidParaList();

```
List<EmvAidPara> aidParaList = mSDKManager.getEmvHandler().getAidParaList();
```

#### Function  setAidParaList(List<EmvAidPara> aidParaList);

```
  int ret = mSDKManager.getEmvHandler().setAidParaList(List<EmvAidPara> aidParaList);
```

#### Function  clearAIDParam();

```
mSDKManager.getEmvHandler().clearAIDParam();
```

#### 

# CAPK

#### Add CAPK

```
String key = "9F0605A0000003339F220102DF050420211231DF060101DF070101DF028190A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57DF040103DF031403BB335A8549A03B87AB089D006F60852E4B8060";
int ret = mSDKManager.getEmvHandler().addCAPKParam(string2byte(key));
```

#### Function getCapkList();

```
List<EmvCapk> capkList = mSDKManager.getEmvHandler().getCapkList();
```

##### Function  setCAPKList(List<EmvCapk> capkList);

```
  int ret = mSDKManager.getEmvHandler().setCAPKList(List<EmvCapk> capkList);
```

#### Function  clearCAPKParam();

```
mSDKManager.getEmvHandler().clearCAPKParam();
```

##### 