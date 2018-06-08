package io.github.anvell.stackoverview.adapter

import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_searchresults_item.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.model.Question
import kotlinx.android.synthetic.main.fragment_searchresults_item.view.*

class SearchResultsAdapter(
        private val values: MutableList<Question>,
        private val interactionListener: OnInteractionListener?)
    : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    interface OnInteractionListener {
        fun onListFragmentInteraction(item: Question?)
    }

    private val listener: View.OnClickListener

    init {
        listener = View.OnClickListener { v ->
            val item = v.tag as Question
            interactionListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_searchresults_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        item.title?.let { holder.bind(it) }

        with(holder.view) {
            tag = item
            setOnClickListener(listener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(title: String) {
            view.questionTitle.text = title
        }
    }
}
