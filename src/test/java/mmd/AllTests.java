package mmd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestMMD.class,
	TestModuleBaseClass.class,
	TestClone.class,
	TestConstants.class,
	TestModule.class})

public class AllTests {

}
