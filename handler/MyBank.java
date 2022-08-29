package scripts.handler;

import java.util.function.BooleanSupplier;

import org.tribot.api2007.Banking;
import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.BankSettings;
import org.tribot.script.sdk.BankSettings.WithdrawQuantity;
import org.tribot.script.sdk.Equipment;
import org.tribot.script.sdk.GrandExchange;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Login;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.cache.BankCache;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.InventoryItem;

import scripts.model.TradeItemInfo;

public class MyBank extends Bank {
	public static class BankItem {
		public String[] name;
		public int id;
		public int num;

		BankItem(int id, int num) {
			this.id = id;
			this.num = num;
		}

		BankItem(int num, String... name) {
			this.name = name;
			this.num = num;
		}

		public static BankItem to(int id, int num) {
			return new BankItem(id, num);
		}

		public static BankItem to(int num, String... name) {
			return new BankItem(num, name);
		}

	}

	/**
	 * 更新银行缓存
	 * 
	 * @return
	 */
	public static boolean updateBankCache() {
		if (Tools.currentAccount != null) {
			if (Tools.currentAccount.isOpenBank) {
				return true;
			} else if (isNearby()) {
				if (MyBank.isOpen()) {
					BankCache.update();
					Tools.currentAccount.isOpenBank = true;
					return true;
				} else {
					MyBank.openBank();
					Waiting.waitNormal(1350, 150);
					if (MyBank.isOpen()) {
						BankCache.update();
						Tools.currentAccount.isOpenBank = true;
						return true;
					}
				}
			} else {
				Tools.walkToBank();
			}
		}
		return false;
	}

