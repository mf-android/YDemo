package com.morefun.ysdk.sample.device;

import android.annotation.SuppressLint;
import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.card.cpu.CPUCardHandler;
import com.morefun.yapi.card.industry.IndustryCardHandler;
import com.morefun.yapi.card.mifare.M1CardHandler;
import com.morefun.yapi.device.beeper.Beeper;
import com.morefun.yapi.device.led.LEDDriver;
import com.morefun.yapi.device.logrecorder.LogRecorder;
import com.morefun.yapi.device.pinpad.PinPad;
import com.morefun.yapi.device.printer.MultipleAppPrinter;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.mag.MagCardReader;
import com.morefun.yapi.device.scanner.InnerScanner;
import com.morefun.yapi.device.serialport.SerialPortDriver;
import com.morefun.yapi.emv.EmvHandler;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ysdk.sample.MyApplication;


public class DeviceHelper {

    private final static String TAG = DeviceHelper.class.getName();

    private static PinPad pinpad;
    private static IccCardReader iccCardReader;
    private static CPUCardHandler cpuCardHandler;
    private static M1CardHandler m1CardHandler;
    private static IndustryCardHandler industryCardHandler;
    private static MagCardReader magCardReader;
    private static LEDDriver ledDriver;
    private static MultipleAppPrinter printer;
    private static SerialPortDriver serialPortDriver;
    private static Beeper beeper;
    private static EmvHandler emvHandler;
    private static InnerScanner innerScanner;
    private static LogRecorder logRecorder;

    private static MyApplication application;
    private static boolean isLogin = false;

    @SuppressLint("NewApi")
    public static void initDevices(MyApplication application) throws RemoteException, RemoteException {
        DeviceHelper.application = application;

        if (application == null) {
            return;
        }

        if (application.getDeviceService() != null) {
            try {
                pinpad = getPinpad();
                magCardReader = getMagCardReader();
                ledDriver = getLedDriver();
                printer = getPrinter();
                beeper = getBeeper();
                emvHandler = getEmvHandler();
                innerScanner = getInnerScanner();
                logRecorder = getLogRecorder();

            } catch (RemoteException e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            application.bindDeviceService();
            reset();
        }
    }

    public static DeviceServiceEngine getDeviceService() {
        return application.getDeviceService();
    }

    public static void checkState() throws RemoteException {
        if (application == null) {
            Log.d(TAG, "============checkState application == null");
            throw new RemoteException("Please restart the application.");
        }

        if (application.getDeviceService() == null) {
            application.bindDeviceService();
            reset();
            throw new RemoteException("Device service connection failed, please try again later.");
        }
    }


    @SuppressLint("NewApi")
    public static PinPad getPinpad() throws RemoteException {
        if (pinpad == null) {
            checkState();
            try {
                return application.getDeviceService().getPinPad();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return pinpad;
        }
    }


    @SuppressLint("NewApi")
    public static IccCardReader getIccCardReader(int cardType) throws RemoteException {
        if (iccCardReader == null) {
            checkState();
            try {
                return application.getDeviceService().getIccCardReader(cardType);
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return iccCardReader;
        }
    }


    @SuppressLint("NewApi")
    public static CPUCardHandler getCpuCardHandler(IccCardReader iccCardReader) throws RemoteException {
        if (cpuCardHandler == null) {
            checkState();
            try {
                return application.getDeviceService().getCPUCardHandler(iccCardReader);
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return cpuCardHandler;
        }
    }

    @SuppressLint("NewApi")
    public static M1CardHandler getM1CardHandler(IccCardReader iccCardReader) throws RemoteException {
        if (m1CardHandler == null) {
            checkState();
            try {
                return application.getDeviceService().getM1CardHandler(iccCardReader);
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return m1CardHandler;
        }
    }

    @SuppressLint("NewApi")
    public static IndustryCardHandler getIndustryCardHandler(IccCardReader iccCardReader) throws RemoteException {
        if (industryCardHandler == null) {
            checkState();
            try {
                return application.getDeviceService().getIndustryCardHandler(iccCardReader);
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return industryCardHandler;
        }
    }

    @SuppressLint("NewApi")
    public static MagCardReader getMagCardReader() throws RemoteException {
        if (magCardReader == null) {
            checkState();
            try {
                return application.getDeviceService().getMagCardReader();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return magCardReader;
        }
    }

    @SuppressLint("NewApi")
    public static LEDDriver getLedDriver() throws RemoteException {
        if (ledDriver == null) {
            checkState();
            try {
                return application.getDeviceService().getLEDDriver();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return ledDriver;
        }
    }

    @SuppressLint("NewApi")
    public static MultipleAppPrinter getPrinter() throws RemoteException {
        if (printer == null) {
            checkState();
            try {
                return application.getDeviceService().getMultipleAppPrinter();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return printer;
        }
    }

    @SuppressLint("NewApi")
    public static SerialPortDriver getSerialPortDriver(int port) throws RemoteException {
        if (serialPortDriver == null) {
            checkState();
            try {
                return application.getDeviceService().getSerialPortDriver(port);
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return serialPortDriver;
        }
    }

    @SuppressLint("NewApi")
    public static Beeper getBeeper() throws RemoteException {
        if (beeper == null) {
            checkState();
            try {
                return application.getDeviceService().getBeeper();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return beeper;
        }
    }

    @SuppressLint("NewApi")
    public static EmvHandler getEmvHandler() throws RemoteException {
        if (emvHandler == null) {
            checkState();
            try {
                return application.getDeviceService().getEmvHandler();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return emvHandler;
        }
    }

    @SuppressLint("NewApi")
    public static InnerScanner getInnerScanner() throws RemoteException {
        if (innerScanner == null) {
            checkState();
            try {
                return application.getDeviceService().getInnerScanner();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return innerScanner;
        }
    }

    @SuppressLint("NewApi")
    public static LogRecorder getLogRecorder() throws RemoteException {
        if (logRecorder == null) {
            checkState();
            try {
                return application.getDeviceService().getLogRecorder();
            } catch (RemoteException e) {
                throw new RemoteException("PinPad service acquisition failed, please try again later.");
            }
        } else {
            return logRecorder;
        }
    }


    public static void reset() {
        pinpad = null;
        iccCardReader = null;
        cpuCardHandler = null;
        m1CardHandler = null;
        industryCardHandler = null;
        magCardReader = null;
        ledDriver = null;
        printer = null;
        serialPortDriver = null;
        beeper = null;
        emvHandler = null;
        innerScanner = null;
        logRecorder = null;

    }

    public static void setLoginFlag(boolean b) {
        isLogin = b;
    }

    public static boolean getLoginSuccess() {
        return isLogin;
    }
}
