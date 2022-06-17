package xyz.eki.begley.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xyz.eki.begley.events.PacketCaptureEvent

class FlowListViewModel : ViewModel() {
    private val mutableFlowList = MutableLiveData<MutableList<Packet>>()
    val flowList: LiveData<MutableList<Packet>> get() = mutableFlowList

    fun addPacket(packet: Packet) {
        if(mutableFlowList.value != null){
            mutableFlowList.value!!.add(packet)
        }else{
            mutableFlowList.value = mutableListOf(packet)
        }

    }
    fun setFlowList(flowList:MutableList<Packet>){
        mutableFlowList.value = flowList
    }

    fun get(packetIndex:Int): Packet? {
        return mutableFlowList.value?.get(packetIndex) ?: null
    }
}