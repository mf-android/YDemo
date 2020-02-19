package com.morefun.ysdk.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.model.Catalog;
import com.morefun.ysdk.sample.utils.SweetDialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private ListView lvCatalog;
    private List<Catalog> catalogs;
    private ArrayAdapter<Catalog> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        lvCatalog = (ListView) findViewById(R.id.lvCatalog);
        init();
        setTitle();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }

    private void setTitle() {
        this.setTitle(getString(R.string.activity_title_mid) + "  V" + getVersionName(this));
    }

    private void init() {
        addCatalog();
        adapter = new ArrayAdapter<Catalog>(this, R.layout.menu_list_item, catalogs) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.menu_list_item, null);

                Catalog catalog = getItem(position);
                TextView name = (TextView) view.findViewById(R.id.value);
                TextView index = (TextView) view.findViewById(R.id.index);

                name.setText(catalog.name);
                index.setText(position + 1 + ".");
                return view;
            }

            @Override
            public int getCount() {
                return catalogs.size();
            }

        };

        lvCatalog.setAdapter(adapter);

        lvCatalog.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!catalogs.get(position).name.equals(getString(R.string.login_devices)) && !DeviceHelper.getLoginSuccess()) {
                    SweetDialogUtil.showError(MainActivity.this, getString(R.string.please_login_first));
                    return;
                }

                if (catalogs.get(position).cls != null) {
                    startActivity(new Intent(MainActivity.this, catalogs.get(position).cls));
                }
            }
        });
    }

    private void addCatalog() {
        catalogs = new ArrayList<Catalog>();

        catalogs.add(new Catalog(getString(R.string.login_devices), LoginActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_mag_card_reader), MagCardActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_ic_card_reader), IcCardActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_pboc), PbocActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_pinkeybord), PinpadActivity.class));
        catalogs.add(new Catalog(getString(R.string.aid_manager), AidManagerActivity.class));
        catalogs.add(new Catalog(getString(R.string.puk_manager), CapkManagerActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_led), LedActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_print), PrintActivity.class));
        catalogs.add(new Catalog(getString(R.string.device_info), DeviceInfoActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_apdu), CpuCardActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_m1card), M1CardActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_felica), FelicaCardActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_scanner), ScannerActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_sign), SignActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_serialport), SerialPortActivity.class));
        catalogs.add(new Catalog(getString(R.string.menu_beep), BeepActivity.class));

    }

}