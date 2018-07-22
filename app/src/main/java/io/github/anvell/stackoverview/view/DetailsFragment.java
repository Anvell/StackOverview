package io.github.anvell.stackoverview.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.anvell.stackoverview.adapter.AnswersAdapter;
import io.github.anvell.stackoverview.databinding.FragmentDetailsBinding;
import io.github.anvell.stackoverview.extension.Utils;
import io.github.anvell.stackoverview.model.QuestionDetails;
import io.github.anvell.stackoverview.viewmodel.MainViewModel;

public class DetailsFragment extends Fragment {

    public static final String TAG = "DETAILS_FRAGMENT";

    private FragmentDetailsBinding binding;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        initObservers();
    }

    private void initObservers() {
        viewModel.selectedQuestion.observe(this, this::bind);
    }

    private void bind(QuestionDetails question) {
        if(question == null) {
            return;
        }

        binding.postTitle.setText(Utils.fromHtml(question.title));
        binding.postBody.setText(Utils.fromHtml(question.body));

        if(question.score != 0) {
            binding.postVotesText.setText(String.valueOf(question.score));
        } else {
            binding.postVotes.setVisibility(View.GONE);
        }

        if(question.answers != null) {
            AnswersAdapter answersAdapter = new AnswersAdapter(question.answers);
            binding.answersView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.answersView.setAdapter(answersAdapter);
            binding.answersView.setNestedScrollingEnabled(false);
        }
    }
}
