package ch.hevs.aislab.magpie.rules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import ch.hevs.aislab.magpie.model.BloodPressure;
import ch.hevs.aislab.magpie.model.BloodPressureRaw;
import ch.hevs.aislab.magpie.model.Glucose;
import ch.hevs.aislab.magpie.model.ObservationRaw;
import ch.hevs.aislab.magpie.model.Patient;
import ch.hevs.aislab.magpie.model.Weight;

public class RuleProcessor {

	private static final String CSV_FILE = "data/test.csv";
	private static final String CSV_SEPARATOR = ";";
	private static final String OUTPUT_FOLDER = "output";
	private static final String STATISTICS_OUT_FOLDER = "statistics";
	
	// Headers for the patients' CSV files
	private static final String PATIENT_ID = "patient_id";
	private static final String CODE = "code";
	private static final String DESCRIPTION = "description";
	private static final String VALUE = "value";
	private static final String UNIT = "unit";
	private static final String EFFECTIVE_FROM = "effective_from";
	// Headers for the statistics CSV files
	private static final String GLUCOSE_MEAN = "glucose_mean";
	private static final String GLUCOSE_SD = "glucose_sd";
	private static final String SYSTOLIC_MEAN = "systolic_mean";
	private static final String SYSTOLIC_SD = "systolic_sd";
	private static final String DIASTOLIC_MEAN = "diastolic_mean";
	private static final String DIASTOLIC_SD = "diastolic_sd";
	private static final String WEIGHT_MEAN = "weight_mean";
	private static final String WEIGHT_SD = "weight_sd";
 	
	private static Map<Long, Patient> patients = new TreeMap<Long, Patient>();
	private static List<BloodPressureRaw> sysRawBPList = new ArrayList<BloodPressureRaw>();
	private static List<BloodPressureRaw> diasRawBPList = new ArrayList<BloodPressureRaw>();
	
