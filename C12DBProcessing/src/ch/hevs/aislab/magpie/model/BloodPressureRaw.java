package ch.hevs.aislab.magpie.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BloodPressureRaw {

	private final String id;
	private final String code;
	private final String description;
	private final String value;
	private final String unit;
	private final String time;

	public BloodPressureRaw(Builder builder) {
		this.id = builder.id;
		this.code = builder.code;
		this.description = builder.description;
		this.value = builder.value;
		this.unit = builder.unit;
		this.time = builder.time;
	}
	
	public String getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getValue() {
		return value;
	}
	
	public long getTimeInMills() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(0);
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
	
	@Override
	public String toString() {
		return "Blood Pressure[id=" + id + ", code=" + getCode() + ", description=" + description +
					", value=" + value + ", unit=" + unit + ", time=" + time + "]";
	}
	
	public static class Builder {
		private String id;
		private String code;
		private String description;
		private String value;
		private String unit;
		private String time;
		
		public Builder() {
			
		}
		
		public Builder id(String id) {
			this.id = id;
			return this;
		}
		
		public Builder code(String code) {
			this.code = code;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder value(String value) {
			this.value = value;
			return this;
		}
		
		public Builder unit(String unit) {
			this.unit = unit;
			return this;
		}
		
		public Builder time(String time) {
			this.time = time;
			return this;
		}
		
		public BloodPressureRaw build() {
			return new BloodPressureRaw(this);
		}
	}
}
