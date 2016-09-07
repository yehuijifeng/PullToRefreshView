package com.gridfromlist.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.gridfromlist.demo.R;
import com.gridfromlist.demo.bean.TestBean;

import java.util.List;

/**
 * lsitView的适配器
 *
 * @author lyy
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    private List<TestBean> data;

    /***
     * listview item position==2
     * <p>
     * GridView里面的数据
     */
    private List<TestBean> gridViewData;
    //private GridViewAdapter gridViewAdapter;

    /**
     * ListView 的数据
     **/
    public void setData(List<TestBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    /***
     * gridView的数据
     **/

    public void setGridViewData(List<TestBean> gridViewData) {
        this.gridViewData = gridViewData;
        this.notifyDataSetChanged();
    }

    public MyAdapter(Context context) {
        this.context = context;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            return listView();
        } else {
            return gridView();
        }

    }

    public View listView() {
        View convertView = LayoutInflater.from(context).inflate(
                R.layout.item_test_one, null);
        TextView test_id_text = (TextView) convertView.findViewById(R.id.test_id_text);
        TextView test_name_text = (TextView) convertView.findViewById(R.id.test_name_text);
        TextView test_money_text = (TextView) convertView.findViewById(R.id.test_money_text);
        return convertView;
    }

    public View gridView() {

        View view = LayoutInflater.from(context).inflate(R.layout.item1, null);
        GridView myGridView = (GridView) view.findViewById(R.id.gridView1);
//        if (gridViewAdapter == null)
//            gridViewAdapter = new GridViewAdapter(context);
//        gridViewAdapter.setData(gridViewData);
//        myGridView.setAdapter(gridViewAdapter);
        myGridView.setOnItemClickListener(onItemClickListener);
        return view;
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            Toast.makeText(context, "你点击的是：" + position, Toast.LENGTH_LONG).show();
        }

    };
}
