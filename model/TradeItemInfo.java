package scripts.model;

public class TradeItemInfo {

	public TradeItemInfo(int id, int noteId) {
		this.id = id;
		this.noteId = noteId;
	}
	
	public static TradeItemInfo to(int id, int noteId) {
		return new TradeItemInfo(id,noteId);
	}

	public int id;
	public int noteId;
}
