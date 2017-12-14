package ru.tinted_knight.sberbanksms.ui.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.viewmodel.AgentViewModel

class AgentEditFragment : Fragment() {

    private var agentId: Int? = null

    private lateinit var tvAgentDefaultName: TextView

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            agentId = arguments.getInt(ARG_AGENT_ID)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(AgentViewModel::class.java)

        registerObservers(viewModel)
    }

    private fun registerObservers(viewModel: AgentViewModel) {
        viewModel.agent.observe(this, Observer { agent ->
            tvAgentDefaultName.text = agent?.defaultText
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root =  inflater!!.inflate(R.layout.fragment_agent_edit, container, false)
        tvAgentDefaultName = root.findViewById(R.id.tvAgentDefaultName)

        return root
    }

    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnAgentsFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private val ARG_AGENT_ID = "agentId"

        fun newInstance(agentId: Int): AgentEditFragment {
            val fragment = AgentEditFragment()
            val args = Bundle()
            args.putInt(ARG_AGENT_ID, agentId)
            fragment.arguments = args
            return fragment
        }
    }
}
