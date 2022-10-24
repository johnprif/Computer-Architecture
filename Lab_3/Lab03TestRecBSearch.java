import org.junit.*;

import static edu.gvsu.mipsunit.munit.MUnit.Register.*;
import static edu.gvsu.mipsunit.munit.MUnit.*;

public class Lab03TestRecBSearch {

int initStackPtr;  // Keep sp before the call
Label str5, str1, str2, str3, str4, str6, str7, str8;
        // randomize registers to ensure the code does not depend on simulator initialised values or 

@Before
    public void randomizeAndSetRegs() {
		// Hack here because Label.address() is only available after run()!
        str5 = asciiData("gorilla");  // Munit stores this @ 0x10010000
        str1 = asciiData("aadvark");  // @ 0x10010008
        str2 = asciiData("ant");      // @ 0x10010010
        str3 = asciiData("elephant"); // @ 0x10010014
        str4 = asciiData("hippo");    // @ 0x1001001d
        str6 = asciiData("moose");    // @ 0x10010023
        str7 = asciiData("panther");  // @ 0x10010029
        str8 = asciiData("whale");    // @ 0x10010031
        // randomize registers to ensure the code does not depend on simulator initialised values or 
        //  illegal parameter passing
        randomizeRegisters(v0, v1, t0, t1, t2, t3, t4, t5, t6, t7, t8, t9, k0, k1, a0, a1, a2, a3, ra);
        // Set the saved registers to veryify they are preserved after the function call
        set(s0, 0x123);
        set(s1, 0x124);
        set(s2, 0x125);
        set(s3, 0x126);
        set(s4, 0x127);
        set(s5, 0x128);
        set(s6, 0x129);
        set(s7, 0x12a);
		initStackPtr = get(sp);
    }

@After
    public void checkSavedRegs() {
		// check saved registers
        Assert.assertEquals("$s0 should be preserved across subroutine calls", 0x123, get(s0));
        Assert.assertEquals("$s1 should be preserved across subroutine calls", 0x124, get(s1));
        Assert.assertEquals("$s2 should be preserved across subroutine calls", 0x125, get(s2));
        Assert.assertEquals("$s3 should be preserved across subroutine calls", 0x126, get(s3));
        Assert.assertEquals("$s4 should be preserved across subroutine calls", 0x127, get(s4));
        Assert.assertEquals("$s5 should be preserved across subroutine calls", 0x128, get(s5));
        Assert.assertEquals("$s6 should be preserved across subroutine calls", 0x129, get(s6));
        Assert.assertEquals("$s7 should be preserved across subroutine calls", 0x12a, get(s7));
		Assert.assertEquals("$sp should be equal to its value before the call", initStackPtr, get(sp));
    }
    

    @Test(timeout=2000)
    public void verify_strcmp_empty() {
        Label str1 = asciiData("");
        Label str2 = asciiData("");

        run("strcmp", str1, str2);

        Assert.assertEquals("Empty strings \'\\0\' are equal", 0, get(v0));
	}

    @Test(timeout=2000)
    public void verify_strcmp_empty_1ch() {
        Label str1 = asciiData("");
        Label str2 = asciiData("a");

        run("strcmp", str1, str2);
        Assert.assertEquals("Empty string \'\\0\' is shorter than single char string (v0 should be negative)", true, get(v0) < 0);
	}

    @Test(timeout=2000)
    public void verify_strcmp_shorter() {
        Label str1 = asciiData("antelope");
        Label str2 = asciiData("ant");

        run("strcmp", str1, str2);

        Assert.assertEquals("antelope comes after ant (v0 should be positive)", true, get(v0) > 0);
	}


    
	// Test array of size 1
    @Test(timeout=2000)
    public void verify_size1() {

        Label array = wordData(0x10010008); // str1.address());
		
        run("rec_b_search", array, array, str1);

        Assert.assertEquals("Should match aadvark", array.address(), get(v0));
	}
	
