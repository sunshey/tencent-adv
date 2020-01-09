package yc.com.tencent_adv;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

/**
 * Created by wanglin  on 2019/1/17 09:17.
 * 开屏广告管理类
 */
public class SplashAdvManager implements OnAdvManagerListener, SplashADListener {

    private Activity mActivity;
    private long featchAdTime;

    private ViewGroup mContainer;
    private View mView;
    private String mMediaId;
    private String mAdvId;
    private OnAdvStateListener mListener;

    private static final int Time = 2500;
    private boolean canJump;

    public SplashAdvManager(Activity activity, ViewGroup container, View view, String mediaId, String advId, OnAdvStateListener onAdvStateListener) {
        this.mActivity = activity;
        this.mContainer = container;
        this.mView = view;
        this.mMediaId = mediaId;
        this.mAdvId = advId;
        this.mListener = onAdvStateListener;
    }


    @Override
    public void showAdv() {
        featchAdTime = System.currentTimeMillis();
        SplashAD splashAD = new SplashAD(mActivity, mContainer, mView, mMediaId, mAdvId, this, 0);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity(Time);
            }
        });
    }

    @Override
    public void onResume() {
        if (canJump) {
            switchActivity(Time);
        }
        canJump = true;
    }

    @Override
    public void onPause() {
        canJump = false;
    }

    @Override
    public void onADDismissed() {
        if (canJump) {
            switchActivity(Time);
        } else {
            canJump = true;
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        long alreadyDelayMills = System.currentTimeMillis() - featchAdTime;//从拉广告开始到onNoAD已经消耗了多少时间
        switchActivity(alreadyDelayMills);
    }

    @Override
    public void onADPresent() {
        mListener.onShow();
    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADTick(long l) {
        if (mView instanceof TextView) {
            TextView textView = (TextView) mView;
            textView.setText(String.format(mActivity.getString(R.string.click_to_skip),
                    Math.round(l / 1000f)));
        }

    }

    @Override
    public void onADExposure() {

    }


    private void switchActivity(long delay) {

        long delayTime = 0;
        if (delay < Time) {
            delayTime = Time - delay;
        }

        mListener.onDismiss(delayTime);

    }
}
