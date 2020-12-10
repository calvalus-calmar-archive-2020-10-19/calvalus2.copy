package com.bc.calvalus.processing.ma;

import java.io.IOException;

/**
 * Processes record values.
 *
 * @author Norman
 */
public interface RecordProcessor {

    void processHeaderRecord(Object[] attributeNames, Object[] annotationNames) throws IOException;

    void processDataRecord(String key, Object[] recordValues, Object[] annotationValues) throws IOException;

    void finalizeRecordProcessing() throws IOException;
}
