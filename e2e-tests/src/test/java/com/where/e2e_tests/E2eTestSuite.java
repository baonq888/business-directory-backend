package com.where.e2e_tests;

import com.where.e2e_tests.tests.AuthTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AuthTest.class
})
public class E2eTestSuite {
}
