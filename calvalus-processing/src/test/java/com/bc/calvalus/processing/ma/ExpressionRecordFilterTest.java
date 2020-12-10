package com.bc.calvalus.processing.ma;

import org.esa.snap.core.jexp.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Norman Fomferra
 */
public class ExpressionRecordFilterTest {

    @Test
    public void testExprFilterWithAggregatedNumbers() throws Exception {
        Header header = new TestHeader("*chl");
        RecordFilter filter = ExpressionRecordFilter.create(header, "chl.mean > 2.0");

        assertEquals(true, filter.accept(RecordUtils.create(new AggregatedNumber(24, 25, 16, 0.0, 3.0, 2.6, 0.4))));
        assertEquals(false, filter.accept(RecordUtils.create(new AggregatedNumber(16, 25, 12, 0.0, 3.0, 1.7, 0.3))));
    }

    @Test
    public void testExprFilterIsCaseSensitive() throws ParseException {

        Header header = new TestHeader("CHL", "chl");
        RecordFilter filter1 = ExpressionRecordFilter.create(header, "CHL == 1.0");
        assertEquals(true, filter1.accept(RecordUtils.create(1.0, 2.0)));
        assertEquals(false, filter1.accept(RecordUtils.create(2.0, 1.0)));

        RecordFilter filter2 = ExpressionRecordFilter.create(header, "chl == 1.0");
        assertEquals(false, filter2.accept(RecordUtils.create(1.0, 2.0)));
        assertEquals(true, filter2.accept(RecordUtils.create(2.0, 1.0)));
    }

    @Test(expected = ParseException.class)
    public void testExprFilterIllegalExpr() throws ParseException {
        Header header = new TestHeader("ch");
        ExpressionRecordFilter.create(header, "chl.min > 2.0");
    }

    @Test
    public void testExprFilterWithPrimitiveTypes() throws Exception {
        Header header = new TestHeader("valid", "chl", "tsm");
        RecordFilter filter = ExpressionRecordFilter.create(header, "valid && chl < 0.5 && tsm < 0.2");

        assertEquals(false, filter.accept(RecordUtils.create(0, 0.4F, 0.1F)));
        assertEquals(true, filter.accept(RecordUtils.create(1, 0.4F, 0.1F)));
        assertEquals(false, filter.accept(RecordUtils.create(0, 0.6F, 0.1F)));
        assertEquals(false, filter.accept(RecordUtils.create(1, 0.6F, 0.1F)));
        assertEquals(false, filter.accept(RecordUtils.create(0, 0.4F, 0.3F)));
        assertEquals(false, filter.accept(RecordUtils.create(1, 0.4F, 0.3F)));
    }

}
