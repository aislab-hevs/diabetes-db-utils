close all;
clear all;
excel_file='24598269.xls';
[event_num,event_txt,event_raw]=xlsread(excel_file);

event_code=event_txt(2:end,2);
event_values=event_num(2:end,4);
event_units=event_txt(2:end,5);

%Find the index for each pysiological value
mmoll_index=find(strcmp(event_units,'mmol/l'));
sys_index=find(strcmp(event_code,'8480-6'));
dias_index=find(strcmp(event_code,'8462-4'));
kg_index=find(strcmp(event_units,'kg'));

%Values in Y axes
event_glucose=event_values(mmoll_index);
event_systolic=event_values(sys_index);
event_diastolic=event_values(dias_index);
event_weight=event_values(kg_index);

%Values in X axes
event_times=datenum(event_txt(2:end,6),'yyyy-mm-dd HH:MM:SS');
time_glucose=event_times(mmoll_index);
time_blood_pressure=event_times(sys_index);
time_weight=event_times(kg_index);

%Plot the physiological values
%Glucose
plot(time_glucose,event_glucose);
ylabel('Glucose (mmol/l)');
datetick('x','dd-mm','keeplimits','keepticks');
strGlucoseValues = strtrim(cellstr(num2str(event_glucose,3)));
text(time_glucose,event_glucose,strGlucoseValues,'VerticalAlignment','bottom');

%Blood Pressure
figure
plot(time_blood_pressure,event_systolic);
hold on;
plot(time_blood_pressure,event_diastolic);
ylabel('Blood Pressure (mmHg)');
datetick('x','dd-mm','keeplimits','keepticks');
strSystolicValues = strtrim(cellstr(num2str(event_systolic)));
text(time_blood_pressure,event_systolic,strSystolicValues,'VerticalAlignment','bottom');
strDiastolicValues = strtrim(cellstr(num2str(event_diastolic)));
text(time_blood_pressure,event_diastolic,strDiastolicValues,'VerticalAlignment','bottom');

%Weight
figure
plot(time_weight,event_weight);
ylabel('Weight (kg)');
datetick('x','dd-mm','keeplimits','keepticks');
strWeightValues = strtrim(cellstr(num2str(event_weight)));
text(time_weight,event_weight,strWeightValues,'VerticalAlignment','bottom');