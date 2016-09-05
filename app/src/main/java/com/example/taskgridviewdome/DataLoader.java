package com.example.taskgridviewdome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.taskgridviewdome.bean.GoodsItem;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

public class DataLoader
{
	private Context context;
	private OnCompletedListener l;
	private static ArrayList<GoodsItem> items=new ArrayList<GoodsItem>();
	private static GoodsItem goodsItem;
	int page;


	public DataLoader(Context mContext)
	{
		this.context = mContext;

	}

	public void setOnCompletedListerner(OnCompletedListener mL)
	{
		l = mL;
	}

	public void startLoading(int page,ArrayList<GoodsItem> items)
	{
		this.page=page;
		//this.items=items;
		Thread thread=new Thread(gaoods_run);
		thread.start();
	}

	/**
	 * 开启商品请求线程
	 */
	private Runnable gaoods_run = new Runnable()
	{
		@Override
		public void run()
		{
			String url="http://113.106.94.171:8088/portal/app/ProductAPI/productList.htm?key=感冒&pageNumber="+page;
			// 拿到数据
			String data = NetService.getHttpService(url);
			if (data == null)
			{
				Message message = goods_handler.obtainMessage();
				message.what = 3;
				message.sendToTarget();
			}
			else
			{
				if (data.length()<3)
				{
					Message message = goods_handler.obtainMessage();
					message.what = 1;
					message.sendToTarget();
				}
				else
				{
					// 传递数据
					Message message = goods_handler.obtainMessage();
					message.what = 2;
					message.obj = data;
					message.sendToTarget();
				}

			}

		}
	};
	/**
	 * 处理数据
	 */
	private Handler goods_handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 1:
					l.onCompletedFailed("暂无更多～");
					break;
				case 2:
					String jsonDate = msg.obj.toString();
					// listView适配
					items=parseJson(jsonDate);
					l.onCompletedSucceed(items);
					break;
				case 3:
					l.onCompletedFailed("加载失败～");
					break;

				default:
					break;
			}
		}
	};

	public interface OnCompletedListener
	{
		/**
		 *
		 * @param list
		 *            列表
		 */
		public void onCompletedSucceed(ArrayList<GoodsItem> items);

		/**
		 * 失败
		 *
		 * @param str
		 */
		public void onCompletedFailed(String str);

		/**
		 * 总数
		 *
		 * @param count
		 *            数量
		 */
		public void getCount(int count);
	}

	/**
	 * 将Json数据解析为集合对象
	 *
	 * @param strResult
	 * @return
	 */
	public static ArrayList<GoodsItem> parseJson(String strResult)
	{
		items.removeAll(items);
		JSONObject jsonObj;
		try
		{
			JSONArray arr = new JSONArray(strResult);
			for (int i = 0; i < arr.length(); i++)
			{
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
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return items;
	}
}
