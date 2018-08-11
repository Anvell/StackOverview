package io.github.anvell.stackoverview.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.IOException;
import java.util.ArrayList;

import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.extension.Utils;
import io.github.anvell.stackoverview.model.Question;
import io.github.anvell.stackoverview.model.QuestionsResponse;
import io.github.anvell.stackoverview.repository.StackOverflowRepository;
import retrofit2.Response;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Question> questions = new ArrayList<>();
    private Context context;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    public void onCreate() {
    }

    public void onDestroy() {
        questions.clear();
    }

    public int getCount() {
        return questions.size();
    }

    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.app_widget_item);

        final Question question = questions.get(position);
        final Spanned title = Utils.fromHtml(question.title);

        rv.setTextViewText(R.id.hotTitle, title);
        rv.setTextViewText(R.id.hotViews, String.valueOf(question.viewCount));
        rv.setTextViewText(R.id.hotComments, String.valueOf(question.answerCount));

        Bundle extras = new Bundle();
        extras.putInt(AppWidget.EXTRA_ITEM, question.questionId);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widgetItem, fillInIntent);

        return rv;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // It's ok to proceed synchronously here
        Response<QuestionsResponse> response;
        try {
            response = StackOverflowRepository.getInstance().requestHotQuestions().execute();
            QuestionsResponse data = response.body();

            if(data != null && data.items != null && data.items.size() > 0) {
                questions.clear();
                questions.addAll(data.items);
            }
        } catch (IOException e) {
            // Can't fetch data, let's wait for user or JobDispatcher to try again
        }
    }
}