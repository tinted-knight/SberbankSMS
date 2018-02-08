package ru.tinted_knight.sberbanksms.ui.settings.agents


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.viewmodel.AliasCreateViewModel

class AliasCreateFragment : Fragment() {

    private var agentId: Int? = null

    private lateinit var viewModel: AliasCreateViewModel

    private lateinit var etAlias: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            agentId = arguments.getInt(AGENT_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_alias_create, container, false)
        etAlias = root.findViewById(R.id.etAlias)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AliasCreateViewModel::class.java)

//        registerObservers()
    }

//    private fun registerObservers() {
//        viewModel.getAliasByAgentId(agentId!!).observe(this, Observer { alias ->
//            if (alias != null && alias != "") {
//                etAlias.setText(alias)
//            }
//        })
//    }

    override fun onDetach() {
        super.onDetach()
        val alias = etAlias.text.toString().trim()
        if (isRemoving && alias != "")
            viewModel.createAlias(alias, agentId!!)
    }

    companion object {
        private val AGENT_ID = "agent_id"

        val TAG = "alias_create_fragment"

        fun newInstance(param1: Int): AliasCreateFragment {
            val fragment = AliasCreateFragment()
            val args = Bundle()
            args.putInt(AGENT_ID, param1)
            fragment.arguments = args
            return fragment
        }
    }

}
