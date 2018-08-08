package com.mm.luna.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.library.view.LabelView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mm.luna.R;
import com.mm.luna.bean.HotMovieBean;
import com.mm.luna.ui.douban.MovieDetailActivity;

import java.util.List;

/**
 * Created by ZMM on 2018/8/2 15:29.
 */

public class HotMovieAdapter extends BaseQuickAdapter<HotMovieBean.SubjectsBean, BaseViewHolder> {
    private int position;

    public HotMovieAdapter(int layoutResId, @Nullable List<HotMovieBean.SubjectsBean> data, int position) {
        super(layoutResId, data);
        this.position = position;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotMovieBean.SubjectsBean item) {
        helper.setText(R.id.tv_movie_title, item.getTitle());
        helper.setVisible(R.id.label_view, position == 2);
        ((LabelView) helper.getView(R.id.label_view)).setText("TOP" + (helper.getAdapterPosition() + 1));
        helper.setText(R.id.tv_director, "导演：" + list2Str(item.getDirectors()));
        helper.setText(R.id.tv_actor, "主演：" + list2Str(item.getCasts()));
        helper.setText(R.id.tv_type, "类型：" + item.getGenres().toString());
        helper.setText(R.id.tv_score, "评分：" + item.getRating().getAverage());
        Glide.with(mContext).load(item.getImages().getLarge()).crossFade().into((ImageView) helper.getView(R.id.iv_movie));
        helper.itemView.setOnClickListener(v -> {
            MovieDetailActivity.start((Activity) mContext, item, helper.getView(R.id.iv_movie));
        });
    }

    private String list2Str(List<HotMovieBean.SubjectsBean.CastsBean> list) {
        if (list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (HotMovieBean.SubjectsBean.CastsBean bean : list) {
            sb.append(bean.getName()).append("/");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
