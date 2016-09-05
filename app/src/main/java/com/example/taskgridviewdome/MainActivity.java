package com.example.taskgridviewdome;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.taskgridviewdome.DataLoader.OnCompletedListener;
import com.example.taskgridviewdome.bean.GoodsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity implements OnCompletedListener, OnClickListener {
    //private String url;
    private int pageNumber = 1;
    private GridView gridview;
    private DataLoader loader;
    private int page = 1;
    public final static int PAGE_SIZE = 10; // 每次加载10个item
    private ItemAdapter adapter;
    private boolean isLoadFinished;


    private static ArrayList<GoodsItem> items = new ArrayList<GoodsItem>();
    private static GoodsItem goodsItem;
    //private NetState netState;

//	private Runnable runnable_first = new Runnable()
//	{
//		@Override
//		public void run()
//		{
//			Message message = handler_first.obtainMessage();
//			message.sendToTarget();
//		}
//	};
//	private Handler handler_first = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			searchGoods();
//		}
//	};
//	/**
//	 * 开启商品请求线程
//	 */
//	private Runnable gaoods_run = new Runnable()
//	{
//		@Override
//		public void run()
//		{
//			// 拿到数据
//			String data = NetService.getHttpService(url);
//			if (data == null)
//			{
//				Message message = goods_handler.obtainMessage();
//				message.what = 3;
//				message.sendToTarget();
//			}
//			else
//			{
//				if (data.length()<3)
//				{
//					Message message = goods_handler.obtainMessage();
//					message.what = 1;
//					message.sendToTarget();
//				}
//				else
//				{
//					// 传递数据
//					Message message = goods_handler.obtainMessage();
//					message.what = 2;
//					message.obj = data;
//					message.sendToTarget();
//				}
//
//			}
//
//		}
//	};
    /**
     * 处理数据
     */
    private Handler goods_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    String jsonDate = msg.obj.toString();
                    // listView适配
                    showGoodsList(parseJson(jsonDate));
                    break;
                case 3:
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_gridview);

        //netState = new NetState();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //getApplicationContext().registerReceiver(netState, intentFilter);

        init();
    }

    private void init()

    {
        items.removeAll(items);
        //url = "http://113.106.94.171:8088//portal/app/ProductAPI/productList.htm?key=感冒&pageNumber=" + pageNumber;
        gridview = (GridView) findViewById(R.id.gridview);

//		Thread thread=new Thread(runnable_first);
//		thread.start();

        showGoodsList(items);


    }


    private void showGoodsList(ArrayList<GoodsItem> parseJson) {


        adapter = new ItemAdapter(MainActivity.this, items);
        adapter.setOnFooterViewClickListener(MainActivity.this);
        gridview.setAdapter(adapter);


        gridview.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 滚动到底部自动加载(很重要)
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == (view.getCount() - 1) && !isLoadFinished && adapter.getFooterView().getStatus() != FooterView.LOADING) {
                        loadMoreData();// 加载数据

                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        loader = new DataLoader(MainActivity.this);
        loader.setOnCompletedListerner(this);
        loader.startLoading(page, items);
    }


//	private void searchGoods()
//	{
//		Thread thread=new Thread(gaoods_run);
//		thread.start();
//	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.foot_view_layout:
                if (adapter != null && adapter.getFooterView().getStatus() == FooterView.MORE) {
                    loadMoreData();
                }
                break;
            case R.id.footview_button:
                loadMoreData();
                break;
        }

    }

    /**
     * 加载下个页码的数据
     */
    private void loadMoreData() {
        if (loader != null) {
            page = page + 1;
            if (adapter != null) {
                adapter.setFooterViewStatus(FooterView.LOADING);
            }
//			// 在添加数据之前删除最后的伪造item
//			if (adapter.isFooterViewEnable()&&items.size()>0)
//			{
//				items.remove(items.get(items.size() - 1));
//			}

            loader.startLoading(page, items);
        }
    }


    @Override
    public void onCompletedSucceed(ArrayList<GoodsItem> l) {
        Log.i("gzeyao", "sadasd" + l.size());

        // 在添加数据之前删除最后的伪造item
        if (adapter.isFooterViewEnable()) {
            items.remove(items.get(items.size() - 1));
        }

        if (l.size() == 0) {
            isLoadFinished = true;
            adapter.setFootreViewEnable(false);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "暂无更多～", Toast.LENGTH_LONG).show();
        }
        // 分页加载
        else if (l.size() > 0 && l.size() < PAGE_SIZE) {
            // 如果加载出来的数目小于指定条数，可视为已全部加载完成
            isLoadFinished = true;
            items.addAll(l);
            // 伪造一个空项来构造一个footerview;
            goodsItem = new GoodsItem();
            items.add(goodsItem);
            adapter.setFootreViewEnable(false);
            adapter.notifyDataSetChanged();
        } else {
            // 还有数据可加载。
            items.addAll(l);
            // 伪造一个空项来构造一个footerview;
            goodsItem = new GoodsItem();
            items.add(goodsItem);
            adapter.setFootreViewEnable(true);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCompletedFailed(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();

    }

    @Override
    public void getCount(int count) {
        // TODO Auto-generated method stub

    }


    /**
     * 将Json数据解析为集合对象
     *
     * @param strResult
     * @return
     */
    public static ArrayList<GoodsItem> parseJson(String strResult) {
        JSONObject jsonObj;
        try {
            JSONArray arr = new JSONArray(strResult);
            for (int i = 0; i < arr.length(); i++) {
                jsonObj = (JSONObject) arr.get(i);
                String name = jsonObj.optString("CHEMICALNAME");
                String imageUrl = jsonObj.optString("IMAGE");
                String norms = jsonObj.optString("PRODUECTSPECIFICATION");
                String factory = jsonObj.optString("FACTORY");
                // String approve = jsonObj.optString("DOCUMENTNO");
                // String formulation = jsonObj.optString("FORMULATION");
                String time = jsonObj.optString("UPDATEDATE");
                String price = jsonObj.optString("UNITPRICE");
                String itemPrice = jsonObj.optString("ITEMPRICE");
                String itemNum = jsonObj.optString("ITEMNUM");
                String num = jsonObj.optString("UNITNUM");
                String itemNorms = jsonObj.optString("PIECEWORKSPECIFICATION");
                String sell = jsonObj.optString("UNITSELLNUM");
                String itemSell = jsonObj.optString("ITEMSELLNUM");
                String provider = jsonObj.optString("GYS");
                int instructions = jsonObj.optInt("INSTRUCTIONS");
                int baseId = jsonObj.optInt("ID");
                int productcode = jsonObj.optInt("PRODUECTCODE");
                Log.i("gzeyao", provider);
                goodsItem = new GoodsItem();
                goodsItem.id = i;
                goodsItem.title = name;
                goodsItem.price = price;
                goodsItem.itemPrice = itemPrice;
                goodsItem.baseId = baseId;
                goodsItem.img = imageUrl;
                goodsItem.norms = norms;
                goodsItem.itemNorms = itemNorms;
                goodsItem.num = num;
                goodsItem.itemNum = itemNum;
                goodsItem.sell = sell;
                goodsItem.itemSell = itemSell;
                goodsItem.factory = factory;
                goodsItem.productcode = productcode;
                goodsItem.time = time;
                goodsItem.instructions = instructions;
                goodsItem.provider = provider;

                items.add(goodsItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }
}
