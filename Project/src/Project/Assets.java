package Project;

public abstract class Assets{

	protected String code;
	protected String type;
	protected String label;

	public Assets(String id,String type, String label) {
		this.code = id;
		this.type = type;
		this.label = label;	
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public String getType() {
		return type;
	}

	public abstract double getValue();
	public abstract double getRisk();
	public abstract double getReturn();

}
