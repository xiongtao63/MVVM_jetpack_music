package com.xiongtao.puremusic.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xiongtao.puremusic.bridge.data.bean.DownloadFile
import com.xiongtao.puremusic.bridge.data.repository.HttpRequestManager

/**
 * @Description:
 * @Author: xiongtao
 * @Date: 2023-03-21 15:27
 */
/**
 * 模拟下载，真正的下载，也是如此
 *
 * 关闭界面，也要可以下载的功能执行
 */
class DownloadViewModel : ViewModel() {

    var downloadFileLiveData: MutableLiveData<DownloadFile>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }
        private set

    var downloadFileCanBeStoppedLiveData: MutableLiveData<DownloadFile>? = null
        get() {
            if (field == null) {
                field = MutableLiveData<DownloadFile>()
            }
            return field
        }
        private set

    // 请求仓库 模拟下载功能
    fun requestDownloadFile() = HttpRequestManager.instance.downloadFile(downloadFileLiveData)
}