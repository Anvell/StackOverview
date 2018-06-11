package io.github.anvell.stackoverview.view

import android.arch.lifecycle.Lifecycle.State.RESUMED
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.enumeration.ActiveScreen
import io.github.anvell.stackoverview.model.QuestionDetails
import io.github.anvell.stackoverview.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initObservers()

        if(savedInstanceState == null) {
            initFragments()
        }

        if (Build.VERSION.SDK_INT >= 21) {
            toolbar.elevation = 4f
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menu?.let {
            val mi = menu.findItem(R.id.mi_search_view)
            searchView = mi.actionView as SearchView
            searchView.setOnQueryTextListener(this)
            searchView.imeOptions = searchView.imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI
        }
        return true
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
        viewModel.activeScreen.observe(this, Observer {
            it?.let {
                if(lifecycle.currentState.isAtLeast(RESUMED)) {
                    navigate(it)
                }
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
                .add(R.id.mainFragment, SearchResultsFragment(), SearchResultsFragment.TAG)
                .commit()
    }

    private fun navigate(screen: ActiveScreen) {
        when(screen) {
            ActiveScreen.DETAILS -> {
                searchView.onActionViewCollapsed()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFragment, DetailsFragment(), DetailsFragment.TAG)
                        .addToBackStack(null)
                        .commit()
            }
            ActiveScreen.SEARCH -> {

            }
        }
    }

}
