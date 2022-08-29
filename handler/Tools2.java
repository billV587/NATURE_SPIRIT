package scripts.handler;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WorldHopper;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.sdk.Camera;
import org.tribot.script.sdk.Combat;
import org.tribot.script.sdk.Equipment;
import org.tribot.script.sdk.GameState;
import org.tribot.script.sdk.GameTab;
import org.tribot.script.sdk.Interaction;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Magic;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Options;
import org.tribot.script.sdk.Prayer;
import org.tribot.script.sdk.Shop;
import org.tribot.script.sdk.Skill;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.antiban.Antiban;
import org.tribot.script.sdk.interfaces.Positionable;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.InventoryItem;
import org.tribot.script.sdk.types.LocalTile;
import org.tribot.script.sdk.types.WorldTile;

import dax.api_lib.DaxWalker;
import dax.api_lib.models.DaxCredentials;
import dax.api_lib.models.DaxCredentialsProvider;
import scripts.handler.MyBank.BankItem;
import scripts.model.ExchangeItemInfo;
import scripts.model.MyArea;
import scripts.model.RSItemPriceMode;
import scripts.model.TradeItemInfo;

public class Tools2 {

	public static boolean isLoop = true;
	
	public static long errorTime = 0, startTime = 0, taskStartTime = 0,waitTime=0;
	
	public static Font f = new Font("KaiTi", 0, 16);

	public static Color COLOR_CHENG = new Color(225, 119, 15);

	public static Color COLOR_BLUE = new Color(0, 46, 155);

	public static Color COLOR_RED = new Color(215, 0, 15);

	public static Color COLOR_RED_2 = new Color(162, 126, 126);

	public static Color COLOR_FUGUANG = new Color(240, 194, 162);

	public static Color COLOR_YELLOW = new Color(250, 192, 61);

	public static Color COLOR_YELLOW_2 = new Color(250, 234, 211);

	public static Color COLOR_GREEN = new Color(147, 199, 161);

	public static Color COLOR_GREEN_1 = new Color(105, 119, 35);

	public static Color COLOR_GREEN_2 = new Color(79, 122, 94);

	public static Color backColor = new Color(41, 41, 41, 150);
	
	public static String[] ringOfWeathName = { "Ring of wealth (1)", "Ring of wealth (2)", "Ring of wealth (3)",
			"Ring of wealth (4)", "Ring of wealth (5)" };
	
	private static HashMap<Integer, String> itemNames = new HashMap<>();

	public static String getTime(long tragTime) {
		if (tragTime <= 0) {
			tragTime = System.currentTimeMillis();
		}
		int time = (int) (System.currentTimeMillis() - tragTime) / 1000;
		int h = time / 60 / 60;
		int m = (time / 60) % 60;
		int s = time % 60;
		return String.format("%d:%d:%d", h, m, s);
	}
	public static DaxCredentials ADXAPI_1 = new DaxCredentials("sub_DPjXXzL5DeSiPf", "PUBLIC-KEY");
	public static void init() {
		DaxWalker.setCredentials(new DaxCredentialsProvider() {
			@Override
			public DaxCredentials getDaxCredentials() {
				return ADXAPI_1;
			}
		});
		// 打开内置antiban
		Antiban.setScriptAiAntibanEnabled(true);
		// 获取antiban设置
	}

	/**
	 * 初始化游戏设置
	 */
	public static void initGame() {
		if (MyChat.isOpenChat()) {
			MyChat.clickContinue();
		} else if (!interfaceIsHidden(134, 6, 9) && interfaceText(134, 6, 9).contains("more")) {
			interfaceCheck(134, 6, 9, "Toggle");
			Waiting.waitNormal(750, 150);
		}
		// 移除房顶
		if (Options.isRoofsEnabled())
			Options.setRemoveRoofsEnabled(true);
		// 关闭所有音乐
		if (Options.isAnySoundOn())
			Options.turnAllSoundsOff();
		// 设置shift删东西
		if (!Options.isShiftClickDropEnabled())
			Options.setShiftClickDrop(true);
		// 打开ESC快速关闭
		if (!Options.isEscapeClosingEnabled()) {
			openESC();
		}
		// 关闭设置弹窗
		if (Options.isAllSettingsOpen())
			Options.closeAllSettings();
		// 打开跑步
		openRunOn();
	}

	public static void openESC() {
		while (!Options.isEscapeClosingEnabled() && Options.isAllSettingsOpen()) {
			if (MyChat.isOpenChat()) {
				MyChat.clickContinue();
			} else if (!interfaceIsHidden(134, 6, 9) && interfaceText(134, 6, 9).contains("more")) {
				interfaceCheck(134, 6, 9, "Toggle");
				Waiting.waitNormal(750, 150);
			}
			if (!interfaceIsHidden(134, 24)) {
				interfaceCheck(134, 24, 39, "Select");
				Waiting.waitNormal(720, 120);
				Mouse.click(502, 306, 1);
				Waiting.waitNormal(720, 120);
				interfaceCheck(134, 18, 193, "Toggle");
				Waiting.waitNormal(720, 120);
			} else {
				GameTab.OPTIONS.open();
				Waiting.waitNormal(720, 120);
				Mouse.click(676, 442, 1);
				Waiting.waitNormal(1200, 120);
			}
		}

	}

	/**
	 * 走路
	 * 
	 * @param x
	 * @param y
	 */
	public static void walkto(int x, int y) {
		if (!Tools2.interfaceIsHidden(664, 29)) {
			Tools2.interfaceCheck(664, 29, -1, "Close");
		}
		MyBank.close();
		Shop.close();
		if (distanceTo(x, y, 2) && MyPlayer.getTile().getPlane() == 0) {
			return;
		}
		DaxWalker.walkTo(new RSTile(x, y,0));
		// DaxWalker.walkTo(new RSTile(x, y),MyWalkingCondition.me);
	}

