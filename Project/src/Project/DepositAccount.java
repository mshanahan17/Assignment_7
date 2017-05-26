package Project;


public class DepositAccount extends Assets{//subclass of Assets(inheritance)

	private double apr;
	private double value;

	public DepositAccount(String code, String type, String label, double apr) {//constructor
		super(code, type, label);//calls Assets constructor
		this.apr = apr/100;
		this.value = 0.0;
	}

	public DepositAccount(DepositAccount d, double value) {// copy constructor
		super(d.code, d.type, d.label);
		this.apr = d.apr;
		this.value = value;
	}

	public double getRate() {
		return apr;
	}

	public double getReturn() {
		double apy = Math.exp(this.apr)-1;
		double total = this.value * apy;
		return total;
	}

	public double getRisk() {
		return 0.0;
	}

	public double getValue() {
		return value;
	}
	
	public double getVal(){
		return this.value;
	}
}
