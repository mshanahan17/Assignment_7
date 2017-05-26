package Project;

import java.util.ArrayList;

public class Broker extends Persons{//subclass of persons(inheritance)

	private String type;
	private String secIdentifier;

	public Broker(String code, String type, String secIdentifier, String lastName, String firstName, Address address, ArrayList<String> email) {
		super(code, lastName, firstName, address, email);
		this.type = type.trim();
		this.secIdentifier = secIdentifier;
	}

	@Override
	public String getType() {
		return type;
	}

	public String getSecIdentifier() {
		return secIdentifier;
	}

	@Override //override from persons superclass
	public double getFees(String a, int b){
		if(b == 0 ){
			return 0.0;
		}
		else{
			if(a.equalsIgnoreCase("J")){
				return b * 50;
			}
			else{
				return b * 10;
			}	
		}
	}

	@Override//override from persons superclass
	public double getCommision(String a, double b){
		if(b == 0.0){
			return 0.0;
		}
		else{
			if(a.equalsIgnoreCase("J")){
				return .02 * b;
			}
			else{
				return .05 * b;
			}
		}
	}


}
