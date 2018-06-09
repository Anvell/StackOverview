package io.github.anvell.stackoverview.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.adapter.SearchResultsAdapter
import io.github.anvell.stackoverview.adapter.SearchResultsAdapter.OnSearchResultsListener
import io.github.anvell.stackoverview.model.Question
import io.github.anvell.stackoverview.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_searchresults.*

class SearchResultsFragment : Fragment(), OnSearchResultsListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var resultsAdapter: SearchResultsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.fragment_searchresults, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        initResultsList()
        initObservers()
    }

    override fun onSearchResultsInteraction(item: Question) {
        viewModel.selectedQuestion.value = item
    }

    private fun initResultsList() {

        resultsAdapter = SearchResultsAdapter(viewModel.questions.value?: mutableListOf(), this)
        with(searchResults) {
            layoutManager = LinearLayoutManager(context)
            adapter = resultsAdapter
        }

        DividerItemDecoration(context, DividerItemDecoration.VERTICAL).let {
            searchResults.addItemDecoration(it)
        }
    }

    private fun initObservers() {

        viewModel.questions.observe(this, Observer {
            it?.let {
                resultsAdapter.replaceItems(it)
            }
        })
    }
}
