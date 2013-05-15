import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import core.Function;
import core.Translator;
import core.Variable;

import parsers.*;
import parsers.configuration.ConfigurationFile;
import parsers.module.ModuleFile;
import parsers.module.ParseException;
import parsers.module.Parser;

public class Main {
	public static void main(String args []) {
		
		new Translator(args).make();
		
	}
}
