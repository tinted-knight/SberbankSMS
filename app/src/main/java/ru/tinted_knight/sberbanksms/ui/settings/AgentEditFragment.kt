package ru.tinted_knight.sberbanksms.ui.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.viewmodel.AgentViewModel
import ru.tinted_knight.sberbanksms.viewmodel.factory.AgentEditVMFactory

class AgentEditFragment : Fragment() {

    private var agentId: Int? = null

    private lateinit var tvAgentDefaultName: TextView

    private lateinit var tvAlias: TextView

    private lateinit var viewModel: AgentViewModel

    private var listener: OnAgentEditFragmentInteractionListener? = null

    companion object {
        private val ARG_AGENT_ID = "agentId"
        val TAG = "agent_edit_fragment"

        val CREATE = 101
        val EDIT = 102
        val SHOW = 103

        fun newInstance(agentId: Int): AgentEditFragment {
            val fragment = AgentEditFragment()
            val args = Bundle()
            args.putInt(ARG_AGENT_ID, agentId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            agentId = arguments.getInt(ARG_AGENT_ID)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = AgentEditVMFactory(activity.application, agentId!!)
        viewModel = ViewModelProviders.of(this, factory).get(AgentViewModel::class.java)
//        val viewModel = ViewModelProviders.of(this).get(AgentViewModel::class.java)

        registerObservers()
    }

    private fun registerObservers() {
        viewModel.agent.observe(this, Observer { agent ->
            tvAgentDefaultName.text = agent?.defaultText
        })
        viewModel.alias.observe(this, Observer { alias ->
            if (alias != "")
                tvAlias.text = alias
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_agent_edit, container, false)
        tvAgentDefaultName = root.findViewById(R.id.tvAgentDefaultName)
        tvAlias = root.findViewById(R.id.tvAlias)
        tvAlias.setOnClickListener({
            listener?.onAddAliasBtnPressed(agentId!!, SHOW)
        })

        val btnAddOrEdit: ImageButton = root.findViewById(R.id.btnAdd)
        btnAddOrEdit.setOnClickListener({
            if (tvAlias.text.trim() != "")
                listener?.onAddAliasBtnPressed(agentId!!, EDIT)
            else
//                listener?.onAddAliasBtnPressed(agentId!!, CREATE)
                listener?.onAddAliasBtnPressed(agentId!!, SHOW)
        })

        return root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnAgentEditFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnAgentEditFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        if (isRemoving) {
//            Toast.makeText(activity, "onDetach", Toast.LENGTH_SHORT).show()
        }
    }

    interface OnAgentEditFragmentInteractionListener {
        fun onAddAliasBtnPressed(agentId: Int, operation : Int)
    }

}
