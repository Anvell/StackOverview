package io.github.anvell.stackoverview.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.adapter.BaseAdapter.OnInteractionListener;
import io.github.anvell.stackoverview.adapter.SearchResultsAdapter;
import io.github.anvell.stackoverview.databinding.FragmentSearchresultsBinding;
import io.github.anvell.stackoverview.listener.EndlessScrollListener;
import io.github.anvell.stackoverview.model.Question;
import io.github.anvell.stackoverview.viewmodel.MainViewModel;

public class SearchResultsFragment extends Fragment implements OnInteractionListener<Question> {

    public static final String TAG = "SEARCH_RESULTS_FRAGMENT";

    private MainViewModel viewModel;
    private SearchResultsAdapter resultsAdapter;
    private FragmentSearchresultsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchresultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        initResultsList();
        initObservers();
    }

    @Override public void onListInteraction(Question item) {
        viewModel.requestQuestion(item.questionId);
    }

    private void initResultsList() {

        ArrayList<Question> questionsValue = viewModel.questions.getValue();
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

        resultsAdapter = new SearchResultsAdapter(questionsValue != null? questionsValue
                                                  : new ArrayList<>(), this);

        binding.searchResults.setLayoutManager(linearLayout);
        binding.searchResults.setAdapter(resultsAdapter);

        binding.searchResults.addOnScrollListener(
                new EndlessScrollListener(linearLayout) {

                    @Override
                    protected void onLoadMore(int totalItemsCount, RecyclerView view) {
                        viewModel.requestMore();
                    }
                }
        );
        binding.searchResults.addItemDecoration(new DividerItemDecoration(getContext(),
                                                    DividerItemDecoration.VERTICAL));
    }

    private void initObservers() {
        viewModel.questions.observe(this, v -> resultsAdapter.replaceItems(v));
    }
}
