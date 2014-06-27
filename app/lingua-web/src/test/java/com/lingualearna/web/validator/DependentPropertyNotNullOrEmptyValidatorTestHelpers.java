package com.lingualearna.web.validator;

/**
 * Apache BeanUtils doesn't like anonymous classes, so we need a declared public
 * type for each case
 */
public abstract class DependentPropertyNotNullOrEmptyValidatorTestHelpers {

    public static class InvalidPropertyNotNullDependentBlank implements TypeUnderValidation {

        @Override
        public String getDependentProperty() {

            return BLANK;
        }

        @Override
        public String getProperty() {

            return NOT_NULL;
        }
    }

    public static class InvalidPropertyNotNullDependentNull implements TypeUnderValidation {

        @Override
        public String getDependentProperty() {

            return NULL;
        }

        @Override
        public String getProperty() {

            return NOT_NULL;
        }
    }

    public static interface TypeUnderValidation {

        String getDependentProperty();

        String getProperty();
    }

    public static class ValidPropertyBlankDependentNull implements TypeUnderValidation {

        @Override
        public String getDependentProperty() {

            return NULL;
        }

        @Override
        public String getProperty() {

            return BLANK;
        }
    }

    public static class ValidPropertyNotNullDependentNotNull implements TypeUnderValidation {

        @Override
        public String getDependentProperty() {

            return NOT_NULL;
        }

        @Override
        public String getProperty() {

            return NOT_NULL;
        }
    }

    public static class ValidPropertyNullDependentNotNull implements TypeUnderValidation {

        @Override
        public String getDependentProperty() {

            return NOT_NULL;
        }

        @Override
        public String getProperty() {

            return NULL;
        }
    }

    public static class ValidPropertyNullDependentNull implements TypeUnderValidation {

        @Override
        public String getDependentProperty() {

            return NULL;
        }

        @Override
        public String getProperty() {

            return NULL;
        }
    }

    private static final String NOT_NULL = "notNull";

    private static final String BLANK = "    ";

    private static final String NULL = null;
}
