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

import parsers.makefile.MakeFile;
import parsers.makefile.ParseException;
import parsers.makefile.Parser;

public class FileManager {
	
	private Component _mainComponent = null;
	private MakeFile _mFile = null;
	
	public FileManager() {
		// scan directories
		Parser parser = new Parser(new StringReader(fread("Makefile")));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_mFile = parser.getParsedFile();
	}
	
	public Component getMainComponent() {
		// get main component from makefile
		if (_mainComponent == null)
			_mainComponent = new Configuration(this, _mFile.componentName);
		return _mainComponent;
	}
	
	public String findAndRead(String filename) {
		for(String[] pathDirs : _mFile.paths) {
			String path = "";
			for (int i = 0; i < pathDirs.length; i++)
				path += pathDirs[i] + File.separator;
			File file = new File(path + File.separator + filename);
			if (file.exists())
				return fread(path + File.separator + filename);
		}
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
		
		boolean inCharSequence = false;
		 
		try {
			int currentChar;
			br = new BufferedReader(new FileReader(filename));
			while ((currentChar = br.read()) != -1) {
				if ( (char)currentChar == '\"') inCharSequence = !inCharSequence;
				if ( inCharSequence && (char)currentChar == '\n') file += "\\n";
				else file += (char)currentChar;
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