	// Test array of size 1 - not found
    @Test(timeout=2000)
    public void verify_not_found_size1() {

        Label array = wordData(0x10010008); // str1.address());
		
        run("rec_b_search", array, array, str2);

        Assert.assertEquals("Should not match any string", 0, get(v0));
	}
	
	// Test array of even size. No match
    @Test(timeout=2000)
    public void verify_not_found_even() {
        Label array = wordData(0x10010008, 0x10010010, 0x10010014, 0x1001001d,
                               0x10010000, 0x10010023, 0x10010029, 0x10010031);
        Label str = asciiData("antelope");  

        run("rec_b_search", array, 0x10010038+7*4, str);
		//System.out.println(array.address());
		Assert.assertEquals("chimanzee is not in the list", 0, get(v0));
	}
	
	// Test array of odd size. No match
    @Test(timeout=2000)
    public void verify_not_found_odd() {
        Label array = wordData(0x10010008, 0x10010010, 0x10010014, 0x1001001d,
                               0x10010000, 0x10010023, 0x10010029);

        run("rec_b_search", array, 0x10010038+6*4, str8);
		Assert.assertEquals("whale is not in the list", 0, get(v0));
	}
	
	// Test array of even size
    @Test(timeout=2000)
    public void verify_even() {
        Label array = wordData(0x10010008, 0x10010010, 0x10010014, 0x1001001d,
                               0x10010000, 0x10010023, 0x10010029, 0x10010031);
        Label str = asciiData("ant");  

        run("rec_b_search", array, 0x10010038+7*4, str);
		Assert.assertEquals("ant is 2nd in the list", array.address()+4, get(v0));
	}
	
	// Test array of odd size
    @Test(timeout=2000)
    public void verify_odd() {
        Label array = wordData(0x10010008, 0x10010010, 0x10010014, 0x1001001d,
                               0x10010000, 0x10010023, 0x10010029);
        Label str = asciiData("moose");  

        run("rec_b_search", array, 0x10010038+6*4, str);
		Assert.assertEquals("moose is 6th in the list", array.address()+5*4, get(v0));
	}
	
	// Test lower than min:
    @Test(timeout=2000)
    public void verify_not_found_min() {
        Label array = wordData(0x10010008, 0x10010010, 0x10010014, 0x1001001d,
                               0x10010000, 0x10010023, 0x10010029, 0x10010031);
        Label str = asciiData("aaarmadilo");  

        run("rec_b_search", array, 0x10010038+7*4, str);
		Assert.assertEquals("aaarmadilo is not in the list", 0, get(v0));
	}
	
	// Test higher than max
    @Test(timeout=2000)
    public void verify_not_found_max() {
        Label array = wordData(0x10010008, 0x10010010, 0x10010014, 0x1001001d,
                               0x10010000, 0x10010023, 0x10010029, 0x10010031);
        Label str = asciiData("what?");  

        run("rec_b_search", array, 0x10010038+7*4, str);
		Assert.assertEquals("what? is not in the list", 0, get(v0));
	}
	
	// Test min
    @Test(timeout=2000)
    public void verify_min() {
        Label array = wordData(0x10010008, 0x10010010, 0x10010014, 0x1001001d,
                               0x10010000, 0x10010023, 0x10010029, 0x10010031);
        Label str = asciiData("aadvark");  

        run("rec_b_search", array, 0x10010038+7*4, str);
		Assert.assertEquals("aadvark is 1st in the list", array.address(), get(v0));
	}
	
	// Test max
    @Test(timeout=2000)
    public void verify_max() {
        Label array = wordData(0x10010008, 0x10010010, 0x10010014, 0x1001001d,
                               0x10010000, 0x10010023, 0x10010029, 0x10010031);
        Label str = asciiData("whale");  

        run("rec_b_search", array, 0x10010038+7*4, str);
		Assert.assertEquals("whale is 8th in the list", array.address()+7*4, get(v0));
	}
}
