package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.serialport.SerialPortDriver;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.HexUtil;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SerialPortActivity extends BaseActivity {

    private static final String TAG = SerialPortActivity.class.getName();

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.etTransData)
    EditText etTransData;

    private SerialPortDriver serialPortDriver;
    private boolean isOpen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.connect, R.id.close, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect:
                connect();
                break;
            case R.id.close:
                close();
                break;
            case R.id.send:
                send();
                break;
        }

    }

    private void connect() {
        try {
            int port = 4;

            serialPortDriver = DeviceHelper.getSerialPortDriver(port);
            int connect = serialPortDriver.connect("115200,N,8,1");

            if (connect == ServiceResult.Success) {
                isOpen = true;
                showResult(textView, "Serial port connect success");
            } else {
                showResult(textView, "connect fail:" + connect);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            showResult(textView, e.getMessage());
        }
    }


    private void close() {
        try {
            serialPortDriver.disconnect();
            isOpen = false;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void send() {

        try {
            String sendMessage = etTransData.getText().toString();
            if (TextUtils.isEmpty(sendMessage) || !isOpen) {
                return;
            }

            byte[] data = sendMessage.getBytes();
            int send = serialPortDriver.send(data, data.length);
            showResult(textView, "send ret:" + send);

            recv();
        } catch (RemoteException e) {
            showResult(textView, e.getMessage());
        }

    }

    private void recv() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isOpen) {
                    try {
                        byte[] recvBytes = new byte[1024];

                        int read = serialPortDriver.recv(recvBytes, recvBytes.length, 1_000);
                        recvBytes = Arrays.copyOfRange(recvBytes, 0, read);

                        if (read > 0) {
                            showResult(textView, HexUtil.bytesToHexString(recvBytes) + "\r\n");
                        }

                        SystemClock.sleep(20);
                    } catch (RemoteException e) {

                    }
                }
            }
        }).start();

    }

    protected void setButtonName() {

    }
}
