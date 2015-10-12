package ch.hevs.aislab.magpie.model;

public class Glucose extends Observation {

	public static final String UNIT = "mmol/l";
	
	private String code;
	private String description;
	private double value;
	
	public Glucose(String code, String description, double value, long timestamp) {
		super(timestamp);
		this.code = code;
		this.description = description;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Glucose[timestamp="+ timestamp +", value=" + value + "]";
	}	
}
