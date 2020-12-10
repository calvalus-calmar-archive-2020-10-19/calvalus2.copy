package com.bc.calvalus.processing.ma;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Norman
 */
public class CsvRecordWriterTest {

    private StringWriter recordsAllWriter;
    private StringWriter recordsAggWriter;
    private StringWriter labeledAllWriter;
    private StringWriter labeledAggWriter;
    private CsvRecordWriter writer;

    @Before
    public void setUp() throws Exception {
        recordsAllWriter = new StringWriter();
        recordsAggWriter = new StringWriter();
        labeledAllWriter = new StringWriter();
        labeledAggWriter = new StringWriter();
        writer = new CsvRecordWriter(recordsAllWriter, recordsAggWriter, labeledAllWriter, labeledAggWriter);
    }

    @Test
    public void testWithScalarsOnly() throws Exception {

        writer.processHeaderRecord(createDataHeader("CHL"), new Object[]{"ExclusionReason"});
        writer.processDataRecord("key1", createDataRecord1(0.7), new Object[]{""});
        writer.processDataRecord("key2", createDataRecord2(2.2), new Object[]{RecordFilterTransformer.EXCLUSION_REASON_EXPRESSION});
        writer.processDataRecord("key3", createDataRecord2(0.3), new Object[]{""});
        writer.processDataRecord("key4", createDataRecord3(1.4), new Object[]{""});
        writer.processDataRecord("key5", createDataRecord3(4.5), new Object[]{OverlappingRecordTransform.EXCLUSION_REASON_OVERLAPPING});
        writer.processDataRecord("key6", createDataRecord4(Double.NaN), new Object[]{""});
        writer.processDataRecord("key7", createDataRecord4(null), new Object[]{""});
        writer.finalizeRecordProcessing();

        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\n" +
                     "key6\t\t\tNaN\t13.9\t\tNaN\n" +
                     "key7\t\t\tNaN\t13.9\t\t\n" +
                     "",
                     recordsAllWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\n" +
                     "key6\t\t\tNaN\t13.9\t\tNaN\n" +
                     "key7\t\t\tNaN\t13.9\t\t\n" +
                     "",
                     recordsAggWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL\tExclusionReason\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\t\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t2.2\tRECORD_EXPRESSION\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\t\n" +
                     "key5\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t4.5\tOVERLAPPING\n" +
                     "key6\t\t\tNaN\t13.9\t\tNaN\t\n" +
                     "key7\t\t\tNaN\t13.9\t\t\t\n" +
                     "",
                     labeledAllWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL\tExclusionReason\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\t\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t2.2\tRECORD_EXPRESSION\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\t\n" +
                     "key5\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t4.5\tOVERLAPPING\n" +
                     "key6\t\t\tNaN\t13.9\t\tNaN\t\n" +
                     "key7\t\t\tNaN\t13.9\t\t\t\n" +
                     "",
                     labeledAggWriter.toString());
    }

    @Test
    public void testWithAggregatedNumbersWithoutDataArrays() throws Exception {

        writer.processHeaderRecord(createDataHeader("*CHL"), new Object[]{"ExclusionReason"});
        writer.processDataRecord("key1", createDataRecord1(new AggregatedNumber(3, 3, 0, 0.0, 1.0, 0.7, 0.01)), new Object[]{""});
        writer.processDataRecord("key2", createDataRecord2(new AggregatedNumber(2, 3, 1, 0.0, 1.0, 0.3, 0.02)), new Object[]{""});
        writer.processDataRecord("key3", createDataRecord2(new AggregatedNumber(2, 3, 1, 0.0, 1.0, 0.3, 0.02)),
                                 new Object[]{OverlappingRecordTransform.EXCLUSION_REASON_OVERLAPPING});
        writer.processDataRecord("key4", createDataRecord3(new AggregatedNumber(3, 3, 0, 0.0, 1.0, 1.4, 0.03)), new Object[]{""});
        writer.processDataRecord("key5", createDataRecord4(new AggregatedNumber(0, 0, 0, 0.0, 0.0, Double.NaN, 0.0)), new Object[]{""});
        writer.processDataRecord("key6", createDataRecord4(new AggregatedNumber(0, 0, 0, 0.0, 0.0, Double.NaN, 0.0)),
                                 new Object[]{PixelExtractor.EXCLUSION_REASON_ALL_MASKED});
        writer.finalizeRecordProcessing();

        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\n" +
                     "",
                     recordsAllWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL_mean\tCHL_sigma\tCHL_n\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\t0.01\t3\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t0.02\t2\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\t0.03\t3\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\t0.0\t0\n" +
                     "",
                     recordsAggWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL\tExclusionReason\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\t\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\tOVERLAPPING\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\t\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\t\n" +
                     "key6\t\t\tNaN\t13.9\t\tNaN\tPIXEL_EXPRESSION\n" +
                     "",
                     labeledAllWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL_mean\tCHL_sigma\tCHL_n\tExclusionReason\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\t0.01\t3\t\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t0.02\t2\t\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t0.02\t2\tOVERLAPPING\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\t0.03\t3\t\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\t0.0\t0\t\n" +
                     "key6\t\t\tNaN\t13.9\t\tNaN\t0.0\t0\tPIXEL_EXPRESSION\n" +
                     "",
                     labeledAggWriter.toString());
    }

    @Test
    public void testWithAggregatedNumbersWithDataArrays() throws Exception {

        writer.processHeaderRecord(createDataHeader("*CHL"), new Object[]{"ExclusionReason"});
        writer.processDataRecord("key1", createDataRecord1(new AggregatedNumber(3, 3, 0, 0.0, 1.0, 0.7, 0.01,
                                                                           new float[]{0.69F, 0.7F, 0.71F})), new Object[]{""});
        writer.processDataRecord("key2", createDataRecord2(new AggregatedNumber(2, 3, 1, 0.0, 1.0, 0.3, 0.01,
                                                                           new float[]{0.29F, 0.3F, 0.31F})), new Object[]{""});
        writer.processDataRecord("key3", createDataRecord2(new AggregatedNumber(5, 5, 5, 24.0, 42.0, 0.42, 0.042,
                                                                           new float[]{42.0F, 0.42F, 0.042F})),
                                 new Object[]{OverlappingRecordTransform.EXCLUSION_REASON_OVERLAPPING});
        writer.processDataRecord("key4", createDataRecord3(new AggregatedNumber(3, 3, 0, 0.0, 1.0, 1.4, 0.01,
                                                                           new float[]{1.39F, 1.4F, 1.41F})), new Object[]{""});
        writer.processDataRecord("key5", createDataRecord4(new AggregatedNumber(0, 0, 0, 0.0, 0.0, Double.NaN, 0.0,
                                                                           new float[]{Float.NaN, Float.NaN, Float.NaN})), new Object[]{""});
        writer.finalizeRecordProcessing();

        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.69\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.71\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.29\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.31\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.39\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.41\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\n" +
                     "",
                     recordsAllWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL_mean\tCHL_sigma\tCHL_n\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\t0.01\t3\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t0.01\t2\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\t0.01\t3\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\t0.0\t0\n" +
                     "",
                     recordsAggWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL\tExclusionReason\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.69\t\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\t\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.71\t\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.29\t\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.31\t\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t42.0\tOVERLAPPING\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.42\tOVERLAPPING\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.042\tOVERLAPPING\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.39\t\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\t\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.41\t\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\t\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\t\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\t\n" +
                     "",
                     labeledAllWriter.toString());
        assertEquals("" +
                     "CALVALUS_ID\tID\tSITE\tLAT\tLON\tTIME\tCHL_mean\tCHL_sigma\tCHL_n\tExclusionReason\n" +
                     "key1\t536\tA\t53.1\t13.9\t2011-09-01 07:07:53\t0.7\t0.01\t3\t\n" +
                     "key2\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.3\t0.01\t2\t\n" +
                     "key3\t538\tB\t53.1\t13.9\t2011-09-01 07:07:54\t0.42\t0.042\t5\tOVERLAPPING\n" +
                     "key4\t539\tC\t53.1\t13.9\t2011-09-01 07:07:55\t1.4\t0.01\t3\t\n" +
                     "key5\t\t\tNaN\t13.9\t\tNaN\t0.0\t0\t\n" +
                     "",
                     labeledAggWriter.toString());
    }

    private static Object[] createDataHeader(String chl) {
        return new Object[]{"ID", "SITE", "LAT", "LON", "TIME", chl};
    }

    private static Object[] createDataRecord1(Object chl) {
        return new Object[]{536, "A", 53.1F, 13.9F, new Date(1314860873000L), chl};
    }

    private static Object[] createDataRecord2(Object number) {
        return new Object[]{538, "B", 53.1F, 13.9F, new Date(1314860874000L), number};
    }

    private static Object[] createDataRecord3(Object chl) {
        return new Object[]{539, "C", 53.1F, 13.9F, new Date(1314860875000L), chl};
    }

    private static Object[] createDataRecord4(Object chl) {
        return new Object[]{null, "", Float.NaN, 13.9F, null, chl};
    }
}
