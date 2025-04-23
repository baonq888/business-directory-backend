package com.where.e2e_tests;

import com.where.e2e_tests.tests.*;
import com.where.e2e_tests.utils.DatabaseCleaner;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        DatabaseCleaner.class,
        AuthTest.class,
        CategoryTest.class,
        LocationTest.class,
        BusinessTest.class
})
public class E2eTestSuite {

}
