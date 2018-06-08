package io.github.anvell.stackoverview.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.adapter.SearchResultsAdapter
import io.github.anvell.stackoverview.adapter.SearchResultsAdapter.OnInteractionListener
import io.github.anvell.stackoverview.model.Question
import io.github.anvell.stackoverview.viewmodel.MainViewModel

class SearchResultsFragment : Fragment(), OnInteractionListener {

    private lateinit var questionsView: RecyclerView
    private lateinit var viewModel: MainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }

        initQuestions()
        initObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_searchresults, container, false)

        if (view is RecyclerView) {
            questionsView = view
        }
        return view
    }

    override fun onListFragmentInteraction(item: Question?) {
        viewModel.selectedQuestion.value = item
    }

    private fun initQuestions() {
        viewModel.questions.value?.let {
            questionsView.layoutManager = LinearLayoutManager(context)
            questionsView.adapter = SearchResultsAdapter(it, this)
        }

        DividerItemDecoration(context, DividerItemDecoration.VERTICAL).let {
            questionsView.addItemDecoration(it)
        }
    }

    private fun initObservers() {

        viewModel.questions.observe(this, Observer {
            it?.let {
                questionsView.adapter.notifyDataSetChanged()
            }
        })
    }
}
