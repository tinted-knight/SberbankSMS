package ru.tinted_knight.sberbanksms.ui.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.ui.adapters.AgentsRecyclerViewAdapter

class AgentsActivity : AppCompatActivity(),
        AgentsFragment.OnAgentsFragmentInteractionListener,
        AgentEditFragment.OnAgentEditFragmentInteractionListener {

    private enum class State { AgentsList, AgentEdit }

    private var state: State = State.AgentsList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agents2)

        val fragment: AgentsFragment = AgentsFragment.newInstance("sss")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                    .add(R.id.flAgents, fragment, AgentsFragment.TAG)
                    .commit()
//        else
//            supportFragmentManager.popBackStackImmediate()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE")
    }

    override fun onItemClick(id: Int, holder: AgentsRecyclerViewAdapter.ViewHolder) {
//        Toast.makeText(this, "id = " + id, Toast.LENGTH_SHORT).show()
        val agentEditFragment = AgentEditFragment.newInstance(id)
        supportFragmentManager.beginTransaction()
                .addToBackStack("agentEdit")
                .replace(R.id.flAgents, agentEditFragment, AgentEditFragment.TAG)
                .commit()
        state = State.AgentEdit
    }

    override fun onFragmentResume() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return false
    }

    override fun onAddAliasBtnPressed(agentId: Int, operation: Int) {
        when (operation) {
            AgentEditFragment.CREATE -> {
                val fragment = AliasCreateFragment.newInstance(agentId)
                supportFragmentManager.beginTransaction()
                        .addToBackStack("aliasCreate")
                        .replace(R.id.flAgents, fragment, AliasCreateFragment.TAG)
                        .commit()
            }
            AgentEditFragment.EDIT -> {
                val fragment = AliasEditFragment.newInstance(agentId)
                supportFragmentManager.beginTransaction()
                        .addToBackStack("aliasEdit")
                        .replace(R.id.flAgents, fragment, AliasEditFragment.TAG)
                        .commit()
            }
            AgentEditFragment.SHOW -> {
                val fragment = AliasListFragment.newInstance(agentId)
                supportFragmentManager.beginTransaction()
                        .addToBackStack("aliasList")
                        .replace(R.id.flAgents, fragment, AliasListFragment.TAG)
                        .commit()
            }
        }
    }

}
