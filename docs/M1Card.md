### [Home](https://github.com/mf-android/YDemo)

#### M1 Card

```
try {
            M1CardHandler m1CardHandler = engine.getM1CardHandler(engine.getIccCardReader(IccReaderSlot.RFSlOT));
            byte key[] = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            if (m1CardHandler == null) {
                return;
            }

            byte[] uid = new byte[64];
            int ret = m1CardHandler.authority(M1KeyTypeConstrants.KEYTYPE_A, 0, key, uid);
            if (ret != ServiceResult.Success) {
            	//TODO
                return;
            }

            int len = 16;
            byte buf[] = new byte[len];
            byte value[] = new byte[]{(byte) 0x0A};
            byte write[] = new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78
                    , (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF
                    , (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78
                    , (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
            Utils.memset(write, 0x0, write.length);
            write[4] = (byte) 0xff;
            write[5] = (byte) 0xff;
            write[6] = (byte) 0xff;
            write[7] = (byte) 0xff;

            write[12] = ~0;
            write[13] = 0;
            write[14] = ~0;
            write[15] = 0;

            String s = String.format("M1Card Test\n");
            ret = m1CardHandler.writeBlock(1, write);
            s += String.format("writeBlock(%d)\n", ret);
            ret = m1CardHandler.readBlock(0, buf);
            s += String.format("readBlock0(%d):", ret) + byte2string(buf) + "\n";
            ret = m1CardHandler.readBlock(1, buf);
            s += String.format("readBlock1(%d):", ret) + byte2string(buf) + "\n";
            Log.v("tag", "read block:" + s);

            ret = m1CardHandler.operateBlock(M1CardOperType.INCREMENT, 1, value, 0);
            Log.v("tag", "INCREMENT block:" + ret);
            s += "INCREMENT block:" + Integer.toString(ret) + "\n";
            ret = m1CardHandler.readBlock(1, buf);
            s += String.format("readBlock1(%d):", ret) + byte2string(buf) + "\n";

            ret = m1CardHandler.operateBlock(M1CardOperType.DECREMENT, 1, value, 0);
            Log.v("tag", "DECREMENT block:" + ret);
            s += "DECREMENT block:" + Integer.toString(ret) + "\n";
            ret = m1CardHandler.readBlock(1, buf);
            s += String.format("readBlock1(%d):", ret) + byte2string(buf) + "\n";

            ret = m1CardHandler.operateBlock(M1CardOperType.BACKUP, 0, null, 1);
            Log.v("tag", "BACKUP block:" + ret);
            s += "BACKUP block:" + Integer.toString(ret) + "\n";
            ret = m1CardHandler.readBlock(1, buf);
            s += String.format("readBlock1(%d):", ret) + byte2string(buf) + "\n";
        } catch (Exception e) {
            e.printStackTrace();
        }
```



