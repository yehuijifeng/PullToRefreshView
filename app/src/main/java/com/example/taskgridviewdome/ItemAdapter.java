package com.example.taskgridviewdome;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.taskgridviewdome.bean.GoodsItem;

import java.util.ArrayList;

/**
 * 数据适配器
 *
 * @author Administrator
 *
 */
public class ItemAdapter extends BaseAdapter {

	private ArrayList<GoodsItem> items;
	private Context mContext;

	private String footerviewItem;

	private FooterView footerView;

	private boolean footerViewEnable = false;
	private OnClickListener ml;

	public ItemAdapter(Context context, ArrayList<GoodsItem> items) {
		this.items = items;
		this.mContext = context;

	}

	public boolean isFooterViewEnable() {
		return footerViewEnable;
	}

	/**
	 * 存放列表项控件句柄
	 */
	public static class ViewHolder {

		//SmartImageView goodsImage;
		TextView goodsTitle;
		TextView goodsPrice;
		TextView goodsItemPrice;
		TextView goodsNorms;

	}

	public void setFootreViewEnable(boolean enable) {
		footerViewEnable = enable;
	}

	public void setOnFooterViewClickListener(OnClickListener l) {
		ml = l;
	}

	private int getDisplayWidth(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		return width;
	}

	@Override
	public View getView(final int i, View convertView, ViewGroup parent) {
		// 伪造的空项可以根据楼盘id来确定。
		if (footerViewEnable && i == items.size() - 1) {
			if (footerView == null) {
				footerView = new FooterView(parent.getContext());
				GridView.LayoutParams pl = new GridView.LayoutParams(getDisplayWidth((Activity) mContext),
						LayoutParams.WRAP_CONTENT);
				footerView.setLayoutParams(pl);
				footerView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (ml != null) {
							ml.onClick(v);
						}
					}
				});
			}
			setFooterViewStatus(FooterView.MORE);
			return footerView;
		}
		final ViewHolder holder;

		if (convertView == null || (convertView != null && convertView == footerView)) {

			convertView = LayoutInflater.from(mContext).inflate(R.layout.home_goods_list_page_item, null);
			holder = new ViewHolder();
			//holder.goodsImage = (SmartImageView) convertView.findViewById(R.id.goods_image);
			holder.goodsTitle = (TextView) convertView.findViewById(R.id.goods_title);
			holder.goodsPrice = (TextView) convertView.findViewById(R.id.goods_price);
			holder.goodsItemPrice = (TextView) convertView.findViewById(R.id.goods_itemprice);
			holder.goodsNorms = (TextView) convertView.findViewById(R.id.goods_norms);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//String imageUrl = items.get(i).img;
		holder.goodsTitle.setText(items.get(i).title);
		holder.goodsNorms.setText("(" + items.get(i).norms + ")");
		holder.goodsPrice.setText("￥ " + items.get(i).price);
		holder.goodsItemPrice.setText("￥ " + items.get(i).itemPrice);
		//holder.goodsImage.setImageUrl(items.get(i).img);
		return convertView;
	}

	public FooterView getFooterView() {
		return footerView;
	}

	public void setFooterViewStatus(int status) {
		if (footerView != null) {
			footerView.setStatus(status);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
