package ch.hevs.aislab.magpie.counter;

public class Day {

	public static final String GLUCOSE = "mmol/l";
	public static final String BLOOD_PRESSURE = "mm[Hg]";
	public static final String WEIGHT = "kg";
	
	private String id;
	private String day;
	private int glucose;
	private int bloodPressure;
	private int weight;
	
	public Day(String id, String day) {
		this.id = id;
		this.day = day;
		this.glucose = 0;
		this.bloodPressure = 0;
		this.weight = 0;
	}
	
	public void increaseGlucose() {
		this.glucose++;
	}
	
	public void increaseBloodPressure() {
		this.bloodPressure++;
	}
	
	public void increaseWeight() {
		this.weight++;
	}
	
	@Override
	public String toString() {
		return day + " Glucose:" + glucose + " Blood Pressure:" + bloodPressure + " Weight:" + weight;
	}

	public String getId() {
		return id;
	}

	public String getDay() {
		return day;
	}

	public int getGlucose() {
		return glucose;
	}

	public int getBloodPressure() {
		return bloodPressure;
	}

	public int getWeight() {
		return weight;
	}
}
