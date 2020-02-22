package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.printer.FontFamily;
import com.morefun.yapi.device.printer.MulPrintStrEntity;
import com.morefun.yapi.device.printer.OnPrintListener;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintActivity extends BaseActivity {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_button);
        ButterKnife.bind(this);

        setButtonName();

    }

    @OnClick({R.id.button})
    public void onClick() {
        showResult(textView, getString(R.string.msg_printing));
        print();
    }

    private void print() {
        try {
            int fontSize = FontFamily.MIDDLE;
            Bundle config = new Bundle();

            List<MulPrintStrEntity> list = new ArrayList<>();
            MulPrintStrEntity entity = new MulPrintStrEntity("POS purchase order", fontSize);
            entity.setMarginX(50);
            entity.setUnderline(true);
            entity.setYspace(30);
            list.add(entity);
            MulPrintStrEntity mulPrintStrEntity = new MulPrintStrEntity("=====================", fontSize);
            list.add(mulPrintStrEntity);
            list.add(new MulPrintStrEntity("MERCHANT NAME：Demo shop name", fontSize));
            list.add(new MulPrintStrEntity("MERCHANT NO.：20321545656687", fontSize));
            list.add(new MulPrintStrEntity("TERMINAL NO.：25689753", fontSize));
            list.add(new MulPrintStrEntity("CARD NUMBER", fontSize));
            list.add(new MulPrintStrEntity("62179390*****3426", fontSize));
            list.add(new MulPrintStrEntity("TRANS TYPE", fontSize));
            list.add(new MulPrintStrEntity("SALE", fontSize));
            list.add(new MulPrintStrEntity("EXP DATE：2029", fontSize));
            list.add(new MulPrintStrEntity("BATCH NO：000012", fontSize));
            list.add(new MulPrintStrEntity("VOUCHER NO：000001", fontSize));
            list.add(new MulPrintStrEntity("DATE/TIME：2016-05-23 16:50:32", fontSize));
            list.add(new MulPrintStrEntity("AMOUNT", fontSize));
            list.add(new MulPrintStrEntity("==========================", fontSize));
            //feed pager one line
            list.add(new MulPrintStrEntity("\n", fontSize));
            list.add(new MulPrintStrEntity("CARD HOLDER SIGNATURE", fontSize));
            list.add(new MulPrintStrEntity("\n", fontSize));
            list.add(new MulPrintStrEntity("--------------------------------------", fontSize));
            list.add(new MulPrintStrEntity(" I ACKNOWLEDGE	SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES", fontSize));
            list.add(new MulPrintStrEntity(" MERCHANT COPY ", fontSize));
            list.add(new MulPrintStrEntity("---X---X---X---X---X--X--X--X--X--X--\n", fontSize));
            DeviceHelper.getPrinter().printStr(list, new OnPrintListener.Stub() {
                @Override
                public void onPrintResult(int result) throws RemoteException {
                    showResult(textView, result == ServiceResult.Success ? getString(R.string.msg_succ) : getString(R.string.msg_fail));
                }
            }, config);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_print));
    }

}