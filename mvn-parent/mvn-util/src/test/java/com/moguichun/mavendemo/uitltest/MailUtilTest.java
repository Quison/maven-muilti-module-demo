package com.moguichun.mavendemo.uitltest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.moguichun.mavendemo.util.MailUtil;

import junit.framework.Assert;

public class MailUtilTest {

	@Test
	public void testSendMail() {
		assertEquals(MailUtil.sendMail(), "send email");
	}
}
