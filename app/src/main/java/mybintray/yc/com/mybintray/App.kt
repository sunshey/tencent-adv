package mybintray.yc.com.mybintray

import android.app.Application
import yc.com.toutiao_adv.TTAdManagerHolder

/**
 *
 * Created by suns  on 2020/1/6 09:45.
 */
class App :Application(){
    override fun onCreate() {
        super.onCreate()
        TTAdManagerHolder.init(this,"5044635")
    }
}