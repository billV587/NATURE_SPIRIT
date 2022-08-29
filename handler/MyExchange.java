package scripts.handler;

import org.tribot.api.input.Keyboard;
import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Equipment;
import org.tribot.script.sdk.GrandExchange;
import org.tribot.script.sdk.GrandExchange.CollectMethod;
import org.tribot.script.sdk.GrandExchange.CreateOfferConfig;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Login;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GrandExchangeOffer;
import org.tribot.script.sdk.types.GrandExchangeOffer.Type;

import dax.teleports.Teleport;
import scripts.handler.MyBank.BankItem;
import scripts.model.ExchangeItemInfo;
import scripts.model.RSItemPriceMode;

public class MyExchange extends org.tribot.api2007.GrandExchange {

	/**
	 * 移动
	 * 
	 * @return
	 */
	public static boolean moveTo() {
		if (GrandExchange.isNearby()) {
			return true;
		}else if(!MyPlayer.isMember()){
			if(Tools.inBox(3061, 3517, 3405, 3186)){
				if(Tools.inBox(3204, 3230, 3216, 3207,1)||Tools.inBox(3204, 3230, 3216, 3207,2)){
					Tools.walkto(3206, 3228,0);
				}else if(Tools.inBox(3194, 3279, 3252, 3198)){
					Tools.walkto(3239, 3292);
				}else{
					Tools.walkto(3166, 3487);
				}
			}else{
				MyBank.close();
				Teleport.LUMBRIDGE_HOME_TELEPORT.trigger();
				Waiting.waitNormal(3500, 350);
			}
		}else if (!Tools.invIsHave(Tools.ringOfWeathName) && !Equipment.contains(Tools.ringOfWeath)
				&& MyBank.contains(Tools.ringOfWeathName)) {
			MyBank.deposit(true, false, false, BankItem.to(1, Tools.ringOfWeathName));
			return false;
		}else if (MyPlayer.getTile().getPlane() > 0) {
			MyBank.close();
			if (Tools.invIsHave(Tools.ringOfWeathName)) {
				Tools.invClick("Rub", Tools.ringOfWeathName);
				Waiting.waitNormal(1350, 150);
				Keyboard.typeSend("2");
				Waiting.waitNormal(5350, 150);
			} else if (Equipment.contains(Tools.ringOfWeathName)) {
				Equipment.Slot.NECK.getItem().map(g -> g.click("Grand Exchange"));
				Waiting.waitNormal(5350, 150);
			} else if (Tools.invIsHave(8007, 8008, 8010)) {
				Tools.invClick("Break", 8007, 8008, 8010);
				Waiting.waitNormal(5500, 350);
			}else if(Tools.inBox(3204, 3230, 3216, 3207,1)||Tools.inBox(3204, 3230, 3216, 3207,2)){
				Tools.walkto(3206, 3228,0);
			} else{
				Teleport.LUMBRIDGE_HOME_TELEPORT.trigger();
				Waiting.waitNormal(3500, 350);
			}
			return false;
		} else{
			Tools.walkto(3166, 3487);
		}
		return false;
	}

	/**
	 * 出售东西
	 * 
	 * @param id
	 * @param name
	 * @param num
	 * @param price
	 * @param adjust
	 * @return
	 */
	public static boolean sell(int id, int num, int adjust) {
		if (id == 995) {
			return true;
		}
		CreateOfferConfig config = GrandExchange.CreateOfferConfig.builder().itemId(id).quantity(num)
				.priceAdjustment(adjust).type(Type.SELL).build();
		return GrandExchange.placeOffer(config);
	}

	public static boolean sellByMoney(int id, int num, int money) {
		if (id == 995) {
			return true;
		}
		CreateOfferConfig config = GrandExchange.CreateOfferConfig.builder().itemId(id).quantity(num).price(money)
				.type(Type.SELL).build();
		return GrandExchange.placeOffer(config);
	}

