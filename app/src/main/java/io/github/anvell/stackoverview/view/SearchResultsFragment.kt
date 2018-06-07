package io.github.anvell.stackoverview.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.adapter.SearchResultsAdapter
import io.github.anvell.stackoverview.adapter.SearchResultsAdapter.OnInteractionListener
import io.github.anvell.stackoverview.model.Question

class SearchResultsFragment : Fragment(), OnInteractionListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_searchresults, container, false)

        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(context)
            view.adapter = SearchResultsAdapter(listOf(Question()), this)
        }
        return view
    }

    override fun onListFragmentInteraction(item: Question?) {

    }
}
