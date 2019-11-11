package com.paper.boat.zrdx.ui.fragment.apply;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.paper.boat.dream.R;
import com.paper.boat.zrdx.common.MyLazyFragment;
import com.paper.boat.zrdx.ui.adapter.FlowTagAdapter;
import com.paper.boat.zrdx.view.layout.FlowTagLayout;
import com.paper.boat.zrdx.widget.text.SpannableFoldTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * SnapHelper*/
public class DetailsFragment extends MyLazyFragment {
    @BindView(R.id.image_recycle)
    RecyclerView mImageRecycle;
    LinearLayoutManager mPagerLayoutManager;
    PagerSnapHelper mSnapHelper;
    @BindView(R.id.fold_text)
    SpannableFoldTextView mFoldText;

    @BindView(R.id.flowlayout_normal_select)
    FlowTagLayout mNormalFlowTagLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_details;
    }

    @Override
    protected void initView() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        mPagerLayoutManager = new LinearLayoutManager( getContext(), LinearLayoutManager.HORIZONTAL, false );
        mImageRecycle.setLayoutManager( mPagerLayoutManager );
        mImageRecycle.setAdapter( adapter );

        List <Integer> list = new ArrayList <>();
        list.add( R.mipmap.uoko_guide_background_1 );
        list.add( R.mipmap.uoko_guide_background_2 );
        list.add( R.mipmap.uoko_guide_background_3 );
        adapter.replaceData( list );

        mFoldText.setText( R.string.demo );
//        mFoldText.setTipColor( R.color.blue );
        // PagerSnapHelper每次只能滚动一个item;用LinearSnapHelper则可以一次滚动多个，并最终保证定位
        // mSnapHelper = new LinearSnapHelper();
//        mSnapHelper = new PagerSnapHelper();
//        mSnapHelper.attachToRecyclerView(mImageRecycle);

        FlowTagAdapter tagAdapter = new FlowTagAdapter( getContext() );
        mNormalFlowTagLayout.setAdapter( tagAdapter );
        /*设置标签检查模式*/
        //FLOW_TAG_CHECKED_SINGLE 单选 FLOW_TAG_CHECKED_MULTI 多选
        mNormalFlowTagLayout.setTagCheckedMode( FlowTagLayout.FLOW_TAG_CHECKED_SINGLE );
        mNormalFlowTagLayout.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, int position, List<Integer> selectedList) {
                toast(getSelectedText(parent, selectedList));
            }
        });
        tagAdapter.addTags( getResources().getStringArray( R.array.tags_values ) );
        tagAdapter.setSelectedPositions( 0 );
    }

    private String getSelectedText(FlowTagLayout parent, List<Integer> selectedList) {
        StringBuilder sb = new StringBuilder("选中的内容：\n");
        for (int index : selectedList) {
            sb.append(parent.getAdapter().getItem(index));
            sb.append(";");
        }
        return sb.toString();
    }

    BaseQuickAdapter <Integer, BaseViewHolder> adapter = new BaseQuickAdapter <Integer, BaseViewHolder>( R.layout.list_image_horizontal ) {
        @Override
        protected void convert(BaseViewHolder helper, Integer item) {
            helper.setImageResource( R.id.img, item );
        }
    };
}
