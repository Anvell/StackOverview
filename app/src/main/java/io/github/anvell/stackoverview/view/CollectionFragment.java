package io.github.anvell.stackoverview.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.adapter.BaseAdapter;
import io.github.anvell.stackoverview.adapter.SearchResultsAdapter;
import io.github.anvell.stackoverview.databinding.FragmentCollectionBinding;
import io.github.anvell.stackoverview.enumeration.ActiveScreen;
import io.github.anvell.stackoverview.model.Question;
import io.github.anvell.stackoverview.viewmodel.MainViewModel;

public class CollectionFragment extends Fragment implements BaseAdapter.OnInteractionListener<Question> {

    public static final String TAG = "COLLECTION_FRAGMENT";

    private MainViewModel viewModel;
    private SearchResultsAdapter resultsAdapter;
    private FragmentCollectionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel.activeScreen.setValue(ActiveScreen.COLLECTION);
        initCollection();
        initObservers();
    }

    @Override
    public void onListInteraction(Question item) {
        viewModel.requestQuestion(item.questionId);
    }

    private void initCollection() {
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        resultsAdapter = new SearchResultsAdapter(new ArrayList<>(), this);

        binding.collectionView.setLayoutManager(linearLayout);
        binding.collectionView.setAdapter(resultsAdapter);
        binding.collectionView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void initObservers() {
        viewModel.questionsCollection.observe(this, v -> resultsAdapter.replaceItems(v));
    }
}
