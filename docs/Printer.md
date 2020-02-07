

#### Print

```
MultipleAppPrinter printer = engine.getMultipleAppPrinter();
List<MulPrintStrEntity> list = getList();
printer.printStr(list,listener,config);
```

#### Print content List

```
public List<MulPrintStrEntity> list getList(){
	 List<MulPrintStrEntity> list = new ArrayList<>();
	 MulPrintStrEntity entity = new MulPrintStrEntity("POS Purchase Order"
	 , FontFamily.MIDDLE);	             
	       entity.setGravity(Gravity.CENTER);
            entity.setMarginX(50);
            entity.setIsBold(1);
            entity.setUnderline(true);
            entity.setYspace(30);
            list.add(entity);
}
```

#### Listener

```
new OnPrintListener.Stub() {
    @Override
    public void onPrintResult(int result) throws RemoteException {
        Log.d(TAG, "onPrintResult = " + (result == ServiceResult.Success));
    }
}
```

#### Config

```
Bundle config = new Bundle();
config.putString(PrinterConfig.COMMON_TYPEFACE_PATH, typefacePath);
config.putInt(PrinterConfig.COMMON_GRAYLEVEL, 15);
```

