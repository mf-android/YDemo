# dukptLoad

### Interface functions
> Inject DUKPT key

### Prototype

```objective-c
public int dukptLoad(DukptLoadObj dukptLoadObj)
```

- #### Parameter
| Name | Type | Description |
| :-------- | :--------| :------ |
| dukptLoadObj | DukptLoadObj | See DukptLoadObj definitions |

- #### Return
| Value | Description |
| :--------| :------ |
| 0 | dukpt load success |
| other | dukpt load fail |

# DukptLoadObj definitions

```
package com.morefun.yapi.device.pinpad;

import android.os.Parcel;
import android.os.Parcelable;

public class DukptLoadObj implements Parcelable {
    private String key;
    private String ksn;
    private DukptKeyType keyType;
    private DukptKeyIndex keyIndex;
		
    public enum DukptKeyType {
        DUKPT_BDK_PLAINTEXT(0x00),  //!< IPEK plaintext
        DUKPT_IPEK_PLAINTEXT(0x01), //!< BDK plaintext
        DUKPT_IPEK_ENC_KEK(0x02),   //!< IPEK ciphertext encrypt by KEK
        DUKPT_BDK_ENC_KEK(0x03),    //!< BDK ciphertext encrypt by KEK
        DUKPT_IPEK_ENC_MAK(0x04),   //!< IPEK ciphertext encrypt by master key encrypt
        DUKPT_BDK_ENC_MAK(0x05);    //!< BDK ciphertext encrypt by master key encrypt

        private int dukptKeyType;

        private DukptKeyType(int type) {
            dukptKeyType = type;
        }

        public int toInt() {
            return (int) dukptKeyType;
        }
    }
//Use master key index. Values are as follows:KEY_INDEX_0~, KEY_INDEX_7, //respectively, index 0~ index 7
    public enum DukptKeyIndex {
        KEY_INDEX_0(0x00),
        KEY_INDEX_1(0x01),
        KEY_INDEX_2(0x02),
        KEY_INDEX_3(0x03),
        KEY_INDEX_4(0x04),
        KEY_INDEX_5(0x05),
        KEY_INDEX_6(0x06),
        KEY_INDEX_7(0x07);

        private int keyIndex;

        private DukptKeyIndex(int type) {
            keyIndex = type;
        }

        public int toInt() {
            return (int) keyIndex;
        }
    }

    public DukptLoadObj() {

    }

    public DukptLoadObj(String key, String ksn, DukptKeyType keyType, DukptKeyIndex keyIndex) {
        this.key = key;
        this.ksn = ksn;
        this.keyType = keyType;
        this.keyIndex = keyIndex;
    }

    public DukptLoadObj(Parcel source) {
        this.ksn = source.readString();
        this.key = source.readString();
        this.keyType = (DukptKeyType) source.readValue(DukptKeyType.class.getClassLoader());
        this.keyIndex = (DukptKeyIndex) source.readValue(DukptKeyIndex.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(ksn);
        dest.writeString(key);
        dest.writeValue(keyType);
        dest.writeValue(keyIndex);

    }

    public static final Parcelable.Creator<DukptLoadObj> CREATOR = new Creator<DukptLoadObj>() {

        @Override
        public DukptLoadObj createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            DukptLoadObj obj = new DukptLoadObj(source);
            return obj;
        }

        @Override
        public DukptLoadObj[] newArray(int size) {
            // TODO Auto-generated method stub
            return null;
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKsn() {
        return ksn;
    }

    public void setKsn(String ksn) {
        this.ksn = ksn;
    }

    public DukptKeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(DukptKeyType keyType) {
        this.keyType = keyType;
    }

    public DukptKeyIndex getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(DukptKeyIndex keyIndex) {
        this.keyIndex = keyIndex;
    }
}

```



### See also
 [Home](../README.md) | [increaseKsn](increaseKsn.md) | [dukptCalcDes](dukptCalcDes.md) | [setKeyIndex](setKeyIndex.md) |

