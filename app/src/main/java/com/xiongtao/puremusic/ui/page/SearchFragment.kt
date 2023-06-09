package com.xiongtao.puremusic.ui.page

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xiongtao.puremusic.R
import com.xiongtao.puremusic.bridge.request.DownloadViewModel
import com.xiongtao.puremusic.bridge.state.SearchViewModel
import com.xiongtao.puremusic.databinding.FragmentSearchBinding
import com.xiongtao.puremusic.ui.base.BaseFragment

/**
 * @Description:
 * @Author: xiongtao
 * @Date: 2023-03-17 15:01
 */
class SearchFragment : BaseFragment(){

    private var mBinding:FragmentSearchBinding ?= null
    private var mSearchViewModel:SearchViewModel ?= null
    private var mDownloadViewModel:DownloadViewModel ?= null   // 下载相关的 VM // todo Request ViewModel 额外 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSearchViewModel = getFragmentViewModelProvider(this).get(SearchViewModel::class.java)
        mDownloadViewModel = getFragmentViewModelProvider(this).get(DownloadViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        mBinding = FragmentSearchBinding.bind(view)
        mBinding?.vm = mSearchViewModel// 设置 自身VM // todo Status ViewModel
        mBinding?.click = ClickProxy()// 设置监听

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 第一种方式 （不需要生命周期观察 变化）
        /*mDownloadViewModel!!.downloadFileLiveData ?.addOnPropertyChangedCallback(object:
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                TODO("Not yet implemented")
            }
        })*/
        // 第二种方式 （不需要生命周期观察 变化）
        // 就是不需要Lifecycle观察 宿主生命周期变化
        mDownloadViewModel!!.downloadFileLiveData?.observeForever {
            // 二次转化
            // 做成果的校验
            // 逻辑1
            // 逻辑2
            // 逻辑3
            // ....

            // 修改了 StatusViewModel
            // 让我自身的VM 数据发送变化, 那么布局就会感应变化
            mSearchViewModel!!.progress.set(it.progress)
        }

        /*mDownloadViewModel!!.downloadFileLiveData ?.observe(viewLifecycleOwner, { downloadFile ->

           // 修改了 StatusViewModel
           // 让我自身的VM 数据发送变化, 那么布局就会感应变化
           mSearchViewModel!!.progress.set(downloadFile.progress)
       })*/
    }


    inner class ClickProxy {

        private val PATH2 = "http://www.xiangxueketang.cn/" // 网页

        // 跳转 加载一个网页
        fun testNav()  = startPATH()

        // 跳转 加载一个网页
        fun subscribe() = startPATH()

        // 返回
        fun back() {
            nav().navigateUp() // back键的时候，返回上一个界面
        }

        private fun startPATH() {
            val uri = Uri.parse(PATH2)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // 测试下载，返回页面依然有效
        fun testLifecycleDownload() {
            mDownloadViewModel ?.requestDownloadFile()
        }

        // 测试下载，返回页面依然有效
        fun testDownload() {
            mDownloadViewModel ?.requestDownloadFile()
        }
    }
}