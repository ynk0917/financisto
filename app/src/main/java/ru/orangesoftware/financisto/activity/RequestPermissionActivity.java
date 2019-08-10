package ru.orangesoftware.financisto.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import ru.orangesoftware.financisto.R;
import ru.orangesoftware.financisto.utils.MyPreferences;

@EActivity(R.layout.activity_request_permissions)
public class RequestPermissionActivity extends Activity {

    @Extra("requestedPermission")
    String requestedPermission;

    @ViewById(R.id.toggleCamera)
    SwitchCompat toggleCamera;

    @ViewById(R.id.toggleCameraWrap)
    ViewGroup toggleCameraWrap;

    @ViewById(R.id.toggleGetAccounts)
    SwitchCompat toggleGetAccounts;

    @ViewById(R.id.toggleGetAccountsWrap)
    ViewGroup toggleGetAccountsWrap;

    @ViewById(R.id.toggleSms)
    SwitchCompat toggleSms;

    @ViewById(R.id.toggleSmsWrap)
    ViewGroup toggleSmsWrap;

    @ViewById(R.id.toggleWriteStorage)
    SwitchCompat toggleWriteStorage;

    @ViewById(R.id.toggleWriteStorageWrap)
    ViewGroup toggleWriteStorageWrap;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        checkPermissions();
    }

    @AfterViews
    public void initViews() {
        checkPermissions();
    }

    @Click(R.id.toggleCamera)
    public void onGrantCamera() {
        requestPermission(Manifest.permission.CAMERA, toggleCamera);
    }

    @Click(R.id.toggleGetAccounts)
    public void onGrantGetAccounts() {
        requestPermission(Manifest.permission.GET_ACCOUNTS, toggleGetAccounts);
    }

    @Click(R.id.toggleSms)
    public void onGrantSms() {
        requestPermission(Manifest.permission.RECEIVE_SMS, toggleSms);
    }

    @Click(R.id.toggleWriteStorage)
    public void onGrantWriteStorage() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, toggleWriteStorage);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(MyPreferences.switchLocale(base));
    }

    private void checkPermissions() {
        disableToggleIfGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, toggleWriteStorage,
                toggleWriteStorageWrap);
        disableToggleIfGranted(Manifest.permission.GET_ACCOUNTS, toggleGetAccounts, toggleGetAccountsWrap);
        disableToggleIfGranted(Manifest.permission.CAMERA, toggleCamera, toggleCameraWrap);
        disableToggleIfGranted(Manifest.permission.RECEIVE_SMS, toggleSms, toggleSmsWrap);
    }

    private void disableToggleIfGranted(String permission, CompoundButton toggleButton, ViewGroup wrapLayout) {
        if (isGranted(permission)) {
            toggleButton.setChecked(true);
            toggleButton.setEnabled(false);
            wrapLayout.setBackgroundResource(0);
        } else if (permission.equals(requestedPermission)) {
            wrapLayout.setBackgroundResource(R.drawable.highlight_border);
        }
    }

    private boolean isGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, CompoundButton toggleButton) {
        toggleButton.setChecked(false);
        ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
    }

}
