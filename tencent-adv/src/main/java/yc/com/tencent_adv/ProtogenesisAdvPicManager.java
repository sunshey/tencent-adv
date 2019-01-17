package yc.com.tencent_adv;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.cfg.MultiProcessFlag;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;
import com.qq.e.comm.util.GDTLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wanglin  on 2019/1/17 09:18.
 * 原生图文广告管理类
 */
public class ProtogenesisAdvPicManager implements OnAdvManagerListener, NativeExpressAD.NativeExpressADListener {


    private Activity mActivity;

    private String mAdvId;
    private String mOriginId;
    private int mAdvCount;
    private List<Integer> mPositions;
    private HashMap<NativeExpressADView, Integer> mAdViewPositionMap = new HashMap<>();

    private OnAdvStateListener advStateListener;

    public ProtogenesisAdvPicManager(Activity activity, String advId, String originId, int advCount, List<Integer> positions, OnAdvStateListener listener) {
        this.mActivity = activity;
        this.mAdvId = advId;
        this.mOriginId = originId;
        this.mAdvCount = advCount;
        this.mPositions = positions;
        this.advStateListener = listener;

    }


    @Override
    public void showAdv() {
        MultiProcessFlag.setMultiProcess(true);
        ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT); // 消息流中用AUTO_HEIGHT // 消息流中用AUTO_HEIGHT
        NativeExpressAD mADManager = new NativeExpressAD(mActivity, adSize, mAdvId, mOriginId, this);
        mADManager.loadAD(mAdvCount);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onNoAD(AdError adError) {

    }

    private NativeExpressADView view;

    @Override
    public void onADLoaded(List<NativeExpressADView> adList) {
        if (adList != null && adList.size() > 0) {

//            if (view != null) {
//                view.destroy();
//            }
//            int position = FIRST_AD_POSITION;
            view = adList.get(0);
//            if (adList.size() > 1) {
//                secondView = adList.get(1);
//            }

            GDTLogger.i("ad load[" + 0 + "]: " + getAdInfo(view));

            for (int i = 0; i < adList.size(); i++) {
                mAdViewPositionMap.put(adList.get(i), mPositions.get(i));
            }


            advStateListener.onNativeExpressShow(mAdViewPositionMap);

        }
    }

    private String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            return infoBuilder.toString();
        }
        return null;
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {
        advStateListener.onNativeExpressDismiss(nativeExpressADView);
    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {
    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }
}
