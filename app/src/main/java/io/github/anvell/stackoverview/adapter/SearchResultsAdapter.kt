package io.github.anvell.stackoverview.adapter

import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.extension.fromHtml
import io.github.anvell.stackoverview.model.Question
import kotlinx.android.synthetic.main.fragment_searchresults_item.view.*

class SearchResultsAdapter(private var values: MutableList<Question>,
                     private val interactionListener: OnInteractionListener<Question>? = null)
    : BaseAdapter<Question>(values, interactionListener) {

    override fun getResourceId(): Int = R.layout.fragment_searchresults_item

    override fun bind(holder: ViewHolder, item: Question) {

        holder.view.questionTitle.text = item.title.fromHtml()

        val answers = holder.resourceString(
                if (item.answerCount != 1) R.string.search_results_item_answers_plural
                    else R.string.search_results_item_answers)

        holder.view.questionAnswers.text = String.format(answers, item.answerCount)

        val votes = holder.resourceString(
                if (item.score != 1) R.string.search_results_item_votes_plural
                    else R.string.search_results_item_votes)

        holder.view.questionVotes.text = String.format(votes, item.score)
    }

    private fun ViewHolder.resourceString(id: Int): String = view.context.getString(id)
}