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
import io.github.anvell.stackoverview.adapter.AnswersAdapter
import io.github.anvell.stackoverview.extension.fromHtml
import io.github.anvell.stackoverview.model.QuestionDetails
import io.github.anvell.stackoverview.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    companion object {
        const val TAG = "DETAILS_FRAGMENT"
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        initObservers()
    }

    private fun initObservers() {
        viewModel.selectedQuestion.observe(this, Observer {
            it?.let {
                bind(it)
            }
        })
    }

    private fun bind(question: QuestionDetails) {
        postTitle.text = question.title.fromHtml()
        postBody.text = question.body.fromHtml()

        if(question.score != 0) {
            postVotesText.text = question.score.toString()
        } else {
            postVotes.visibility = View.GONE
        }

        question.answers?.let {
            val answersAdapter = AnswersAdapter(question.answers)
            with(answersView) {
                layoutManager = LinearLayoutManager(context)
                adapter = answersAdapter
                isNestedScrollingEnabled = false
            }
        }
    }

}
