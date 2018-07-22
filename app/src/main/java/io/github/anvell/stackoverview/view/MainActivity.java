package io.github.anvell.stackoverview.view;

import android.animation.LayoutTransition;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.databinding.ActivityMainBinding;
import io.github.anvell.stackoverview.enumeration.ActiveScreen;
import io.github.anvell.stackoverview.viewmodel.MainViewModel;

import static android.arch.lifecycle.Lifecycle.State.RESUMED;
import static io.github.anvell.stackoverview.enumeration.ActiveScreen.*;
import static io.github.anvell.stackoverview.enumeration.ActiveScreen.SEARCH;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private MainViewModel viewModel;
    private SearchView searchView;
    private Toolbar toolbar;
    private ActivityMainBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        toolbar = (Toolbar)binding.toolbarView;
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        if (viewModel.activeScreen.getValue() != null) {
            setupHomeIcon(viewModel.activeScreen.getValue());
        }
        initObservers();

        if (savedInstanceState == null) {
            initFragments();
        }

        toolbar.setNavigationOnClickListener(it -> {
            viewModel.clearSelectedQuestion();
            navigate(SEARCH);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewModel.clearSelectedQuestion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem mi = menu.findItem(R.id.mi_search_view);
        searchView = (SearchView) mi.getActionView();
        LinearLayout searchBar = searchView.findViewById(R.id.search_bar);
        searchBar.setLayoutTransition(new LayoutTransition());

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(() -> {

            if (viewModel.selectedQuestion.getValue() != null) {
                if (viewModel.activeScreen.getValue() != DETAILS) {
                    navigate(DETAILS);
                }
            }
            return false;
        });

        searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (query != null) {
            searchView.clearFocus();
            searchView.onActionViewCollapsed();
            viewModel.submitQuery(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText != null) {
            viewModel.submitQuery(newText);
        }
        return true;
    }

    private void initObservers() {

        viewModel.activeScreen.observe(this, it -> {
            if (it != null && getLifecycle().getCurrentState().isAtLeast(RESUMED)) {
                navigate(it);
            }
        });

        viewModel.connectionStatus().observe(this, it -> {
            if(it != null && !it && getLifecycle().getCurrentState().isAtLeast(RESUMED)) {
                displayConnectionWarning();
            }
        });

        viewModel.isBusy.observe(this, it -> {
            if(it != null) binding.progressIndicator.setVisibility(it? View.VISIBLE : View.GONE);
        });
    }

    private void setupHomeIcon(ActiveScreen screen) {
        switch (screen) {
            case DETAILS:
                toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_back, null));
                break;
            case SEARCH:
                toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_code, null));
                break;
        }
    }

    private void displayConnectionWarning() {
        if(binding.toolbarView == null) {
            return;
        }

        Snackbar.make(binding.toolbarView, R.string.error_no_connection, Snackbar.LENGTH_LONG)
                .setAction(R.string.error_no_connection_button_label, it -> {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                })
                .show();
    }

    private void initFragments() {
        ActiveScreen activeScreen = viewModel.activeScreen.getValue();
        if(activeScreen == null) {
            return;
        }

        switch (activeScreen) {
            case SEARCH:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.mainFragment, new SearchResultsFragment(), SearchResultsFragment.TAG)
                        .commit();
                break;
            case DETAILS:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.mainFragment, new DetailsFragment(), DetailsFragment.TAG)
                        .commit();
                break;
        }
    }

    private Boolean navigate(ActiveScreen screen) {
        switch (screen) {
            case DETAILS:
                searchView.onActionViewCollapsed();

                if (!(getSupportFragmentManager().findFragmentById(R.id.mainFragment) instanceof DetailsFragment)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFragment, new DetailsFragment(), DetailsFragment.TAG)
                            .addToBackStack(null)
                            .commit();
                }
                break;

            case SEARCH:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                break;
        }
        setupHomeIcon(screen);
        return true;
    }
}
