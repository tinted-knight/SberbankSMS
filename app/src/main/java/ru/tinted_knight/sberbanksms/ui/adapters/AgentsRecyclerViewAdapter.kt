package ru.tinted_knight.sberbanksms.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.tinted_knight.sberbanksms.R
import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity

class AgentsRecyclerViewAdapter(val listener: ListItemClickListener) : RecyclerView.Adapter<AgentsRecyclerViewAdapter.ViewHolder>() {

    var data: List<AgentEntity>? = null

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val e = data!![position]
        holder!!.text1.text = e.defaultText
        holder.text1.setOnClickListener { listener.onItemClick(e._id, holder) }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val itemView = inflater.inflate(R.layout.simple_list_item, parent, false)

        return ViewHolder(itemView)
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val text1: TextView = itemView!!.findViewById(android.R.id.text1)
    }

    interface ListItemClickListener {
        fun onItemClick(id : Int, holder : ViewHolder)
    }
}