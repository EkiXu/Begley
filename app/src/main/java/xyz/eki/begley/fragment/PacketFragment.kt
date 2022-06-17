package xyz.eki.begley.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.flow
import xyz.eki.begley.R
import xyz.eki.begley.databinding.FragmentPacketBinding
import xyz.eki.begley.entity.FlowListViewModel
import xyz.eki.begley.entity.adapter.SectionsPagerAdapter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PacketFragment : Fragment() {



    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PacketFragment {
            return PacketFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private var _binding: FragmentPacketBinding? = null
    val args: PacketFragmentArgs by navArgs()
    private val flowListViewModel: FlowListViewModel by navGraphViewModels(R.id.nav_graph)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPacketBinding.inflate(inflater, container, false)
        val payload = flowListViewModel.get(args.packetIndex)?.payload
        if(payload!=null) {
            _binding!!.payload.text = String(payload)
        }else _binding!!.payload.text = "failed"
//        val sectionsPagerAdapter = SectionsPagerAdapter(context, supportFragmentManager)
//        val viewPager: ViewPager = binding.viewPager
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = binding.tabs
//        tabs.setupWithViewPager(viewPager)

        return binding.root

    }

    override fun onViewCreated(view  : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_PacketFragment_to_FlowFragment)
//
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}