package xyz.eki.begley.entity

class Packet constructor(url:String="",dstIp:String="255.255.255.255",dstPort:Int=0,payload:ByteArray=byteArrayOf(0)){
    var dstIP: String = dstIp
    var dstPort: Int = dstPort
    var url:String = url
    var payload: ByteArray = payload
}