	/**
	 * @param args
	 * @throws  
	 */
	public static void main(String[] args) {

		RuleProcessor rt = new RuleProcessor();
		rt.parseCSVpatients(CSV_FILE);
		System.out.println("Total number of patients: " + patients.size());
		rt.parseCSVobservations(CSV_FILE);
		rt.processRawBP();
		
		File folder = new File(OUTPUT_FOLDER);
		folder.mkdir();
		boolean first = true;
		for (Patient p : patients.values()) {
			
			// Create a csv per patient
			File patientFile = new File(OUTPUT_FOLDER + "/" + p.getId() + ".csv");
			try {
				patientFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Remove duplicate observations and sort them by date
			List<ObservationRaw> list = p.getObservations();
			Set<ObservationRaw> set = new HashSet<ObservationRaw>(list);
			list.clear();
			list.addAll(set);
			Collections.sort(list);
			
			// Objects to compute statistics on the events
			DescriptiveStatistics glucoseDS = new DescriptiveStatistics(160);
			DescriptiveStatistics systolicDS = new DescriptiveStatistics(160);
			DescriptiveStatistics diastolicDS = new DescriptiveStatistics(160);
			DescriptiveStatistics weightDS = new DescriptiveStatistics(160);
			
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(patientFile, true));
				String header = PATIENT_ID + CSV_SEPARATOR + CODE + CSV_SEPARATOR + DESCRIPTION + CSV_SEPARATOR +
						VALUE + CSV_SEPARATOR + UNIT + CSV_SEPARATOR + EFFECTIVE_FROM;
				bw.write(header + "\n");
				for (ObservationRaw obs : p.getObservations()) {
					
					// Count the Observation
					switch (obs.getUnit()) {
						case Glucose.UNIT:
							glucoseDS.addValue(Double.parseDouble(obs.getValue()));
							break;
						case BloodPressure.UNIT:
							if (obs.getCode().equals(BloodPressure.DIASTOLIC_BP_CODE)) {
								diastolicDS.addValue(Double.parseDouble(obs.getValue()));
							} else if (obs.getCode().equals(BloodPressure.SYSTOLIC_BP_CODE)) {
								systolicDS.addValue(Double.parseDouble(obs.getValue()));
							}
							break;
						case Weight.UNIT:
							weightDS.addValue(Double.parseDouble(obs.getValue()));
							break;
						default:
					}
					
					// Write the Observation
					String line = obs.getId() + CSV_SEPARATOR + obs.getCode() + CSV_SEPARATOR + obs.getDescription()
							+ CSV_SEPARATOR + obs.getValue() + CSV_SEPARATOR + obs.getUnit() + CSV_SEPARATOR
							+ obs.getTimestamp();
					bw.write(line + "\n");
				}
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			// Write statistics in file
			File statsFolder = new File(STATISTICS_OUT_FOLDER);
			File statsFile = new File(STATISTICS_OUT_FOLDER + "/statistics.csv");
			if (!statsFolder.exists()) {
				statsFolder.mkdir();
			}
			bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(statsFile, true));
				
				if (first) {
					String header = PATIENT_ID + CSV_SEPARATOR +
							GLUCOSE_MEAN + CSV_SEPARATOR + GLUCOSE_SD + CSV_SEPARATOR +
							SYSTOLIC_MEAN + CSV_SEPARATOR + SYSTOLIC_SD + CSV_SEPARATOR +
							DIASTOLIC_MEAN + CSV_SEPARATOR + DIASTOLIC_SD + CSV_SEPARATOR +
							WEIGHT_MEAN + CSV_SEPARATOR + WEIGHT_SD;
					bw.write(header + "\n");
					first = false;
				}
				
				double glucose_mean = glucoseDS.getMean();
				double glucose_sd = glucoseDS.getStandardDeviation();
				double systolic_mean = systolicDS.getMean();
				double systolic_sd = systolicDS.getStandardDeviation();
				double diastolic_mean = diastolicDS.getMean();
				double diastolic_sd = diastolicDS.getStandardDeviation();
				double weight_mean = weightDS.getMean();
				double weight_sd = weightDS.getStandardDeviation();
				
				String line = p.getId() + CSV_SEPARATOR + glucose_mean + CSV_SEPARATOR + glucose_sd + CSV_SEPARATOR +
						systolic_mean + CSV_SEPARATOR + systolic_sd + CSV_SEPARATOR +
						diastolic_mean + CSV_SEPARATOR + diastolic_sd + CSV_SEPARATOR +
						weight_mean + CSV_SEPARATOR + weight_sd + CSV_SEPARATOR;
				bw.write(line + "\n");
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}	
	}
	
	public void parseCSVpatients(String filename) {
		File db = new File(filename);
		BufferedReader br = null;
		String line = "";
		
		try {
			br = new BufferedReader(new FileReader(db));
			br.readLine(); // Skip the first line
			while ( (line = br.readLine()) != null ) {
				String[] observation = line.split(CSV_SEPARATOR);
				long id = Long.parseLong(observation[0]);
				patients.put(id, new Patient(id));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void parseCSVobservations(String filename) {
		File db = new File(filename);
		BufferedReader br = null;
		String line = "";
		
		try {
			br = new BufferedReader(new FileReader(db));
			br.readLine(); // Skip the first line
			while ( (line = br.readLine()) != null ) {
				String[] observation = line.split(CSV_SEPARATOR);
				
				long id = Long.parseLong(observation[0]);
				String code = observation[1];
				String description = observation[2];
				double value = Double.parseDouble(observation[3]);
				String units = observation[4];
				long timestamp = parseDate(observation[5]);
				ObservationRaw obs = new ObservationRaw(observation[0], code, description,
						observation[3], units, observation[5]);
				
				Patient p = patients.get(id);
				p.getObservations().add(obs);
				
				switch (units) {
					case Glucose.UNIT:
						Glucose g = new Glucose(code, description, value, timestamp);
						p.getGlucose().add(g);
						break;
					case BloodPressure.UNIT:
						BloodPressureRaw bp = new BloodPressureRaw.Builder()
							.id(observation[0])
							.code(observation[1])
							.description(observation[2])
							.value(observation[3])
							.unit(units)
							.time(observation[5])
							.build();
						if (bp.getCode().equals(BloodPressure.SYSTOLIC_BP_CODE)) {
							sysRawBPList.add(bp);
						} else if (bp.getCode().equals(BloodPressure.DIASTOLIC_BP_CODE)) {
							diasRawBPList.add(bp);
						}
						break;
					case Weight.UNIT:
						Weight w = new Weight(value, timestamp);
						p.getWeight().add(w);
						break;
					default:
						System.out.println("Units '" + units + "' not recognized");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void processRawBP() {
		for (int i = 0; i < diasRawBPList.size(); i++) {
			BloodPressureRaw diasRawBP = diasRawBPList.get(i);
			long id = Long.parseLong(diasRawBP.getId());
			double diasValue = Double.parseDouble(diasRawBP.getValue());
			long timestamp = diasRawBP.getTimeInMills();
			Iterator<BloodPressureRaw> it = sysRawBPList.iterator();
			while (it.hasNext()) {
				BloodPressureRaw sysRawBP = it.next();
				long idSys = Long.parseLong(sysRawBP.getId());
				if ( (idSys == id) &&
						(sysRawBP.getTimeInMills() == timestamp) ) {
					double sysValue = Double.parseDouble(diasRawBP.getValue());
					BloodPressure bp = new BloodPressure(sysValue, diasValue, timestamp);
					Patient p = patients.get(id);
					p.getBloodPressure().add(bp);
					break;
				}
			}
		}		
	}
	
	private long parseDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(0);
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();	
	}
}