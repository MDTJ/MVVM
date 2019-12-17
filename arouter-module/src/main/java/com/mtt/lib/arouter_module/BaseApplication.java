package com.mtt.lib.arouter_module;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mtt.lib.base_module.BuildConfig;
import com.mtt.lib.base_module.data.BaseSp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mtt on 2019-11-15
 */

public class BaseApplication extends Application {
    private static Context context;
    private static Activity cruActivity;

    public static Context getContext() {
        if (context == null) {
            return new BaseApplication();
        }
        return context;
    }
    public static Activity getCruActivity(){
        return cruActivity;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                cruActivity=activity;
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (isAppMainProcess()) {
            BaseSp.getInstance().init(this);
            initArouter();

        }
        context = getApplicationContext();
    }


    private void initArouter() {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this);
        //注入 provider
        ARouter.getInstance().inject(this);

    }

    /**
     * 判断是不是UI主进程
     */
    public boolean isAppMainProcess() {
        try {
            int pid = android.os.Process.myPid();
            String process = getAppNameByPID(this, pid);
            if (TextUtils.isEmpty(process)) {
                return true;
            } else if (BuildConfig.ID.equalsIgnoreCase(process)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }


    /**
     * 根据Pid得到进程名
     */
    public String getAppNameByPID(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
