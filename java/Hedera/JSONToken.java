package Hedera;
 
public class JSONToken {
	private String id;
	private String symbol;
	private int numOfDocs;
	

	public JSONToken(String id) {
		this.id = id;
	}

	public JSONToken(String id, String symbol, int numOfDocs) {
		this.id = id;
		this.symbol = symbol;
		this.numOfDocs = numOfDocs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setName(String symbol) {
		this.symbol = symbol;
	}
	
	public int getNumOfDocs() {
		return numOfDocs;
	}

	public void setNumOfDocs(int numOfDocs) {
		this.numOfDocs = numOfDocs;
	}

}
