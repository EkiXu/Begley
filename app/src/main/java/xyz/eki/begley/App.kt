package xyz.eki.begley

import android.app.Application
import android.content.Context
import com.github.megatronking.netbare.NetBare
import com.github.megatronking.netbare.NetBareUtils
import com.github.megatronking.netbare.ssl.JKS
import me.weishu.reflection.Reflection

class App : Application() {
    companion object {
        const val CRT_ALIAS = "Begley"
        const val CRT_PASSWORD = "Begley_test"

        private lateinit var sInstance: App

        fun getInstance(): App {
            return sInstance
        }
    }
    private lateinit var myCRT: JKS

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        myCRT = JKS(this, CRT_ALIAS, CRT_PASSWORD.toCharArray(), CRT_ALIAS, CRT_ALIAS,
                CRT_ALIAS, CRT_ALIAS, CRT_ALIAS)

        NetBare.get().attachApplication(this,BuildConfig.DEBUG)
    };

    fun getCRT(): JKS{
        return myCRT;
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // On android Q, we can't access Java8EngineWrapper with reflect.
        if (NetBareUtils.isAndroidQ()) {
            Reflection.unseal(base);
        }
    }
}