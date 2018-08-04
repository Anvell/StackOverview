package io.github.anvell.stackoverview.view;

import android.animation.LayoutTransition;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.google.android.gms.ads.MobileAds;

import io.github.anvell.stackoverview.R;
import io.github.anvell.stackoverview.databinding.ActivityMainBinding;
import io.github.anvell.stackoverview.enumeration.ActiveScreen;
import io.github.anvell.stackoverview.model.QuestionDetails;
import io.github.anvell.stackoverview.viewmodel.MainViewModel;

import static android.arch.lifecycle.Lifecycle.State.RESUMED;
import static io.github.anvell.stackoverview.enumeration.ActiveScreen.DETAILS;
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

        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null) {
            getSupportActionBar().setCustomView(searchView);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
        }

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        initObservers();
        setupHomeIcon();

        if (savedInstanceState == null) {
            initFragments();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        invalidateOptionsMenu();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        setupHomeIcon();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.onNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem favorite = menu.findItem(R.id.mi_favorite);
        favorite.setVisible(viewModel.activeScreen.getValue() == DETAILS);

        QuestionDetails question = viewModel.selectedQuestion.getValue();
        if(question != null) {
            favorite.setIcon(getResources()
                            .getDrawable(question.isFavorite?
                                         R.drawable.ic_star :
                                         R.drawable.ic_star_hollow));
            favorite.setChecked(question.isFavorite);
        }

        MenuItem searchItem = menu.findItem(R.id.mi_search_view);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        LinearLayout searchBar = searchView.findViewById(R.id.search_bar);
        searchBar.setLayoutTransition(new LayoutTransition());

        searchView.setOnQueryTextListener(this);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (viewModel.activeScreen.getValue() == SEARCH) {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                    setupHomeIcon();
                }
                return true;
            }
        });

        searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.mi_favorite) {
            if(!item.isChecked()) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_star));
                viewModel.storeSelectedQuestion();
                item.setChecked(true);
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_star_hollow));
                viewModel.deleteSelectedQuestion();
                item.setChecked(false);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (query != null) {
            searchView.clearFocus();
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

    private void setupHomeIcon() {
        boolean rootScreen = getSupportFragmentManager().getBackStackEntryCount() == 0;
        toolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(),
                                  rootScreen? R.drawable.ic_code : R.drawable.ic_back, null));
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
            case COLLECTION:
                addMainFragment(new CollectionFragment(), CollectionFragment.TAG);
                break;
            case SEARCH:
                addMainFragment(new SearchResultsFragment(), SearchResultsFragment.TAG);
                break;
            case DETAILS:
                addMainFragment(new DetailsFragment(), DetailsFragment.TAG);
                break;
        }
    }

    private void navigate(ActiveScreen screen) {
        switch (screen) {
            case COLLECTION:
                replaceMainFragment(new CollectionFragment(), CollectionFragment.TAG);
                break;

            case DETAILS:
                invalidateOptionsMenu();
                replaceMainFragment(new DetailsFragment(), DetailsFragment.TAG);
                break;

            case SEARCH:
                replaceMainFragment(new SearchResultsFragment(), SearchResultsFragment.TAG);
                break;
        }
        setupHomeIcon();
    }

    private void addMainFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainFragment, fragment, tag)
                .commit();
    }

    private void replaceMainFragment(Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
