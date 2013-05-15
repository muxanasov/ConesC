// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

public class Translator {
	
	private FileManager _fManager = null;
	
	public Translator(String[] args) {
		// args should contain path to Makefile and parameters for compiler
		
		// creating a FileManager
		_fManager = new FileManager();
	}

	public void make() {
		
	}

}
