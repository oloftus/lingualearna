package com.lingualearna.web.testutil;

import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;

public class UnitTestBase {

	@Before
	public void setup() throws Exception {

		initMocks(this);
	}
}
