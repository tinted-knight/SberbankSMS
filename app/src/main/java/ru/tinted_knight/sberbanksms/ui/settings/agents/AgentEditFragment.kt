package ru.tinted_knight.sberbanksms.ui.settings.agents

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.viewmodel.AgentCommonViewModel

class AgentEditFragment : Fragment() {

    private var agentId: Int? = null

    private lateinit var tvAgentDefaultName: TextView

    private lateinit var tvAlias: TextView

    private lateinit var viewModel: AgentCommonViewModel

    private var listener: OnAgentEditFragmentInteractionListener? = null

    companion object {
        private const val ARG_AGENT_ID = "agentId"
        const val TAG = "agent_edit_fragment"

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
        viewModel = ViewModelProviders.of(activity).get(AgentCommonViewModel::class.java)

        registerObservers()
    }

    private fun registerObservers() {
        viewModel.agent(agentId!!).observe(this, Observer { agent ->
            tvAgentDefaultName.text = agent?.defaultText
        })
        viewModel.alias(agentId!!).observe(this, Observer { alias ->
            if (alias != "")
                tvAlias.text = alias
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_agent_edit, container, false)

        tvAgentDefaultName = root.findViewById(R.id.tvAgentDefaultName)

        tvAlias = root.findViewById(R.id.tvAlias)
        tvAlias.setOnClickListener(tvAliasClick)

        val btnCreate = root.findViewById<Button>(R.id.btnCreate)
        btnCreate.setOnClickListener(btnCreateClick)

        val btnEdit = root.findViewById<Button>(R.id.btnEdit)
        btnEdit.setOnClickListener(btnEditClick)

        return root
    }

    private val btnCreateClick = View.OnClickListener {
        if (it is Button && it.id == R.id.btnCreate)
            listener?.onBtnCreatePressed(agentId!!)
    }

    private val btnEditClick = View.OnClickListener {
        if (it is Button && it.id == R.id.btnEdit && tvAlias.text.trim() != "")
            listener?.onBtnEditPressed(agentId!!)
    }

    private val tvAliasClick = View.OnClickListener {
        listener?.onBtnCreatePressed(agentId!!)
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
        fun onBtnEditPressed(agentId: Int)
        fun onBtnCreatePressed(agentId: Int)
    }

}
