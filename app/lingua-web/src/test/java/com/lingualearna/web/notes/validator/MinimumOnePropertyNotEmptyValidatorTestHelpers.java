package com.lingualearna.web.notes.validator;

/**
 * Apache BeanUtils doesn't like anonymous classes, so we need a declared public
 * type for each case
 */
public class MinimumOnePropertyNotEmptyValidatorTestHelpers {

    public static class AllFieldsNotNull implements TypeUnderValidation {

        @Override
        public String getProperty1() {

            return NOT_NULL;
        }

        @Override
        public String getProperty2() {

            return NOT_NULL;
        }
    }

    public static class AllFieldsNull implements TypeUnderValidation {

        @Override
        public String getProperty1() {

            return NULL;
        }

        @Override
        public String getProperty2() {

            return NULL;
        }
    }

    public static class OneFieldNotNull implements TypeUnderValidation {

        @Override
        public String getProperty1() {

            return NULL;
        }

        @Override
        public String getProperty2() {

            return NOT_NULL;
        }
    }

    public static interface TypeUnderValidation {

        String getProperty1();

        String getProperty2();
    }

    private static String NULL = null;

    private static String NOT_NULL = "notNull";
}
