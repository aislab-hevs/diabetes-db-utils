package ch.hevs.aislab.magpie.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObservationRaw implements Comparable<ObservationRaw> {

	private String id;
	private String code;
	private String description;
	private String value;
	private String unit;
	private String timestamp;
	
	public ObservationRaw(String id, String code, String description,
			String value, String unit, String timestamp) {
		this.id = id;
		this.code = code;
		this.description = description;
		this.value = value;
		this.unit = unit;
		this.timestamp = timestamp;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObservationRaw) {
			ObservationRaw other = (ObservationRaw) obj;
			return (code.equals(other.code)) &&
					(timestamp.equals(other.timestamp));
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public String toString() {
		return id + " " + code + " " + description + " " + value + " " + unit + " " + timestamp;
	}
	
	@Override
	public int compareTo(ObservationRaw other) {
		final int SMALLER = -1;
		final int EQUAL = 0;
		final int BIGGER = 1;
		
		if (this == other) {
			return EQUAL;
		}
		
		long millsThis = getTimestampMills(timestamp);
		long millsOther = getTimestampMills(other.getTimestamp());
		
		if (millsThis < millsOther) {
			return SMALLER;
		}
		
		if (millsThis > millsOther) {
			return BIGGER;
		}
		
		if (millsThis == millsOther) {
			// This case handles blood pressure. First we need systolic then diastolic value
			if ( (code.equals("8480-6")) && (other.code.equals("8462-4")) ) {
				return SMALLER;
			} else if ( (code.equals("8462-4")) && (other.code.equals("8480-6")) ) {
				return BIGGER;
			}
			
		}
		
		return EQUAL;
	}
	
	private long getTimestampMills(String timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(0);
		try {
			date = sdf.parse(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
