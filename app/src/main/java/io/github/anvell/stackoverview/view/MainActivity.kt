package io.github.anvell.stackoverview.view

import android.arch.lifecycle.Lifecycle.State.RESUMED
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import io.github.anvell.stackoverview.R
import io.github.anvell.stackoverview.enumeration.ActiveScreen
import io.github.anvell.stackoverview.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.animation.LayoutTransition
import android.widget.LinearLayout


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setupHomeIcon(viewModel.activeScreen.value!!)
        initObservers()

        if(savedInstanceState == null) {
            initFragments()
        }

        toolbar.setNavigationOnClickListener {
            viewModel.clearSelectedQuestion()
            navigate(ActiveScreen.SEARCH)
        }

        if (Build.VERSION.SDK_INT >= 21) {
            toolbar.elevation = 4f
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.clearSelectedQuestion()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        menu?.let {
            val mi = menu.findItem(R.id.mi_search_view)
            searchView = mi.actionView as SearchView
            val searchBar = searchView.findViewById(R.id.search_bar) as LinearLayout
            searchBar.layoutTransition = LayoutTransition()

            searchView.setOnQueryTextListener(this)
            searchView.setOnCloseListener {
                viewModel.selectedQuestion.value?.let {
                    if(viewModel.activeScreen.value != ActiveScreen.DETAILS)
                        navigate(ActiveScreen.DETAILS)
                }
                false
            }

            searchView.imeOptions = searchView.imeOptions or EditorInfo.IME_FLAG_NO_EXTRACT_UI
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            searchView.clearFocus()
            searchView.onActionViewCollapsed()
            viewModel.submitQuery(query)
        }
        return false
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

        viewModel.isBusy.observe(this, Observer {
            progressIndicator.visibility = when (it!!) {
                true -> View.VISIBLE
                false -> View.GONE
            }
        })
    }

    private fun setupHomeIcon(screen: ActiveScreen) {
        toolbar.navigationIcon = when (screen) {
            ActiveScreen.DETAILS -> ResourcesCompat.getDrawable(resources, R.drawable.ic_back, null)
            ActiveScreen.SEARCH -> ResourcesCompat.getDrawable(resources, R.drawable.ic_code, null)
        }
    }

    private fun displayConnectionWarning() {
        Snackbar.make(toolbar, R.string.error_no_connection, Snackbar.LENGTH_LONG).apply {
            setAction(R.string.error_no_connection_button_label, {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            })
            show()
        }
    }

    private fun initFragments() {
        val (fragment, tag) = when (viewModel.activeScreen.value) {
            ActiveScreen.DETAILS -> Pair(DetailsFragment(), DetailsFragment.TAG)
            ActiveScreen.SEARCH -> Pair(SearchResultsFragment(), SearchResultsFragment.TAG)
            else -> return
        }

        supportFragmentManager.beginTransaction()
                .add(R.id.mainFragment, fragment, tag)
                .commit()
    }

    private fun navigate(screen: ActiveScreen): Boolean {
        when(screen) {
            ActiveScreen.DETAILS -> {
                searchView.onActionViewCollapsed()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.mainFragment, DetailsFragment(), DetailsFragment.TAG)
                        .addToBackStack(null)
                        .commit()
            }

            ActiveScreen.SEARCH -> {
                with(supportFragmentManager) {
                    if (backStackEntryCount > 0) {
                        popBackStack()
                    }
                }
            }
        }
        setupHomeIcon(screen)
        return true
    }
}
