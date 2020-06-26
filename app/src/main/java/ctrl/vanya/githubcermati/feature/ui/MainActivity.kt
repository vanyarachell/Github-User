package ctrl.vanya.githubcermati.feature.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ctrl.vanya.githubcermati.R
import ctrl.vanya.githubcermati.core.base.BaseViewState
import ctrl.vanya.githubcermati.core.di.CoreModule
import ctrl.vanya.githubcermati.core.utils.OnLoadMoreListener
import ctrl.vanya.githubcermati.feature.di.DaggerMainComponent
import ctrl.vanya.githubcermati.feature.di.MainModule
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_load_more.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: MainAdapter
    private var mKeyword: String = "a"
    private var mPage: Int = 1

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        injectDI()
        initViewAction()
        initObserve()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        mAdapter = MainAdapter(this)
        mAdapter.setLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                rvUserList.post {
                    mPage++
                    showLoading()
                    mViewModel.getUser(mKeyword, mPage)
                }
            }
        })
        rvUserList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun initObserve() {
        mViewModel.apply {
            userResult.observe(this@MainActivity, Observer {
                when (it) {
                    is BaseViewState.Loading -> {
                        if (mForceRefresh && !mAdapter.isLoading) {
                            showLoading()
                        }
                    }
                    is BaseViewState.Success -> {
                        stopLoading()
                        it.data?.items.let { it1 -> it1?.let { it2 -> mAdapter.setData(it2) } }
                    }
                    is BaseViewState.Error -> {
                        stopLoading()
                    }
                }
            })
        }
    }

    private fun showLoading() {
        progress_dialog.isVisible = true
    }

    private fun stopLoading() {
        progress_dialog.isVisible = false
    }

    private fun injectDI() {
        DaggerMainComponent.builder().mainModule(MainModule())
            .coreModule(CoreModule(this))
            .build().inject(this)
    }

    private fun initViewAction(){
        mViewModel.getUser(mKeyword, mPage)

        //setup toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //search manager
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //reset list on adapter
                mKeyword = query
                mPage = 1
                mAdapter.setMoreDataAvailable(true)
                mAdapter.clearList()
                mViewModel.getUser(query, mPage)
                //hide keyboard
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow((currentFocus)!!.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView.setOnCloseListener { false }
        return true
    }
}
