package ch.hevs.aislab.magpie.model;

public class Observation {

	protected long timestamp;
	
	public Observation(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}