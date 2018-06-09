package io.github.anvell.stackoverview.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.model.Question
import kotlinx.android.synthetic.main.fragment_searchresults_item.view.*

class SearchResultsAdapter(
        private var values: MutableList<Question>,
        private val interactionListener: OnSearchResultsListener?)
    : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    interface OnSearchResultsListener {
        fun onSearchResultsInteraction(item: Question)
    }

    private val listener: View.OnClickListener

    init {
        listener = View.OnClickListener {
            val item = it.tag as Question
            interactionListener?.onSearchResultsInteraction(item)
        }
    }

    fun replaceItems(newValues: MutableList<Question>) {
        values = newValues
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_searchresults_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

        with(holder.view) {
            tag = item
            setOnClickListener(listener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Question) {
            view.questionTitle.text = item.title

            val answers = resourceString(if (item.answerCount != 1) R.string.search_results_item_answers_plural
                                         else R.string.search_results_item_answers)
            view.questionAnswers.text = String.format(answers, item.answerCount)

            val votes = resourceString(if (item.score != 1) R.string.search_results_item_votes_plural
                                       else R.string.search_results_item_votes)
            view.questionVotes.text = String.format(votes, item.score)

        }

        private fun resourceString(id: Int): String = view.context.getString(id)
    }
}
