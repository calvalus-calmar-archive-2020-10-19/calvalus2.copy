package com.bc.calvalus.processing.l3.multiband;

import com.bc.calvalus.processing.l3.L3TemporalBin;
import com.bc.calvalus.processing.l3.multiregion.L3MultiRegionBinIndex;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 *  The mapper for for formatting
 *  multiple regions of a Binning product at once.
 *
 *  For each region that contains the center lat/lon of the
 *  bin cell the bin emitted.
 */
public class L3MultiBandFormatMapper extends Mapper<LongWritable, L3TemporalBin, L3MultiRegionBinIndex, FloatWritable> {

    private final L3MultiRegionBinIndex key = new L3MultiRegionBinIndex(0, 0);
    private final FloatWritable binValue = new FloatWritable();

    @Override
    protected void map(LongWritable binIndex, L3TemporalBin temporalBin, Context context) throws IOException, InterruptedException {
        float[] temporalFeatureValues = temporalBin.getFeatureValues();
        key.setBinIndex(binIndex.get());
        for (int i=0; i< temporalFeatureValues.length; ++i) {
            key.setRegionIndex(i);
            binValue.set(temporalFeatureValues[i]);
            context.write(key, binValue);
        }
    }
}
