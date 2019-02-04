import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ComponentManager {

	private static HashMap<String, Boolean> installedComponents;
	private static HashMap<String, ArrayList<String>> dependencies;
	
	private static final String TAB = "\t";

	public static void main(String[] args) {
		installedComponents = new HashMap<String, Boolean>();
		dependencies = new HashMap<String, ArrayList<String>>();
		
		
		List<String> commandList = new ArrayList<String>();
		try {
			for (String line : Files.readAllLines(Paths.get("System Dependencies v3.in.txt"))) {
				System.out.println(line);
				commandList.add(line);
			}
		} catch (IOException e) {
			System.out.println("Error reading input file");
			e.printStackTrace();
		}
		
		
		
		
		System.out.println("--Input--");
		Scanner scan = new Scanner(System.in);
		
		String input = "";
		do {
			input = scan.nextLine();
			commandList.add(input);
		} while(input.compareTo("END") != 0);

		scan.close();
		
		
		System.out.println("--Output--");
		
		
		
		
		for(String s : commandList) {
			//remove extra spaces before splitting
			s = s.trim();
			s = s.replaceAll("\\s+", " ");
			
			String[] parts = s.split(" ");
			switch (parts[0]) {
			case "DEPEND":  
				//run depend
				depend(parts);
				break;
			case "INSTALL":
				//run install
				install(parts);
				break;
			case "REMOVE":
				//run remove
				remove(parts);
				break;
			case "LIST": 
				//run list
				list();
				break;
			case "END": 
				//run list
				end();
				return;
			default:
				System.out.println("Invalid Command - " + parts[0]);
				break;
			}
		}

	}

	private static void depend(String[] dependencyList){
		if(dependencyList.length < 2) {
			System.out.println("Error with dependency command. No dependencies listed");
			return;
		}
		ArrayList<String> tempList = new ArrayList<String>();
		for(int i = 1; i < dependencyList.length; i++) {
			String s = dependencyList[i];
			if(!createdComponents.contains(s)) {
				createdComponents.add(s);
			}
			tempList.add(s);
		}

		String main_comp = tempList.get(0);
		tempList.remove(0);
		dependencies.put(main_comp, tempList);

		System.out.println(Arrays.toString(dependencyList));
	}

	private static void install(String[] installList) {
		if(installList.length < 2) {
			System.out.println("Error with install command. No component listed");
			return;
		}
		String s = installList[1];
		System.out.println(Arrays.toString(installList));
		
		installComponent(s, true);
		if(dependencies.get(s) != null) {
			ArrayList<String> tempList = dependencies.get(s);
			for(String comp : tempList) {
				if(!installedComponents.containsKey(comp)) {
					installComponent(comp, false);
				}
			}
		}
	}
	private static void installComponent(String name, boolean isExplicitInstall) {
		if(installedComponents.containsKey(name)) {
			System.out.println(TAB + name + " is already installed.");
			if(isExplicitInstall && !installedComponents.get(name)) {
				installedComponents.put(name, isExplicitInstall);
			}
		}
		else {
			System.out.println(TAB + "Installing " + name);
			installedComponents.put(name, isExplicitInstall);
		}	
	}

	private static void remove(String[] removeList){
		if(removeList.length < 2) {
			System.out.println("Error with remove command. No component listed");
			return;
		}
		String s = removeList[1];
		System.out.println(Arrays.toString(removeList));
	
		boolean deleted = removeComponent(s, checkDependency(s));
		
		if(dependencies.get(s) != null) {
			ArrayList<String> tempList = dependencies.get(s);
			for(String comp : tempList) {
				Boolean isExplicit = installedComponents.get(comp);
				if(!isExplicit) {
					if(deleted) {
						dependencies.remove(s);
					}
					removeComponent(comp, checkDependency(comp));
				}
			}
		}
	}
	
	private static boolean removeComponent(String name, boolean dependencyExists) {
		if(dependencyExists) {
			System.out.println(TAB + name + " is still needed.");
			return false;
		}
		else {
			if(installedComponents.containsKey(name)) {
				installedComponents.remove(name);
				System.out.println(TAB + "Removing " + name);
				return true;
			}
			else {
				System.out.println(TAB + name + " is not installed.");
				return false;
			}
			
		}
	}
	
	private static boolean checkDependency(String name) {
		for (HashMap.Entry<String, ArrayList<String>> entry : dependencies.entrySet()) {
			//String key = entry.getKey();
			ArrayList<String> l = entry.getValue();
			for(String value : l) {
				if(value.compareTo(name) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	private static void list() {
		System.out.println("LIST");
		for (HashMap.Entry<String, Boolean> entry : installedComponents.entrySet()) {
			System.out.println(TAB + entry.getKey());
		}
	}
	private static void end() {
		System.out.println("END");
	}

	private static void printDependencies() {
		for (HashMap.Entry<String, ArrayList<String>> entry : dependencies.entrySet()) {
			String key = entry.getKey();
			ArrayList<String> l = entry.getValue();
			for(String value : l) {
				System.out.println(key + "-" + value);
			}
		}
	}
}
