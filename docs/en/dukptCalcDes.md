# dukptCalcDes

### Interface functions
> Through dukpt encryption and decryption

### Prototype

```objective-c
public Bundle dukptCalcDes(DukptCalcObj dukptCalcObj)
```

- #### Parameter
| Name | Type | Description |
| :-------- | :--------| :------ |
| dukptCalcObj | DukptCalcObj | See DukptCalcObj definitions |

- #### Return
| Value | Description |
| :--------| :------ |
| DukptCalcObj.DUKPT_DATA | dukpt encrypted data |
| DukptCalcObj.DUKPT_KSN | Ksn currently used for encryption |

# DukptCalcObj definitions

```
package com.morefun.yapi.device.pinpad;

import android.os.Parcel;
import android.os.Parcelable;

public class DukptCalcObj implements Parcelable {
    public static final String DUKPT_KSN = "dukptKsn";
    public static final String DUKPT_DATA = "dukptData";

    private String data;

    private DukptType dukptType;
    private DukptAlg dukptAlg;
    private DukptOper dukptOper;

    public enum DukptType {
        DUKPT_DES_KEY_PIN(0x00),
        DUKPT_DES_KEY_MAC1(0x01),
        DUKPT_DES_KEY_MAC2(0x02),
        DUKPT_DES_KEY_DATA1(0x03),
        DUKPT_DES_KEY_DATA2(0x04),
        DUKPT_DES_KEY_PEK(0x05);

        private int dukptType;

        private DukptType(int type) {
            this.dukptType = type;
        }

        public int toInt() {
            return (int) dukptType;
        }
    }

    public enum DukptAlg {
        DUKPT_ALG_ECB(0x00),
        DUKPT_ALG_CBC(0x01);

        private int alg;

        private DukptAlg(int alg) {
            this.alg = alg;
        }

        public int toInt() {
            return (int) alg;
        }
    }

    public enum DukptOper {
        DUKPT_ENCRYPT(0x00),
        DUKPT_DECRYPT(0x01);

        private int oper;

        private DukptOper(int oper) {
            this.oper = oper;
        }

        public int toInt() {
            return (int) oper;
        }
    }

    public DukptCalcObj() {

    }

    public DukptCalcObj(DukptType type, DukptOper oper, DukptAlg alg, String data) {
        this.dukptType = type;
        this.dukptOper = oper;
        this.dukptAlg = alg;
        this.data = data;
    }

    public DukptCalcObj(Parcel source) {
        this.data = source.readString();
        this.dukptType = (DukptType) source.readValue(DukptType.class.getClassLoader());
        this.dukptOper = (DukptOper) source.readValue(DukptOper.class.getClassLoader());
        this.dukptAlg = (DukptAlg) source.readValue(DukptAlg.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(data);
        dest.writeValue(dukptType);
        dest.writeValue(dukptOper);
        dest.writeValue(dukptAlg);

    }

    public static final Creator<DukptCalcObj> CREATOR = new Creator<DukptCalcObj>() {

        @Override
        public DukptCalcObj createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            DukptCalcObj obj = new DukptCalcObj(source);
            return obj;
        }

        @Override
        public DukptCalcObj[] newArray(int size) {
            // TODO Auto-generated method stub
            return null;
        }
    };

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DukptType getDukptType() {
        return dukptType;
    }

    public void setDukptType(DukptType dukptType) {
        this.dukptType = dukptType;
    }

    public DukptAlg getDukptAlg() {
        return dukptAlg;
    }

    public void setDukptAlg(DukptAlg dukptAlg) {
        this.dukptAlg = dukptAlg;
    }

    public DukptOper getDukptOper() {
        return dukptOper;
    }

    public void setDukptOper(DukptOper dukptOper) {
        this.dukptOper = dukptOper;
    }
}


```



### See also
 [Home](../README.md) | [increaseKsn](increaseKsn.md) | [dukptLoad](dukptLoad.md) | [setKeyIndex](setKeyIndex.md) 

