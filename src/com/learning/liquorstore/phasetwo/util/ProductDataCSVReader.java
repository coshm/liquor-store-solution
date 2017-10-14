package com.learning.liquorstore.phasetwo.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public class ProductDataCSVReader {

    private static final String PRODUCT_DATA_CSV = "src/com/learning/liquorstore/phasetwo/data/product_data.csv";
    private static final String INVENTORY_DATA_CSV = "src/com/learning/liquorstore/phasetwo/data/inventory_data.csv";
    private static final int INVENTORY_VALUE_COUNT = 2;

    private static final String DELIMITER = ",";

    /**
     * Reads in data from a CSV file, parses the data, creates
     *   objects of the given dataClass, and returns them all in a List.
     * @param dataClass the class of the data being initialized.
     * @return a List of all the data objects read from the CSV.
     * @throws IOException - if file does not exist or cannot be read.
     * @throws ParseException - if an error is encountered while parsing the CSV.
     */
    public static <T> List<T> loadProductDataFromCSV(Class<T> dataClass) throws IOException, ParseException {
        List<T> dataObjects = new ArrayList<>();
        BufferedReader csvReader = null;
        int lineCount = 0;

        try {
            Constructor<T> dataClassConstructor = getDataClassConstructor(dataClass);
            Map.Entry<String, Class<?>>[] constructorParams = getConstructorParams(dataClassConstructor);
            int paramCount = constructorParams.length;

            csvReader = new BufferedReader(new FileReader(PRODUCT_DATA_CSV));

            // Check headers has the right number of values and that they
            //   match the spelling and ordering of the constructor parameters.
            String[] headers = validateCSVLine(csvReader.readLine(), lineCount++, paramCount);
            validateHeaders(headers, constructorParams);

            String line = csvReader.readLine();
            lineCount++;

            while (line != null) {
                Logger.debug("CSV Line #%s='%s'", lineCount, line);

                String[] productValues = validateCSVLine(line, lineCount, paramCount);

                // Create an array of constructor arg values parsed from CSV file.
                //   Bail if the column headers don't match the constructor param names.
                Object[] constructorArgValues = new Object[constructorParams.length];
                for (int paramIdx = 0; paramIdx < constructorParams.length; paramIdx++) {
                    constructorArgValues[paramIdx] =
                            parseProductValue(constructorParams[paramIdx].getValue(), productValues[paramIdx]);
                }

                // Instantiate a new object of the given class and add it the list.
                T dataObj = dataClassConstructor.newInstance(constructorArgValues);
                Logger.debug("DataObject='%s'", dataObj.toString());
                dataObjects.add(dataObj);

                // Read in the next line
                line = csvReader.readLine();
                lineCount++;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse Price as double for line " + lineCount);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(String.format("Failed to instantiate %s object.", dataClass.getName()));
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }

        return dataObjects;
    }

    /**
     * Reads in quantity data from a CSV file, parses the data, and adds the
     *   quantities to a Map under their respective Ids. Will sum quantities
     *   if multiple are found for the same Id.
     * @return a Map of Ids to total quantities read from the CSV.
     * @throws IOException - if file does not exist or cannot be read.
     * @throws ParseException - if any line does not have the correct number of Inventory values.
     */
    public static Map<String, Integer> loadProductQtyDataFromCSV() throws IOException, ParseException {
        Map<String, Integer> productQuantities = new HashMap<>();
        BufferedReader csvReader = null;
        int lineCount = 0;

        try {
            csvReader = new BufferedReader(new FileReader(INVENTORY_DATA_CSV));

            // First line is just column headers which
            //   we don't need here.
            csvReader.readLine();
            lineCount++;

            // Get first line to process
            String line = csvReader.readLine();
            lineCount++;

            while (line != null) {
                Logger.debug("Line #%s='%s'", lineCount, line);

                // Assume our values are ProductId and Quantity
                String[] productValues = validateCSVLine(line, lineCount, INVENTORY_VALUE_COUNT);

                // Parse the Inventory values and add quantity
                //   of the productId to the Inventory
                String productId = productValues[0];
                int quantity = Integer.valueOf(productValues[1]);

                // Combine with existing quantity if any
                if (productQuantities.containsKey(productId)) {
                    quantity += productQuantities.get(productId);
                }

                // Update totals
                productQuantities.put(productId, quantity);
                Logger.debug("Quantity of '%s' now at %s", productId, quantity);

                // Read in the next line
                line = csvReader.readLine();
                lineCount++;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse quantity as int for line " + lineCount);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }

        return productQuantities;
    }

    private static <T> Constructor<T> getDataClassConstructor(Class<T> dataClass) {
        Constructor<T>[] constructors = (Constructor<T>[]) dataClass.getConstructors();
        if (constructors != null && constructors.length == 1) {
            return constructors[0];
        }
        return null;
    }

    private static Map.Entry<String, Class<?>>[] getConstructorParams(Constructor<?> constructor) throws IOException {
        Class<?> declaringClass = constructor.getDeclaringClass();
        ClassLoader declaringClassLoader = declaringClass.getClassLoader();

        Type declaringType = Type.getType(declaringClass);
        String constructorDescriptor = Type.getConstructorDescriptor(constructor);
        String url = declaringType.getInternalName() + ".class";

        InputStream classFileInputStream = declaringClassLoader.getResourceAsStream(url);
        if (classFileInputStream == null) {
            throw new IllegalArgumentException("The constructor's class loader cannot find the bytecode that defined the constructor's class (URL: " + url + ")");
        }

        ClassNode classNode;
        try {
            classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classFileInputStream);
            classReader.accept(classNode, 0);
        } finally {
            classFileInputStream.close();
        }

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("<init>") && method.desc.equals(constructorDescriptor)) {
                Class<?>[] paramClasses = constructor.getParameterTypes();
                Map<String, Class<?>> paramClassesByName = new LinkedHashMap<>(paramClasses.length);
                for (int i = 0; i < paramClasses.length; i++) {
                    // The first local variable actually represents the "this" object
                    paramClassesByName.put(method.localVariables.get(i + 1).name, paramClasses[i]);
                }

                return paramClassesByName.entrySet().toArray(new Map.Entry[paramClassesByName.size()]);
            }
        }

        return null;
    }

    private static String[] validateCSVLine(String line, int lineCount, int expectedValueCount) throws ParseException {
        // Check for empty/null line
        if (line == null || line.isEmpty()) {
            throw new ParseException(String.format("Encountered empty/null line. LineNum=%d, Line=%s",
                    lineCount, line), lineCount);
        }
        String[] values = line.split(DELIMITER);

        // Check for mismatch in number of values read from CSV
        if (values.length != expectedValueCount) {
            throw new ParseException(String.format("Expected to find %d values but found %d. LineNum=%d, Line=%s",
                    expectedValueCount, values.length, lineCount, line), lineCount);
        }

        return values;
    }

    private static void validateHeaders(String[] headers, Map.Entry<String, Class<?>>[] constructorParams)
            throws ParseException {
        // Column header values should match the construction parameter names
        //   in order and spelling.
        for (int i = 0; i < constructorParams.length; i++) {
            if (!constructorParams[i].getKey().equalsIgnoreCase(headers[i])) {
                throw new ParseException(String.format("Expected Header at idx=%d to be '%s' but was '%s'.",
                        i, constructorParams[i].getKey(), headers[i]), 0);
            }
        }
    }

    private static Object parseProductValue(Class<?> paramClass, String productValue) {
        if (String.class == paramClass) {
            return productValue;
        } else if (char.class == paramClass || Character.class == paramClass) {
            return productValue.charAt(0);
        } else if (int.class == paramClass || Integer.class == paramClass) {
            return Integer.parseInt(productValue);
        } else if (long.class == paramClass || Long.class == paramClass) {
            return Long.parseLong(productValue);
        } else if (double.class == paramClass || Double.class == paramClass) {
            return Double.parseDouble(productValue);
        } else if (float.class == paramClass || Float.class == paramClass) {
            return Float.parseFloat(productValue);
        } else if (boolean.class == paramClass || Boolean.class == paramClass) {
            return Boolean.parseBoolean(productValue);
        } else if (paramClass.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>) paramClass, productValue);
        } else {
            throw new IllegalArgumentException(String.format("ProductValue must be String, char, int,"
                    + "long, double, float, or boolean. Instead was given paramClass=%s", paramClass.getName()));
        }
    }

}
