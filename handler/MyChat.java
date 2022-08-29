package scripts.handler;

import org.tribot.script.sdk.ChatScreen;
import org.tribot.script.sdk.EnterInputScreen;
import org.tribot.script.sdk.Log;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.input.Keyboard;

public class MyChat extends ChatScreen {

	public static boolean checkChat(int selectIndex, int inputNum) {
		if (isOpenChat()) {
			if (isClickContinueOpen()) {
				clickContinue();
			}

			if (EnterInputScreen.isOpen()) {
				EnterInputScreen.enter(inputNum);
			}

			if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen()) {
				if (getOptions().contains("Yes.")) {
					selectOption("yes");
				} else {
					Keyboard.typeString(selectIndex + "");
				}
			}
			for (int i = 0; i < 20; i++) {
				clickContinue();
				Waiting.waitUniform(50, 100);
			}
			return true;
		} else {
			return false;
		}
	}

	public static void talkNpc(int npc, int selectIndex, int inputNum) {
		if (isOpenChat()) {
			checkChat(selectIndex, inputNum);
		} else {
			Waiting.waitUniform(1350, 100);
			if(!isOpenChat()){
				Tools.npcClick("Talk-to",npc );
				Waiting.waitUniform(1350, 100);
			}
		}
	}
	
	public static void strikeNpc(int npc, int selectIndex, int inputNum) {
		if (isOpenChat()) {
			checkChat(selectIndex, inputNum);
		} else {
			Waiting.waitUniform(1350, 100);
			if(!isOpenChat()){
				Tools.npcClick("Strike",npc);
				Waiting.waitUniform(1350, 100);
			}
		}
	}

	public static boolean talkNpcWaitMsg(int npc, int selectIndex, String msg) {
		if (isOpenChat()) {
			if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen()) {
				Keyboard.typeString(selectIndex + "");
			} else if (getMessage().isPresent()) {
				if (getMessage().get().contains(msg)) {
					Keyboard.pressEscape();
					clickContinue();
					return true;
				} else {
					Keyboard.pressEscape();
					clickContinue();
				}
			}

		} else {
			Waiting.waitUniform(1350, 100);
			if (!isOpenChat()) {
				Tools.npcClick("Talk-to", npc);
				Waiting.waitUniform(1350, 100);
			}
		}
		return false;
	}
	
	public static boolean commissionNpcWaitMsg(int npc, String msg) {
		if (isOpenChat()) {
			if (getMessage().isPresent()) {
				if (getMessage().get().contains(msg)) {
					Keyboard.pressEscape();
					return true;
				} else {
					Keyboard.pressEscape();
				}
			}else{
				Keyboard.typeString("1");
			}
		} else {
			if (!isOpenChat()) {
				Tools.npcClick("Commission", npc);
				Waiting.waitUniform(1350, 100);
			}
		}
		return false;
	}

	public static boolean talkWaitMsg(int selectIndex, String msg) {
		if (isOpenChat()) {
			if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen()) {
				Keyboard.typeString(selectIndex + "");
			} else if (getMessage().isPresent()) {
				if (getMessage().get().contains(msg)) {
					Keyboard.pressEscape();
					Log.info(1);
					return true;
				} else {
					Keyboard.pressEscape();
				}
			}

		}
		return false;
	}

	public static void talkNpc(int npc, int selectIndex, boolean isTwoSelectFrist) {
		if (isOpenChat()) {
			if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen()) {
				if (getOptions().size() == 2 && isTwoSelectFrist) {
					Keyboard.typeString("1");
				} else {
					Keyboard.typeString(selectIndex + "");
				}
			} else if (isClickContinueOpen()) {
				for (int i = 0; i < 20; i++) {
					clickContinue();
					Waiting.waitUniform(50, 100);
					if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen()) {
						break;
					}
				}
			}
		} else {
			Waiting.waitUniform(1350, 100);
			if(!isOpenChat()){
				Tools.npcClick("Talk-to", npc);
				Waiting.waitUniform(1350, 100);
			}
		}
	}

	public static void talkNpc(int npc, String... options) {
		if (isOpenChat()) {
			if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen()) {
				if (containsOption(options)) {
					selectOption(options);
					Waiting.waitUniform(350, 600);
				}
			} else if (isClickContinueOpen()) {
				for (int i = 0; i < 20; i++) {
					clickContinue();
					Waiting.waitUniform(50, 100);
					if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen()) {
						break;
					}
				}
			}
		} else {
			Waiting.waitUniform(1350, 100);
			if(!isOpenChat()){
				Tools.npcClick("Talk-to", npc);
				Waiting.waitUniform(1350, 100);
			}
		}
	}

	public static boolean talkNpc(String... options) {
		if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen()) {
			if (containsOption(options)) {
				selectOption(options);
				Waiting.waitUniform(350, 600);
				for (int i = 0; i < 90; i++) {
					clickContinue();
					Waiting.waitUniform(50, 100);
					if (isSelectOptionOpen() || ChatScreen.isSelectOptionOpen() || !isOpenChat()) {
						break;
					}
				}
				return true;
			}
		}
		return false;
	}

	public static void talkNpc(int npc) {
		if (isOpenChat()) {
			clickContinue();
			Waiting.waitUniform(350, 50);
		} else {
			Waiting.waitUniform(1350, 100);
			if(!isOpenChat()){
				Tools.npcClick("Talk-to", npc);
				Waiting.waitUniform(1350, 100);
			}
		}
	}

	public static boolean isOpenChat() {// !Tools.interfaceIsHidden(229, 0) ||
		return isOpen() || EnterInputScreen.isOpen() || isSelectOptionOpen() || isClickContinueOpen()
				|| ChatScreen.isSelectOptionOpen() || !Tools.interfaceIsHidden(231, 0) || !Tools.interfaceIsHidden(219, 0)
				|| !Tools.interfaceIsHidden(217, 0) || !Tools.interfaceIsHidden(193, 0)
				|| !Tools.interfaceIsHidden(11, 0);
	}
}
