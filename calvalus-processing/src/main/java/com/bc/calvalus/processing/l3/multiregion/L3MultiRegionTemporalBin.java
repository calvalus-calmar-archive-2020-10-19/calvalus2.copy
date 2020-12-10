package com.bc.calvalus.processing.l3.multiregion;

import org.apache.hadoop.io.Writable;
import org.esa.snap.binning.TemporalBin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;


/**
 * A Hadoop-serializable, temporal bin for multi region formatting.
 * It also serializes the bin index
 *
 * The class is final for allowing method in-lining.
 *
 * @author Marco Zuehlke
 */
public final class L3MultiRegionTemporalBin extends TemporalBin implements Writable {

    public L3MultiRegionTemporalBin() {
        super();
    }

    public L3MultiRegionTemporalBin(long index, int numFeatures) {
        super(index, numFeatures);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(getIndex());
        super.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        setIndex(dataInput.readLong());
        super.readFields(dataInput);
    }

    @Override
    public String toString() {
        return String.format("%s{index=%d, numObs=%d, numPasses=%d, featureValues=%s}",
                                     getClass().getSimpleName(),
                                     getIndex(),
                                     getNumObs(),
                                     getNumPasses(),
                                     Arrays.toString(getFeatureValues()));
    }
}
