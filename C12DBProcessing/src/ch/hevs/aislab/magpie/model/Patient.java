package ch.hevs.aislab.magpie.model;

import java.util.ArrayList;
import java.util.List;

public class Patient implements Comparable<Patient> {

	private long id;
	private List<Glucose> glucose;
	private List<BloodPressure> bloodPressure;
	private List<Weight> weight;
	private List<ObservationRaw> observations;
	
	public Patient(long id) {
		this.id = id;
		this.glucose = new ArrayList<Glucose>();
		this.bloodPressure = new ArrayList<BloodPressure>();
		this.weight = new ArrayList<Weight>();
		this.observations = new ArrayList<ObservationRaw>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Glucose> getGlucose() {
		return glucose;
	}

	public void setGlucose(List<Glucose> glucose) {
		this.glucose = glucose;
	}
	
	public List<BloodPressure> getBloodPressure() {
		return bloodPressure;
	}
	
	public void setBloodPressure(List<BloodPressure> bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	
	public List<Weight> getWeight() {
		return weight;
	}

	public void setWeight(List<Weight> weight) {
		this.weight = weight;
	}
	
	public List<ObservationRaw> getObservations() {
		return observations;
	}

	public void setObservations(List<ObservationRaw> observations) {
		this.observations = observations;
	}

	@Override
	public int compareTo(Patient other) {
		final int SMALLER = -1;
		final int EQUAL = 0;
		final int BIGGER = 1;
		
		if (this == other) {
			return EQUAL;
		}
		
		if (id < other.id) {
			return SMALLER;
		}
		
		if (id > other.id) {
			return BIGGER;
		}
		
		return EQUAL;
	}
}
