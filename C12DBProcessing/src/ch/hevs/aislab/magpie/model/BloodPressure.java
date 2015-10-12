package ch.hevs.aislab.magpie.model;

public class BloodPressure extends Observation {

	public static final String UNIT = "mm[Hg]";
	public static final String SYSTOLIC_BP_CODE = "8480-6";
	public static final String DIASTOLIC_BP_CODE = "8462-4";
	public static final String SYSTOLIC_BP_DESC = "Systolic blood pressure";
	public static final String DIASTOLIC_BP_DESC = "Diastolic blood pressure";
	
	private double systolic;
	private double diastolic;
	
	public BloodPressure(double systolic, double diastolic, long timestamp) {
		super(timestamp);
		this.systolic = systolic;
		this.diastolic = diastolic;
	}

	public double getSystolic() {
		return systolic;
	}

	public void setSystolic(double systolic) {
		this.systolic = systolic;
	}

	public double getDiastolic() {
		return diastolic;
	}

	public void setDiastolic(double diastolic) {
		this.diastolic = diastolic;
	}
}
