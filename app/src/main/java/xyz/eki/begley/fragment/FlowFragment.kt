package xyz.eki.begley.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xyz.eki.begley.R
import xyz.eki.begley.databinding.FragmentFlowBinding
import xyz.eki.begley.entity.FlowListViewModel
import xyz.eki.begley.entity.adapter.FlowListAdapter
import xyz.eki.begley.events.PacketCaptureEvent


/**
 * A fragment representing a list of Items.
 */
class FlowFragment : Fragment() {

    private var columnCount = 1
    private val flowListViewModel: FlowListViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var binding: FragmentFlowBinding
    //private lateinit var flowReceiver: FlowReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }


    }

//    class FlowReceiver(val flowListViewModel: FlowListViewModel) : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val bundle = intent.extras
//            val data = bundle?.getString("data")
//            if (data != null) {
//                val packet = Packet(dstIp = data)
//                flowListViewModel.addPacket(packet)
//            }
//            Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPacketReceive(packetCaptureEvent: PacketCaptureEvent ) {
        packetCaptureEvent.getPacket()?.let { flowListViewModel.addPacket(it) }
    }

    private fun subscribeFlow() {
        flowListViewModel.flowList.observe(viewLifecycleOwner, Observer { data ->
            // Perform an action with the latest item data
            val adapter = binding.list.adapter as FlowListAdapter
            adapter.replaceData(data)
            adapter.notifyDataSetChanged()
            //Toast.makeText(context, data.last().srcIP, Toast.LENGTH_SHORT).show()
            //binding.list.scrollToPosition(adapter.getItemCount()-1)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlowBinding.inflate(layoutInflater)

        val view = inflater.inflate(R.layout.fragment_flow, container, false)

        binding.list.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }

        val mAdapter = FlowListAdapter(mutableListOf())

        mAdapter.setOnItemClickListener(object : FlowListAdapter.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                if (view != null) {
                    val direction = FlowFragmentDirections.actionFlowFragmentToPacketFragment()
                    direction.packetIndex = position

                    findNavController(view).navigate(direction)
                }
                Toast.makeText(context, "这是数据包" + position, Toast.LENGTH_LONG)
                    .show()
            }
        })

        binding.list.adapter = mAdapter



        subscribeFlow()

//        val mFlowList : MutableList<Packet> = mutableListOf()
//
//        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
//                adapter = FlowListAdapter(mFlowList)
//
//            }
//        }
        EventBus.getDefault().register(this)

//        //动态注册广播接收器
//        flowReceiver = FlowReceiver(flowListViewModel)
//
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("xyz.eki.begley.commuication.flow")
//        context?.let {
//            LocalBroadcastManager.getInstance(it)
//                .registerReceiver(flowReceiver, intentFilter)
//        };

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
//        //取消广播接收器
//        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(flowReceiver) }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            FlowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}