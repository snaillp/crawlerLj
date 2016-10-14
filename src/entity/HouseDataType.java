package entity;

public enum HouseDataType {
	FangyuanData,            //房源信息
	HouseDayStat,            //房价，成交等每天的统计数据
	HousePriceMonthStat,     //房价价格每月统计信息
	HouseQuantityMonthStat,  //成交量每月统计
	HouseSupDemMonthStat,    //供需每月统计
	HouseSupDemDayStat,      //供需每天统计
	HouseDistricDayStat;     //各个区均价，成交量每天统计
}
