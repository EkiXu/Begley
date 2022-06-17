package xyz.eki.begley.entity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.eki.begley.R
import xyz.eki.begley.databinding.FlowListItemBinding
import xyz.eki.begley.entity.Packet

/**
 * [RecyclerView.Adapter] that can display a [Packet].
 * TODO: Replace the implementation with code for your data type.
 */
class FlowListAdapter(
    private var values: MutableList<Packet>
) : RecyclerView.Adapter<FlowListAdapter.ViewHolder>() {

    companion object{
        private fun getPayloadSummary(payload:ByteArray):String{
            return String(payload)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    private lateinit var mOnItemClickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FlowListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun replaceData(listDatas: MutableList<Packet>) {
        this.values = listDatas
    }

    fun addPacket(packet: Packet){
        if(values == null){
            values = mutableListOf(packet)
        }else{
            values.add(packet);
        }
        notifyItemInserted(values.size-1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.titleText.text = "${item.dstIP}:${item.dstPort}"
        holder.contentView.text = item.url
        holder.appIconView.setImageResource(R.mipmap.ic_launcher_round)


        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(View.OnClickListener { view ->
                mOnItemClickListener.onItemClick(
                    view,
                    position
                )
            })
        }
        //通过为条目设置点击事件触发回调
//        if (mOnItemClickLitener != null) {
//            holder.itemView.setOnClickListener(object : View.OnClickListener {
//                fun onClick(view: View?) {
//                    mOnItemClickLitener.onItemClick(view, position)
//                }
//            })
//        }
    }

    override fun getItemCount(): Int = values.size


    inner class ViewHolder(binding: FlowListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleText: TextView = binding.title
        val contentView: TextView = binding.description
        val appIconView: ImageView = binding.icon

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}