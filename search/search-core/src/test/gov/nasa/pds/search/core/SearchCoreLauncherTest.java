package gov.nasa.pds.search.core;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import gov.nasa.pds.search.core.SearchCoreLauncher;
import gov.nasa.pds.search.core.constants.TestConstants;

//JUnit imports
import junit.framework.TestCase;

/**
 * Tests SearchCoreLauncher CLI for proper behavior
 * 
 * @author jpadams
 *
 */
public class SearchCoreLauncherTest extends TestCase {
	
	@Before public void setUp() {
		File testDir = new File(System.getProperty("user.dir") + "/" + TestConstants.SERVICE_HOME_RELATIVE);
		testDir.mkdirs();
	}
	
	@After public void tearDown() {
		File testDir = new File(System.getProperty("user.dir") + "/" + TestConstants.SERVICE_HOME_RELATIVE);
		testDir.delete();
	}
	
	/**
	 * Test arguments are not empty
	 */
	@Ignore
	public void testEmptyArgs() {
		// Test empty args
        String[] args = { };
        try {
        	SearchCoreLauncher.main(args);
        	fail("Allows for no arguments.");
        } catch (Exception e) { /* Expected */ }
	}
	
	/**
	 * Test for missing required arguments
	 */
	@Ignore
	public void testRequiredArgs() {
	    String[] args = { "-H", TestConstants.SERVICE_HOME_RELATIVE, 
	    		"-e", 
	    		"-m", "5", 
	    		"-c", TestConstants.CONFIG_DIR_RELATIVE + "pds" };
	    try {
	    	SearchCoreLauncher.main(args);
	    	fail("Allows for registry not specified");
	    } catch (Exception e) { /* Expected */ }
	}
	
	/**
	 * Test using an invalid argument
	 */
	@Ignore
	public void testInvalidArg() {
        String[] args = { "-H", TestConstants.SERVICE_HOME_RELATIVE, 
        		"-e", 
        		"-m", "5", 
        		"-c", TestConstants.CONFIG_DIR_RELATIVE + "pds", 
        		"-x" };
        try {
        	SearchCoreLauncher.main(args);
        	fail("Allows invalid flag.");
        } catch (Exception e) { /* Expected */ }  
	}

    /**
     * Test Registry Extractor with absolute paths and max query = 5
     * @throws Exception 
     */
	@Ignore
    public void testExtractorAbsolute() {
    	try {
	    	String[] args = { "-r", TestConstants.PDS_REGISTRY_URL, 
	    			"-H", System.getProperty("user.dir") + "/" + TestConstants.SERVICE_HOME_RELATIVE, 
	    			"-e", 
	    			"-m", "5", 
	    			"-c", System.getProperty("user.dir") + "/" + TestConstants.CONFIG_DIR_RELATIVE + "pds", };
	    	SearchCoreLauncher.main(args);
    	} catch (Exception e) {
    		fail("Registry Extractor with Absolute Paths failed: " + e.getMessage());
    	}
    }
    
    /**
     * Test Registry Extractor with relative paths and max query = 5
     * @throws Exception 
     */
    @Ignore
    public void testExtractorRelative() {
    	try {
	    	String[] args = { "-r", TestConstants.PDS_REGISTRY_URL, 
	    			"-H", TestConstants.SERVICE_HOME_RELATIVE, 
	    			"-e", 
	    			"-m", "5", 
	    			"-c", TestConstants.CONFIG_DIR_RELATIVE + "pds", };
	    	SearchCoreLauncher.main(args);
		} catch (Exception e) {
			fail("Registry Extractor with Relative Paths failed: " + e.getMessage());
		}
    }
    
    /**
     * Test Registry Extractor with 1 properties file and max query = 5
     * @throws Exception 
     */
    @Ignore
    public void testExtractorSinglePropsPDS() {
    	try {
	    	String[] args = { "-p", TestConstants.CONFIG_DIR_RELATIVE + "pds/pds-search-service.properties",
	    			"-e", 
	    			"-m", "5"
	    			};
	    	SearchCoreLauncher.main(args);
		} catch (Exception e) {
			fail("Registry Extractor with Properties File failed: " + e.getMessage());
		}
    }
    
    /**
     * Test Registry Extractor with PSA data and max query = 5
     * @throws Exception 
     */
    public void testExtractorMultipleProps() {
    	try {
	    	String[] args = { "-p",  
	    					TestConstants.CONFIG_DIR_RELATIVE + "psa/psa-search-service.properties",
	    			//"-e", 
	    			"-m", "5"
	    			};
	    	SearchCoreLauncher.main(args);
		} catch (Exception e) {
			fail("Registry Extractor with Properties File failed: " + e.getMessage());
		}
    }
	
}