	/**
	 * 买物品
	 * 
	 * @param id
	 * @param name
	 * @param num
	 * @param price
	 * @param adjust
	 * @return
	 */
	public static boolean buy(int id, String name, int num, int price, int adjust) {
		if (id == 995) {
			return true;
		}
		if (price > 0) {
			CreateOfferConfig config = GrandExchange.CreateOfferConfig.builder().itemId(id).quantity(num)
					.searchText(name).price(price).type(Type.BUY).build();
			return GrandExchange.placeOffer(config);
		} else {
			CreateOfferConfig config = GrandExchange.CreateOfferConfig.builder().itemId(id).quantity(num)
					.searchText(name).priceAdjustment(adjust).type(Type.BUY).build();
			return GrandExchange.placeOffer(config);
		}

	}

	public static boolean isOpenBank = false;
	
	public static boolean isOpen(){
		return GrandExchange.isOpen() || Query.widgets().inIndexPath(465,2,11).actionContains("Close").isAny();
	}
	
	/**
	 * 购买物品
	 * 
	 * @param sellId
	 * @param buyList
	 * @return
	 */
	public static boolean buyList(int[] sellId, ExchangeItemInfo... buyList) {
		int[] newSellIds = null;
		if(sellId != null){
			newSellIds=Tools.addList(sellId, 995);
		}
		Tools.errorTime = System.currentTimeMillis() + 5 * 60 * 1000;
		while (Login.isLoggedIn()) {
			if (System.currentTimeMillis() - Tools.errorTime > 5 * 60 * 1000) {
				Tools.errorTime = System.currentTimeMillis() - 10 * 60 * 1000;
				Tools.isLoop = false;
				return false;
			}
			if (!Tools.interfaceIsHidden(664, 29)) {
				Tools.interfaceCheck(664, 29, -1, "Close");
			}
			if (moveTo()) {
				if (BankCache.isInitialized()) {
					if (sellId != null && MyBank.contains(sellId)) {
						MyBank.deposit(true, true, false, newSellIds);
					} else if (sellId != null && Tools.invNoteIsHave(sellId)) {
						if (isOpen()) {
							for (int item : sellId) {
								if(!Tools.invIsHave(item,item+1)){
									continue;
								}
								if (item == 5075) {//鸟窝
									MyExchange.sellByMoney(item, 0, -1);
									Waiting.waitUniform(1800, 150);
									MyExchange.sellByMoney(20783, 0, -1);
									Waiting.waitUniform(1800, 150);
								} else if (item == 12640) {
									MyExchange.sellByMoney(item, 0, 1);
									Waiting.waitUniform(1800, 150);
									MyExchange.sellByMoney(item, 0, 1);
									Waiting.waitUniform(1800, 150);
								} else if (item == 440 || item == 453) {
									MyExchange.sellByMoney(item+1, 0, 1);
									Waiting.waitUniform(1800, 150);
									MyExchange.sellByMoney(item+1, 0, 1);
									Waiting.waitUniform(1800, 150);
								} else if (item == 575 || item == 573 || item == 48 || item == 54 || item == 56
										|| item == 58
										|| item == 48 || item == 60 || item == 64
										|| item == 62 || item == 851 || item == 66
										|| item == 855 || item == 70 || item == 859 || item == 8007
										 || item == 8008  || item == 8009) {
									MyExchange.sellByMoney(Tools.invIsHave(item)?item:(item + 1), 0, 1);
									Waiting.waitUniform(1800, 150);
									MyExchange.sellByMoney(Tools.invIsHave(item)?item:(item + 1), 0, 1);
									Waiting.waitUniform(1800, 150);
								} else if (Tools.invIsHave(item + 1)) {
									MyExchange.sell(item + 1, 0, -2);
									Waiting.waitUniform(1800, 150);
									MyExchange.sell(item + 1, 0, -2);
									Waiting.waitUniform(1800, 150);
								} else if (Tools.invIsHave(item)) {
									MyExchange.sell(item, 0, -2);
									Waiting.waitUniform(1800, 150);
									MyExchange.sell(item, 0, -2);
									Waiting.waitUniform(1800, 150);
								}
								GrandExchange.collectAll();
							}
							Waiting.waitUniform(1800, 150);
							GrandExchange.collectAll();
							Waiting.waitUniform(1800, 150);
							GrandExchange.close();
							Waiting.waitUniform(1800, 150);
						} else {
							if (MyBank.isOpen()) {
								if (!Tools.interfaceIsHidden(664, 29)) {
									Tools.interfaceCheck(664, 29, -1, "Close");
								}
								Bank.close();
								Waiting.waitUniform(1800, 150);
							}
							GrandExchange.open();
						}
					} else if (MyBank.contains(995) || !Tools.invIsHave(995) || Tools.invNum() > 20) {
						MyBank.deposit(true, false, false, 995);
					} else if (isOpen() && cancelAllExcept(sellId)) {
						boolean isFinish = true;
						for (ExchangeItemInfo info : buyList) {
							if (info.name != null && info.num < MyBank.getCount(info.name) + Tools.invNum(info.name)
									+ Equipment.getCount(info.id)) {
								continue;
							}
							RSItemPriceMode itemInfo = HttpTools.getItemInfo(info.id);
							int num = 0;
							if (info.name != null && info.name.length > 0) {
								num = info.num - Tools.invNum(info.name) - MyBank.getCount(info.name)
										- Equipment.getCount(info.id);
							} else {
								num = info.num - Tools.invNum(itemInfo.name) - MyBank.getCount(itemInfo.name)
										- Equipment.getCount(info.id);
							}
							if (num > 0) {
								if(info.addNum > 15) info.addNum = 2;
								buy(info.id,
										itemInfo.name.substring(0,
												itemInfo.name.length() >= 9 ? 9 : itemInfo.name.length()),
										num, info.price, info.addNum++);
								Waiting.waitUniform(1800, 150);
								GrandExchange
										.collectAll(Inventory.isFull() ? CollectMethod.BANK : CollectMethod.INVENTORY);
								Waiting.waitUniform(1800, 150);
								isFinish = false;
								break;
							}
						}
						if (isFinish) {
							for (int i = 0; i < 20; i++) {
								close();
								if(Inventory.isEmpty()){
									return true;
								}else if(MyBank.isOpen()){
									Bank.depositInventory();
								}else{
									MyBank.openBank();
								}
								Waiting.waitNormal(500, 150);
							}
							return true;
						}
					} else {
						if (MyBank.isOpen()) {
							Bank.close();
							Waiting.waitUniform(1800, 150);
						}
						if(GrandExchange.open()){
							Waiting.waitUntil(3000, GrandExchange::isOpen);
						}
					}
				} else {
					Bank.open();
				}
			}
		}

		return true;
	}

