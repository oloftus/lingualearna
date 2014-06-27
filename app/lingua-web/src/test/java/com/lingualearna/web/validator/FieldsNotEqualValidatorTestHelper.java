package com.lingualearna.web.validator;

/**
 * Apache BeanUtils doesn't like anonymous classes, so we need a declared public
 * type for each case
 */
public class FieldsNotEqualValidatorTestHelper {

    public static class InvalidPropertiesEqual implements TypeUnderValidation {

        @Override
        public String getProperty1() {

            return VALUE_1;
        }

        @Override
        public String getProperty2() {

            return VALUE_1;
        }
    }

    public static interface TypeUnderValidation {

        String getProperty1();

        String getProperty2();
    }

    public static class ValidPropertiesBlank implements TypeUnderValidation {

        @Override
        public String getProperty1() {

            return BLANK;
        }

        @Override
        public String getProperty2() {

            return BLANK;
        }
    }

    public static class ValidPropertiesNotEqual implements TypeUnderValidation {

        @Override
        public String getProperty1() {

            return VALUE_1;
        }

        @Override
        public String getProperty2() {

            return VALUE_2;
        }
    }

    private static final String VALUE_1 = "value1";

    private static final String VALUE_2 = "value2";

    private static final String BLANK = "";

}
