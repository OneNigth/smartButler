package com.yj.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yj.smartbutler.R;
import com.yj.smartbutler.empty.CouriserData;
import com.yj.smartbutler.utils.LogUtils;

import java.util.List;

/**
 * Created by yj on 2018/4/16.
 * 功能 快递物流详情适配器
 */

public class CourierListAdapter extends BaseAdapter {
    private List<CouriserData> mList;//数据
    private ViewHolder viewHolder;
    private LayoutInflater inflater;//布局
    private Context context;
    private CouriserData mData;//物流信息

    public CourierListAdapter(Context context, List<CouriserData> mList) {
        this.context = context;
        this.mList = mList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_courier,null);
            viewHolder.mRemarkTv = convertView.findViewById(R.id.courier_remark_tv);
            viewHolder.mTimeTv = convertView.findViewById(R.id.courier_time_tv);
            viewHolder.mZoneTv = convertView.findViewById(R.id.courier_zone_tv);
            //设置缓存
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mData = mList.get(position);

        LogUtils.d(mData.datetime+mData.zone+mData.remark);
        viewHolder.mTimeTv.setText(mData.datetime);
        viewHolder.mZoneTv.setText(mData.zone);
        viewHolder.mRemarkTv.setText(mData.remark);

        return convertView;
    }

    class ViewHolder {
        public TextView mTimeTv;//时间
        public TextView mRemarkTv;//详情
        public TextView mZoneTv;//区
    }
}
