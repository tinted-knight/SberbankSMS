package ru.tinted_knight.sberbanksms.ui.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.ui.adapters.AgentsRecyclerViewAdapter

class AgentsActivity : AppCompatActivity(), AgentsFragment.OnAgentsFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agents2)

        val fragment: AgentsFragment = AgentsFragment.newInstance("sss")

        supportFragmentManager.beginTransaction()
                .add(R.id.flAgents, fragment, AgentsFragment.TAG).commit()

    }

    override fun onItemClick(id: Int, holder: AgentsRecyclerViewAdapter.ViewHolder) {
        Toast.makeText(this, "id = " + id, Toast.LENGTH_SHORT).show()
    }

}
