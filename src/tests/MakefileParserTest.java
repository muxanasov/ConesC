// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import parsers.makefile.MakeFile;
import parsers.makefile.ParseException;
import parsers.makefile.Parser;

public class MakefileParserTest {
	
	String TEST_COMPONENT_NAME = "TestApp";

	@Test
	public void testParse() {
		String makeFile = 
		"COMPONENT = " + TEST_COMPONENT_NAME + "\n" +
		"PFLAGS += -I ./interfaces -I ./\n" +
		"include $(MAKERULES)\n";
		
		Parser parser = new Parser(new StringReader(makeFile));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MakeFile pfile = parser.getParsedFile();
		
		assertEquals(pfile.componentName, TEST_COMPONENT_NAME);
	}

}
