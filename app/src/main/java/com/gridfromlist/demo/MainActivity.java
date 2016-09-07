package com.gridfromlist.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gridfromlist.demo.adapter.GridAdapterTest;
import com.gridfromlist.demo.bean.TestBean;
import com.gridfromlist.demo.view.MyGridView;
import com.gridfromlist.demo.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener {

    private PullToRefreshView main_pull_refresh_view;
    private MyGridView my_gridview;
    //private GridAdapter gridAdapter;
    private List<TestBean> testBeens;
    private GridAdapterTest gridAdapterTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_pull_refresh_view = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
        my_gridview = (MyGridView) findViewById(R.id.my_gridview);
        initData();

    }

    private void initData() {
        testBeens = new ArrayList<>();
        addData();
        gridAdapterTest = new GridAdapterTest(testBeens, this);
        my_gridview.setAdapter(gridAdapterTest);
        main_pull_refresh_view.setOnHeaderRefreshListener(this);
        main_pull_refresh_view.setOnFooterRefreshListener(this);
    }

    private void addData() {
        testBeens.clear();
        TestBean testBean = new TestBean();
        for (int i = 0; i < 34; i++) {
            testBean.setTestId(i);
            testBean.setTestName("王小鱼 " + i + "号");
            testBean.setTestMoney(i * i);
            testBeens.add(testBean);
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        main_pull_refresh_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                main_pull_refresh_view.onFooterRefreshComplete();
                gridAdapterTest.addData(testBeens);
                gridAdapterTest.notifyDataSetChanged();
            }
        }, 3000);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        main_pull_refresh_view.postDelayed(new Runnable() {
            @Override
            public void run() {
                addData();
                gridAdapterTest.addData(testBeens);
                main_pull_refresh_view.onHeaderRefreshComplete();
            }
        }, 3000);
    }
}
