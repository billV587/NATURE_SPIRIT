package scripts.model;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class MyArea{
	
	public static Area to(int x,int y,int x1,int y1){
		return Area.fromRectangle(new WorldTile(x1, y1), new WorldTile(x,y));
	}
	
	public static void main(String[] args) {
		String aString = "Ac";
		System.out.println(aString.concat("a"));
	}
}