	public static int[] getIds(BankItem... items) {
		int[] ids = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			ids[i] = items[i].id;
		}
		return ids;
	}

	/**
	 * 初始化银行缓存
	 * 
	 * @return
	 */
	public static boolean initialiseCache() {
		return BankCache.isInitialized();
	}

	public static boolean isOpen() {
		return Bank.isOpen() || Query.widgets().inIndexPath(12, 2, 11).actionContains("Close").isAny();
	}

	/**
	 * 存物品(保留物品)
	 * 
	 * @param isNote
	 * @param isDepositEquipment
	 * @param ids
	 * @return
	 */
	public static boolean deposit(boolean isDepositAll, boolean isNote, boolean isDepositEquipment, BankItem... ids) {
		if (isOpen()) {
			if (!Tools.interfaceIsHidden(664, 29)) {
				Tools.interfaceCheck(664, 29, -1, "Close");
			}
			BankSettings.setNoteEnabled(isNote);
			BankSettings.setWithdrawQuantity(WithdrawQuantity.ALL);
			if (isDepositEquipment && Equipment.getAll().size() > 0)
				depositEquipment();
			if (isDepositAll || !Tools.invIsHave(ids)) {
				depositInventory();
			} else {
				boolean isloop = true;
				while (isloop) {
					if (!isOpen() || !Login.isLoggedIn()) {
						return false;
					}
					isloop = false;
					for (InventoryItem item : Inventory.getAll()) {
						if (!Tools.isContain(item, ids)) {
							Tools.invClick(item.getId(), "Deposit-All");
							Waiting.waitNormal(350, 50);
							isloop = true;
							break;
						}
					}
					Waiting.wait(100);
				}
			}
			while (Query.inventory().idEquals(22586).isAny()) {
				if (!isOpen() || !Login.isLoggedIn()) {
					return false;
				}
				if (!Tools.interfaceIsHidden(15, 8)) {
					Tools.interfaceCheck(15, 8, -1, "Deposit loot");
					Waiting.waitNormal(350, 150);
					Tools.interfaceCheck(15, 10, -1, "Dismiss");
					Waiting.waitNormal(750, 150);
				} else {
					boolean isout = true;
					for (int i = 0; i < 28; i++) {
						if (Tools.interfaceGetItemId(15, 3, i) == 22586) {
							if (Query.widgets().inIndexPath(15, 3, i).actionContains("View").isAny()) {
								isout = false;
								if (Tools.invClick(22586, "View")) {
									Waiting.waitNormal(750, 150);
								}
							}
						}
					}
					if (isout) {
						break;
					}
				}
				Waiting.wait(100);
			}
			for (BankItem i : ids) {
				if (i.id > 0) {
					if ((Tools.invNum(i.id) + Equipment.getCount(i.id)) > i.num && i.num != -1) {
						deposit(i.id, Tools.invNum(i.id) + Equipment.getCount(i.id) - i.num);
						Waiting.waitNormal(300, 50);
					} else if (Tools.invNum(i.id) + Equipment.getCount(i.id) == i.num) {
					} else if (i.num == -1 && Tools.invIsHave(i.id)) {
					} else if (i.num == -1) {
						withdraw(i.id, 0);
						Waiting.waitNormal(300, 50);
					} else {
						withdraw(i.id, i.num - Tools.invNum(i.id) - Equipment.getCount(i.id));
						Waiting.waitNormal(300, 50);
					}
				} else {
					if ((Tools.invNum(i.name) + Equipment.getCount(i.name)) > i.num && i.num != -1) {
						Banking.deposit(Tools.invNum(i.name) + Equipment.getCount(i.name) - i.num, i.name);
						Waiting.waitNormal(300, 50);
					} else if (Tools.invNum(i.name) + Equipment.getCount(i.name) == i.num) {
					} else if (i.num == -1 && Tools.invisHaveAll(i.name)) {
					} else if (i.num == -1) {
						withdrawName(0, i.name);
						Waiting.waitNormal(300, 50);
					} else {
						withdrawName(i.num, i.name);
						Waiting.waitNormal(300, 50);
					}
				}
			}
			Waiting.waitNormal(1200, 120);
			if (Tools.invisHaveAll(ids)) {
				close();
			}
			return true;
		} else if (isNearby()) {
			if (open()) {
				Waiting.waitUntil(3000, MyBank::isOpen);
			}
		} else {
			Tools.walkToBank();
		}
		return false;
	}

	public static boolean depositX(boolean isDepositAll, boolean isNote, boolean isDepositEquipment, BankItem... ids) {
		if (isOpen()) {
			if (!Tools.interfaceIsHidden(664, 29)) {
				Tools.interfaceCheck(664, 29, -1, "Close");
			}
			BankSettings.setNoteEnabled(isNote);
			BankSettings.setWithdrawQuantity(WithdrawQuantity.X);
			if (isDepositEquipment && Equipment.getAll().size() > 0)
				depositEquipment();
			if (isDepositAll) {
				depositInventory();
			} else {
				boolean isloop = true;
				while (isloop) {
					if (!isOpen() || !Login.isLoggedIn()) {
						return false;
					}
					isloop = false;
					for (InventoryItem item : Inventory.getAll()) {
						if (!Tools.isContain(item, ids)) {
							// depositAll(item.getId());
							Tools.invClick(item.getId(), "Deposit-All");
							Waiting.waitNormal(1250, 150);
							isloop = true;
							break;
						}
					}
					Waiting.wait(100);
				}
			}
			Waiting.waitNormal(1350, 150);
			for (BankItem i : ids) {
				if (i.id > 0) {
					if ((Tools.invNum(i.id) + Equipment.getCount(i.id)) > i.num && i.num != -1) {
						deposit(i.id, Tools.invNum(i.id) + Equipment.getCount(i.id) - i.num);
						Waiting.waitNormal(300, 50);
					} else if (Tools.invNum(i.id) + Equipment.getCount(i.id) == i.num) {
					} else if (i.num == -1 && Tools.invIsHave(i.id)) {
					} else if (i.num == -1) {
						withdraw(i.id, 0);
						Waiting.waitNormal(300, 50);
					} else {
						withdraw(i.id, i.num - Tools.invNum(i.id) - Equipment.getCount(i.id));
						Waiting.waitNormal(300, 50);
					}
				} else {
					if ((Tools.invNum(i.name) + Equipment.getCount(i.name)) > i.num && i.num != -1) {
						Banking.deposit(Tools.invNum(i.name) + Equipment.getCount(i.name) - i.num, i.name);
						Waiting.waitNormal(300, 50);
					} else if (Tools.invNum(i.name) + Equipment.getCount(i.name) == i.num) {
					} else if (i.num == -1 && Tools.invisHaveAll(i.name)) {
					} else if (i.num == -1) {
						withdrawName(0, i.name);
						Waiting.waitNormal(300, 50);
					} else {
						withdrawName(i.num, i.name);
						Waiting.waitNormal(300, 50);
					}
				}
			}
			Waiting.waitNormal(1200, 120);
			close();
			return true;
		} else if (isNearby()) {
			if (open()) {
				Waiting.waitUntil(3000, MyBank::isOpen);
			}
		} else {
			Tools.walkToBank();
		}
		return false;
	}

	public static boolean deposit(TradeItemInfo... ids) {
		if (isOpen()) {
			if (!Tools.interfaceIsHidden(664, 29)) {
				Tools.interfaceCheck(664, 29, -1, "Close");
			}
			BankSettings.setNoteEnabled(true);
			BankSettings.setWithdrawQuantity(WithdrawQuantity.ALL);
			boolean isloop = true;
			while (isloop) {
				if (!isOpen() || !Login.isLoggedIn()) {
					return false;
				}
				isloop = false;
				for (InventoryItem item : Inventory.getAll()) {
					if (!Tools.isContain(item, ids)) {
						Tools.invClick(item.getId(), "Deposit-All");
						Waiting.waitNormal(1250, 150);
						isloop = true;
						break;
					}
				}
				Waiting.wait(100);
			}
			for (TradeItemInfo i : ids) {
				withdraw(i.id, 0);
				Waiting.waitNormal(200, 120);
			}
			Waiting.waitNormal(1200, 120);
			close();
			return true;
		} else if (isNearby()) {
			if (open()) {
				Waiting.waitUntil(3000, MyBank::isOpen);
			}
		} else {
			Tools.walkToBank();
		}
		return false;
	}

	public static void withdrawName(int num, String... names) {
		for (String name : names) {
			if (contains(name)) {
				int have = Tools.invNum(name);
				withdraw(name, num - Tools.invNum(names) - Equipment.getCount(names));
				Waiting.waitUntil(3000, new BooleanSupplier() {
					@Override
					public boolean getAsBoolean() {
						return Tools.invNum(name) > have;
					}
				});
			}
			if (Tools.invNum(names) + Equipment.getCount(names) >= num) {
				return;
			}
		}
	}

	public static void withdrawId(int num, int... ids) {
		for (int id : ids) {
			if (contains(id)) {
				withdraw(id, num);
				Waiting.waitNormal(1200, 120);
			}
			if (Tools.invNum(id) + Equipment.getCount(ids) >= num) {
				return;
			}
		}
	}

	public static void openBank() {
		GrandExchange.close();
		if (!isOpen()) {
			if (isNearby()) {
				if (open()) {
					Waiting.waitUntil(3000, MyBank::isOpen);
				}
			} else {
				Tools.walkToBank();
			}
		}
	}

	public static boolean deposit(boolean isDepositAll, boolean isNote, boolean isDepositEquipment, int... ids) {
		if (isOpen()) {
			if (!Tools.interfaceIsHidden(664, 29)) {
				Tools.interfaceCheck(664, 29, -1, "Close");
			}
			BankSettings.setNoteEnabled(isNote);
			BankSettings.setWithdrawQuantity(WithdrawQuantity.ALL);
			if (isDepositEquipment && Equipment.getAll().size() > 0)
				depositEquipment();
			if (isDepositAll) {
				depositInventory();
			} else
				Banking.depositAllExcept(ids);
			Waiting.waitNormal(1000, 150);
			for (int i : ids) {
				if (contains(i)) {
					withdraw(i, 0);
					Waiting.waitNormal(1000, 150);
				}
			}
			close();
			return true;
		} else if (isNearby()) {
			if (open()) {
				Waiting.waitUntil(3000, MyBank::isOpen);
			}
		} else {
			Tools.walkToBank();
		}
		return false;
	}

	/**
	 * 检查银行是否有物品
	 * 
	 * @param items
	 * @return
	 */
	public static boolean isHava(BankItem... items) {
		for (BankItem item : items) {
			if (item.num == 0)
				item.num = 1;
			if (item.id > 0) {
				if (!Tools.invIsHave(item.id) && getCount(item.id) <= 0 && !Equipment.contains(item.id)) {
					Log.info(String.format("not have item:%d invhave:%d bankHave:%d", item.id, Tools.invNum(item.id),
							getCount(item.id)));
					return false;
				}
			} else if (!Tools.invIsHave(item.name) && getCount(item.name) <= 0 && !Equipment.contains(item.name)) {
				Log.info(String.format("not have item:%s invhave:%d bankHave:%d", item.name[0], Tools.invNum(item.name),
						getCount(item.name)));
				return false;
			}
		}
		return true;
	}

	public static boolean isHavaAll(BankItem... items) {
		for (BankItem item : items) {
			if (item.num < 0)
				item.num = 1;
			if (item.id > 0) {
				if (Tools.invNum(item.id) + getCount(item.id) + Equipment.getCount(item.id) < item.num) {
					Log.info(String.format("not have item:%d invhave:%d bankHave:%d", item.id, Tools.invNum(item.id),
							getCount(item.id)));
					return false;
				}
			} else if (Tools.invNum(item.name) + getCount(item.name) + Equipment.getCount(item.name) < item.num) {
				Log.info(String.format("not have item:%s invhave:%d bankHave:%d", item.name[0], Tools.invNum(item.name),
						getCount(item.name)));
				return false;
			}
		}
		return true;
	}

	public static boolean isContains(String... names) {
		for (String name : names) {
			if (contains(name) || Equipment.contains(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isContains(int... ids) {
		for (int id : ids) {
			if (contains(id) || Equipment.contains(id)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isContains(TradeItemInfo... items) {
		for (TradeItemInfo item : items) {
			if (contains(item.id)) {
				return true;
			}
		}
		return false;
	}

}