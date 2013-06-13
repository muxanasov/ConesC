// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import parsers.makefile.MakeFile;
import parsers.makefile.ParseException;
import parsers.makefile.Parser;

public class FileManager {
	
	private String _mainComponent = "";
	
	public FileManager() {
		// scan directories
		Parser parser = new Parser(new StringReader(fread("Makefile")));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MakeFile mFile = parser.getParsedFile();
		
		// get main component from makefile
		_mainComponent = mFile.componentName;
		// get included paths from makefile
	}
	
	public FileTreeElem generateTree() {
		return new FileTreeElem(this, _mainComponent , "", new ArrayList<Function>());
	}
	
	public String findAndRead(String filename) {
		// for every included path
		File file = new File(filename);
		if (file.exists())
			return fread(filename);
		return null;
	}
	
	public static void fwrite(String name, String data) {
		File f = new File(name);
		if (!f.exists())
			try {
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(name, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.print(data);
		writer.close();
	}
	
	public static String fread(String filename){
		String file = "";
		BufferedReader br = null;
		 
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filename));
			while ((sCurrentLine = br.readLine()) != null) {
				file += sCurrentLine + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return file;
	}

}
