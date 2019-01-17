package yc.com.tencent_adv;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * Created by wanglin  on 2019/1/17 09:18.
 * 原生视频广告管理类
 */
public class ProtogenesisAdvVideoManager implements OnAdvManagerListener, NativeExpressAD.NativeExpressADListener {

    private Activity mActivity;
    private String mAdvId;
    private String mNativeId;
    private int mNativeCount;

    private ViewGroup mContainer;
    private static final String TAG = "ProtogenesisAdvVideoMan";

    private OnAdvStateListener mListener;

    public ProtogenesisAdvVideoManager(Activity activity, ViewGroup viewGroup, String advId, String nativeId, int nativeCount, OnAdvStateListener onAdvStateListener) {
        this.mActivity = activity;
        this.mAdvId = advId;
        this.mNativeId = nativeId;
        this.mNativeCount = nativeCount;
        this.mContainer = viewGroup;
        this.mListener = onAdvStateListener;
    }


    @Override
    public void showAdv() {
        NativeExpressAD nativeExpressAD = new NativeExpressAD(mActivity, new ADSize(340, ADSize.AUTO_HEIGHT), mAdvId, mNativeId, this); // 传入Activity
        // 注意：如果您在联盟平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        nativeExpressAD.loadAD(mNativeCount);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    // 2.设置监听器，监听广告状态
    @Override
    public void onNoAD(AdError error) {
        Log.i("AD_DEMO", String.format("onADError, error code: %d, error msg: %s", error.getErrorCode(), error.getErrorMsg()));
    }


    @Override
    public void onADLoaded(List<NativeExpressADView> adList) {
        Log.i(TAG, "onADLoaded: " + adList.size());
        // 释放前一个 NativeExpressADView 的资源

        if (adList.size() > 0) {
            // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
            NativeExpressADView nativeExpressADView = adList.get(0);
            if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                nativeExpressADView.setMediaListener(mediaListener);
            }
            nativeExpressADView.render();
            if (mContainer.getChildCount() > 0) {
                mContainer.removeAllViews();
            }

            // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
            mContainer.addView(nativeExpressADView);
        }

    }

    @Override
    public void onRenderFail(NativeExpressADView adView) {
        Log.i(TAG, "onRenderFail");
    }

    @Override
    public void onRenderSuccess(NativeExpressADView adView) {
        Log.i(TAG, "onRenderSuccess");
    }

    @Override
    public void onADExposure(NativeExpressADView adView) {
        Log.i(TAG, "onADExposure");
    }

    @Override
    public void onADClicked(NativeExpressADView adView) {
        Log.i(TAG, "onADClicked");
    }

    @Override
    public void onADClosed(NativeExpressADView adView) {
        Log.i(TAG, "onADClosed");
    }

    @Override
    public void onADLeftApplication(NativeExpressADView adView) {
        Log.i(TAG, "onADLeftApplication");
    }

    @Override
    public void onADOpenOverlay(NativeExpressADView adView) {
        Log.i(TAG, "onADOpenOverlay");
    }

    @Override
    public void onADCloseOverlay(NativeExpressADView adView) {
        Log.i(TAG, "onADCloseOverlay");
    }


    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {
//            Log.i(TAG, "onVideoInit: "
//                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoLoading");
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
            Log.i(TAG, "onVideoReady");
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {
//            Log.i(TAG, "onVideoStart: "
//                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {
//            Log.i(TAG, "onVideoPause: "
//                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
//            Log.i(TAG, "onVideoComplete: "
//                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
            Log.i(TAG, "onVideoError");
        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageOpen");
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageClose");
        }
    };
}
