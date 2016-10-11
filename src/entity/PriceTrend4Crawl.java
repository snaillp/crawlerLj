package entity;

import java.util.List;

public class PriceTrend4Crawl {
	public class CurrentLevel {
		public class Quantity{
			private List<Integer> bed_1;
			private List<Integer> bed_2;
			private List<Integer> bed_3;
			private List<Integer> other;
			private List<Integer> total; //成交量
			
//			public int getLastestQuantity(){
//				if()
//			}
			public List<Integer> getBed_1() {
				return bed_1;
			}
			public void setBed_1(List<Integer> bed_1) {
				this.bed_1 = bed_1;
			}
			public List<Integer> getBed_2() {
				return bed_2;
			}
			public void setBed_2(List<Integer> bed_2) {
				this.bed_2 = bed_2;
			}
			public List<Integer> getBed_3() {
				return bed_3;
			}
			public void setBed_3(List<Integer> bed_3) {
				this.bed_3 = bed_3;
			}
			public List<Integer> getOther() {
				return other;
			}
			public void setOther(List<Integer> other) {
				this.other = other;
			}
			public List<Integer> getTotal() {
				return total;
			}
			public void setTotal(List<Integer> total) {
				this.total = total;
			}
			
		}
		public class Price{
			private List<Double> bed_1;
			private List<Double> bed_2;
			private List<Double> bed_3;
			private List<Double> other;
			private List<Double> total;
			public List<Double> getBed_1() {
				return bed_1;
			}
			public void setBed_1(List<Double> bed_1) {
				this.bed_1 = bed_1;
			}
			public List<Double> getBed_2() {
				return bed_2;
			}
			public void setBed_2(List<Double> bed_2) {
				this.bed_2 = bed_2;
			}
			public List<Double> getBed_3() {
				return bed_3;
			}
			public void setBed_3(List<Double> bed_3) {
				this.bed_3 = bed_3;
			}
			public List<Double> getOther() {
				return other;
			}
			public void setOther(List<Double> other) {
				this.other = other;
			}
			public List<Double> getTotal() {
				return total;
			}
			public void setTotal(List<Double> total) {
				this.total = total;
			}
			
		}
		private List<String> month;
		private Quantity quantity;    //成交量
		private Price listPrice;     //挂牌价
		private Price dealPrice;     //成交价格
		public List<String> getMonth() {
			return month;
		}
		public void setMonth(List<String> month) {
			this.month = month;
		}
		public Quantity getQuantity() {
			return quantity;
		}
		public void setQuantity(Quantity quantity) {
			this.quantity = quantity;
		}
		public Price getListPrice() {
			return listPrice;
		}
		public void setListPrice(Price listPrice) {
			this.listPrice = listPrice;
		}
		public Price getDealPrice() {
			return dealPrice;
		}
		public void setDealPrice(Price dealPrice) {
			this.dealPrice = dealPrice;
		}
	}
	private CurrentLevel currentLevel;
	public CurrentLevel getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(CurrentLevel currentLevel) {
		this.currentLevel = currentLevel;
	}
	
}
