package yc.com.tencent_adv;

/**
 * Created by wanglin  on 2019/1/17 09:30.
 */
public enum AdvType {
    SPLASH(1), BANNER(2), ORIGIN_PIC(3), ORIGIN_VIDEO(4);

    public int type;

    AdvType(int i) {
        this.type = i;
    }
}
