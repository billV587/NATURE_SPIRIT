package scripts;

import java.awt.Color;

import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.WorldHopper;
import org.tribot.script.ScriptManifest;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Quest;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.painting.Painting;
import org.tribot.script.sdk.script.TribotScript;

import scripts.handler.MyBank;
import scripts.handler.MyBank.BankItem;
import scripts.handler.MyChat;
import scripts.handler.MyExchange;
import scripts.handler.Tools2;
import scripts.model.ExchangeItemInfo;

@ScriptManifest(authors = { "bill" }, category = "bill", name = "Task_Nature_Spirit")
public class Task_Nature_Spirit implements TribotScript {

	private static final String Verson = "1.0";

	public Quest task = Quest.NATURE_SPIRIT;
	
	private boolean isBuy = false;

	public ExchangeItemInfo[] buyItem = { ExchangeItemInfo.to(1191, 1, 10000, 0), // 盾牌
			ExchangeItemInfo.to(1331, 1, 10000, 0), // A刀
			ExchangeItemInfo.to(2434, 1, 100000, 0), // 祈祷药水
			ExchangeItemInfo.to(19619, 5, 10000, 0), // Salve graveyard teleport
			ExchangeItemInfo.to(2961, 1, 10000, 0), // Silver sickle
			ExchangeItemInfo.to(379, 20, 500, 0), // 鱼
			ExchangeItemInfo.to(8008, 5, 5000, 0), // L
			ExchangeItemInfo.to(12625, 2, 10000, 0), // 跑步药水
			ExchangeItemInfo.to(11980, 1, 0, 2) };// 拍卖行戒指

