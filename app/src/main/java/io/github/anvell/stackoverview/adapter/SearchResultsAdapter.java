package io.github.anvell.stackoverview.adapter;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.extension.Utils;
import io.github.anvell.stackoverview.model.Question;

public class SearchResultsAdapter extends BaseAdapter<Question> {

    public SearchResultsAdapter(List<Question> values,
                         OnInteractionListener<Question> interactionListener) {
        super(values, interactionListener);
    }

    @Override
    public int getResourceId() {
        return R.layout.fragment_searchresults_item;
    }

    @Override
    public void bind(ViewHolder holder, Question item) {

        TextView questionTitle = holder.itemView.findViewById(R.id.questionTitle);
        questionTitle.setText(Utils.fromHtml(item.title));

        String answers = resourceString(holder.itemView,
                        item.answerCount != 1 ? R.string.search_results_item_answers_plural
                        : R.string.search_results_item_answers);

        TextView questionAnswers = holder.itemView.findViewById(R.id.questionAnswers);
        questionAnswers.setText(String.format(answers, item.answerCount));

        String votes = resourceString(holder.itemView,
                        item.score != 1 ? R.string.search_results_item_votes_plural
                        : R.string.search_results_item_votes);

        TextView questionVotes = holder.itemView.findViewById(R.id.questionVotes);
        questionVotes.setText(String.format(votes, item.score));
    }

    private String resourceString(View view, int id) {
        return view.getContext().getString(id);
    }
}