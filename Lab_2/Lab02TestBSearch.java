import org.junit.*;

import static edu.gvsu.mipsunit.munit.MUnit.Register.*;
import static edu.gvsu.mipsunit.munit.MUnit.*;

public class Lab02TestBSearch {
    
	// Test array of size 1
    @Test(timeout=2000)
    public void verify_size1() {
        Label array = wordData(1);
        run("bsearch", array, 1, 1);
		Assert.assertEquals("$s7 should be &(sarray[0])", array.address(), get(s7));
	}
	
	// Test array of size 1 - not found
    @Test(timeout=2000)
    public void verify_not_found_size1() {
        Label array = wordData(1);
        run("bsearch", array, 1, 0);
		Assert.assertEquals("$s7 should be 0", 0, get(s7));
	}
	
	// Test array of even size. No matching number
    @Test(timeout=2000)
    public void verify_not_found_even() {
        Label array = wordData(1, 5, 9, 20, 321, 432, 555, 854);
        run("bsearch", array, 8, 19);
		Assert.assertEquals("$s7 should be 0", 0, get(s7));
	}
	
	// Test array of odd size. No matching number
    @Test(timeout=2000)
    public void verify_not_found_odd() {
        Label array = wordData(1, 5, 9, 20, 321, 432, 555, 854, 940);
        run("bsearch", array, 9, 25);
		Assert.assertEquals("$s7 should be 0", 0, get(s7));
	}
	
	// Test array of even size
    @Test(timeout=2000)
    public void verify_even() {
        Label array = wordData(1, 5, 9, 20, 321, 432, 555, 854);
        run("bsearch", array, 8, 555);
		Assert.assertEquals("$s7 should be &(sarray[6]", array.address()+24 , get(s7));
	}
	
	// Test array of odd size
    @Test(timeout=2000)
    public void verify_odd() {
        Label array = wordData(1, 5, 9, 20, 321, 432, 555, 854, 940);
        run("bsearch", array, 9, 854);
		Assert.assertEquals("$s7 should be &(sarray[7]", array.address()+28 , get(s7));
	}
	
	// Test lower than min:
    @Test(timeout=2000)
    public void verify_not_found_min() {
        Label array = wordData(1, 5, 9, 20, 321, 432, 555, 854, 940);
        run("bsearch", array, 9, -1);
		Assert.assertEquals("$s7 should be 0", 0, get(s7));
	}
	
	// Test higher than max
    @Test(timeout=2000)
    public void verify_not_found_max() {
        Label array = wordData(1, 5, 9, 20, 321, 432, 555, 854, 940);
        run("bsearch", array, 9, 2000);
		Assert.assertEquals("$s7 should be 0", 0, get(s7));
	}
	
	// Test min
    @Test(timeout=2000)
    public void verify_min() {
        Label array = wordData(1, 5, 9, 20, 321, 432, 555, 854, 940);
        run("bsearch", array, 9, 1);
		Assert.assertEquals("$s7 should be &(sarray[0])", array.address(), get(s7));
	}
	
	// Test max
    @Test(timeout=2000)
    public void verify_max() {
        Label array = wordData(1, 5, 9, 20, 321, 432, 555, 854, 940);
        run("bsearch", array, 9, 940);
		Assert.assertEquals("$s7 should be &(sarray[8])", array.address()+32 , get(s7));
	}
}
