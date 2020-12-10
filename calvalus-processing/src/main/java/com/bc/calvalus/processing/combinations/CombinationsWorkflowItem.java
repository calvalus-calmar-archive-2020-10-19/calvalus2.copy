package com.bc.calvalus.processing.combinations;

import com.bc.calvalus.processing.JobConfigNames;
import com.bc.calvalus.processing.JobUtils;
import com.bc.calvalus.processing.beam.SimpleOutputFormat;
import com.bc.calvalus.processing.hadoop.HadoopProcessingService;
import com.bc.calvalus.processing.hadoop.HadoopWorkflowItem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * A workflow item creating a Hadoop job for iterating over all possible combinations of the variables..
 *
 * @author MarcoZ
 */
public class CombinationsWorkflowItem extends HadoopWorkflowItem {

    public static final String COMBINATION_CONFIG = "calvalus.combinations.config";

    public CombinationsWorkflowItem(HadoopProcessingService processingService, String username, String jobName, Configuration jobConfig) {
        super(processingService, username, jobName, jobConfig);
    }

    @Override
    public String getOutputDir() {
        return getJobConfig().get(JobConfigNames.CALVALUS_OUTPUT_DIR);
    }

    @Override
    protected String[][] getJobConfigDefaults() {
        return new String[][]{
                {JobConfigNames.CALVALUS_BUNDLES, null},
                {JobConfigNames.CALVALUS_L2_OPERATOR, null},
                {COMBINATION_CONFIG, NO_DEFAULT},
                {JobConfigNames.CALVALUS_OUTPUT_DIR, NO_DEFAULT},
        };
    }

    @Override
    protected void configureJob(Job job) throws IOException {
        job.setInputFormatClass(CombinationsInputFormat.class);
        job.setMapperClass(CombinationsMapper.class);
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(SimpleOutputFormat.class);

        FileOutputFormat.setOutputPath(job, new Path(getOutputDir()));
    }
}
