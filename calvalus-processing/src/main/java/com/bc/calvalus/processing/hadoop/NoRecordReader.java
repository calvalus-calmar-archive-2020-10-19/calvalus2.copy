package com.bc.calvalus.processing.hadoop;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * Does nothing by intention.
 *
 * @author Martin Boettcher
 */
public class NoRecordReader extends RecordReader<NullWritable, NullWritable> {

    private ProgressSplit progressSplit;

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        if (split instanceof ProgressSplit) {
            progressSplit = (ProgressSplit) split;
        }
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        return false;
    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return null;
    }

    @Override
    public NullWritable getCurrentValue() throws IOException, InterruptedException {
        return null;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        if (progressSplit != null) {
            return progressSplit.getProgress();
        } else {
            return 0;
        }
    }

    @Override
    public void close() throws IOException {
    }
}
