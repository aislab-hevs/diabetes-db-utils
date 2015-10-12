# diabetes-db-utils
Code to clean, compute statistics and represent data from C12 and GDM

-[C12DBProcessing](https://github.com/aislab-hevs/diabetes-db-utils/tree/master/C12DBProcessing): takes a csv file like the one in the data folder, and splits it into different files (one per patient). This util also removes duplicates, and computes statistics (mean ± sd) for the different physiological values. 

-[C12DBCounter](https://github.com/aislab-hevs/diabetes-db-utils/tree/master/C12DBCounter/src/ch/hevs/aislab/magpie/counter): takes the output from C12DBProcessing and computes per each day the number of physiological values.