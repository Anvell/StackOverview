package io.github.anvell.stackoverview.view

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Lifecycle.State.*
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.widget.Toast
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.model.Question
import io.github.anvell.stackoverview.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchView.setOnQueryTextListener(this)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initObservers()

        if(savedInstanceState == null) {
            initFragments()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.submitQuery(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            viewModel.submitQuery(newText)
        }
        return true
    }

    private fun initObservers() {
        viewModel.selectedQuestion.observe(this, Observer {
            it?.let {
                onResultSelected(it)
            }
        })

        viewModel.connectionStatus().observe(this, Observer {
            it?.let {
                if(!it && lifecycle.currentState.isAtLeast(RESUMED)) {
                    displayConnectionWarning()
                }
            }
        })
    }

    private fun displayConnectionWarning() {
        Snackbar.make(currentFocus, R.string.error_no_connection, Snackbar.LENGTH_LONG).apply {
            setAction(R.string.error_no_connection_button_label, {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            })
            show()
        }
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction()
                .add(R.id.mainFragment, SearchResultsFragment())
                .commit()
    }

    private fun onResultSelected(item: Question) {
        Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
    }
}