	@Override
	public void execute(String arg0) {

		Painting.addPaint(g -> {
			g.setColor(Tools2.backColor);
			g.fillRoundRect(0, 340, 520, 140, 10, 10);
			g.setColor(Color.black);
			g.drawRoundRect(0, 340, 520, 140, 10, 10);
			g.setColor(Color.WHITE);
			g.setFont(Tools2.f);
			g.drawString("[" + Verson + "]Task_Nature_Spirit", 8, 340 + 1 * 30);
			g.drawString("[" + WorldHopper.getWorld() + "]<World", 6, 340 + 2 * 30);
			g.drawString("[" + Tools2.getTime(Tools2.startTime) + "]RuneTime", 6, 340 + 3 * 30);
		});

		Tools2.startTime = System.currentTimeMillis();
		Tools2.init();
		Tools2.isLoop = true;
		while (Tools2.isLoop) {
			Waiting.wait(100);
			if (Tools2.readBook()) {
			} else if (Login.getLoginState() == STATE.INGAME) {
				if (!isBuy) {
					if (Tools2.invisHaveAll(buyItem)) {
						Tools2.taskStartTime = System.currentTimeMillis();
						isBuy = true;
					} else {
						MyExchange.buyList(null, buyItem);
					}
				} else if (task.getStep() == 0) {// 接任务
					if (Tools2.invisHaveAndNum(BankItem.to(1, Tools2.PrayerPotion), BankItem.to(1, Tools2.ringOfWeathName),
							BankItem.to(1331, 1), BankItem.to(1191, 1), BankItem.to(19619, -1), BankItem.to(2961, 1),
							BankItem.to(379, 18), BankItem.to(1, Tools2.StaminaPotionAll), BankItem.to(552, 1))) {
						if (Tools2.invIsHave(552, 1331, 1191)) {
							Tools2.invClick(552, "Wear");
							Tools2.invClick(1191, "Wear");
							Tools2.invClick(1331, "Wield");
							Waiting.waitNormal(1350, 250);
						} else if (Tools2.inBox(3437, 9899, 3442, 3441)) {
							MyChat.talkNpc(9805, 1, 0);
						} else {
							Tools2.walkto(3440, 9895);
						}
					} else if (MyBank.isOpen()) {
						if (!MyBank.contains(552) && !Tools2.invisHaveAll(BankItem.to(552, 1))) {
							
						} else {
							MyBank.deposit(false, false, true, BankItem.to(1, Tools2.PrayerPotion),
									BankItem.to(1, Tools2.ringOfWeathName), BankItem.to(1331, 1), BankItem.to(19619, 5),
									BankItem.to(1191, 1), BankItem.to(2961, 1), BankItem.to(379, 18), BankItem.to(12625, 1),
									BankItem.to(552, 1));
						}
					} else {
						MyBank.openBank();
					}
				} else if (Tools2.invIsHave(2959, 2327, 2313, 2323)) {//
					Inventory.drop(2959, 2327, 2313, 2323);
				} else if (Inventory.isFull()) {//
					Tools2.invClick(379, "Eat");
					Waiting.waitNormal(1350, 150);
				} else if (task.getStep() == 5) {//
					if (Tools2.inBox(3437, 3461, 3450, 3457)) {
						if (!Tools2.interfaceIsHidden(580, 17)) {
							Tools2.interfaceCheck(580, 17, -1, "Yes");
						} else {
							Tools2.objectClick(3507, "Open");
						}
					} else {
						Tools2.walkto(3443, 3459);
					}
				} else if (task.getStep() == 10) {//
					if (Tools2.inBox(3434, 3342, 3446, 3332)) {
						if (Tools2.isHaveNpc(943)) {
							MyChat.talkNpc(943, 1, 0);
						} else {
							Tools2.objectClick(3516, "Enter");
						}
					} else {
						Tools2.drinkStam();
						Tools2.walkto(3439, 3336);
					}
				} else if (task.getStep() == 20) {//
					if (Tools2.invIsHave(2966)) {
						if (Tools2.isHaveNpc(943)) {
							Tools2.invUseToNpc(943, 2966);
							while (MyChat.isOpenChat()) {
								MyChat.checkChat(1, 0);
							}
						} else {
							Tools2.objectClick(3516, "Enter");
						}
					} else if (!Tools2.groundItemClickAt(2964, 3438, 3337, "Take")) {
						Tools2.groundItemClick(2966, "Take");
					}
				} else if (task.getStep() == 25) {//
					if (Tools2.invIsHave(2968)) {
						MyChat.checkChat(4, 0);
					} else if (Tools2.invIsHave(2967)) {
						if (Tools2.isHaveNpc(943)) {
							Tools2.invUseToNpc(943, 2967);
							while (MyChat.isOpenChat()) {
								MyChat.checkChat(4, 0);
							}
						} else {
							Tools2.objectClick(3516, "Enter");
						}
					} else {
						Tools2.objectClick(3517, "Search");
					}
				} else if (task.getStep() == 30) {//
					if (Tools2.isHaveNpc(943)) {
						MyChat.checkChat(4, 0);
					} else {
						Tools2.objectClick(3516, "Enter");
					}
				} else if (task.getStep() == 35) {//
					if (Tools2.inBox(3437, 9899, 3442, 3441)) {
						MyChat.talkNpc(9805, 1, 0);
					} else {
						Tools2.walkto(3440, 9895);
					}
				} else if (task.getStep() == 40 || task.getStep() == 45) {//
					if (Tools2.distanceTo(3423, 3336, 0)) {
						if (Tools2.objectClick(3509, "Pick")) {
						} else {
							Tools2.invClick(2968, "Cast");
						}
					} else if (Tools2.inBox(3419, 3340, 3429, 3329)) {
						Tools2.walkScreento0(3423, 3336);
					} else {
						Tools2.drinkStam();
						Tools2.walkto(3424, 3339);
					}
				} else if (task.getStep() == 55 || task.getStep() == 50) {//
					if (Tools2.inBox(3434, 3342, 3446, 3332)) {
						if (Tools2.invIsHave(2970)) {
							Tools2.invUseToObject(3527, 2970);
						} else if (Tools2.invIsHave(2969)) {
							Tools2.invUseToObject(3529, 2969);
						} else if (Tools2.isHaveNpc(943)) {
							if (Tools2.distanceTo(3440, 3335, 0)) {
								MyChat.talkNpc(943, 3, 0);
							} else {
								Tools2.walkScreento0(3440, 3335);
							}
						} else {
							Tools2.objectClick(3516, "Enter");
						}
					} else if (!Tools2.invIsHave(2970)) {
						if (Tools2.objectClick(3509, "Pick")) {
						} else {
							Tools2.invClick(2968, "Cast");
						}
					} else {
						Tools2.walkto(3439, 3336);
					}
				} else if (task.getStep() == 60) {//
					Tools2.objectClick(3516, "Enter");
				} else if (task.getStep() == 65) {//
					if (Tools2.distanceTo(3442, 9734, 10)) {
						if (MyChat.isOpenChat()) {
							MyChat.checkChat(1, 0);
						} else {
							Tools2.objectClick(3520, "Search");
						}
					} else {
						Tools2.objectClick(3516, "Enter");
					}
				} else if (task.getStep() == 70) {//
					Tools2.objectClick(3520, "Search");
					for (int i = 0; i < 4; i++) {
						if (task.getStep() == 75) {//
							break;
						}
						MyChat.checkChat(2, 0);
					}
				} else if (task.getStep() == 75) {//
					if (Tools2.isHaveNpc(944)) {
						MyChat.checkChat(2, 0);
					} else if (Tools2.distanceTo(3442, 9734, 10)) {
						if (Tools2.invIsHave(2957)) {
							Tools2.objectClick(3525, "Exit");
						} else {
							Tools2.groundItemClick(2957, "Take");
						}
					} else if (Tools2.distanceTo(3423, 3336, 0)) {
						if (Tools2.drinkPrayer(1)) {

						} else if (Tools2.objectClick(3509, "Pick")) {

						} else {
							Tools2.invClick(2963, "Cast Bloom");
						}
					} else if (Tools2.inBox(3419, 3340, 3429, 3329)) {
						Tools2.walkScreento0(3423, 3336);
					} else {
						Tools2.walkto(3424, 3339);
					}
				} else if (task.getStep() == 80 || task.getStep() == 85) {//
					if (Tools2.distanceTo(3423, 3336, 0)) {
						if (Tools2.invNum(2970) >= 3) {
							Tools2.invClick(2957, "Fill");
						} else if (Tools2.drinkPrayer(1)) {

						} else if (Tools2.objectClick(3509, "Pick")) {

						} else {
							Tools2.invClick(2963, "Cast Bloom");
						}
					} else if (Tools2.inBox(3419, 3340, 3429, 3329)) {
						Tools2.walkScreento0(3423, 3336);
					} else {
						Tools2.walkto(3424, 3339);
					}
				} else if (task.getStep() == 90 || task.getStep() == 95 || task.getStep() == 100) {//
					if (Tools2.inBox(3412, 3349, 3436, 3323)) {
						if (Tools2.isHaveNpc(946)) {
							Tools2.eat();
						} else if (Tools2.invIsHave(2957)) {

							if (Tools2.distanceTo(3423, 3336, 0)) {
								if (Tools2.invNum(2970) >= 3) {
									Tools2.invClick(2957, "Fill");
								} else if (Tools2.drinkPrayer(1)) {
								} else if (Tools2.objectClick(3509, "Pick")) {
								} else {
									Tools2.invClick(2963, "Cast Bloom");
								}
							} else if (Tools2.inBox(3419, 3340, 3429, 3329)) {
								Tools2.walkScreento0(3423, 3336);
							} else {
								Tools2.walkto(3424, 3339);
							}

						} else {
							Tools2.invUseToNpc(945, 2958);
							Waiting.waitNormal(2350, 150);
						}
					} else {
						Tools2.walkto(3423, 3338);
					}

				} else if (task.getStep() == 105) {//
					if (Tools2.distanceTo(3442, 9734, 10)) {
						if (MyChat.isOpenChat()) {
							MyChat.checkChat(1, 0);
						} else if (Tools2.isHaveNpc(944)) {
							MyChat.talkNpc(944);
						} else {
							Tools2.objectClick(3520, "Search");
						}
					} else if (Tools2.inBox(3434, 3342, 3446, 3332)) {
						Tools2.objectClick(3516, "Enter");
					} else {
						Tools2.walkto(3439, 3336);
					}
				}
			} else {
				Login.login();
			}
		}
	}
}
