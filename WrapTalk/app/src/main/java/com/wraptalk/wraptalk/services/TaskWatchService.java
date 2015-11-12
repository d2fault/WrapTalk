package com.wraptalk.wraptalk.services;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lk on 15. 11. 11..
 */
public class TaskWatchService{
    Thread thread;
    ActivityManager mActivityManager;
    String currentTask = "";
    private Context context;
    private TaskWatchCallback callback;

    public TaskWatchService(Context context, TaskWatchCallback callback){
        this.context = context;
        this.callback = callback;

        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        String[] activePackages;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            activePackages = getActivePackages();
        } else {
            activePackages = getActivePackagesCompat();
        }
        if (activePackages != null) {
            for (String activePackage : activePackages) {
                currentTask = activePackage;
            }
        }
    }

    public void setCallback(TaskWatchCallback callback){
        this.callback = callback;
    }

    public void start() {
        Log.e("WatchService", "Start Service");
        final Handler handler = new Handler();



        Runnable runThread = new Runnable() {
            @Override
            public void run() {

                String[] activePackages;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                    activePackages = getActivePackages();
                } else {
                    activePackages = getActivePackagesCompat();
                }
                if (activePackages != null) {
                    for (String activePackage : activePackages) {
                        //Log.i("TASK", activePackage);
                        if (!currentTask.equals(activePackage)) {
                            if(callback.TaskCallback(activePackage));
                                currentTask = activePackage;
                        }
                    }
                }
                handler.postDelayed(this,1000);
            }
        };
        handler.post(runThread);

//        thread = new Thread(){
//            @Override
//            public void run(){
//                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(10);
//                List<ActivityManager.RunningAppProcessInfo> app_list = am.getRunningAppProcesses();
//
//                for(int i=0; i<app_list.size(); i++)	{
//
//                    Log.d("TEST", "[" + getApplicationName(app_list.get(i).processName) + "] processName:" + app_list.get(i).processName + ", importance: " + getProcessImportance(app_list.get(i).importance));
//
//                }
//
//                ComponentName topActivity = taskInfo.get(0).topActivity;
//                ComponentName topActivity2 = taskInfo.get(1).topActivity;
//                ComponentName topActivity3;
//                try {
//                    topActivity3 = taskInfo.get(1).topActivity;
//                }catch (IndexOutOfBoundsException e){
//                    topActivity3 = taskInfo.get(1).topActivity;
//                }
//                try {
//                    Log.i("TASK", getProcess());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.e("TEST", topActivity.getPackageName() + "\n" + topActivity2.getPackageName()+ "\n" + topActivity3.getPackageName());

//            }
//        };
//        thread.start();
        //thread.run();
    }



    String[] getActivePackagesCompat() {
        final List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager.getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        final String[] activePackages = new String[1];
        activePackages[0] = componentName.getPackageName();
        return activePackages;
    }

    String[] getActivePackages() {
        final Set<String> activePackages = new HashSet<String>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return activePackages.toArray(new String[activePackages.size()]);
    }
//
//    String getApplicationName(String package_name)	{
//
//        List<PackageInfo> packageinfo = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
//
//        for(int i=0;i< packageinfo .size();i++)	{
//            PackageInfo pi =  packageinfo.get(i);
//            if(package_name.equals(pi.packageName) == true)	{
//                String app_name = pi.applicationInfo.loadLabel(getPackageManager()).toString();
//                return app_name;
//            }
//        }
//        return null;
//    }
//
//    String getProcessImportance(int importance)	{
//
//        if(ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY == importance)		{	return "IMPORTANCE_EMPTY";	}
//
//        if(ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND == importance)	{	return "IMPORTANCE_BACKGROUND";	}
//
//        if(ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE == importance)		{	return "IMPORTANCE_SERVICE";	}
//
//        if(ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE == importance)		{	return "IMPORTANCE_VISIBLE";	}
//
//        if(ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == importance)	{	return "IMPORTANCE_FOREGROUND";	}
//
//        return null;
//
//    }
//
//    private String getProcess() throws Exception {
//        if (Build.VERSION.SDK_INT >= 21) {
//            return getProcessNew();
//        } else {
//            return getProcessOld();
//        }
//    }

//    //API 21 and above
//    private String getProcessNew() throws Exception {
//        String topPackageName = null;
//        UsageStatsManager usage = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
//        long time = System.currentTimeMillis();
//        List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
//        if (stats != null) {
//            SortedMap<Long, UsageStats> runningTask = new TreeMap<Long,UsageStats>();
//            for (UsageStats usageStats : stats) {
//                runningTask.put(usageStats.getLastTimeUsed(), usageStats);
//            }
//            if (runningTask.isEmpty()) {
//                return null;
//            }
//            topPackageName =  runningTask.get(runningTask.lastKey()).getPackageName();
//        }
//        return topPackageName;
//    }
//
//    //API below 21
//    @SuppressWarnings("deprecation")
//    private String getProcessOld() throws Exception {
//        String topPackageName = null;
//        ActivityManager activity = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTask = activity.getRunningTasks(1);
//        if (runningTask != null) {
//            ActivityManager.RunningTaskInfo taskTop = runningTask.get(0);
//            ComponentName componentTop = taskTop.topActivity;
//            topPackageName = componentTop.getPackageName();
//        }
//        return topPackageName;
//    }


}
