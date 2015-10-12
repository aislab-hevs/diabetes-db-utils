package ch.hevs.aislab.magpie.counter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class DatabaseCounter {

	private static final String PATIENT_FOLDER = "output";
	private static final String OUTPUT_FOLDER = "results";
	private static final String CSV_SEPARATOR = ";";
	
	private static final String PATIENT_ID = "patient_id";
	private static final String DAY = "day";
	private static final String GLUCOSE = "glucose";
	private static final String BLOOD_PRESSURE = "blood_pressure";
	private static final String WEIGHT = "weight";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File root = new File(PATIENT_FOLDER);
		File[] files = root.listFiles();
		for (File file : files) {			
			Map<String,Day> days = new TreeMap<String,Day>();
			
			// Read the input file
			BufferedReader br = null;
			String patientId = null;
			try {
				br = new BufferedReader(new FileReader(file));
				String line = null;
				br.readLine();
				while ((line = br.readLine()) != null) {
					String[] observation = line.split(CSV_SEPARATOR);
					patientId = observation[0];
					String units = observation[4];
					String timestamp = compressDay(observation[5]);
					if (!days.containsKey(timestamp)) {
						Day day = new Day(patientId, timestamp);
						countObservation(day, units);
						days.put(timestamp,day);
					} else {
						Day day = days.get(timestamp);
						countObservation(day, units);
						days.put(timestamp, day);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// Write the output file
			File outputFolder = new File(OUTPUT_FOLDER);
			if (!outputFolder.exists()) {
				outputFolder.mkdir();
			}
			File outputFile = new File(OUTPUT_FOLDER + "/" + patientId + "_.csv");
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(outputFile));
				String header = PATIENT_ID + CSV_SEPARATOR + DAY + CSV_SEPARATOR + GLUCOSE + CSV_SEPARATOR +
						BLOOD_PRESSURE + CSV_SEPARATOR + WEIGHT;
				bw.write(header + "\n");
				for (Day day : days.values()) {
					String line = day.getId() + CSV_SEPARATOR + day.getDay() + CSV_SEPARATOR + day.getGlucose() +
							CSV_SEPARATOR + day.getBloodPressure()/2 + CSV_SEPARATOR + day.getWeight();
					bw.write(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String compressDay(String day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		String output = null;
		try {
			date = sdf.parse(day);
			output = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	private static void countObservation(Day day, String units) {
		switch(units) {
			case Day.GLUCOSE:
				day.increaseGlucose();
				break;
			case Day.BLOOD_PRESSURE:
				day.increaseBloodPressure();
				break;
			case Day.WEIGHT:
				day.increaseWeight();
				break;
			default:
				System.out.println("Units '" + units + "' not recognized");
		}
	}

}
