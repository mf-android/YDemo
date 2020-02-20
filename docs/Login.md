### [Home](https://github.com/mf-android/YDemo)


enble dukpt, Please set login with 09000000
```
int ret = DeviceServiceEngine.login(new Bundle(), "09000000");
boolean loginResult = (ret == ServiceResult.Success);
```

mk/sk
```
int ret = DeviceServiceEngine.login(new Bundle(), "00000000");
boolean loginResult = (ret == ServiceResult.Success);
```