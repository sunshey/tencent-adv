package yc.com.tencent_adv;

import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.Map;

/**
 * Created by wanglin  on 2019/1/17 09:59.
 */
public interface OnAdvStateListener {

    void onShow();

    void onDismiss(long delayTime);

    void onError();

    void onNativeExpressDismiss(NativeExpressADView view);

    void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas);

}
