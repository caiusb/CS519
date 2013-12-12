package edu.oregonstate.cs519.touchdevelop.main;

import java.nio.file.Files;
import java.nio.file.Paths;

import edu.oregonstate.cs519.touchdevelop.ast.ASTNode;
import edu.oregonstate.cs519.touchdevelop.ast.History;

public class ForMergeMain {

	public static void main(String[] args) throws Exception {
		String json1 = new String(Files.readAllBytes(Paths.get("../microedits/sum/v01.json")));
		String json2 = new String(Files.readAllBytes(Paths.get("../microedits/sum/v02.json")));
		
		History history = new History(json1, "a");
		History history2 = new History(json2, "b");
		history2.getProgram().matchWith(history.getProgram());
		ASTNode afterFirstApplication = history.apply();
		history2 = new History(afterFirstApplication, json2, "b");
		ASTNode finalMerge = history2.apply();
		
		System.out.println(finalMerge.getJSON());
		System.out.println("There were " + history2.getConflicts().size() + " conflicts");
	}

}
