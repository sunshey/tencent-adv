package yc.com.tencent_adv;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2019/1/17 09:20.
 * 广告调度类
 */
public class AdvDispatchManager {

    private static AdvDispatchManager manager;

    private Activity mActivity;

    private OnAdvManagerListener listener;

    private AdvType mAdvType;
    private ViewGroup mContanier;
    private View mView;
    private String mMediaId;
    private String mAdvId;
    private int mNativeCount;
    private List<Integer> mNativePositions;
    private OnAdvStateListener mOnAdvStateListener;


    private AdvDispatchManager() {
    }

    public static AdvDispatchManager getManager() {
        if (manager == null) {
            synchronized (AdvDispatchManager.class) {
                if (manager == null) {
                    manager = new AdvDispatchManager();
                }
            }
        }
        return manager;

    }


    public void init(Activity activity, AdvType advType, ViewGroup container, View view, String mediaId, String advId, int nativeCount, List<Integer> nativePositions, OnAdvStateListener onAdvStateListener) {
        this.mActivity = activity;
        this.mAdvType = advType;
        this.mContanier = container;
        this.mView = view;
        this.mMediaId = mediaId;
        this.mAdvId = advId;
        this.mNativeCount = nativeCount;
        this.mNativePositions = nativePositions;
        this.mOnAdvStateListener = onAdvStateListener;
        dispatchPermisson();

    }

    public void init(Activity activity, AdvType advType, ViewGroup container, View view, String mediaId, String advId, OnAdvStateListener onAdvStateListener) {
        this.init(activity, advType, container, view, mediaId, advId, 0, null, onAdvStateListener);
    }


    private void showAD() {
        if (mAdvType.type == 1) {
            listener = new SplashAdvManager(mActivity, mContanier, mView, mMediaId, mAdvId, mOnAdvStateListener);
        } else if (mAdvType.type == 2) {
            listener = new BannerAdvManager(mActivity, mContanier, mMediaId, mAdvId, mOnAdvStateListener);
        } else if (mAdvType.type == 3) {
            listener = new ProtogenesisAdvPicManager(mActivity, mMediaId, mAdvId, mNativeCount, mNativePositions, mOnAdvStateListener);
        } else if (mAdvType.type == 4) {
            listener = new ProtogenesisAdvVideoManager(mActivity, mContanier, mMediaId, mAdvId, mNativeCount, mOnAdvStateListener);
        }
        listener.showAdv();
    }


    private void dispatchPermisson() {
        if (mActivity == null) {
            throw new RuntimeException("必须先调用init()方法");
        }

        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
            if (mOnAdvStateListener != null) mOnAdvStateListener.onPermissionGranted();
            showAD();
        }
    }


    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {

        if (mActivity == null) {
            throw new RuntimeException("必须先调用init()方法");
        }
        List<String> lackedPermission = new ArrayList<>();


        if (!(mActivity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            if (mOnAdvStateListener != null) mOnAdvStateListener.onPermissionGranted();
            showAD();
        } else {
            if (mOnAdvStateListener != null) mOnAdvStateListener.onPermissionDenyed();
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            mActivity.requestPermissions(requestPermissions, 1024);

        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (hasAllPermissionsGranted(grantResults)) {
            showAD();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(mActivity, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    public void onResume() {
        if (listener != null)
            listener.onResume();
    }

    public void onPause() {
        if (listener != null)
            listener.onPause();
    }

}
