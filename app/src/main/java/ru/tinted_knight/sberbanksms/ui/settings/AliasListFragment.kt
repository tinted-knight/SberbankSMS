package ru.tinted_knight.sberbanksms.ui.settings

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.ui.adapters.AliasesRecyclerViewAdapter
import ru.tinted_knight.sberbanksms.viewmodel.AliasListViewModel

class AliasListFragment : Fragment(), AliasesRecyclerViewAdapter.ListItemClickListener {

    private var agentId: Int? = null

    private lateinit var viewModel: AliasListViewModel

    private val adapter = AliasesRecyclerViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            agentId = arguments.getInt(AliasListFragment.ARG_AGENT_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_alias_list, container, false)

        val rvAliases: RecyclerView = root.findViewById(R.id.rvAliases)
        rvAliases.adapter = adapter
        rvAliases.layoutManager = LinearLayoutManager(activity)
        rvAliases.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rvAliases.isVerticalScrollBarEnabled = true

        val fab: FloatingActionButton = root.findViewById(R.id.fabAdd)
        fab.setOnClickListener({
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("input dialog title")
            val input = EditText(activity)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            val dialogListener = DialogInterface.OnClickListener { _, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    val text = input.text.toString().trim()
                    viewModel.createAlias(text)
                } else {
                    Toast.makeText(activity, "canceled", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setPositiveButton("ok", dialogListener)
            builder.setNegativeButton("cancel", dialogListener)
            builder.show()
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AliasListViewModel::class.java)
        registerObservers()
    }

    private fun registerObservers() {
        viewModel.aliasList.observe(this, Observer { aliases ->
            if (aliases != null) {
                adapter.data = aliases
                adapter.notifyDataSetChanged()
            } else
                Toast.makeText(activity, "empty", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onItemClick(id: Int, holder: AliasesRecyclerViewAdapter.ViewHolder) {
        viewModel.connectToAgent(id, agentId!!)
        activity.onBackPressed()
    }

    companion object {
        private val ARG_AGENT_ID = "agentId"
        val TAG = "alias_list_fragment"

        fun newInstance(agentId: Int): AliasListFragment {
            val fragment = AliasListFragment()
            val args = Bundle()
            args.putInt(AliasListFragment.ARG_AGENT_ID, agentId)
            fragment.arguments = args
            return fragment
        }
    }
}
