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
 * Created by Luhao on 2016/9/5.
 * 将listview改成gridview的适配器
 */
public class GridAdapter extends BaseAdapter {

    private List<TestBean> testBeans;
    private int number;
    private Context context;

    public GridAdapter(List<TestBean> testBeans, int number, Context context) {
        this.testBeans = testBeans;
        this.number = number;
        this.context = context;
    }

    public void addData(List<TestBean> testBeans) {
        testBeans.addAll(testBeans);
    }

    @Override
    public int getCount() {
        // 每列需要的多少根据传递进来的列数为准
        if (testBeans.size() % number == 0)
            return testBeans.size() / number;
        return testBeans.size() / 2 + 1;//如果item有多余没被整除的，则再加一行
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
        //position计算  两列的情况下：position* 2 +1 多列依次类推
        for (int i = 0; i < number; i++) {
            if (testBeans.size() <= position * number + i)
                continue;
            TestBean testBean = testBeans.get(position * number + i);
            viewHolder.test_id_text.setText(testBean.getTestId());
            viewHolder.test_name_text.setText(testBean.getTestName());
            viewHolder.test_money_text.setText("￥ " + testBean.getTestMoney());

        }

        return convertView;
    }

    class ViewHolder {
        private TextView test_id_text, test_name_text, test_money_text;
    }
}
