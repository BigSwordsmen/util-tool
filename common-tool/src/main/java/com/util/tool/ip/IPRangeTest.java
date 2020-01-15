package com.util.tool.ip;

import com.util.exception.InvalidIPRangeException;
import org.junit.Assert;
import org.junit.Test;

import javax.management.RuntimeErrorException;

public class IPRangeTest {
	@Test
	public void testIPRange() {
		String ip = "10.237.10.99";
		String rangeStr = "10.237.10.33/24";
		IPRange range = new IPRange(rangeStr);
		System.out.println(range);
		Assert.assertTrue(24 == range.getExtendedNetworkPrefix());
		Assert.assertTrue(range.isIPAddressInRange(new IPAddress(ip)));
		
		String rangeStr1 = "10.237.10.33/28";
		IPRange subRange = new IPRange(rangeStr1);
		Assert.assertTrue(range.isIPAddressInRange(new IPAddress(ip)));
		Assert.assertTrue(range.isRangeContainOther(subRange));
		Assert.assertTrue(subRange.compareTo(range) < 0);
	}
	
	@Test
	public void testParseRange() {
		try {
			new IPRange("");
		} catch(Exception e) {
			Assert.assertTrue(e instanceof InvalidIPRangeException);
		}
		try {
			new IPRange("111222333444");
		} catch(Exception e) {
			Assert.assertTrue(e instanceof InvalidIPRangeException);
		}
		try {
			new IPRange("10.22.33/24");
		} catch(Exception e) {
			Assert.assertTrue(e instanceof InvalidIPRangeException);
		}
		try {
			new IPRange("10.22.33.5/aa");
		} catch(Exception e) {
			Assert.assertTrue(e instanceof InvalidIPRangeException);
		}
		try {
			new IPRange("10.22.33.5/-4");
		} catch(Exception e) {
			Assert.assertTrue(e instanceof InvalidIPRangeException);
		}
		try {
			new IPRange("10.22.33.5/124");
		} catch(Exception e) {
			Assert.assertTrue(e instanceof InvalidIPRangeException);
		}
		try {
			new IPRange("2001:0db8:02de:0000:0000:0000:0000:0e13/324");
		} catch(Exception e) {
			Assert.assertTrue(e instanceof InvalidIPRangeException);
		}
	}
	
	@Test
	public void testEquals() {
		IPRange ir = new IPRange("10.22.33.5/12");
		IPRange ir1 = new IPRange("10.22.33.5/12");
		Assert.assertTrue(ir.hashCode() == ir1.hashCode());
		Assert.assertTrue(ir.equals(ir));
		Assert.assertTrue(!ir.equals(null));
		Assert.assertTrue(!ir.equals("othre class object"));
		Assert.assertTrue(!ir.equals(new IPRange("10.22.33.5/22")));
		ir = new IPRange(null, 20);
		ir1 = new IPRange(new IPAddress("10.22.33.44"), 20);
		Assert.assertTrue(!ir.equals(ir1));
		ir = new IPRange(new IPAddress("10.22.33.45"), 20);
		Assert.assertTrue(!ir.equals(ir1));
		ir = new IPRange(new IPAddress("10.22.33.44"), 20);
		Assert.assertTrue(ir.equals(ir1));
	}
	
	@Test
	public void testCompare() {
		IPRange ir = new IPRange("10.22.33.5/12");
		try {
			ir.compareTo(null);
		} catch(Exception e) {
			Assert.assertTrue(e instanceof RuntimeErrorException);
		}
		IPRange ir1 = new IPRange("10.22.33.5/22");
		Assert.assertTrue(ir1.compareTo(ir) < 0);
	}
}
