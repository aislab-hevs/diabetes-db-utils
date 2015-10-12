package ch.hevs.aislab.magpie.model;

public class Weight extends Observation {

	public static final String UNIT = "kg";
	public static final String WEIGHT_CODE = "27113001";
	public static final String WEIGHT_DESC = "Body weight";
	
	private double value;
	
	public Weight(double value, long timestamp) {
		super(timestamp);
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Weight[timestamp="+ timestamp +", value=" + value + "]";
	}
}
