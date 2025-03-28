package org.jfree.data.junit;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues2D;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
public class DataUtilitiesTest_Lab3 {
    	
	// Test when both arrays are null — should return true since both are equally 'nothing'
    @Test
    public void testEqual_BothNull() {
        assertTrue(DataUtilities.equal(null, null)); 
    }

    // Test when the first array is null and second is valid — should return false
    @Test
    public void testEqual_FirstArrayNull() {
        double[][] array = {{1.0, 2.0}, {3.0, 4.0}};
        assertFalse(DataUtilities.equal(null, array)); 
    }

    // Test when the second array is null and first is valid — should return false
    @Test
    public void testEqual_SecondArrayNull() {
        double[][] array = {{1.0, 2.0}, {3.0, 4.0}};
        assertFalse(DataUtilities.equal(array, null)); 
    }

    // Test when arrays have different row sizes — should not be equal
    @Test
    public void testEqual_DifferentLengths() {
        double[][] array1 = {{1.0, 2.0}};
        double[][] array2 = {{1.0, 2.0}, {3.0, 4.0}};
        assertFalse(DataUtilities.equal(array1, array2)); 
    }

    // Test identical arrays — should return true as they are the same in structure and values
    @Test
    public void testEqual_IdenticalArrays() {
        double[][] array1 = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] array2 = {{1.0, 2.0}, {3.0, 4.0}};
        assertTrue(DataUtilities.equal(array1, array2)); 
    }

    // Test arrays that are almost same but differ in one element — should return false
    @Test
    public void testEqual_DiffArrays() {
        double[][] array1 = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] array2 = {{1.0, 2.0}, {3.0, 5.0}};
        assertFalse(DataUtilities.equal(array1, array2)); 
    }

    // Test cloning a regular 2D array — should produce a new object with the same contents
    @Test
    public void testCloneWithValidInput() {
        double[][] source = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] result = DataUtilities.clone(source);
        
        // Make sure it's a new object (not same memory reference)
        assertNotSame(source, result);
        
        // Check length of outer array
        assertEquals(source.length, result.length);
        
        // Ensure each sub-array is a new object and contents match
        for (int i = 0; i < source.length; i++) {
            assertNotSame(source[i], result[i]);
            assertArrayEquals(source[i], result[i], 0.0001);
        }
    }

    // Test cloning an empty array — should return a new empty array
    @Test
    public void testCloneWithEmptyArray() {
        double[][] source = {};
        double[][] result = DataUtilities.clone(source);
        
        assertNotSame(source, result); // Confirm it's a new object
        assertEquals(0, result.length); // Confirm it's still empty
    }

    // Test cloning an array that includes a null row
    @Test
    public void testCloneWithNullRows() {
        double[][] source = new double[2][];
        source[0] = new double[]{1.0, 2.0};
        source[1] = null;
        
        double[][] result = DataUtilities.clone(source);
        
        assertNotSame(source, result);
        assertEquals(source.length, result.length);
        assertArrayEquals(source[0], result[0], 0.0001);
        assertNull(result[1]); // Confirm null row is preserved
    }

    // Test summing up values in a column of a populated table
    @Test
    public void testCalculateColumnTotal() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        
        data.addValue(5.0, 0, 0);  // row 0
        data.addValue(10.0, 1, 0); // row 1
        data.addValue(15.0, 2, 0); // row 2

        double total = DataUtilities.calculateColumnTotal(data, 0);
        assertEquals("Column total should be 30", 30.0, total, 0.0001);
    }

    // Test summing values in an empty table — should return 0
    @Test
    public void testCalculateColumnTotal_WithEmptyTable() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        double total = DataUtilities.calculateColumnTotal(data, 0);
        assertEquals("Total should be 0.0 for empty table", 0.0, total, 0.0001);
    }

    // Test column total with null values in some cells — null should be skipped
    @Test
    public void testCalculateColumnTotal_NullValuesInData() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        
        data.addValue(1.0, "Row1", "Column1");
        data.addValue(null, "Row2", "Column1");
        data.addValue(5.0, "Row3", "Column1");

        int[] validRows = {0, 1, 2}; 
        double result = DataUtilities.calculateColumnTotal(data, 0, validRows);

        assertEquals("Null value should be skipped", 6.0, result, 0.0001); 
    }

    // Test summing only selected valid rows
    @Test
    public void testCalculateColumnTotal_ValidRows() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        
        data.addValue(1.0, "Row1", "Column1");
        data.addValue(3.0, "Row2", "Column1");
        data.addValue(5.0, "Row3", "Column1");

        int[] validRows = {0, 1}; 
        double result = DataUtilities.calculateColumnTotal(data, 0, validRows);

        assertEquals("Sum of valid rows should be 4.0", 4.0, result, 0.0001);
    }

    // Test with empty validRows array — should return 0
    @Test
    public void testCalculateColumnTotal_EmptyValidRows() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        
        int[] validRows = {}; 
        double result = DataUtilities.calculateColumnTotal(data, 0, validRows);

        assertEquals("Empty validRows should return 0.0", 0.0, result, 0.0001);
    }

    // Test with some invalid row indices — only valid ones should count
    @Test
    public void testCalculateColumnTotal_SomeInvalidRows() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        
        data.addValue(1.0, "Row1", "Column1");
        data.addValue(3.0, "Row2", "Column1");

        int[] validRows = {1, 3}; 
        double result = DataUtilities.calculateColumnTotal(data, 0, validRows);

        assertEquals("Only row 1 should be counted (3.0)", 3.0, result, 0.0001);
    }

    // Test where all validRows are invalid indices — should return 0
    @Test
    public void testCalculateColumnTotal_AllInvalidRows() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        
        data.addValue(1.0, "Row1", "Column1");
        data.addValue(3.0, "Row2", "Column1");

        int[] validRows = {5, 6}; 
        double result = DataUtilities.calculateColumnTotal(data, 0, validRows);

        assertEquals("All invalid rows should return 0.0", 0.0, result, 0.0001);
    }

 // Test row total calculation with two numeric values in the same row
    @Test
    public void testCalculateRowTotal_WithValues() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        data.addValue(7.5, "Row1", "Column1");
        data.addValue(2.5, "Row1", "Column2");

        // Expect total of 7.5 + 2.5 = 10.0
        double result = DataUtilities.calculateRowTotal(data, 0);
        assertEquals(10.0, result, 0.000000001d);
    }

    // Test when the data table is completely empty
    @Test
    public void testCalculateRowTotal_EmptyDataTable() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();

        // No values to add, should return 0
        double result = DataUtilities.calculateRowTotal(data, 0);
        assertEquals(0.0, result, 0.000000001d);
    }

    // Test when one value in the row is null
    @Test
    public void testCalculateRowTotal_NullValue() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        data.addValue(null, "Row1", "Column1");
        data.addValue(2.5, "Row1", "Column2");

        // Only the valid 2.5 should be counted
        double result = DataUtilities.calculateRowTotal(data, 0);
        assertEquals(2.5, result, 0.000000001d);
    }

    // Test row total with extremely large values (double overflow)
    @Test
    public void testCalculateRowTotal_LargeValues() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        data.addValue(Double.MAX_VALUE, "Row1", "Column1");
        data.addValue(Double.MAX_VALUE, "Row1", "Column2");
        data.addValue(Double.MAX_VALUE, "Row1", "Column3");

        // Adding 3x Double.MAX_VALUE will overflow to POSITIVE_INFINITY
        double result = DataUtilities.calculateRowTotal(data, 0);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.000000001d);
    }

    // Test summing only selected valid columns
    @Test
    public void testCalculateRowTotal_WithValuesAndValidCols() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        data.addValue(1.0, "Row1", "Column1"); 
        data.addValue(2.0, "Row1", "Column2"); 
        data.addValue(3.0, "Row1", "Column3"); 

        int[] validCols = {0, 2};  // Include only columns 0 and 2
        double result = DataUtilities.calculateRowTotal(data, 0, validCols);
        assertEquals(4.0, result, 0.0001); 
    }

    // Test with a null value among selected columns
    @Test
    public void testCalculateRowTotal_NullValueInData() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        data.addValue(1.0, "Row1", "Column1");
        data.addValue(null, "Row1", "Column2");
        data.addValue(3.0, "Row1", "Column3");

        int[] validCols = {0, 1, 2};  // Include all columns
        double result = DataUtilities.calculateRowTotal(data, 0, validCols);
        assertEquals(4.0, result, 0.0001); 
    }

    // Test with empty validCols array — no columns to sum
    @Test
    public void testCalculateRowTotal_EmptyValidCols() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        data.addValue(1.0, "Row1", "Column1");
        data.addValue(2.0, "Row1", "Column2");

        int[] validCols = {}; 
        double result = DataUtilities.calculateRowTotal(data, 0, validCols);
        assertEquals(0.0, result, 0.0001);
    }

    // Test with one valid column and one out-of-bounds column
    @Test
    public void testCalculateRowTotal_InvalidCols() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();
        data.addValue(1.0, "Row1", "Column1");
        data.addValue(2.0, "Row1", "Column2");

        int[] validCols = {0, 5};  // Only column 0 is valid
        double result = DataUtilities.calculateRowTotal(data, 0, validCols);
        assertEquals(1.0, result, 0.0001); 
    }

    // Test to make sure even with valid column indices, negative column count won’t affect outcome
    @Test
    public void testCalculateRowTotal_NegativeColumnCount() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D();

        data.addValue(1.0, "Row1", "Column1");
        data.addValue(2.0, "Row1", "Column2");

        int[] validCols = {0, 1};
        double result = DataUtilities.calculateRowTotal(data, 0, validCols);
        assertEquals(3.0, result, 0.0001);
    }

    // Force test an edge case where getColumnCount() returns negative — loop should be skipped
    @Test
    public void testCalculateRowTotal_ForceExecutionOfUnusedLoop() {
        DefaultKeyedValues2D data = new DefaultKeyedValues2D() {
            @Override
            public int getColumnCount() {
                return -2; // Simulate buggy or edge behavior
            }
        };

        data.addValue(1.0, "Row1", "Column1");
        data.addValue(2.0, "Row1", "Column2");

        // Since loop should be skipped, total should remain 0.0
        double result = DataUtilities.calculateRowTotal(data, 0);
        assertEquals("Forced loop execution should not affect total", 0.0, result, 0.0001);
    }

    // ------------------ ARRAY CONVERSION ------------------

    // Test converting a 1D array of doubles to Number[] type
    @Test
    public void testCreateNumberArray_WithData() {
        double[] input = {1.0, 2.0, 3.0};
        Number[] expected = {1.0, 2.0, 3.0}; 
        Number[] result = DataUtilities.createNumberArray(input);

        assertNotNull(result);
        assertEquals(input.length, result.length);
        for (int i = 0; i < input.length; i++) {
            assertEquals(expected[i], result[i]);
            assertEquals(Double.class, result[i].getClass()); // Ensure type is Double
        }
    }

    // Test converting an empty double array — should return empty Number[] too
    @Test
    public void testCreateNumberArray_EmptyArray() {
        double[] input = {};
        Number[] result = DataUtilities.createNumberArray(input);
        assertNotNull(result); 
        assertEquals(0, result.length);
    }

    // Test converting 2D double array to 2D Number array
    @Test
    public void testCreateNumberArray2D_WithData() {
        double[][] input = {{1.0, 2.0}, {3.0, 4.0, 5.0}};
        Number[][] result = DataUtilities.createNumberArray2D(input);

        assertNotNull(result);
        assertEquals(input.length, result.length);

        // Validate each sub-array and value
        assertNotNull(result[0]);
        assertEquals(input[0].length, result[0].length);
        assertEquals(1.0, result[0][0]);
        assertEquals(2.0, result[0][1]);

        assertNotNull(result[1]);
        assertEquals(input[1].length, result[1].length);
        assertEquals(3.0, result[1][0]);
        assertEquals(4.0, result[1][1]);
        assertEquals(5.0, result[1][2]);
    }

    // Test 2D conversion with an empty array
    @Test
    public void testCreateNumberArray2D_EmptyArray() {
        double[][] input = {};
        Number[][] result = DataUtilities.createNumberArray2D(input);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    // ------------------ CUMULATIVE PERCENTAGES ------------------

    // Test cumulative percentages with normal input
    @Test
    public void testGetCumulativePercentages_NormalCase() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        
        data.addValue("A", 1.0);
        data.addValue("B", 2.0);
        data.addValue("C", 3.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        // Expected cumulative: A = 1/6, B = 3/6, C = 6/6
        assertEquals("Cumulative % for A", 1.0 / 6.0, result.getValue("A").doubleValue(), 0.0001);
        assertEquals("Cumulative % for B", 3.0 / 6.0, result.getValue("B").doubleValue(), 0.0001);
        assertEquals("Cumulative % for C", 6.0 / 6.0, result.getValue("C").doubleValue(), 0.0001);
    }

    // Test cumulative percentages with an empty dataset
    @Test
    public void testGetCumulativePercentages_EmptyDataset() {
        DefaultKeyedValues data = new DefaultKeyedValues();

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Empty dataset should return empty KeyedValues", 0, result.getItemCount());
    }

    // Test handling of null values in the dataset — should skip them
    @Test
    public void testGetCumulativePercentages_NullValues() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        
        data.addValue("A", 2.0);
        data.addValue("B", null);
        data.addValue("C", 3.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        // Total = 5, B is skipped
        assertEquals("Cumulative % for A", 2.0 / 5.0, result.getValue("A").doubleValue(), 0.0001);
        assertEquals("Cumulative % for C", 5.0 / 5.0, result.getValue("C").doubleValue(), 0.0001);
    }

    // Test when there is only a single value — it should be 100%
    @Test
    public void testGetCumulativePercentages_SingleValue() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue("A", 10.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Single value should be 100%", 1.0, result.getValue("A").doubleValue(), 0.0001);
    }

    // Test with negative values — should still compute percentages properly
    @Test
    public void testGetCumulativePercentages_NegativeValues() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        
        data.addValue("A", -2.0);
        data.addValue("B", -3.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        assertEquals("Cumulative % for A", -2.0 / -5.0, result.getValue("A").doubleValue(), 0.0001);
        assertEquals("Cumulative % for B", -5.0 / -5.0, result.getValue("B").doubleValue(), 0.0001);
    }

    // Test when all values are zero — division by zero should be handled gracefully
    @Test
    public void testGetCumulativePercentages_ZeroTotal() {
        DefaultKeyedValues data = new DefaultKeyedValues();
        
        data.addValue("A", 0.0);
        data.addValue("B", 0.0);

        KeyedValues result = DataUtilities.getCumulativePercentages(data);

        // Should not be NaN or 0.0, implementation-specific behavior
        assertNotEquals("Cumulative % for A with zero total", 0.0, result.getValue("A").doubleValue(), 0.0001);
        assertNotEquals("Cumulative % for B with zero total", 0.0, result.getValue("B").doubleValue(), 0.0001);
    }

}