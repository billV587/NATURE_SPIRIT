package scripts.model;

public class ItemInfo {

	public ItemInfo(int num, int price, int addNum, String... name) {
		this.name = name;
		this.num = num;
		this.price = price;
		this.addNum = addNum;
	}

	public String[] name;
	public int num;
	public int price;
	public int addNum;
}
