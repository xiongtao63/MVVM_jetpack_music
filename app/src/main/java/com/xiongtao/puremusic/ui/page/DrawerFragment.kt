package com.xiongtao.puremusic.ui.page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.xiongtao.architecture.ui.adapter.SimpleBaseBindingAdapter
import com.xiongtao.puremusic.R
import com.xiongtao.puremusic.bridge.callback.SharedViewModel
import com.xiongtao.puremusic.bridge.data.bean.LibraryInfo
import com.xiongtao.puremusic.bridge.request.InfoRequestViewModel
import com.xiongtao.puremusic.bridge.state.DrawerViewModel
import com.xiongtao.puremusic.databinding.AdapterLibraryBinding
import com.xiongtao.puremusic.databinding.FragmentDrawerBinding
import com.xiongtao.puremusic.ui.base.BaseFragment

/**
 * @Description:
 * @Author: xiongtao
 * @Date: 2023-03-17 15:01
 */
class DrawerFragment :BaseFragment(){

    private var mBinding : FragmentDrawerBinding?=null

    private var mDrawerViewModel: DrawerViewModel?= null
    private var mInfoRequestViewModel:InfoRequestViewModel?=null

    private var mAdapter:SimpleBaseBindingAdapter<LibraryInfo?,AdapterLibraryBinding?>? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDrawerViewModel = getFragmentViewModelProvider(this).get(DrawerViewModel::class.java)
        mInfoRequestViewModel = getFragmentViewModelProvider(this).get(InfoRequestViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drawer, container, false)
        mBinding = FragmentDrawerBinding.bind(view)
        mBinding?.vm = mDrawerViewModel
        mBinding?.click = ClickProxy()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = object : SimpleBaseBindingAdapter<LibraryInfo?,AdapterLibraryBinding?>(context,R.layout.adapter_library){
            override fun onSimpleBindItem(
                binding: AdapterLibraryBinding?,
                item: LibraryInfo?,
                holder: RecyclerView.ViewHolder?
            ) {
                // 把数据 设置好，就显示数据了
                binding?.info = item
                binding?.root?.setOnClickListener {
                    Toast.makeText(mContext, "哎呀，还在研发中，猴急啥?...", Toast.LENGTH_SHORT).show()
                }
            }

        }
        // 设置适配器 到 RecyclerView
        mBinding?.rv?.adapter = mAdapter
// 观察者 监听 眼睛 数据发送改变，UI就马上刷新
        // 观察这个数据的变化，如果 libraryLiveData 变化了，我就要要变，我就要更新到 RecyclerView
        mInfoRequestViewModel?.libraryLiveData?.observe(viewLifecycleOwner,{libraryInfos ->
            // 以前是 间接的通过 状态VM 修改
            // mDrawerViewModel.xxx.setValue = libraryInfos

            // 这里特殊：直接更新UI，越快越好
            mAdapter ?.list = libraryInfos
            mAdapter?.notifyDataSetChanged()
        })

        mInfoRequestViewModel?.requestLibraryInfo()
    }


    inner class ClickProxy {
        fun logoClick() = Toast.makeText(mActivity, "哎呀，你能不能不要乱点啊，程序员还在玩命编码中...", Toast.LENGTH_SHORT).show()
    }
}