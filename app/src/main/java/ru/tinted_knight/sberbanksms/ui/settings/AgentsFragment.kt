package ru.tinted_knight.sberbanksms.ui.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.ui.adapters.AgentsRecyclerViewAdapter
import ru.tinted_knight.sberbanksms.ui.adapters.DividerItemDecoration
import ru.tinted_knight.sberbanksms.viewmodel.AgentsViewModel

class AgentsFragment : Fragment(), AgentsRecyclerViewAdapter.ListItemClickListener {

    private var mParam1: String? = null

    private val adapter = AgentsRecyclerViewAdapter(this)

    private var listener: OnAgentsFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_agents, container, false)
        val rvAgents = root.findViewById<RecyclerView>(R.id.rvAgents)
        rvAgents.adapter = adapter
        rvAgents.layoutManager = LinearLayoutManager(activity)
        rvAgents.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST))
//        rvAgents.isVerticalScrollBarEnabled = true
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val agentsViewModel = ViewModelProviders.of(this).get(AgentsViewModel::class.java)

        registerObservers(agentsViewModel)
    }

    private fun registerObservers(agentsViewModel: AgentsViewModel) {
        agentsViewModel.agents.observe(this, Observer { agents ->
            adapter.data = agents!!
            adapter.notifyDataSetChanged()
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAgentsFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnAgentsFragmentInteractionListener")
        }
    }

    override fun setRetainInstance(retain: Boolean) {
        super.setRetainInstance(true)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnAgentsFragmentInteractionListener {
        fun onItemClick(id: Int, holder: AgentsRecyclerViewAdapter.ViewHolder)
        fun onFragmentResume();
    }

    companion object {
        private val ARG_PARAM1 = "param1"

        const val TAG = "agents_fragment"

        fun newInstance(param1: String): AgentsFragment {
            val fragment = AgentsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onItemClick(id: Int, holder: AgentsRecyclerViewAdapter.ViewHolder) {
        listener?.onItemClick(id, holder)
    }

}