	public static void walkto(int x, int y, int z) {
		MyBank.close();
		if (MyPlayer.getTile().getPlane() == z && distanceTo(x, y, 2)) {
			return;
		}
		DaxWalker.walkTo(new RSTile(x, y, z));
		// DaxWalker.walkTo(new RSTile(x, y, z),MyWalkingCondition.me);
	}
	public static String[] PrayerPotion = { "Prayer potion(4)", "Prayer potion(3)", "Prayer potion(2)",
	"Prayer potion(1)" };
	public static boolean drinkPrayer(int potion) {
		if (Prayer.getPrayerPoints() < potion) {
			invClick("Drink", PrayerPotion);
			Waiting.waitNormal(2050, 150);
			return true;
		}
		return false;
	}
	public static String[] StaminaPotionAll = { "Stamina potion(1)", "Stamina potion(2)", "Stamina potion(3)",
	"Stamina potion(4)" };
	public static void drinkStam() {
		if (invIsHave(StaminaPotionAll) && !MyPlayer.isStaminaActive()) {
			invClick("Drink", StaminaPotionAll);
			Waiting.waitNormal(1750, 150);
			if (MyPlayer.getRunEnergy() < 50) {
				invClick("Drink", StaminaPotionAll);
				Waiting.waitNormal(1750, 150);
			}
		}
	}

	/**
	 * 屏幕走路
	 * 
	 * @param x
	 * @param y
	 */
	public static void walkScreento(int x, int y) {
		if (distanceTo(x, y, 1))
			return;
		Walking.walkScreenPath(Walking.generateStraightScreenPath(new RSTile(x, y)));
		Waiting.waitUntil(() -> !MyPlayer.isMoving());
	}
	public static void walkScreento0(int x, int y) {
		Walking.walkScreenPath(Walking.generateStraightScreenPath(new RSTile(x, y)));
		Waiting.waitUntil(() -> !MyPlayer.isMoving());
	}
	
	public static void walkRandomTo(int x, int y) {
		if (MyPlayer.getTile().getPlane() == 0) {
			walkto(x, y);
		} else if (General.random(1, 100) < 50) {
			walkScreento(x, y);
			Waiting.waitNormal(750, 150);
			if (!isMoving()) {
				Camera.setRotation(Camera.getRotation() + 5);
			}
		} else {
			Walking.walkTo(new RSTile(x, y));
			Waiting.waitUniform(750, 150);
			if (!isMoving()) {
				Camera.setRotation(Camera.getRotation() + 5);
			}
		}
	}

	/**
	 * 去银行
	 */
	public static void walkToBank() {
		DaxWalker.walkToBank();
	}

	public static void walkPath(RSTile[] xy) {
		MyBank.close();
		Shop.close();
		int wp = General.random(1, 8);
		if (wp < 7) {
			Walking.walkPath(xy);
		} else {
			Walking.walkScreenPath(xy);
		}
	}

	/**
	 * 角色距离目标
	 * 
	 * @param x
	 * @param y
	 * @param distance
	 * @return
	 */
	public static boolean distanceTo(int x, int y, int distance) {
		return MyPlayer.getTile().distanceTo(new WorldTile(x, y)) <= distance;
	}

	/**
	 * 角色距离目标
	 * 
	 * @param p
	 * @param distance
	 * @return
	 */
	public static boolean distanceTo(Positionable p, int distance) {
		return MyPlayer.getTile().distanceTo(p) <= distance;
	}

	/**
	 * 打开跑步
	 */
	public static void openRunOn() {
		if (!Options.isRunEnabled() && Antiban.shouldTurnOnRun()) {
			Options.setRunEnabled(true);
		}
	}

	/**
	 * 等待做事完成
	 */
	public static boolean waitDoing() {
		while (true) {
			Waiting.waitNormal(300, 400);
			int j = 0;
			for (int i = 0; i < 10; i++) {
				j = i;
				if (MyPlayer.isAnimating()) {
					break;
				}
				Waiting.wait(150);
			}
			if (j >= 9) {
				return true;
			}
		}
	}

	/**
	 * 等待做事完成
	 */
	public static boolean waitDoing(Skill skill) {
		while (true) {
			int xp = skill.getXp();
			Waiting.waitNormal(300, 400);
			int j = 0;
			for (int i = 0; i < 30; i++) {
				j = i;
				if (skill.getXp() > xp) {
					break;
				}
				Waiting.wait(200);
			}
			if (j >= 29) {
				return true;
			}
		}
	}

	/**
	 * 等待做事完成
	 */
	public static boolean waitDoing(int itemId) {
		while (true) {
			int num = invNum(itemId);
			Waiting.waitNormal(300, 400);
			int j = 0;
			for (int i = 0; i < 20; i++) {
				j = i;
				if (invNum(itemId) < num) {
					break;
				} else if (invNum(itemId) <= 0 || MyChat.isOpenChat()) {
					return true;
				}
				Waiting.wait(200);
			}
			if (j >= 19) {
				return true;
			}
		}
	}

	public static boolean waitDoing(int itemId, int waitTime) {
		while (true) {
			int num = invNum(itemId);
			Waiting.waitNormal(300, 400);
			int j = 0;
			for (int i = 0; i < 30; i++) {
				j = i;
				if (invNum(itemId) < num) {
					break;
				} else if (invNum(itemId) <= 0 || MyChat.isOpenChat()) {
					return true;
				}
				Waiting.wait(waitTime / 30);
			}
			if (j >= 29) {
				return true;
			}
		}
	}

	/**
	 * 是否在范围内
	 * 
	 * @param minX
	 * @param maxY
	 * @param maxX
	 * @param minY
	 * @return
	 */
	public static boolean inBox(int minX, int maxY, int maxX, int minY) {
		int x = MyPlayer.getTile().getX();
		int y = MyPlayer.getTile().getY();
		return x >= minX && x <= maxX && y >= minY && y <= maxY;
	}

	public static boolean inBox(int minX, int maxY, int maxX, int minY, int z) {
		if (MyPlayer.getTile().getPlane() != z) {
			return false;
		}
		int x = MyPlayer.getTile().getX();
		int y = MyPlayer.getTile().getY();
		return x >= minX && x <= maxX && y >= minY && y <= maxY;
	}

