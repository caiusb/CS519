package edu.oregonstate.cs519.touchdevelop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ScriptTest {

	private Map<String, Object> hashMap;

	@Before
	public void setUp() {
		hashMap = new HashMap<String, Object>();
	}

	@Test
	public void testCreateScriptWithName() {
		hashMap.put(Script.NAME, "TestScript");
		Script script = new Script(hashMap);
		assertEquals("TestScript", script.getName());
	}

	@Test
	public void testCreateScriptWithId() {
		hashMap.put(Script.ID, "aaaa");
		Script script = new Script(hashMap);
		assertEquals("aaaa", script.getID());
	}

	@Test
	public void testCreateScriptWithUserId() {
		hashMap.put(Script.USER_ID, "bob");
		Script script = new Script(hashMap);
		assertEquals("bob", script.getUserID());
	}

	@Test
	public void testCreateScriptWithRootId() {
		hashMap.put(Script.ROOT_ID, "bbbb");
		Script script = new Script(hashMap);
		assertEquals(script.getRootID(), "bbbb");
	}

	@Test
	public void testGetSuccessors() {
		hashMap.put(Script.ID, "qoipfdvx");
		Script script = new Script(hashMap);
		List<Script> successors = script.getSuccessors();
		assertEquals(1, successors.size());
		assertEquals("qfbr", successors.get(0).getID());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetSuccessorsInMap() {
		hashMap.put(Script.ID, "qoipfdvx");
		Script script = new Script(hashMap);
		script.getSuccessors();
		Map<String, Object> scriptMap = script.getHashMap();
		List<String> successors = (List<String>) scriptMap.get(Script.SUCCESSORS);
		assertEquals(1, successors.size());
		assertEquals("qfbr", successors.get(0));
	}

	@Test
	public void testGetSuccessorsFromMap() {
		// base script
		hashMap.put(Script.ID, "qoipfdvx");
		List<String> scriptList = Collections.singletonList("aaaaa");
		hashMap.put(Script.SUCCESSORS, scriptList);
		Script script = new Script(hashMap);

		// successor script
		HashMap<String, Object> successorScriptMap = new HashMap<String, Object>();
		successorScriptMap.put(Script.ID, "aaaaa");
		MemoryLibrary.getInstance().addScript(new Script(successorScriptMap));

		// testing the thing
		List<Script> successors = script.getSuccessors();
		assertEquals(1, successors.size());
		assertEquals("aaaaa", successors.get(0).getID());
	}

	private void assertTreeDepth(int expectedDepth, String scriptId) {
		hashMap.put(Script.ID, scriptId);
		Script script = new Script(hashMap);

		assertEquals(expectedDepth, script.successorTreeDepth());
	}

	@Test
	public void testTreeDepthOne() throws Exception {
		assertTreeDepth(1, "qvutzxps");
	}

	@Test
	public void testTreeDepthTwo() throws Exception {
		assertTreeDepth(2, "vftdahsd");
	}

	@Test
	public void testTreeDepthThree() throws Exception {
		assertTreeDepth(3, "tkjtgiyt");
	}

	@Test
	public void testTreeDepthFour() throws Exception {
		assertTreeDepth(4, "dkagdijf");
	}

	private void assertTreeWidth(int expectedWidth, String scriptId) {
		hashMap.put(Script.ID, scriptId);
		Script script = new Script(hashMap);

		assertEquals(expectedWidth, script.successorTreeWidth());
	}
	
	private void assertTransitiveSuccessors(Set<String> expectedTransitiveSuccessors, String scriptId) {
		hashMap.put(Script.ID, scriptId);
		Script script = new Script(hashMap);

		Set<Script> actualTransitiveSuccessors = script.getTransitiveSuccessors();

		assertEquals(expectedTransitiveSuccessors.size(), actualTransitiveSuccessors.size());
		
		for (Script succScript : actualTransitiveSuccessors) {
			assertTrue(expectedTransitiveSuccessors.contains(succScript.getID()));
		}
	}

	@Test
	public void testWidthOne() throws Exception {
		assertTreeWidth(1, "nxeqstmo");
	}

	@Test
	public void testWidthTwo() throws Exception {
		assertTreeWidth(2, "rzirrqjl");
	}

	@Test
	public void testWidthThree() throws Exception {
		assertTreeWidth(3, "yrwtmtde");
	}

	@Test
	public void testWidthThreeForDepthTwo() throws Exception {
		assertTreeWidth(3, "rmxlwvli");
	}
	
	@Test
	public void testGetTransitiveSuccessorsZero() throws Exception {
		assertTransitiveSuccessors(new HashSet<String>(), "pwrjydht");
	}
	
	@Test
	public void testGetTransitiveSuccessorsOne() throws Exception {
		Set<String> expectedTrans = new HashSet<>();
		expectedTrans.add("xplgtpnn");
		assertTransitiveSuccessors(expectedTrans, "clrfwzok");
	}
	
	@Test
	public void testGetTransitiveSuccessorsTwo() throws Exception {
		Set<String> expectedTrans = new HashSet<>();
		expectedTrans.add("nxeqstmo");
		expectedTrans.add("ykoqzvfk");
		assertTransitiveSuccessors(expectedTrans, "rzirrqjl");
	}
	
	@Test
	public void testGetTransitiveSuccessorsMany() throws Exception {
		Set<String> expectedTrans = new HashSet<>();
		expectedTrans.add("rkxtbcaf");
		expectedTrans.add("rmxlwvli");
		expectedTrans.add("znzbinys");
		expectedTrans.add("akegupsx");
		expectedTrans.add("xplgtpnn");
		expectedTrans.add("clrfwzok");
		assertTransitiveSuccessors(expectedTrans, "yrwtmtde");
	}
}
