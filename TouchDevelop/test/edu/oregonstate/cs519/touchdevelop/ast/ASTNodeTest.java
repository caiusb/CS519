package edu.oregonstate.cs519.touchdevelop.ast;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ASTNodeTest {

	private String initialProgramWithOneEmptyStatement = "{\n" + "      \"textVersion\": \"v2.2,js,ctx\",\n"
			+ "      \"jsonVersion\": \"v1.0,resolved,short\",\n"
			+ "      \"name\": \"edits-test-dumb\",\n"
			+ "      \"comment\": \"\",\n"
			+ "      \"autoIcon\": \"Exit\",\n"
			+ "      \"autoColor\": \"#EEDC82\",\n"
			+ "      \"platform\": \"current\",\n"
			+ "      \"rootId\": \"ycXVAstFZ325M0PRsuXtUu7F\",\n"
			+ "      \"showAd\": false,\n"
			+ "      \"isLibrary\": false,\n"
			+ "      \"allowExport\": false,\n"
			+ "      \"hasUniqueIds\": false,\n" + "      \"decls\": [\n"
			+ "        {\n" + "          \"name\": \"main\",\n"
			+ "          \"inParameters\": [],\n"
			+ "          \"outParameters\": [],\n"
			+ "          \"isPrivate\": false,\n"
			+ "          \"isOffloaded\": false,\n"
			+ "          \"isTest\": false,\n"
			+ "          \"isAsync\": false,\n"
			+ "          \"nodeType\": \"action\",\n"
			+ "          \"body\": [\n" + "            {\n"
			+ "              \"expr\": \"\",\n"
			+ "              \"nodeType\": \"exprStmt\",\n"
			+ "              \"id\": \"x0jQd1BtQGFLL1XBIeiT9kmL\",\n"
			+ "              \"locals\": []\n" + "            }\n"
			+ "          ],\n"
			+ "          \"id\": \"SZwwuN9ffv5TLJuO8buwjifz\"\n"
			+ "        }\n" + "      ],\n"
			+ "      \"deletedDecls\": [],\n"
			+ "      \"nodeType\": \"app\",\n" + "      \"id\": \"app\"\n"
			+ "    }";
	private ASTNode root;
	
	@Before
	public void setUp() {
		root = new ASTNode(initialProgramWithOneEmptyStatement);
	}

	@Test
	public void testEmptyAppCreation() {
		String json = "{\"textVersion\": \"v2.2,js,ctx\",\n"
				+ "      \"jsonVersion\": \"v1.0,resolved,short\",\n"
				+ "      \"name\": \"edits-test-dumb\",\n"
				+ "      \"comment\": \"\",\n"
				+ "      \"autoIcon\": \"Exit\",\n"
				+ "      \"autoColor\": \"#EEDC82\",\n"
				+ "      \"platform\": \"current\",\n"
				+ "      \"rootId\": \"ycXVAstFZ325M0PRsuXtUu7F\",\n"
				+ "      \"showAd\": false,\n"
				+ "      \"isLibrary\": false,\n"
				+ "      \"allowExport\": false,\n"
				+ "      \"hasUniqueIds\": false,\n" + "      \"decls\": [],\n"
				+ "      \"deletedDecls\": [],\n"
				+ "      \"nodeType\": \"app\",\n" + "      \"id\": \"app\"}";

		Map<String, Object> map = (Map<String, Object>) JSONValue.parse(json);
		ASTNode astNode = new ASTNode(map);
		String id = (String) astNode.getProperty(ASTNode.ID);
		assertEquals("app", id);
	}

	@Test
	public void testAppWithMainCreation() {
		String json = "{\"textVersion\": \"v2.2,js,ctx\",\n"
				+ "      \"jsonVersion\": \"v1.0,resolved,short\",\n"
				+ "      \"name\": \"edits-test-dumb\",\n"
				+ "      \"comment\": \"\",\n"
				+ "      \"autoIcon\": \"Exit\",\n"
				+ "      \"autoColor\": \"#EEDC82\",\n"
				+ "      \"platform\": \"current\",\n"
				+ "      \"rootId\": \"ycXVAstFZ325M0PRsuXtUu7F\",\n"
				+ "      \"showAd\": false,\n"
				+ "      \"isLibrary\": false,\n"
				+ "      \"allowExport\": false,\n"
				+ "      \"hasUniqueIds\": false,\n" + "      \"decls\": [{\n"
				+ "          \"name\": \"main\",\n"
				+ "          \"inParameters\": [],\n"
				+ "          \"outParameters\": [],\n"
				+ "          \"isPrivate\": false,\n"
				+ "          \"isOffloaded\": false,\n"
				+ "          \"isTest\": false,\n"
				+ "          \"isAsync\": false,\n"
				+ "          \"nodeType\": \"action\",\n"
				+ "          \"body\": [],\n"
				+ "          \"id\": \"gCZN28r7NjF4okBgicbUzSTA\"\n"
				+ "        }],\n" + "      \"deletedDecls\": [],\n"
				+ "      \"nodeType\": \"app\",\n" + "      \"id\": \"app\"}";

		Map<String, Object> map = (Map<String, Object>) JSONValue.parse(json);
		ASTNode astNode = new ASTNode(map);
		List<ASTNode> decls = (List<ASTNode>) astNode
				.getProperty(ASTNode.DECLARATIONS);
		assertEquals(1, decls.size());
		assertEquals("gCZN28r7NjF4okBgicbUzSTA",
				decls.get(0).getProperty(ASTNode.ID));
	}

	@Test
	public void testAppWithMainAndBodyCreation() {

		Map<String, Object> map = (Map<String, Object>) JSONValue.parse(initialProgramWithOneEmptyStatement);
		ASTNode astNode = new ASTNode(map);
		List<ASTNode> decls = (List<ASTNode>) astNode
				.getProperty(ASTNode.DECLARATIONS);
		List<ASTNode> body = (List<ASTNode>) decls.get(0).getProperty(
				ASTNode.BODY);
		assertEquals(1, body.size());
		assertEquals("x0jQd1BtQGFLL1XBIeiT9kmL",
				body.get(0).getProperty(ASTNode.ID));
	}

	@Test
	@Ignore
	public void testUnpackAndPack() {
		packAndUnpackAndAssert(initialProgramWithOneEmptyStatement);
	}

	@SuppressWarnings("rawtypes")
	public void packAndUnpackAndAssert(String json) {
		Map<String, Object> map = (Map<String, Object>) JSONValue.parse(json);
		ASTNode astNode = new ASTNode(map);
		((ASTNode) ((List) astNode.getProperty(ASTNode.DECLARATIONS)).get(0))
				.getProperty(ASTNode.BODY);
		String outputJSON = astNode.getJSON();
		Map<String, Object> expected = (Map<String, Object>) JSONValue.parse(json);
		Map actual = (Map) JSONValue.parse(outputJSON);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSimplePackAndUnpack() {
		String json = "{\"textVersion\": \"v2.2,js,ctx\",\n"
				+ "      \"decls\": [{\n"
				+ "          \"id\": \"gCZN28r7NjF4okBgicbUzSTA\"\n"
				+ "        }],\n" + "      \"deletedDecls\": [],\n"
				+ "      \"nodeType\": \"app\",\n" + "      \"id\": \"app\"}";
		
		packAndUnpackAndAssert(json);
	}
	
	@Test
	public void testParentNode() {
		assertNull(root.getParent());
		ASTNode expr = ASTNodeManager.getInstance().getNode("x0jQd1BtQGFLL1XBIeiT9kmL");
		assertNotNull(expr.getParent());
		assertEquals(expr.getParent().getProperty(ASTNode.ID),"SZwwuN9ffv5TLJuO8buwjifz");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testDelete() throws Exception {
		ASTNode expression = ASTNodeManager.getInstance().getNode("x0jQd1BtQGFLL1XBIeiT9kmL");
		expression.delete();
		assertEquals("{\"isLibrary\":false,\"jsonVersion\":\"v1.0,resolved,short\",\"platform\":\"current\",\"textVersion\":\"v2.2,js,ctx\",\"rootId\":\"ycXVAstFZ325M0PRsuXtUu7F\",\"allowExport\":false,\"id\":\"app\",\"autoColor\":\"#EEDC82\",\"deletedDecls\":[],\"name\":\"edits-test-dumb\",\"autoIcon\":\"Exit\",\"hasUniqueIds\":false,\"decls\":[{\"id\":\"SZwwuN9ffv5TLJuO8buwjifz\",\"inParameters\":[],\"body\":[],\"isPrivate\":false,\"isOffloaded\":false,\"name\":\"main\",\"isAsync\":false,\"isTest\":false,\"outParameters\":[],\"nodeType\":\"action\"}],\"nodeType\":\"app\",\"comment\":\"\",\"showAd\":false}",root.getJSON());
		assertTrue(expression.isDeleted());
	}
	
	@Test
	public void testSimpleMarkupOfNode() throws Exception {
		root.updateProperty(ASTNode.ID, "test1");
		assertTrue(root.isNodeChanged());
	}

	@Test
	public void testSimpleMarkupOfProperty() throws Exception {
		root.updateProperty(ASTNode.ID, "test1");
		assertTrue(root.isPropertyChanged(ASTNode.ID));
		assertFalse(root.isPropertyChanged(ASTNode.DECLARATIONS));
	}
	
	@Test
	public void testSimpleMarkupAndOwnership() throws Exception {
		root.updateProperty(ASTNode.ID, "test1");
		assertEquals(root.getOwner(ASTNode.ID),ASTNode.DEFAULT_OWNER);
	}
	
	@Test
	public void testNoChangeBaseOwnership() {
		assertEquals(root.getOwner(ASTNode.ID),ASTNode.DEFAULT_OWNER);
	}
	
	@Test(expected=ConflictException.class)
	public void testThrowExceptionOnConflictingChange() throws Exception {
		ASTNode exprNode = ASTNodeManager.getInstance().getNode("x0jQd1BtQGFLL1XBIeiT9kmL");
		exprNode.updateProperty(ASTNode.EXPRESSION, "", "somebody");
		exprNode.updateProperty(ASTNode.EXPRESSION, "", "somebody else");
	}
	
	@Test(expected=ConflictException.class)
	public void testThrowExceptionOnDeleteOfChangedNode() throws Exception {	
		ASTNode exprNode = ASTNodeManager.getInstance().getNode("x0jQd1BtQGFLL1XBIeiT9kmL");
		exprNode.updateProperty(ASTNode.EXPRESSION, "", "somebody");
		exprNode.delete("somebody else");
	}

	@Test(expected=ConflictException.class)
	public void testDeleteAndThenUpdateConflict() throws Exception {
		ASTNode exprNode = ASTNodeManager.getInstance().getNode("x0jQd1BtQGFLL1XBIeiT9kmL");
		exprNode.delete();
		exprNode.updateProperty("expr", "");
	}
	
	@Test
	public void testAddTwoStatementsConcurently() throws ConflictException {
		ASTNode method = ASTNodeManager.getInstance().getNode("SZwwuN9ffv5TLJuO8buwjifz");
		ASTNode node = new ASTNode("{\"id\": \"new-one\"}","first");
		method.updateProperty(ASTNode.BODY, JSONValue.parse("[\"SZwwuN9ffv5TLJuO8buwjifz\",\"new-one\"]"),"first");
		node = new ASTNode("{\"id\": \"another-new-one\"}","second");
		method.updateProperty(ASTNode.BODY, JSONValue.parse("[\"SZwwuN9ffv5TLJuO8buwjifz\",\"another-new-one\"]"),"second");
		assertEquals(3,((List)method.getProperty(ASTNode.BODY)).size());
	}
	
	@Test
	public void testDeleteAndAddStatement() throws ConflictException {
		ASTNode method = ASTNodeManager.getInstance().getNode("SZwwuN9ffv5TLJuO8buwjifz");
		method.updateProperty(ASTNode.BODY, JSONValue.parse("[]"),"first");
		ASTNode node = new ASTNode("{\"id\": \"another-new-one\"}","second");
		method.updateProperty(ASTNode.BODY, JSONValue.parse("[\"SZwwuN9ffv5TLJuO8buwjifz\",\"another-new-one\"]"),"second");
		// I revive the deleted statement, for correctness
		// not because I'm too lazy to change a good chunk of the code
		assertEquals(2,((List)method.getProperty(ASTNode.BODY)).size());
	}
	
	@Test
	public void testMatchNode() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\"}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\"}");
		assertTrue(node1.matchWith(node2));
		assertEquals(node2.getProperty(ASTNode.ID),node1.getProperty(ASTNode.ID));
	}
	
	@Test
	public void testDontMatchIfTypesDiffer() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\"}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"method\"}");
		assertFalse(node1.matchWith(node2));
		assertFalse(node1.getProperty(ASTNode.ID).equals(node2.getProperty(ASTNode.ID)));
	}
	
	@Test
	public void testMatchIfTheSameType() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\"}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\"}");
		assertTrue(node1.matchWith(node2));
		assertEquals(node1.getProperty(ASTNode.ID), node2.getProperty(ASTNode.ID));
	}
	
	@Test
	public void testDontMatchIfSomePropertiesDiffer() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\",\"px\": \"this\"}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\",\"px\": \"that\"}");
		assertFalse(node1.matchWith(node2));
		assertFalse(node1.getProperty(ASTNode.ID).equals(node2.getProperty(ASTNode.ID)));

	}
	
	@Test
	public void testDontMatchIfPropertiesAreMissing() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\"}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\",\"px\": \"that\"}");
		assertFalse(node1.matchWith(node2));
		assertFalse(node1.getProperty(ASTNode.ID).equals(node2.getProperty(ASTNode.ID)));
	}
	
	@Test
	public void testDontMatchIfPropertiesAreMissing2() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\",\"px\": \"this\"}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\"}");
		assertFalse(node1.matchWith(node2));
		assertEquals("bla",node1.getProperty(ASTNode.ID));
		assertEquals("bla2", node2.getProperty(ASTNode.ID));
	}
	
	@Test
	public void testDontMatchIfPropertyTypesAreNotMatching() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\",\"px\": \"this\"}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\",\"py\": \"this\"}");
		assertFalse(node1.matchWith(node2));
		assertFalse(node1.getProperty(ASTNode.ID).equals(node2.getProperty(ASTNode.ID)));
	}
	
	@Test
	public void testMatchingWithMoreProperties() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\",\"px\": \"this\"}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\",\"px\": \"this\"}");
		assertTrue(node1.matchWith(node2));
		assertEquals(node1.getProperty(ASTNode.ID), node2.getProperty(ASTNode.ID));
	}
	
	@Test
	public void testMatchIfPropertiesAndChildrenMatch() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\",\"px\": \"this\",\"body\":{\"id\": \"smth\", \"py\": \"this\"}}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\",\"px\": \"this\",\"body\":{\"id\": \"smth2\", \"py\": \"this\"}}");
		assertTrue(node1.matchWith(node2));
		assertEquals(node1.getProperty(ASTNode.ID), node2.getProperty(ASTNode.ID));
	}
	
	@Test
	public void testMatchIfPropertiesandListOfNodesMatch() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\",\"px\": \"this\",\"body\":[{\"id\": \"smth\", \"py\": \"this\"}]}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\",\"px\": \"this\",\"body\":[{\"id\": \"smth2\", \"py\": \"this\"}]}");
		assertTrue(node1.matchWith(node2));
		assertEquals(node1.getProperty(ASTNode.ID), node2.getProperty(ASTNode.ID));
	}
	
	@Test
	public void testDontMatchForMultipleIdenticalChildren() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\",\"px\": \"this\",\"body\":[{\"id\": \"smth\", \"py\": \"this\"}]}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\",\"px\": \"this\",\"body\":[{\"id\": \"smth2\", \"py\": \"this\"},{\"id\": \"smth3\", \"py\": \"this\"}]}");
		assertFalse(node1.matchWith(node2));
		assertFalse(node1.getProperty(ASTNode.ID).equals(node2.getProperty(ASTNode.ID)));
	}
	
	@Test
	public void testDontMatchForMultipleIdenticalChildren2() {
		ASTNode node1 = new ASTNode("{\"id\": \"bla\",\"nodeType\": \"app\",\"px\": \"this\",\"body\":[{\"id\": \"smth\", \"py\": \"this\"},{\"id\": \"smth4\", \"py\": \"that\"}]}");
		ASTNode node2 = new ASTNode("{\"id\": \"bla2\",\"nodeType\": \"app\",\"px\": \"this\",\"body\":[{\"id\": \"smth2\", \"py\": \"this\"},{\"id\": \"smth3\", \"py\": \"this\"}]}");
		assertFalse(node1.matchWith(node2));
		assertFalse(node1.getProperty(ASTNode.ID).equals(node2.getProperty(ASTNode.ID)));
	}
	
	@Test
	public void testMatchTwoRealPrograms() throws Exception {
		String json1 = new String(Files.readAllBytes(Paths.get("test-scripts/matchIDs1.json")));
		String json2 = new String(Files.readAllBytes(Paths.get("test-scripts/matchIDs2.json")));
		ASTNode program1 = new ASTNode(json1);
		ASTNode program2 = new ASTNode(json2);
		assertTrue(program1.matchWith(program2));
	}
	
	@Test
	public void testTreeWithShortForm() throws Exception {
		String json = new String(Files.readAllBytes(Paths.get("test-scripts/short-form.json")));
		ASTNode root = new ASTNode(json);
		ASTNode node = ASTNodeManager.getInstance().getNode("Mk59PRqnpNVeXggvLSRaMFLh");
		Object property = node.getProperty(ASTNode.EXPRESSION);
		assertEquals(ArrayList.class,property.getClass());
	}
	
}