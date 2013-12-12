package edu.oregonstate.cs519.touchdevelop.ast.expression;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.oregonstate.cs519.touchdevelop.ast.ASTNode;

public class ExpressionTokenizerTest {

	// @formatter:off
	@Test
	public void testArithOp() {
		assertTokens("$mNMHXmR6bIKpou2PYSrG7Dv4L0 ,:= ,2 ,/ ,3", 
				ExpressionTokenizer.createLocalRef("mNMHXmR6bIKpou2PYSrG7Dv4L0"),
				ExpressionTokenizer.createOperator(":="),
				ExpressionTokenizer.createOperator("2"),
				ExpressionTokenizer.createOperator("/"),
				ExpressionTokenizer.createOperator("3"));
	}
	
	@Test
	public void testComplexArithOp() throws Exception {
		assertTokens("$xBTNp286Ucy26f9tjIaqATEqL0 ,:= ,3 ,+ $mNMHXmR6bIKpou2PYSrG7Dv4L0 ,- ,5", 
				ExpressionTokenizer.createLocalRef("xBTNp286Ucy26f9tjIaqATEqL0"),
				ExpressionTokenizer.createOperator(":="),
				ExpressionTokenizer.createOperator("3"),
				ExpressionTokenizer.createOperator("+"),
				ExpressionTokenizer.createLocalRef("mNMHXmR6bIKpou2PYSrG7Dv4L0"),
				ExpressionTokenizer.createOperator("-"),
				ExpressionTokenizer.createOperator("5"));
	}
	
	@Test
	public void testMethodCall() throws Exception {
		assertTokens("$xOlX69iyNsS9wnlj0uFrZtLbL0 ,:= :code #vVOK9ePVR7FvRuQgfyH6tNln ,( $mNMHXmR6bIKpou2PYSrG7Dv4L0 ,, $xBTNp286Ucy26f9tjIaqATEqL0 ,)", 
				ExpressionTokenizer.createLocalRef("xOlX69iyNsS9wnlj0uFrZtLbL0"),
				ExpressionTokenizer.createOperator(":="),
				ExpressionTokenizer.createSingletonRef("code"),
				ExpressionTokenizer.createPropertyRefDeclid("vVOK9ePVR7FvRuQgfyH6tNln"),
				ExpressionTokenizer.createOperator("("),
				ExpressionTokenizer.createLocalRef("mNMHXmR6bIKpou2PYSrG7Dv4L0"),
				ExpressionTokenizer.createOperator(","),
				ExpressionTokenizer.createLocalRef("xBTNp286Ucy26f9tjIaqATEqL0"),
				ExpressionTokenizer.createOperator(")"));
	}
	
	@Test
	public void testBoolOp() {
		assertTokens("$EWzImiHGvsT0TzruK4EXIh1wL0 ,:= T ,and F", 
				ExpressionTokenizer.createLocalRef("EWzImiHGvsT0TzruK4EXIh1wL0"),
				ExpressionTokenizer.createOperator(":="),
				ExpressionTokenizer.createBooleanLiteral((1 == 1) + ""),
				ExpressionTokenizer.createOperator("and"),
				ExpressionTokenizer.createBooleanLiteral((1 != 1) + ""));
	}
	
	@Test
	public void testStringAndProperty() {
		assertTokens("'asd .is_empty .post_to_wall", 
				ExpressionTokenizer.createStringLiteral("asd"),
				ExpressionTokenizer.createPropertyRefName("is empty"),
				ExpressionTokenizer.createPropertyRefName("post to wall"));
	}
	
	// @formatter:on

	private void assertTokens(String inputString, ASTNode... tokens) {
		List<ASTNode> expected = new ArrayList<>();
		expected.addAll(Arrays.asList(tokens));

		List<ASTNode> actual = ExpressionTokenizer.tokenize(inputString);
		assertEqualsTokens(expected, actual);
	}

	private void assertEqualsTokens(List<ASTNode> expected, List<ASTNode> actual) {

		assertEquals(expected.size(), actual.size());

		for (int i = 0; i < expected.size(); i++) {
			assertJSONEqual((JSONObject) JSONValue.parse(expected.get(i).getJSON()), (JSONObject) JSONValue.parse(actual.get(i).getJSON()));
		}
	}

	private void assertJSONEqual(JSONObject expected, JSONObject actual) {

		assertEquals(expected.keySet(), actual.keySet());

		for (Object key : expected.keySet()) {
			assertEquals(expected.get(key), actual.get(key));
		}
	}
}