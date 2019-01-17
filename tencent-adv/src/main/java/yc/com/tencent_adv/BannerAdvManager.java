package yc.com.tencent_adv;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;

/**
 * Created by wanglin  on 2019/1/17 09:17.
 * banner广告管理类
 */
public class BannerAdvManager implements OnAdvManagerListener {

    private Activity mActivity;
    private ViewGroup mContanier;
    private String mAdvId;
    private String mBannerId;
    private OnAdvStateListener listener;

    public BannerAdvManager(Activity activity, ViewGroup viewGroup, String advId, String bannerId,OnAdvStateListener stateListener) {
        this.mActivity = activity;
        this.mContanier= viewGroup;
        this.mAdvId= advId;
        this.mBannerId= bannerId;
        this.listener= stateListener;
    }


    @Override
    public void showAdv() {
        // 创建 Banner 广告 AdView 对象
        // appId : 在 http://e.qq.com/dev/ 能看到的 app 唯一字符串
        // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey

//        if (banner != null) {
//            banner.destroy();
//        }
        BannerView banner = new BannerView(mActivity, ADSize.BANNER, mAdvId, mBannerId);
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);
        banner.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode());
//                banner.loadAD();
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }

            @Override
            public void onADClicked() {
                super.onADClicked();
                Log.e("AD_DEMO", "onADClicked: ");
            }
        });
        mContanier.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据   */
        banner.loadAD();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
