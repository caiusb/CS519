package edu.oregonstate.cs519.touchdevelop.ast.expression;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import edu.oregonstate.cs519.touchdevelop.ast.ASTNode;

public class ExpressionTokenizer {

	private String uq(String s) {
		String r = "";
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (c == '_') {
				r += " ";
			} else if (c == '/') {
				r += (char) (Integer.parseInt(s.substring(i + 1, i + 5), 16));
				i += 4;
			} else {
				r += c;
			}
		}
		return r;
	}

	public ASTNode oneToken(String s) {
		String v = s.substring(1);
		switch (s.charAt(0)) {
		case ',':
			return createOperator(v);
		case '#':
			return createPropertyRefDeclid(v);
		case '.':
			return createPropertyRefName(uq(v));
		case '\'':
			return createStringLiteral(uq(v));
		case 'F':
		case 'T':
			return createBooleanLiteral((s.charAt(0) == 'T') + "");
		case '$':
			return createLocalRef(v);
		case ':':
			return createSingletonRef(uq(v));
//		case '?':
//			int cln = v.indexOf(':');
//			if (cln > 0)
//				return createPlaceHolderWithName((uq(v.substring(0, cln))), (uq(v.substring(cln + 1))));
//			else
//				return createPlaceHolder(uq(v));
		default:
			throw new Error("wrong short form: " + s);
		}
	}

//	protected JSONObject createPlaceHolder(String type) {
//		JSONObject obj = createCoreJSON("placeHolder");
//		obj.put(type, type);
//
//		return obj;
//	}

//	protected ASTNode createPlaceHolderWithName(String type, String name) {
//		JSONObject obj = createPlaceHolder(type);
//		obj.put("name", name);
//
//		return new ASTNode(obj);
//	}

	protected ASTNode createSingletonRef(String v) {
		JSONObject obj = createCoreJSON("singletonRef");
		obj.put("name", v);

		return new ASTNode(obj);
	}

	protected ASTNode createLocalRef(String v) {
		JSONObject obj = createCoreJSON("localRef");
		obj.put("localId", v);

		return new ASTNode(obj);
	}

	protected ASTNode createBooleanLiteral(String s) {
		JSONObject obj = createCoreJSON("booleanLiteral");
		obj.put("value", s);

		return new ASTNode(obj);
	}

	protected ASTNode createStringLiteral(String v) {
		JSONObject obj = createCoreJSON("stringLiteral");
		obj.put("value", v);

		return new ASTNode(obj);
	}

	protected ASTNode createPropertyRefName(String v) {
		JSONObject obj = createCoreJSON("propertyRef");
		obj.put("name", v);

		return new ASTNode(obj);
	}

	protected JSONObject createCoreJSON(String nodeType) {
		JSONObject obj = new JSONObject();
		Object put = obj.put("nodeType", nodeType);
		return obj;
	}

	protected ASTNode createPropertyRefDeclid(String v) {
		JSONObject obj = createCoreJSON("propertyRef");
		obj.put("declId", v);
		
		return new ASTNode(obj);
	}

	protected ASTNode createOperator(String op) {
		JSONObject obj = createCoreJSON("operator");
		obj.put("op", op);

		return new ASTNode(obj);
	}

	public List<ASTNode> tokenize(String shortForm) {
		if (shortForm == null || shortForm.isEmpty())
			return new ArrayList<>(); // handles "" and null; the code below is
										// incorrect for ""
		List<ASTNode> tokens = new ArrayList<>();

		for (String element : shortForm.split(" ")) {
			tokens.add(oneToken(element));
		}

		return tokens;
	}
}
