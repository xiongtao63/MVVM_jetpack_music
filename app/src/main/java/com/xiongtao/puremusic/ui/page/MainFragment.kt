package com.xiongtao.puremusic.ui.page

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xiongtao.puremusic.bridge.data.bean.TestAlbum
import com.xiongtao.architecture.ui.adapter.SimpleBaseBindingAdapter
import com.xiongtao.puremusic.R
import com.xiongtao.puremusic.bridge.player.PlayerManager
import com.xiongtao.puremusic.bridge.request.MusicRequestViewModel
import com.xiongtao.puremusic.databinding.FragmentMainBinding
import com.xiongtao.puremusic.bridge.state.MainViewModel
import com.xiongtao.puremusic.databinding.AdapterPlayItemBinding
import com.xiongtao.puremusic.ui.base.BaseFragment

/**
 * @Description:
 * @Author: xiongtao
 * @Date: 2023-03-17 15:01
 */
class MainFragment : BaseFragment(){

    var mainBinding:FragmentMainBinding ?= null
    var mainViewModel:MainViewModel ?= null
    var adapter:SimpleBaseBindingAdapter<TestAlbum.TestMusic?,AdapterPlayItemBinding?>? = null
    private var musicRequestViewModel: MusicRequestViewModel? = null // 音乐资源相关的VM  todo Request ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = getFragmentViewModelProvider(this).get(MainViewModel::class.java)
        musicRequestViewModel = getFragmentViewModelProvider(this).get(MusicRequestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mainBinding = FragmentMainBinding.bind(view)
        mainBinding?.click = ClickProxy()  // 设置点击事件，布局就可以直接绑定
        mainBinding?.vm = mainViewModel   // 设置VM，就可以实时数据变化
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// 触发  --->  MainFragment初始化页面的标记，初始化选项卡和页面
        mainViewModel!!.initTabAndPage.set(true)

        // 触发，---> 还要加载WebView
        mainViewModel!!.pageAssetPath.set("JetPack之 WorkManager.html")

        adapter = object : SimpleBaseBindingAdapter<TestAlbum.TestMusic?,AdapterPlayItemBinding?>(context,R.layout.adapter_play_item){
            override fun onSimpleBindItem(
                binding: AdapterPlayItemBinding?,
                item: TestAlbum.TestMusic?,
                holder: RecyclerView.ViewHolder?
            ) {
                binding?.tvTitle?.text = item?.title
                binding?.tvArtist?.text = item?.artist?.name
                Glide.with(binding?.ivCover!!.context).load(item?.coverImg).into(binding.ivCover)

                // 歌曲下标记录
                val currentIndex = PlayerManager.instance.albumIndex // 歌曲下标记录

                // 播放的标记
                binding.ivPlayStatus.setColor(
                    if (currentIndex == holder ?.adapterPosition) resources.getColor(R.color.colorAccent) else Color.TRANSPARENT
                ) // 播放的时候，右变状态图标就是红色， 如果对不上的时候，就是没有

                // 点击Item
                binding.root.setOnClickListener { v ->
                    Toast.makeText(mContext, "播放音乐", Toast.LENGTH_SHORT).show()
                    PlayerManager.instance.playAudio(holder !!.adapterPosition)
                }
            }




        }

        mainBinding?.rv?.adapter = adapter



        PlayerManager.instance.changeMusicLiveData.observe(viewLifecycleOwner,{
            adapter?.notifyDataSetChanged()
        })

        if(PlayerManager.instance.album == null){
            musicRequestViewModel!!.requestFreeMusics()
        }

        musicRequestViewModel!!.freeMusicesLiveData!!.observe(viewLifecycleOwner,{musicAlbum: TestAlbum? ->
            if (musicAlbum != null && musicAlbum.musics != null) {
                // 这里特殊：直接更新UI，越快越好
                adapter ?.list = musicAlbum.musics // 数据加入适配器
                adapter ?.notifyDataSetChanged()

                // 播放相关的业务需要这个数据
                if (PlayerManager.instance.album == null ||
                    PlayerManager.instance.album !!.albumId != musicAlbum.albumId) {
                    PlayerManager.instance.loadAlbum(musicAlbum)
                }
            }

        })


    }

    inner class ClickProxy{
        // 当在首页点击 “菜单” 的时候，直接导航到 ---> 菜单的Fragment界面
        fun openMenu() {
            sharedViewModel.openOrCloseDrawer.value = true
            sharedViewModel.enableSwipeDrawer.value = true
        } // 间接通过共享VM 触发到 openDrawer 触发到 @BindingAdapter

        // 当在首页点击 “搜索图标” 的时候，直接导航到 ---> 搜索的Fragment界面
        fun search() = nav().navigate(R.id.action_mainFragment_to_searchFragment)
    }
}