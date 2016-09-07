package com.gridfromlist.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gridfromlist.demo.R;
import com.gridfromlist.demo.bean.TestBean;

import java.util.List;

/**
 * Created by Luhao on 2016/9/6.
 */
public class GridAdapterTest extends BaseAdapter {
    private List<TestBean> testBeans;
    private Context context;

    public GridAdapterTest(List<TestBean> testBeans, Context context) {
        this.testBeans = testBeans;
        this.context = context;
    }

    public void addData(List<TestBean> testBeans) {
        this.testBeans.addAll(testBeans);
    }

    public void updateData(List<TestBean> testBeans) {
        this.testBeans = testBeans;
    }

    @Override
    public int getCount() {
        return testBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return testBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_test_one, null);
            viewHolder.test_id_text = (TextView) convertView
                    .findViewById(R.id.test_id_text);
            viewHolder.test_name_text = (TextView) convertView
                    .findViewById(R.id.test_name_text);
            viewHolder.test_money_text = (TextView) convertView
                    .findViewById(R.id.test_money_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TestBean testBean = testBeans.get(position);
        viewHolder.test_id_text.setText("编号：" + testBean.getTestId());
        viewHolder.test_name_text.setText(testBean.getTestName());
        viewHolder.test_money_text.setText("￥ " + testBean.getTestMoney());
        return convertView;
    }

    class ViewHolder {
        private TextView test_id_text, test_name_text, test_money_text;
    }
}
