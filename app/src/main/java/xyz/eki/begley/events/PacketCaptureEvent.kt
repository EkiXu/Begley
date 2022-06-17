package xyz.eki.begley.events

import xyz.eki.begley.entity.Packet

class PacketCaptureEvent internal constructor(packet: Packet) {
    private var packet:Packet ? = null
    init {
        this.packet = packet
    }
    internal fun getPacket(): Packet? {
        return packet
    }
    fun setPacket(packet: Packet) {
        this.packet = packet
    }
}
