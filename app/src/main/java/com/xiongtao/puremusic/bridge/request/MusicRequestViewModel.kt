package com.xiongtao.puremusic.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xiongtao.puremusic.bridge.data.bean.TestAlbum
import com.xiongtao.puremusic.bridge.data.repository.HttpRequestManager

/**
 * @Description:
 * @Author: xiongtao
 * @Date: 2023-03-20 11:08
 */
class MusicRequestViewModel : ViewModel() {

    var freeMusicesLiveData : MutableLiveData<TestAlbum>? = null
    get() {
        if(field == null){
            field = MutableLiveData()
        }
        return field
    }
    private set


    fun requestFreeMusics() {
        HttpRequestManager.instance.getFreeMusic(freeMusicesLiveData)
    }
}