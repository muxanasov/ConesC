// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

public class Coords {
	public int beginColumn;
	public int beginLine;
	public int endColumn;
	public int endLine;
	public Coords(int bl, int bc, int el, int ec){
		beginLine = bl; beginColumn = bc;
		endLine = el; endColumn = ec;
	}
	public Coords(int bl, int bc){
		beginLine = bl; beginColumn = bc;
	}
	public void setEnd(int el, int ec) {
		endLine = el; endColumn = ec;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Coords)) return false;
		Coords otherCoords = (Coords)other;
		return  (otherCoords.beginColumn == beginColumn)&&
				(otherCoords.beginLine == beginLine)&&
				(otherCoords.endColumn == endColumn)&&
				(otherCoords.endLine == endLine);
	}
}