	/**
	 * 是否在范围内 练敏捷
	 * 
	 * @param minX
	 * @param maxY
	 * @param maxX
	 * @param minY
	 * @return
	 */
	public static boolean inBoxByAg(int minX, int maxY, int maxX, int minY) {
		if (inBox(minX, maxY, maxX, minY)) {
			if (Tools2.groundItemClickOnArea(11849, "Take", MyArea.to(minX, maxY, maxX, minY))) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * npc对话
	 * 
	 * @param id
	 * @param action
	 * @return
	 */
	public static boolean npcTo(int id, String action) {
		return Interaction.interactNpc(id, action);
	}

	public static boolean npcTo(String name, String action) {
		return Interaction.interactNpc(name, action);
	}


	public static boolean isInDead() {
		if (isHaveNpc(9855)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取技能等级
	 * 
	 * @param skill
	 * @return
	 */
	public static float getSkillLevel(Skill skill) {
		return Float
				.valueOf(String.format("%.2f", skill.getActualLevel() + (float) skill.getXpPercentToNextLevel() / 100));
	}

	public static boolean isMoving() {
		int x = MyPlayer.getTile().getX();
		int y = MyPlayer.getTile().getY();
		Waiting.wait(200);
		return MyPlayer.getTile().getX() != x || MyPlayer.getTile().getY() != y;
	}

	/**
	 * 背包是否有物品
	 * 
	 * @param ids
	 * @return
	 */
	public static boolean invIsHave(int... ids) {
		return Inventory.contains(ids);
	}
	
	public static boolean invIsHaveAndNotEquiment(int... ids) {
		for (int i : ids) {
			if(Inventory.contains(i) && !Equipment.contains(i)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean invIsHaveAndNotEquiment(String... names) {
		return Inventory.contains(names) && !Equipment.contains(names);
	}

	public static boolean invIsHave(TradeItemInfo... list) {
		for (TradeItemInfo item : list) {
			if (item.id != 995) {
				if (invNum(item.id) > 0 || invNum(item.noteId) > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean invIsHave(BankItem... list) {
		for (BankItem item : list) {
			if (item.id > 0) {
				if (invIsHave(item.id)) {
					return true;
				}
			} else if (invIsHave(item.name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean invOrEquipmentIsHave(int... ids) {
		return Inventory.contains(ids) || Equipment.contains(ids);
	}

	public static boolean invOrEquipmentOrBankIsHave(int... ids) {
		return Inventory.contains(ids) || Equipment.contains(ids) || MyBank.contains(ids);
	}
	
	public static boolean invOrEquipmentOrBankIsHave(String... names) {
		return Inventory.contains(names) || Equipment.contains(names) || MyBank.contains(names);
	}

	public static boolean invOrEquipmentIsHave(String... names) {
		return Query.inventory().nameEquals(names).isNotNoted().isAny() || Equipment.contains(names);
	}

	public static boolean invNoteIsHave(int... ids) {
		for (int i : ids) {
			if (i == 5075) {// 鸟窝
				return invIsHave(i) || invIsHave(20783);
			} else if (invIsHave(i) || invIsHave(i + 1)) {
				return true;
			}
		}
		return false;
	}

	public static boolean invIsHave(String... names) {
		return Inventory.contains(names);
	}

	public static boolean invIsHaveAndNotEq(int... ids) {
		for (int i : ids) {
			if(Query.inventory().idEquals(i).isNotNoted().isAny() && !Query.equipment().idEquals(i).isAny()){
				Log.info("have EQ = " +i);
				return true;
			}
			
		}
		return false;
	}
	
	public static boolean invIsHaveAndNotEq(String... names) {
		return Query.inventory().nameEquals(names).isNotNoted().isAny() && !Equipment.contains(names);
	}

	public static int invNum() {
		return Inventory.getFilledSlots();
	}

	public static int invNum(int... ids) {
		return Inventory.getCount(ids);
	}

	public static int invNum(String... names) {
		return Inventory.getCount(names);
	}

	public static boolean invisHaveAll(String... name) {
		for (String n : name) {
			if (!Inventory.contains(n)) {
				return false;
			}
		}
		return true;
	}

	public static boolean invisHaveAll(BankItem... items) {
		for (BankItem n : items) {
			if (n.id > 0) {
				if (!Inventory.contains(n.id) && !Equipment.contains(n.id) && n.num != 0) {
					Log.info(n.id + " not have");
					return false;
				}
			} else if (!Inventory.contains(n.name) && !Equipment.contains(n.name) && n.num != 0) {
				Log.info(n.name[0] + " not have");
				return false;
			}
		}
		return true;
	}
	public static boolean isHaveAll(BankItem... items) {
		if (!MyBank.updateBankCache()) {
			Log.info("bankCache not update");
			return true;
		}
		for (BankItem n : items) {
			if (n.id > 0) {
				if (Inventory.getCount(n.id) + MyBank.getCount(n.id) + Equipment.getCount(n.id) < n.num) {
					Log.info(n.id + " not have A");
					return false;
				}
			} else if (Inventory.getCount(n.name) + MyBank.getCount(n.name) + Equipment.getCount(n.name) < n.num) {
				Log.info(n.name[0] + " not have A");
				return false;
			}
			if(n.name != null && n.name[0].contains("Burning amulet")){
				Log.info((Inventory.getCount(n.name) + MyBank.getCount(n.name) + Equipment.getCount(n.name))+"="+n.num);
			}
		}
		return true;
	}
	
	public static boolean invisHaveAll(ExchangeItemInfo... items) {
		if (!MyBank.updateBankCache()) {
			Log.info("bankCache not update");
			return false;
		}
		for (ExchangeItemInfo n : items) {
			String name = itemNames.get(n.id);
			if(name == null){
				RSItemPriceMode itemInfo = HttpTools.getItemInfo(n.id);
				name = itemInfo != null?itemInfo.name:"";
			}
			if(name == null || "".equals(name)){
				if (Inventory.getCount(n.id) + MyBank.getCount(n.id) + Equipment.getCount(n.id) < n.num) {
					Log.info(n.id + " not have BB");
					return false;
				}
			}else{
				if (Inventory.getCount(name) + MyBank.getCount(name) + Equipment.getCount(name) < n.num) {
					Log.info(n.id + " not have BB");
					return false;
				}
			}
			
		}
		return true;
	}

	public static void changeWorldForManyPlayer(int num) {// 人太多换线路
		if (Query.players().maxDistance(30).count() > num) {
			WorldHopper.changeWorld(WorldHopper.getRandomWorld(MyPlayer.isMember(), false));
		}
	}
	
	public static void changeWorldForManyPlayer(int num,int dis) {// 人太多换线路
		if (Query.players().maxDistance(dis).count() > num) {
			Shop.close();
			WorldHopper.changeWorld(WorldHopper.getRandomWorld(MyPlayer.isMember(), false));
		}
	}

	/**
	 * 计算物品价值
	 * 
	 * @param isContainMoney
	 * @param ids
	 * @return
	 */
	public static float getItemTotalMoney(boolean isContainMoney, int... ids) {
		float total = 0;
		if (isContainMoney) {
			total = (float) (invNum(995) + MyBank.getCount(995)) / 1000000;
		}
		for (int id : ids) {
			int moeny = (invNum(id) + invNum(id + 1) + MyBank.getCount(id)) * HttpTools.getItemPrice(id);
			total += (float) moeny / 1000000;
		}
		return total;
	}

	public static boolean invisHaveAll(int... ids) {
		for (int n : ids) {
			if (!Inventory.contains(n)) {
				return false;
			}
		}
		return true;
	}

	public static boolean invisHaveAndNum(BankItem... items) {
		for (BankItem item : items) {
			if (item.id > 0) {
				if (!invIsHave(item.id) && !Equipment.contains(item.id)) {
					Log.error(item.id + "  is Inventory not have 1");
					return false;
				} else if (item.num != -1 && (invNum(item.id) + Equipment.getCount(item.id)) != item.num) {
					Log.error(item.id + "  is Inventory not have 2=" + (invNum(item.id) + Equipment.getCount(item.id)));
					return false;
				}
			} else {
				if (!invIsHave(item.name) && !Equipment.contains(item.name)) {
					Log.error(item.name[0] + "  is Inventory not have 3");
					return false;
				} else if (item.num != -1 && (invNum(item.name) + Equipment.getCount(item.name)) != item.num) {
					Log.error(item.name[0] + "num = " + item.num + "  is Inventory not have 4");
					return false;
				}
			}

		}
		return true;
	}

	public static boolean isContain(InventoryItem item, BankItem... items) {
		for (BankItem bankItem : items) {
			if (bankItem.id > 0) {
				if (item.getId() == bankItem.id) {
					return true;
				}
			} else if (isContain(item.getName(), bankItem.name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isContain(InventoryItem item, TradeItemInfo... items) {
		for (TradeItemInfo bankItem : items) {
			if (item.getId() == bankItem.noteId) {
				return true;
			}
		}
		return false;
	}

	public static boolean isContain(String name, String... names) {
		for (String n : names) {
			if (name.equals(n)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isContain(int id, int... ids) {
		for (int n : ids) {
			if (id == n) {
				return true;
			}
		}
		return false;
	}

	public static boolean readBook() {
		if (Inventory.getSelected().isPresent() || Magic.isAnySpellSelected() || GameState.getUpText().contains("->")) {
			Mouse.click(535, 227, 1);
			return true;
		} else if (!interfaceIsHidden(153, 17)) {
			interfaceCheck(153, 17, -1, "Close");
			Waiting.waitNormal(1200, 150);
			return true;
		} else if (!interfaceIsHidden(392, 77)) {
			interfaceCheck(392, 77, -1, "Continue");
			Waiting.waitNormal(1200, 150);
			return true;
		} else if (!interfaceIsHidden(392, 7)) {
			interfaceCheck(392, 7, -1, "Close");
			return true;
		} else if (!interfaceIsHidden(184, 11)) {
			interfaceCheck(184, 11, -1, "Close");
			return true;
		} else if (!interfaceIsHidden(440, 7)) {
			interfaceCheck(440, 7, -1, "Close");
			return true;
		} else if (!interfaceIsHidden(441, 7)) {
			interfaceCheck(441, 7, -1, "Close");
			return true;
		} else if (!interfaceIsHidden(444, 7)) {
			interfaceCheck(444, 7, -1, "Close");
			return true;
		} else if (!interfaceIsHidden(297, 3)) {
			interfaceCheck(297, 3, -1, "Close");
			return true;
		} else if (!interfaceIsHidden(326, 100)) {
			interfaceCheck(326, 100, -1, "Decline");
			return true;
		}else if (!interfaceIsHidden(108, 5)) {
			interfaceCheck(108, 5, -1, "Close");
			return true;
		}else if (!Tools2.interfaceIsHidden(664, 29)) {
			Tools2.interfaceCheck(664, 29, -1, "Close");
		}else if (!Tools2.interfaceIsHidden(595, 38)) {
			Tools2.interfaceCheck(595, 38, -1, "Close");
		}
		if (!Combat.isAutoRetaliateOn()) {
			Combat.setAutoRetaliate(true);
		}

		return false;
	}

	public static boolean invClick(int id, String action) {
		out = false;
		Query.inventory().idEquals(id).findRandom().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitNormal(1200, 150);
		}
		return out;
	}

	public static void invClick(String action, String... names) {
		out = false;
		Query.inventory().nameEquals(names).findRandom().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitUniform(1250, 150);
		}
	}

	public static void invClick(String action, int... ids) {
		Query.inventory().idEquals(ids).findRandom().map(g -> g.click(action));
	}

	public static boolean invUseToItem(int useId, int... targetids) {
		if (Inventory.getSelected().isPresent()) {
			if (Inventory.getSelected().get().getId() == useId) {
				invClick("Use", targetids);
				return true;
			} else {
				Inventory.getSelected().ifPresent(g -> g.click("Cancel"));
			}
		} else {
			invClick(useId, "Use");
			Waiting.waitNormal(300, 50);
			invClick("Use", targetids);
			Waiting.waitNormal(750, 150);
		}
		return false;
	}

	public static boolean invUseToItemFast(int useId, int... targetids) {
		out = false;
		Query.inventory().idEquals(useId).findRandom().map(g -> out = g.click("Use"));
		if (out) {
			Waiting.waitNormal(300, 50);
			invClick("Use", targetids);
			Waiting.waitNormal(750, 150);
		}
		return false;
	}

	public static boolean magicUseToItem(String magic, int... targetids) {
		if (Magic.isAnySpellSelected()) {
			Magic.ensureSpellSelected(magic);
		} else {
			Magic.selectSpell(magic);
		}
		Waiting.waitNormal(750, 150);
		Tools2.invClick("Cast", targetids);
		Waiting.waitNormal(1050, 150);
		return false;
	}

	public static boolean invUseToObject(int targetids, String... useNames) {
		if (Inventory.getSelected().isPresent()) {
			objectClick(targetids, "Use");
		} else {
			invClick("Use", useNames);
			Waiting.waitNormal(750, 150);
			objectClick(targetids, "Use");
		}
		return false;
	}

	public static boolean invUseToObjectByXy(int targetids, int x, int y, int id) {
		if (Inventory.getSelected().isPresent()) {
			objectClick(targetids, "Use");
		} else {
			invClick("Use", id);
			Waiting.waitNormal(750, 150);
			objectClick(targetids, x, y, "Use");
			Waiting.waitUniform(2850, 250);
		}
		return false;
	}

	public static boolean invUseToObject(int targetids, int... ids) {
		if (Inventory.getSelected().isPresent()) {
			objectClick(targetids, "Use");
		} else {
			invClick("Use", ids);
			Waiting.waitNormal(750, 150);
			return objectClick(targetids, "Use");
		}
		return false;
	}

	public static boolean invUseToNpc(int id, String... npcName) {
		RSNPC[] npc = NPCs.findNearest(npcName);
		if (npc != null && npc.length > 0) {
			if (!npc[0].isOnScreen()) {
				if (distanceTo(npc[0].getPosition().getX(), npc[0].getPosition().getY(), 5)) {
					walkScreento(npc[0].getPosition().getX(), npc[0].getPosition().getY());
				} else {
					turnTo(npc[0].getPosition());
				}
				return false;
			}
		}
		if (Inventory.getSelected().isPresent()) {
			npcClick("Use", npcName);
		} else {
			invClick("Use", id);
			Waiting.waitNormal(750, 150);
			npcClick("Use", npcName);
			return true;
		}
		return false;
	}

	public static boolean invUseToNpc(String npcName, int... targetids) {
		RSNPC[] npc = NPCs.findNearest(npcName);
		if (npc != null && npc.length > 0) {
			if (!npc[0].isOnScreen()) {
				if (distanceTo(npc[0].getPosition().getX(), npc[0].getPosition().getY(), 5)) {
					walkScreento(npc[0].getPosition().getX(), npc[0].getPosition().getY());
				} else {
					turnTo(npc[0].getPosition());
					Waiting.waitNormal(750, 150);
					if (!npc[0].isOnScreen()) {
						walkScreento(npc[0].getPosition().getX(), npc[0].getPosition().getY());
						Waiting.waitNormal(750, 150);
					}
				}
				return false;
			}
		}
		if (Inventory.getSelected().isPresent()) {
			npcClick("Use", npcName);
		} else {
			invClick("Use", targetids);
			Waiting.waitNormal(750, 150);
			npcClick("Use", npcName);
		}
		return false;
	}

	public static boolean invUseToNpc(int npcid, int targetids) {
		RSNPC[] npc = NPCs.findNearest(npcid);
		if (npc != null && npc.length > 0) {
			if (!npc[0].isOnScreen()) {
				if (distanceTo(npc[0].getPosition().getX(), npc[0].getPosition().getY(), 5)) {
					walkScreento(npc[0].getPosition().getX(), npc[0].getPosition().getY());
				} else {
					turnTo(npc[0].getPosition());
					Waiting.waitNormal(750, 150);
					if (!npc[0].isOnScreen()) {
						walkScreento(npc[0].getPosition().getX(), npc[0].getPosition().getY());
						Waiting.waitNormal(750, 150);
					}
				}
				return false;
			}
		}
		if (Inventory.getSelected().isPresent()) {
			npcClick("Use", npcid);
		} else {
			invClick("Use", targetids);
			Waiting.waitNormal(750, 150);
			return npcClick("Use", npcid);
		}
		return false;
	}

	private static boolean out = false;

	public static boolean objectClickWalkTo(int id, int x, int y, String action) {
		out = false;
		if (objectIsOnScreen(id)) {
			Query.gameObjects().idEquals(id).sortedByDistance().findFirst().map(g -> out = g.click(action));
			if (out) {
				Waiting.waitNormal(3350, 250);
			}
		} else {
			walkRandomTo(x, y);
		}
		return out;
	}

	public static boolean objectClickWalkToScreen(int id, int x, int y, String action) {
		out = false;
		if (objectIsOnScreen(id)) {
			Query.gameObjects().idEquals(id).sortedByDistance().findFirst().map(g -> out = g.click(action));
		} else {
			walkScreento(x, y);
		}
		return out;
	}

	public static boolean objectClickWalkToFast(int id, int x, int y, String action) {
		out = false;
		if (objectIsOnScreen(id)) {
			Query.gameObjects().idEquals(id).sortedByDistance().findFirst().map(g -> out = g.click(action));
		} else {
			walkScreento(x, y);
		}
		return out;
	}

	public static void objectClickWalkToWaitAnimating(int id, int x, int y, String action) {
		out = false;
		if (objectIsOnScreen(id)) {
			Query.gameObjects().idEquals(id).sortedByDistance().findFirst().map(g -> out = g.click(action));
			if (out) {
				for (int i = 0; i < 60; i++) {
					if (MyPlayer.isAnimating()) {
						return;
					}
					Waiting.wait(100);
				}
			}
		} else {
			walkRandomTo(x, y);
		}
	}

	public static void objectClickWalkToWaitAnimatingNotTurnTo(int id, int x, int y, String action) {
		out = false;
		if (objectIsOnScreenNotTurn(id)) {
			Query.gameObjects().idEquals(id).sortedByDistance().findFirst().map(g -> out = g.click(action));
			if (out) {
				for (int i = 0; i < 60; i++) {
					if (MyPlayer.isAnimating()) {
						return;
					}
					Waiting.wait(100);
				}
			}
		} else {
			walkRandomTo(x, y);
		}
	}

	public static void objectRandomClickWalkTo(int id, int x, int y, String action) {
		out = false;
		if (objectIsOnScreen(id)) {
			Query.gameObjects().idEquals(id).findRandom().map(g -> out = g.click(action));
			if (out) {
				Waiting.waitNormal(3350, 150);
			}
		} else {
			walkScreento(x, y);
		}
	}

	public static boolean objectClick(int id, String action) {
		out = false;
		Query.gameObjects().idEquals(id).sortedByPathDistance().findFirst().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitUniform(1850, 250);
		}
		return out;
	}

	public static boolean objectClickFast(int id, String action) {
		out = false;
		Query.gameObjects().idEquals(id).sortedByPathDistance().findFirst().map(g -> out = g.click(action));
		return out;
	}

	public static boolean objectActionContains(int id, String... action) {
		return Query.gameObjects().idEquals(id).actionContains(action).isAny();
	}

	public static boolean objectClick(int id, int x, int y, String action) {
		out = false;
		Query.gameObjects().idEquals(id).sortedByDistance(new WorldTile(x, y)).findFirst()
				.map(g -> out = g.click(action));
		if (out) {
			Waiting.waitUniform(1850, 250);
		}
		return out;
	}

	public static boolean objectIsInArea(int id, Area... area) {
		return Query.gameObjects().idEquals(id).inArea(area).isAny();
	}

	public static boolean objectIsInArea(Area area, int... ids) {
		return Query.gameObjects().idEquals(ids).inArea(area).isAny();
	}

	public static boolean objectIsInScreen(int... ids) {
		return Query.gameObjects().idEquals(ids).maxDistance(5).isAny();
	}

	public static boolean objectClickWalkTo(String action, int... ids) {
		if (objectIsInScreen(ids)) {
			return objectClick(action, ids);
		} else {
			WorldTile xy = Query.gameObjects().idEquals(ids).sortedByDistance().findFirst().get().getTile();
			walkScreento(xy.getX(), xy.getY());
		}
		return false;
	}

	public static boolean objectIsHave(String... names) {
		return Query.gameObjects().nameContains(names).isAny();
	}

	public static boolean objectClick(String action, int... ids) {
		out = false;
		Query.gameObjects().idEquals(ids).sortedByPathDistance().findFirst().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitUniform(1850, 250);
		}
		return out;
	}

	public static boolean objectClickInArea(String action, Area area, int... ids) {
		out = false;
		Query.gameObjects().idEquals(ids).inArea(area).sortedByPathDistance().findFirst()
				.map(g -> out = g.click(action));
		if (out) {
			Waiting.waitUniform(1850, 250);
		}
		return out;
	}

	public static boolean objectClick(String action, String... names) {
		out = false;
		Query.gameObjects().nameContains(names).sortedByPathDistance().findFirst().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitUniform(1850, 250);
		}
		return out;
	}

	public static boolean npcClick(String action, String... npcName) {
		out = false;
		Query.npcs().nameContains(npcName).stream().findFirst().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitUniform(1850, 250);
		}
		return out;
	}

	public static boolean npcClickOnArea(String action, String npcName, Area... areas) {
		out = false;
		if (Query.npcs().nameContains(npcName).inArea(areas).sortedByDistance().isInteractingWithMe()
				.isHealthBarVisible().isAny()) {
			Waiting.waitNormal(1350, 250);
			return true;
		}
		Query.npcs().nameContains(npcName).inArea(areas).sortedByDistance().isHealthBarNotVisible()
				.isNotBeingInteractedWith().findFirst().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitNormal(1350, 250);
		}
		return out;
	}

	public static boolean npcIsOnArea(int id, Area... areas) {
		return Query.npcs().idEquals(id).inArea(areas).isAny();
	}

	public static boolean npcIsWithMeOnArea(int id, Area... areas) {
		return Query.npcs().idEquals(id).isInteractingWithMe().isHealthBarVisible().inArea(areas).isAny();
	}

	public static boolean npcClickTanglFootOnArea(String action, int id, Area... areas) {
		out = false;
		if (Query.npcs().idEquals(id).inArea(areas).isInteractingWithMe().isHealthBarVisible().isAny()) {
			return false;
		}
		Query.npcs().idEquals(id).inArea(areas).isHealthBarNotVisible().isNotBeingInteractedWith().findFirst()
				.map(g -> out = g.click(action));
		return out;
	}

	public static boolean npcClickOnXYByMagic(String action, String npcName, int x, int y) {
		out = false;
		Query.npcs().nameContains(npcName).sortedByDistance(new WorldTile(x, y)).maxDistance(2).findFirst()
				.map(g -> out = g.click(action));
		if (out) {
			Waiting.waitNormal(1350, 250);
		}
		return out;
	}

	public static boolean npcClickOnAreaByMagic(String action, String npcName, Area... areas) {
		out = false;
		Query.npcs().nameContains(npcName).inArea(areas).sortedByDistance().findFirst().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitNormal(1350, 250);
		}
		return out;
	}

	public static boolean npcClickOnArea(String action, int... id) {
		out = false;
		if (Query.npcs().idEquals(id).isInteractingWithMe().isAny()) {
			Waiting.waitNormal(1350, 250);
			return true;
		}
		Query.npcs().idEquals(id).isNotBeingInteractedWith().findFirst().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitNormal(1350, 250);
		}
		return out;
	}

	public static boolean npcIsWithMe() {
		return Query.npcs().maxDistance(10).isInteractingWithMe().isAny();
	}

	public static boolean npcIsWithMe(int... ids) {
		return Query.npcs().idEquals(ids).maxDistance(10).isInteractingWithMe().isAny();
	}

	public static boolean npcIsWithMeAndHitBar(int... ids) {
		return Query.npcs().idEquals(ids).maxDistance(10).isHealthBarVisible().isInteractingWithMe().isAny();
	}

	public static void npcAttWithMe() {
		if (Query.npcs().maxDistance(10).isInteractingWithMe().isAny()) {
			out = false;
			Query.npcs().maxDistance(10).isInteractingWithMe().findFirst().map(g -> out = g.click("Attack"));
			if (out) {
				Waiting.waitNormal(1350, 250);
			}
		}
	}

	public static void npcAttWithMe(int... ids) {
		if (Query.npcs().idEquals(ids).maxDistance(10).isInteractingWithMe().isAny()) {
			out = false;
			Query.npcs().idEquals(ids).maxDistance(10).isInteractingWithMe().findFirst()
					.map(g -> out = g.click("Attack"));
			if (out) {
				Waiting.waitNormal(1350, 250);
			}
		}
	}

	public static boolean eat() {
		if (MyPlayer.getCurrentHealthPercent() < 70) {
			invClick("Eat", 361, 379);
			Waiting.waitNormal(1350, 250);
			return true;
		}
		return false;
	}

	public static boolean eat(int hip) {
		if (MyPlayer.getCurrentHealthPercent() < hip) {
			invClick("Eat", 24595, 24589, 361, 379, 385);
			Waiting.waitNormal(1350, 250);
			return true;
		}
		return false;
	}

	public static void setRotation() {
		if (Camera.getRotation() > 5 || Camera.getRotation() < -5) {
			Camera.setRotation(0);
		}
	}

	public static boolean npcClick(String action, int... ids) {
		out = false;
		Query.npcs().idEquals(ids).sortedByDistance().findFirst().map(g -> out = g.click(action));
		if (out) {
			Waiting.waitNormal(1350, 250);
		}
		return out;
	}

	public static boolean npcClickNotWith(String action, int... ids) {
		out = false;
		Query.npcs().idEquals(ids).isNotBeingInteractedWith().sortedByDistance().findFirst()
				.map(g -> out = g.click(action));
		if (out) {
			Waiting.waitNormal(1350, 250);
		}
		return out;
	}

	public static boolean npcClickNotHealthBar(String action, int... ids) {
		out = false;
		if (Query.npcs().idEquals(ids).sortedByDistance().isHealthBarNotVisible().isVisible().isAny()) {
			Query.npcs().idEquals(ids).sortedByDistance().isHealthBarNotVisible().findFirst()
					.map(g -> out = g.click(action));
			if (out) {
				Waiting.waitNormal(1350, 250);
			}
		} else {
			WorldTile item = Query.npcs().idEquals(ids).sortedByDistance().isHealthBarNotVisible().findFirst().get()
					.getTile();
			walkScreento(item.getX(), item.getY());
		}
		return out;
	}

	public static boolean npcIsShowHealthBar(String... npcName) {
		return Query.npcs().nameEquals(npcName).sortedByDistance().isHealthBarVisible().isAny();
	}

	public static void npcClickEqualsName(String action, String... npcName) {
		Query.npcs().nameEquals(npcName).stream().findFirst().map(g -> g.click(action));
	}

	public static boolean groundItemClick(int id, String action) {
		if (Query.groundItems().idEquals(id).isAny()) {
			Query.groundItems().idEquals(id).sortedByPathDistance().stream().findFirst()
					.map(g -> out = g.click(action));
			Waiting.waitNormal(750, 150);
			return out;
		}
		return false;
	}

	public static boolean groundItemClick(String action, int... id) {
		if (Query.groundItems().idEquals(id).isAny()) {
			Query.groundItems().idEquals(id).sortedByPathDistance().stream().findFirst()
					.map(g -> out = g.click(action));
			Waiting.waitNormal(750, 150);
			return out;
		}
		return false;
	}

	public static boolean groundItemByWalktoClick(String action, int... id) {
		if (Query.groundItems().maxDistance(8).idEquals(id).isAny()) {
			if (Query.groundItems().maxDistance(8).idEquals(id).isVisible().isAny()) {
				if (Query.groundItems().maxDistance(8).idEquals(id).sortedByDistance().findFirst().get().getTile()
						.isInLineOfSight()) {
					Query.groundItems().maxDistance(8).idEquals(id).sortedByDistance().findFirst()
							.map(g -> out = g.click(action));
					Waiting.waitNormal(750, 150);
				} else {
					Query.groundItems().maxDistance(8).idEquals(id).sortedByDistance().findFirst().get()
							.adjustCameraTo();
					WorldTile xy = Query.groundItems().maxDistance(8).sortedByDistance().idEquals(id).findFirst().get()
							.getTile();
					walkScreento0(xy.getX(), xy.getY());
				}
			} else {
				WorldTile item = Query.groundItems().maxDistance(8).sortedByDistance().idEquals(id).findFirst().get()
						.getTile();
				walkScreento(item.getX(), item.getY());
			}
			return true;
		}
		return false;
	}

	public static boolean groundItemClickAt(int id, int x, int y, String action) {
		if (Query.groundItems().idEquals(id).sortedByDistance(new WorldTile(x, y)).isAny()) {
			Query.groundItems().idEquals(id).sortedByDistance(new WorldTile(x, y)).findFirst()
					.map(g -> out = g.click(action));
			Waiting.waitNormal(750, 150);
			return out;
		}
		return false;
	}

	public static boolean groundItemClickOnArea(int id, String action, Area... areas) {
		if (Query.groundItems().idEquals(id).inArea(areas).isAny()) {
			if (Query.groundItems().idEquals(id).inArea(areas).isVisible().isAny()) {
				if (Query.groundItems().idEquals(id).inArea(areas).findFirst().get().getTile().isInLineOfSight()) {
					Query.groundItems().idEquals(id).inArea(areas).findFirst().map(g -> g.click(action));
					Waiting.waitNormal(750, 150);
				} else {
					Query.groundItems().idEquals(id).inArea(areas).findFirst().get().adjustCameraTo();
					WorldTile xy = Query.groundItems().idEquals(id).inArea(areas).findFirst().get().getTile();
					walkScreento0(xy.getX(), xy.getY());
				}
			} else {
				WorldTile item = Query.groundItems().idEquals(id).inArea(areas).findFirst().get().getTile();
				walkScreento(item.getX(), item.getY());
			}
			return true;
		}
		return false;
	}

	public static boolean isHaveNpc(int... ids) {
		RSNPC[] npc = NPCs.findNearest(ids);
		return npc != null && npc.length > 0;
	}

	public static RSTile npcXY(int... ids) {
		RSNPC[] npc = NPCs.findNearest(ids);
		if (npc != null && npc.length > 0) {
			return npc[0].getPosition();
		}
		return new RSTile(0, 0);
	}

	public static int npcOrientation(int... ids) {
		RSNPC[] npc = NPCs.findNearest(ids);
		if (npc != null && npc.length > 0) {
			return npc[0].getOrientation();
		}
		return 0;
	}

	public static RSObject findObject(int id) {
		RSObject[] rook = Objects.findNearest(10, id);
		if (rook.length > 0) {
			return rook[0];
		}
		return null;
	}
	
	public static boolean hintArrowIsPosition(int x,int y){
		if(!GameState.getHintArrowPosition().isPresent()){
			return false;
		}
		LocalTile xy = GameState.getHintArrowPosition().get();
//		Log.info("hintArrowPosition:"+xy.getX()+","+xy.getY());
		return xy.getX() == x && xy.getY() == y;
	}

	public static Boolean objectIsOnScreen(int... id) {
		RSObject[] rook = Objects.findNearest(10, id);
		if (rook.length > 0) {
			if (rook[0].isOnScreen()) {
				return true;
			} else if (distanceTo(rook[0].getPosition().getX(), rook[0].getPosition().getY(), 10)) {
				turnTo(rook[0].getPosition());
				Waiting.waitNormal(750, 150);
				if (rook[0].isOnScreen()) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public static Boolean objectIsOnScreenNotTurn(int... id) {
		RSObject[] rook = Objects.findNearest(10, id);
		if (rook.length > 0) {
			return rook[0].isOnScreen();
		}
		return false;
	}

	/**
	 * 移除列表
	 * 
	 * @param list
	 * @param id
	 * @return
	 */
	public static int[] removeList(int[] list, int id) {
		if (!isContain(id, list)) {
			return list;
		}
		int[] n = new int[list.length - 1];
		int k = 0;
		for (int i = 0; i < list.length; i++) {
			if (list[i] != id) {
				n[k] = list[i];
				k++;
			}
		}
		return n;
	}

	public static int[] addList(int[] list, int id) {
		int[] n = new int[list.length + 1];
		for (int i = 0; i < list.length; i++) {
			n[i] = list[i];
		}
		n[list.length] = id;
		return n;
	}

	/**
	 * 面向坐标
	 * 
	 * @param x
	 * @param y
	 */
	public static void turnTo(RSTile xy) {
		org.tribot.api2007.Camera.turnToTile(xy);
	}

	/**
	 * Interface
	 * 
	 * @param group
	 * @param child
	 * @param child_cild
	 * @return
	 */
	public static boolean interfaceIsHidden(int group, int child, int child_cild) {
		if (Interfaces.get(group, child) == null) {
			return true;
		}

		if (Interfaces.get(group, child).getChild(child_cild) == null) {
			return true;
		}

		return Interfaces.get(group, child).getChild(child_cild).isHidden();
	}

	public static int interfaceGetItemId(int group, int child, int child_cild) {

		if (Interfaces.get(group, child) == null) {
			return 0;
		}

		if (child_cild < 0) {
			return Interfaces.get(group, child).getComponentItem();
		}

		if (Interfaces.get(group, child).getChild(child_cild) == null) {
			return 0;
		}

		return Interfaces.get(group, child).getChild(child_cild).getComponentItem();
	}

	
	public static boolean interfaceActionContains(int group, int child, int child_cild,String action) {

		if (Interfaces.get(group, child) == null) {
			return false;
		}

		if (Interfaces.get(group, child).getChild(child_cild) == null) {
			return false;
		}
		String[] actions = Interfaces.get(group, child).getChild(child_cild).getActions();
		if(actions == null || actions.length <=0){
			return false;
		}
		for (String ac : actions) {
			if(ac.equals(action)){
				return true;
			}
		}
		return false;
	}
	
	public static int interfaceGetModelId(int group, int child, int child_cild) {

		if (Interfaces.get(group, child) == null) {
			return 0;
		}

		if (child_cild < 0) {
			return Interfaces.get(group, child).getModelID();
		}

		if (Interfaces.get(group, child).getChild(child_cild) == null) {
			return 0;
		}

		return Interfaces.get(group, child).getChild(child_cild).getModelID();
	}

	public static int interfaceGetMidX(int group, int child) {

		if (Interfaces.get(group, child) == null) {
			return 0;
		}

		return Interfaces.get(group, child).getAbsoluteBounds().x
				+ Interfaces.get(group, child).getAbsoluteBounds().width / 2;
	}

	public static int interfaceGetSprintId(int group, int child, int child_cild) {

		if (Interfaces.get(group, child) == null) {
			return 0;
		}

		if (child_cild < 0) {
			return Interfaces.get(group, child).getSpriteID();
		}

		if (Interfaces.get(group, child).getChild(child_cild) == null) {
			return 0;
		}

		return Interfaces.get(group, child).getChild(child_cild).getSpriteID();
	}

	public static String interfaceGetItemName(int group, int child, int child_cild) {

		if (Interfaces.get(group, child) == null) {
			return "";
		}

		if (child_cild < 0) {
			return Interfaces.get(group, child).getComponentName();
		}

		if (Interfaces.get(group, child).getChild(child_cild) == null) {
			return "";
		}

		return Interfaces.get(group, child).getChild(child_cild).getComponentName();
	}

	public static boolean interfaceIsHidden(int group, int child) {
		if (Interfaces.get(group, child) == null) {
			return true;
		}
		return Interfaces.get(group, child).isHidden();
	}

	public static int interfaceGetPositionY(int group, int child, int child_cild) {
		if (Interfaces.get(group, child) == null) {
			return 0;
		}
		if (child_cild == -1) {
			return Interfaces.get(group, child).getAbsolutePosition().y;
		}
		if (Interfaces.get(group, child, child_cild) == null) {
			return 0;
		}

		return Interfaces.get(group, child, child_cild).getAbsolutePosition().y;
	}

	public static int interfaceGetPositionX(int group, int child, int child_cild) {
		if (Interfaces.get(group, child) == null) {
			return 0;
		}
		if (child_cild == -1) {
			return Interfaces.get(group, child).getAbsolutePosition().x;
		}
		if (Interfaces.get(group, child, child_cild) == null) {
			return 0;
		}

		return Interfaces.get(group, child, child_cild).getAbsolutePosition().x;
	}

	public static int interfaceGetPositionMaxX(int group, int child) {
		if (Interfaces.get(group, child) == null) {
			return 0;
		}
		return Interfaces.get(group, child).getAbsoluteBounds().x
				+ Interfaces.get(group, child).getAbsoluteBounds().width;
	}

	public static boolean interfaceCheck(int group, int child, int child_cild, String action) {
		if (Interfaces.get(group, child) != null && !Interfaces.get(group, child).isHidden()) {
			if (child_cild == -1) {
				Interfaces.get(group, child).click(action);
				return true;
			}
			if (Interfaces.get(group, child).getChild(child_cild) != null
					&& !Interfaces.get(group, child).getChild(child_cild).isHidden()) {
				Interfaces.get(group, child).getChild(child_cild).click(action);
				return true;
			}
		}
		return false;
	}

	public static String interfaceText(int group, int child) {
		if (Interfaces.get(group, child) == null) {
			return "";
		}
		return Interfaces.get(group, child).getText();
	}

	public static int interfaceTextureId(int group, int child) {
		if (Interfaces.get(group, child) == null) {
			return 0;
		}
		return Interfaces.get(group, child).getTextureID();
	}

	public static int interfaceTextureId(int group, int child, int child_cild) {
		if (Interfaces.get(group, child) == null) {
			return 0;
		}
		return Interfaces.get(group, child, child_cild).getTextureID();
	}

	public static String interfaceText(int group, int child, int child_cild) {
		if (Interfaces.get(group, child) == null) {
			return "";
		}
		if (Interfaces.get(group, child).getChild(child_cild) == null) {
			return "";
		}
		return Interfaces.get(group, child).getChild(child_cild).getText();
	}
}
