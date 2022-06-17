package xyz.eki.begley.interceptor

import android.content.Context
import com.github.megatronking.netbare.gateway.*
import com.github.megatronking.netbare.http.HttpInterceptorFactory
import com.github.megatronking.netbare.ip.IpInterceptor
import com.github.megatronking.netbare.ip.IpInterceptorFactory
import org.greenrobot.eventbus.EventBus
import xyz.eki.begley.entity.Packet
import xyz.eki.begley.events.PacketCaptureEvent
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.Throws

class IPPacketInterceptor : IpInterceptor {
    companion object {
        const val TAG = "URL"

        fun createFactory(): IpInterceptorFactory {
            return IpInterceptorFactory { IPPacketInterceptor() }
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: RequestChain, buffer: ByteBuffer) {
        EventBus.getDefault().post(PacketCaptureEvent(Packet(dstIp = chain.request().ip(), dstPort = chain.request().port())))
        chain.process(buffer)
    }

    @Throws(IOException::class)
    override fun intercept(chain: ResponseChain, buffer: ByteBuffer) {
        chain.process(buffer)
    }

    override fun onRequestFinished(request: Request) {}
    override fun onResponseFinished(response: Response) {}
}