package com.ning.phatamorgana.models;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * A file containing source code. 
 */
public class SourceFile {

	/** The file being represented. */
	private File file;
	
	/** The contents of the file. */
	private String contents;

	/**
	 * Creates a SourceFile.
	 * @param file the file being represented
	 */
	public SourceFile(File file) {
		this(file, null);
	}
	
	/**
	 * Creates a SourceFile.
	 * @param file the file being represented
	 * @param contents the contents of the file, or null to load it later
	 */
	public SourceFile(File file, String contents) {
		if (!file.isFile()) {
			throw new RuntimeException("Not a file: " + file.getAbsolutePath());
		}
		this.file = file;
		this.contents = contents;
	}
	
	/**
	 * Returns the contents of the file.
	 * @return the source code inside the file
	 * @throws IOException 
	 */
	public String getContents() {
		if (contents != null) {
			return contents;
		}
		// TODO: Allow the user to choose the encoding. [Jon Aquino 2010-10-19]
		try {
			contents = FileUtils.readFileToString(file, "UTF-8");
			return contents;
		} catch (IOException e) {
			try {
				contents = FileUtils.readFileToString(file);
				return contents;
			} catch (IOException e2) {
				throw new RuntimeException(e2);
			}
		}
	}
	
}
