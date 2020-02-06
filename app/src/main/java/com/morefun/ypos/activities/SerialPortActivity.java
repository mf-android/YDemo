package com.morefun.ypos.activities;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.serialport.SerialPortDriver;
import com.morefun.ypos.R;
import com.morefun.ypos.SDKManager;
import com.morefun.ypos.uitls.Utils;

public class SerialPortActivity extends AppCompatActivity {

    private static final String TAG = "SerialPortActivity";
    Button mBtConnect, mBtClose;
    Button mBtWriteData;
    EditText mEtContent;
    TextView mTv_read_messsage;
    SerialPortDriver mSerialPortDriver;
    volatile boolean isOpen;
    volatile boolean isWrite;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port);

        findViews();
    }

    private void connectPC() throws RemoteException{
        isOpen = false;
        SDKManager mSDKManager = SDKManager.getInstance();
        if (mSDKManager == null){
            throw new NullPointerException("Please install YDEMO.APK first");
        }
        mSerialPortDriver = mSDKManager.getDeviceServiceEngine().getSerialPortDriver(4);
        int connect = mSerialPortDriver.connect("115200,N,8,1");
        Log.d(TAG, "SerialPortDriver connect reuslt = " + (connect == ServiceResult.Success));
        if (connect == ServiceResult.Success) {
            Toast.makeText(this,"Connect success", Toast.LENGTH_SHORT).show();
            isOpen = true;
        }
    }

    private void findViews() {
        mBtConnect = findViewById(R.id.Coneect);
        mBtWriteData = findViewById(R.id.writeData);
        mEtContent = findViewById(R.id.et_send);
        mTv_read_messsage = findViewById(R.id.tv_read_messsage);
        mBtClose = findViewById(R.id.Close);
        mBtConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen){
                    Toast.makeText(SerialPortActivity.this,"Connected", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    connectPC();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (isOpen){
                    createReadThread();
                }
            }
        });
        mBtWriteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    writeData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        mBtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    private void close() {
        isOpen  = false;
        try {
            mSerialPortDriver.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void writeData() throws RemoteException {
        if (!isOpen){
            Toast.makeText(SerialPortActivity.this,"Please connect first", Toast.LENGTH_SHORT).show();
            return;
        }
        String sendMessage = mEtContent.getText().toString();
        if (TextUtils.isEmpty(sendMessage)){
            return;
        }
        isWrite = true;
        byte[] messageBytes = sendMessage.getBytes();
        int send = mSerialPortDriver.send(messageBytes,messageBytes.length);
        Log.d(TAG, "SerialPortDriver read reuslt = " + (send == ServiceResult.Success));
        isWrite = false;
    }
    private void ReadMessageFromPC() throws RemoteException {
//        SystemClock.sleep(1000);
        while (isOpen){
            final byte[] recvBytes = new byte[1024];
            if (!isWrite){
                final int read = mSerialPortDriver.recv(recvBytes, recvBytes.length, 1_000);
                Log.d(TAG, "SerialPortDriver read reuslt = " + (read > ServiceResult.Success));
                if (read > 0){
                    Log.d(TAG, "SerialPortDriver recv reuslt = " + Utils.pubByteToHexString(Utils.getByteArray(recvBytes, 0, read)));
                    SerialPortActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTv_read_messsage.append(Utils.pubByteToHexString(Utils.getByteArray(recvBytes, 0, read)) + "\r\n");
                        }
                    });
                }
            }
            SystemClock.sleep(20);
        }
    }

    private void createReadThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ReadMessageFromPC();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
