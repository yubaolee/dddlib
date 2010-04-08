package com.dayatang.script;


import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.script.ScriptService;
import com.dayatang.script.ScriptType;

import static org.junit.Assert.*;

public class ScriptServiceTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void displayEngines() {
		ScriptEngineManager manager = new ScriptEngineManager();
		for (ScriptEngineFactory factory : manager.getEngineFactories()) {
			System.out.println("Engine name: " + factory.getEngineName());
			System.out.println("Engine version: " + factory.getEngineVersion());
			System.out.println("Language name: " + factory.getLanguageName());
			System.out.println("Language version: " + factory.getLanguageVersion());
			
			System.out.println("\n\rLanguage Extensions: ");
			for (String extension : factory.getExtensions()) {
				System.out.println(extension);
			}
			
			System.out.println("\n\rMIME types: ");
			for (String mimeType : factory.getMimeTypes()) {
				System.out.println(mimeType);
			}
			
			System.out.println("\n\rFactory names: ");
			for (String name : factory.getNames()) {
				System.out.println(name);
			}
			System.out.println("");
			System.out.println("");
		}
	}
	
	@Test
	public void testConstructorWithoutArgs() {
		ScriptService service = new ScriptService();
		String engineName = service.getEngine().getFactory().getEngineName();
		assertEquals("Groovy Scripting Engine", engineName);
	}
	
	@Test
	public void testConstructorWithArgs() {
		ScriptService service = new ScriptService(ScriptType.GROOVY);
		String engineName = service.getEngine().getFactory().getEngineName();
		assertEquals("Groovy Scripting Engine", engineName);

		service = new ScriptService(ScriptType.JS);
		engineName = service.getEngine().getFactory().getEngineName();
		assertEquals("Mozilla Rhino", engineName);
	}

	
	@Test
	public void testEvalString() throws ScriptException {
		ScriptEngine engine = new ScriptService().getEngine();
		String script = "System.out.println(\"Hello!!!!\";)";
		engine.eval(script);
	}
	
	
	@Test
	public void testEvalReader() throws ScriptException, FileNotFoundException {
		ScriptEngine engine = new ScriptService().getEngine();
		engine.eval(new FileReader(getClass().getResource("/test.groovy").getFile()));
	}
	
}
