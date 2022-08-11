package ru.jeinmentalist.mail.mnemolist.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.jeinmentalist.mail.domain.type.HandleOnce
import ru.jeinmentalist.mail.domain.type.exception.Failure
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    var failureData: MutableLiveData<HandleOnce<Failure>> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failureData.value = HandleOnce(failure)
    }
}