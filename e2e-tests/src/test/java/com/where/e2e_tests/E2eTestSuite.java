package com.where.e2e_tests;

import com.where.e2e_tests.tests.AuthTest;
import com.where.e2e_tests.tests.FullSystemE2ETest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        FullSystemE2ETest.class,
        AuthTest.class
})
public class E2eTestSuite {
}
