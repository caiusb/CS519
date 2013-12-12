package edu.oregonstate.cs519.touchdevelop.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import edu.oregonstate.cs519.touchdevelop.ast.expression.ExpressionTokenizer;

public class ASTNode implements JSONAware {
	
	public static final String ID = "id";
	public static final String AST = "ast";
	public static final String DECLARATIONS = "decls";
	public static final String BODY = "body";
	public static final String EXPRESSION = "expr";
	public static final String NODE_TYPE = "nodeType";
	
	public static final String DEFAULT_OWNER = "base";
	public static final String BASE_OWNER = "base";
	
	private ASTNode parent;
	private boolean deleted = false;
	private HashMap<String,String> propertiesChanged;
	
	private Map<String, Object> map;
	
	public ASTNode(String JSONString) {
		this(JSONString, DEFAULT_OWNER);
	}

	@SuppressWarnings("unchecked")
	public ASTNode(String JSONString, String owner) {
		this((Map<String, Object>) JSONValue.parse(JSONString));
		propertiesChanged.put(ASTNode.ID, owner);
	}

	public ASTNode(Map<String, Object> map, ASTNode parent) {
		this.map = map;
		this.parent = parent;
		
		if ("exprStmt".equals(getProperty(NODE_TYPE))) {
			map.put(ASTNode.EXPRESSION, ExpressionTokenizer.tokenize((String) getProperty(EXPRESSION)));
		}

		ASTNodeManager.getInstance().addNode(this);
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object thing = map.get(key);
			expandProperty(key, thing);
		}
		propertiesChanged = new HashMap<String,String>();
	}
	
	public ASTNode(Map<String, Object> map) {
		this(map ,null);
	}

	public Object getProperty(String name) {
		Object object = map.get(name);
		return object;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object expandProperty(String propertyName, Object contents) {
		
		//list of nodes
		if (contents instanceof List) {
			List temp = new ArrayList<ASTNode>();
			for (Object item : (List) contents) {
				if (item instanceof Map) //actual nodes
					temp.add(new ASTNode((Map)item, this));
				else if (item instanceof String) { // references to other nodes
					ASTNode node = ASTNodeManager.getInstance().getNode((String) item);
					temp.add(node);
				}
			}
			map.put(propertyName, temp);
			contents = temp;
		}
		
		//actual node
		if (contents instanceof Map) {
			contents = new ASTNode((Map) contents, this);
			map.put(propertyName,contents);
		}
		
		return contents;
	}

	public void updateProperty(String name, Object newProperty) throws ConflictException {
		updateProperty(name, newProperty, DEFAULT_OWNER);
	}

	public void updateProperty(String name, Object newProperty, String origin) throws ConflictException {
		if (currentOriginIsNotOwner(name, origin) || isDeleted())
			if (newProperty instanceof List)
				newProperty = mergeList((List) newProperty, name);
			else
				throw new ConflictException(getProperty(ID) + ":" + name + ":" + newProperty);
		Object expandedProperty = expandProperty(name, newProperty);
		map.put(name, expandedProperty);
		propertiesChanged.put(name, origin);
	}
	
	private List mergeList(List newProperty, String propertyName) {
		List currentList = (List) map.get(propertyName);
		List newList = new ArrayList();
		int j = 0;
		for (int i=0; i<currentList.size();i++) {
			ASTNode currentNode = (ASTNode) currentList.get(i);
			String nodeID = (String) newProperty.get(j);
			ASTNode newNode = ASTNodeManager.getInstance().getNode(nodeID);
			if (currentNode.getProperty(ASTNode.ID).equals(newNode.getProperty(ASTNode.ID))) {
				j++;
				newList.add(currentNode.getProperty(ID));
				continue;
			}
			String currentOwner = currentNode.getOwner(ID);
			String newOwner = newNode.getOwner(ID);
			if (currentOwner != newOwner) {
				newList.add(currentNode.getProperty(ID));
			}
			newList.add(newNode.getProperty(ID));
			j++;
		}
		if (j < newProperty.size())
			for(; j < newProperty.size(); j++)
				newList.add(newProperty.get(j));
		return newList;
	}

	private boolean currentOriginIsNotOwner(String propertyName, String newOwner) {
		if (isPropertyChanged(propertyName)) {
			String owner = propertiesChanged.get(propertyName);
			if (owner == null)
				return false;
			return !owner.equals(newOwner);
		}
		return false;
	}

	public String getJSON() {
		return JSONObject.toJSONString(map);
	}
	
	@Override
	public String toJSONString() {
		return JSONObject.toJSONString(map);
	}
	
	@Override
	public String toString() {
		return getProperty(ID).toString();
	}

	@Deprecated
	public void delete() throws ConflictException {
		delete("");
	}

	public void delete(String origin) throws ConflictException {
		for (String key : map.keySet()) {
			if(currentOriginIsNotOwner(key, origin))
				throw new ConflictException(getProperty(ID) + ":delete");
		}
		parent.removeNode(this);
		//ASTNodeManager.getInstance().deleteNode(this);
		deleted = true;
	}

	@SuppressWarnings("rawtypes")
	private void removeNode(ASTNode astNode) {
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object thing = map.get(key);
			if (thing instanceof List) {
				((List) thing).remove(astNode);
			}
		}
		map.remove(astNode);
	}

	public ASTNode getParent() {
		return parent;
	}

	protected boolean isNodeChanged() {
		return !propertiesChanged.isEmpty();
	}

	protected boolean isPropertyChanged(String property) {
		return propertiesChanged.keySet().contains(property);
	}

	public String getOwner(String propertyName) {
		String owner = propertiesChanged.get(propertyName);
		if (owner == null)
			return BASE_OWNER;
		return owner;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public boolean matchWith(ASTNode node) {
		Set<String> properties = map.keySet();
		if (properties.size() != node.map.size())
			return false;
		
		for (String property : properties) {
			if (property.equals(ASTNode.ID))
				continue;
			Object value = node.getProperty(property);
			if (value == null)
				return false;
			if (value instanceof ASTNode) {
				if (!((ASTNode)value).matchWith((ASTNode) this.getProperty(property)))
					return false;
			} else if (value instanceof List) {
				List<ASTNode> list = (List<ASTNode>) value;
				List<ASTNode> theOtherList = (List<ASTNode>) this.getProperty(property);
				if (!matchTwoLists(list, theOtherList))
					return false;
			} else {
				if (!value.equals(this.getProperty(property)))
					return false;
			}
		}
		
		try {
			ASTNodeManager.getInstance().addNode(this);
			updateProperty(ID, node.getProperty(ID), DEFAULT_OWNER);
		} catch (ConflictException e) {
			return false;
		}
		return true;
	}

	private boolean matchTwoLists(List<ASTNode> list, List<ASTNode> theOtherList) {
		for (ASTNode element : list) {
			boolean matched = false;
			for (ASTNode otherElement : theOtherList)
				if (element.matchWith(otherElement)) {
					matched = true;
					otherElement.map.put("matched", "true");
					break;
				}
			if (!matched)
				return false;
		}
		for (ASTNode element : theOtherList) {
			Object v = element.map.remove("matched");
			if (v == null)
				return false;
		}
		
		return true;
	}
}
