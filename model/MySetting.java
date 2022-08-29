package scripts.model;

import org.tribot.script.sdk.util.ScriptSettings;

public class MySetting {

	public int comId;

	public int windowId;

	public int targComId;

	public int targWindowId;

	public static void save(int comId, int windowId, int targComId, int targWindowId) {
		MySetting instance = new MySetting();
		instance.comId = comId;
		instance.windowId = windowId;
		instance.targComId = targComId;
		instance.targWindowId = targWindowId;
		ScriptSettings.builder().build().save("task", instance);
	}
	
	public static void save(String name ,int comId, int windowId, int targComId, int targWindowId) {
		if(name == null || "".equals(name)){
			return;
		}
		MySetting instance = new MySetting();
		instance.comId = comId;
		instance.windowId = windowId;
		instance.targComId = targComId;
		instance.targWindowId = targWindowId;
		ScriptSettings.builder().build().save(name, instance);
	}

	public static MySetting load() {
		if (ScriptSettings.builder().build().load("task", MySetting.class).isPresent()) {
			return ScriptSettings.builder().build().load("task", MySetting.class).get();
		}else{
			return new MySetting();
		}
	}
	
	public static MySetting load(String name) {
		if(name == null || "".equals(name)){
			return new MySetting();
		}
		if (ScriptSettings.builder().build().load(name, MySetting.class).isPresent()) {
			return ScriptSettings.builder().build().load(name, MySetting.class).get();
		}else{
			return new MySetting();
		}
	}

}
