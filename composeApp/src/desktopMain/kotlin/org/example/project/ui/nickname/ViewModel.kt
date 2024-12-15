package org.example.project.ui.nickname

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class ViewModel(): ViewModel() {

    private val _nickName = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickName
    private val _promt = MutableStateFlow("")
    val promt: StateFlow<String> = _promt

    fun onChange(nickName:String) {
        this._nickName.update { nickName}
    }

    fun onPromt(promt:String) {
        this._promt.update { promt}
    }

}