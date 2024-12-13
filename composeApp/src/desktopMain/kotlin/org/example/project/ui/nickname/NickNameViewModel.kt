package org.example.project.ui.nickname

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class NickNameViewModel(): ViewModel() {

    private val _nickName = MutableStateFlow("D")
    val nickname: StateFlow<String> = _nickName

    fun onChange(nickName:String) {
        this._nickName.update { nickName}
    }

}