	/**
	 * 打开拍卖行
	 * 
	 * @return
	 */
	public static boolean open() {
		if (isOpen()) {
			return true;
		}
		if (MyBank.isOpen()) {
			MyBank.close();
		} else if (GrandExchange.open()) {
			Waiting.waitUntil(MyExchange::isOpen);
		}
		return false;
	}

	/**
	 * 取消交易
	 * 
	 * @param ids
	 */
	public static boolean cancelAllExcept(int... ids) {
		GrandExchange.collectAll();
		if (ids == null) {
			return cancelAll();
		}
		if (Query.grandExchangeOffers().statusNotEquals(GrandExchangeOffer.Status.EMPTY).itemIdNotEquals(ids).isAny()) {
			Query.grandExchangeOffers().statusNotEquals(GrandExchangeOffer.Status.EMPTY).itemIdNotEquals(ids).stream()
					.forEach(g -> GrandExchange.abort(g.getSlot()));
			Waiting.waitNormal(2000, 150);
			GrandExchange.collectAll();
			return false;
		}
		return true;
	}

	public static boolean cancelAll() {
		GrandExchange.collectAll();
		if (Query.grandExchangeOffers().statusNotEquals(GrandExchangeOffer.Status.EMPTY).isAny()) {
			Query.grandExchangeOffers().statusNotEquals(GrandExchangeOffer.Status.EMPTY).stream()
					.forEach(g -> GrandExchange.abort(g.getSlot()));
			Waiting.waitNormal(2000, 150);
			GrandExchange.collectAll();
			return false;
		}
		return true;
	}
}
