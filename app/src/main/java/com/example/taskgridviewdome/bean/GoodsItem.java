package com.example.taskgridviewdome.bean;

import java.io.Serializable;

public class GoodsItem implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int id;
	//标题
	public String title;
	//图片
	public String img;
	//价格
	public String price;
	//件价格
	public String itemPrice;
	//规格
	public String norms;
	//件规格
	public String itemNorms;
	//件库存
	public String itemNum;
	//总库存
	public String num;
	//件销售量
	public String itemSell;
	//销售量
	public String sell;
	//厂商
	public String factory;
	//种类
	public String category;
	//地址
	public String address;
	//批准文号
	public String approve;
	//剂型
	public String formulation;
	//条形码
	public String barcode;
	//产品编码
	public int productcode;
	//产品id
	public int baseId;
	//收藏时间
	public String time;
	//供应商
	public String provider;
	//说明书id
	public int instructions;


}
