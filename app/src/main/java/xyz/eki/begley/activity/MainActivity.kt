package xyz.eki.begley.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.github.megatronking.netbare.NetBare
import com.github.megatronking.netbare.NetBareConfig
import com.github.megatronking.netbare.NetBareListener
import com.github.megatronking.netbare.http.HttpInterceptorFactory
import com.github.megatronking.netbare.ssl.JKS
import com.google.android.material.snackbar.Snackbar
import xyz.eki.begley.App
import xyz.eki.begley.R
import xyz.eki.begley.databinding.ActivityMainBinding
import xyz.eki.begley.interceptor.HttpPacketInterceptor

import java.io.IOException


class MainActivity : AppCompatActivity(),NetBareListener {
    companion object {
        private const val REQUEST_CODE_PREPARE = 1
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNetBare : NetBare

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        mNetBare = NetBare.get()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        binding.fab.setOnClickListener { view ->
            if (mNetBare.isActive) {
                Snackbar.make(view, "停止抓包中", Snackbar.LENGTH_LONG).show()
                mNetBare.stop()
            } else{
                Snackbar.make(view, "开始抓包", Snackbar.LENGTH_LONG).show()
                prepareNetBare()
            }
        }

        mNetBare.registerNetBareListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun prepareNetBare() {
        // 安装自签证书
        //Log.i("DEBUG","Here");
        if (!JKS.isInstalled(this, App.CRT_ALIAS)) {
            try {
                JKS.install(this, App.CRT_ALIAS, App.CRT_ALIAS)
            } catch(e : IOException) {
                // 安装失败
                e.printStackTrace()
            }
            return
        }
        // 配置VPN
        val intent = NetBare.get().prepare()
        if (intent != null) {
            startActivityForResult(intent, REQUEST_CODE_PREPARE)
            return
        }
        // 启动NetBare服务
        mNetBare.start(NetBareConfig.defaultHttpConfig(
            App.getInstance().getCRT(),
                interceptorFactories()))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PREPARE) {
            prepareNetBare()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mNetBare.unregisterNetBareListener(this)
        mNetBare.stop()
    }

    override fun onServiceStarted() {
        runOnUiThread{
            binding.fab.setImageResource(R.drawable.stop)
        }
    }

    override fun onServiceStopped() {

        runOnUiThread{
            binding.fab.setImageResource(R.drawable.start)
        }
    }

    private fun interceptorFactories() : List<HttpInterceptorFactory> {
        // 拦截器范例1：打印请求url
        val interceptor1 = HttpPacketInterceptor.createFactory()
        // 注入器范例1：替换百度首页logo
//        val injector1 = HttpInjectInterceptor.createFactory(BaiduLogoInjector())
//        // 注入器范例2：修改发朋友圈定位
//        val injector2 = HttpInjectInterceptor.createFactory(WechatLocationInjector())
        // 可以添加其它的拦截器，注入器
        // ...
        return listOf(interceptor1)
    }

//    override fun addPacket(packet: Packet) {
//
//    }
}