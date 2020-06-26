package ctrl.vanya.githubcermati.feature.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ctrl.vanya.githubcermati.core.base.BaseViewState
import ctrl.vanya.githubcermati.core.model.User
import ctrl.vanya.githubcermati.core.model.UserMdl
import ctrl.vanya.githubcermati.core.utils.AppDispatchers
import ctrl.vanya.githubcermati.data.repository.MainRepository
import ctrl.vanya.githubcermati.data.utils.ResultState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: AppDispatchers
) : ViewModel() {

    private val _mUserResult = MutableLiveData<BaseViewState<UserMdl>>()

    val userResult: LiveData<BaseViewState<UserMdl>> = _mUserResult

    private var jobCallApi: Job? = null

    var mForceRefresh = false

    override fun onCleared() {
        super.onCleared()
        jobCallApi?.cancel()
    }

    fun getUser(mKeyword: String, mPage: Int) {
        _mUserResult.value = BaseViewState.Loading
        jobCallApi?.cancel()
        jobCallApi = viewModelScope.launch {
            val request = withContext(dispatchers.io) {
                repository.getUser(mKeyword, mPage)
            }
            when (request) {
                is ResultState.Success -> {
                    _mUserResult.value = BaseViewState.Success(request.data)
                }
                is ResultState.Error -> {
                    _mUserResult.value = BaseViewState.Error(request.errorMessage)
                }
            }
        }
    }
}