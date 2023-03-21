package com.xiongtao.puremusic.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xiongtao.puremusic.bridge.data.bean.LibraryInfo
import com.xiongtao.puremusic.bridge.data.repository.HttpRequestManager

/**
 * @Description:
 * @Author: xiongtao
 * @Date: 2023-03-21 15:51
 */
class InfoRequestViewModel : ViewModel() {

    var libraryLiveData: MutableLiveData<List<LibraryInfo>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }
        private set

    fun requestLibraryInfo(){
        // 请求服务器，协程  RxJava
        // 调用仓库
        HttpRequestManager.instance.getLibraryInfo(libraryLiveData)
    }
}