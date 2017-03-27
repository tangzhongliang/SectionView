package com.leonard.android.views.sectionsample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.leonard.android.views.sectionsample.databinding.MainActivityBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Copyright (C) 2016 RICOH Co.,LTD.
 * All rights reserved.
 */
public class MainActivity extends AppCompatActivity {

    private MainActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);
        initView();
    }

    private void initView() {
        TestListFragment alarmListFragment = TestListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.alarmListContentFrame, alarmListFragment).commitAllowingStateLoss();
    }
}
