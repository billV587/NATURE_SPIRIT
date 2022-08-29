package scripts.model;

public class ExchangeItemInfo {

	public ExchangeItemInfo(int id, int num, int price, int addNum, String... name) {
		this.id = id;
		this.num = num;
		this.price = price;
		this.addNum = addNum;
		this.name = name;
	}

	public int id;
	public String[] name;
	public int num;
	public int price;
	public int addNum;

	public static ExchangeItemInfo to(int id, int num, int price, int addNum, String... name) {
		return new ExchangeItemInfo(id, num, price, addNum, name);
	}

}
