/**************************************************************/
/* Bryan Sanchez                                              */
/* Login ID: bryans1                                          */
/* CS 3310, Fall 2025                                         */
/* Programming Assignment 1                                   */
/* Prog1: Outputs connected components of undirected graphs   */
/**************************************************************/

import java.io.*;             // For reading files
import java.util.*;           // For lists, sets, maps, scanners, etc.

public class Prog1 {

    /**************************************************************/
    /* Method: main                                               */
    /* Purpose: Reads input file and processes each graph line    */
    /* Parameters:                                                */
    /*   String[] args - command-line arguments                   */
    /* Returns: none                                              */
    /**************************************************************/

    public static void main(String[] args) {
    //Check if file name is provided in command line
        if (args.length != 1) {
            System.out.println("Usage: java Prog1 <input_file>");
            return;
        }
        //store filename 
        String filename = args[0];

        try {
        //open file and prepare to read line by line
            File inputFile = new File(filename);
            Scanner scanner = new Scanner(inputFile);

            //keep track of how many graphs are processed
            int graphCount = 1;

            //read each line of the file and if empty skip it
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                //print a lable for current graph
                System.out.println("Graph" + graphCount + ":");

                //return the list of connected components
                List<List<Integer>> components = processGraphLine(line);

                //print out message for how many commponets the graph has
                if (components.size() == 1) {
                    System.out.print("One connected component: ");
                } else {
                    System.out.print(components.size() + " connected components: ");
                }

                //loops through each connected componet and proints the verticies
                for (List<Integer> component : components) {
                    System.out.print("{");
                    for (int v : component) {
                        System.out.print(v + " ");
                    }
                    System.out.print("} ");
                }
                //increment graph count and go to next line
                System.out.println("\n");
                graphCount++;
            }

            //close the canner 
            scanner.close();
        } catch (FileNotFoundException e) {
            //print error if file is not found
            System.out.println("Error: File not found - " + filename);
        }
    }

    /**************************************************************/
    /* Method: processGraphLine                                   */
    /* Purpose: Parses a line of input and finds connected comps  */
    /* Parameters:                                                */
    /*   String line - a line like "5 (1,2) (3,4) (3,5)"           */
    /* Returns:                                                   */
    /*   List of components (each as a list of vertices)          */
    /**************************************************************/
    public static List<List<Integer>> processGraphLine(String line) {
        //first value is the number of vertices
        String[] tokens = line.split(" ");
        int numVertices = Integer.parseInt(tokens[0]);

        //initialize the list each node maps to an empty list
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (int i = 1; i <= numVertices; i++) {
            graph.put(i, new ArrayList<>());
        }

        //parse each edge like (1,2) 
        //removes the parentheses 
        //splits by camma
        //adds both directions since undirected 
        for (int i = 1; i < tokens.length; i++) {
            String edge = tokens[i].replaceAll("[()]", "");
            String[] parts = edge.split(",");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);

            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        Set<Integer> visited = new HashSet<>();
        List<List<Integer>> components = new ArrayList<>();

        for (int i = 1; i <= numVertices; i++) {
            if (!visited.contains(i)) {
                List<Integer> component = new ArrayList<>();
                dfs(i, graph, visited, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        return components;
    }

    /**************************************************************/
    /* Method: dfs                                                */
    /* Purpose: DFS traversal to find all nodes in a component    */
    /* Parameters:                                                */
    /*   int node - current node                                  */
    /*   graph - adjacency list                                   */
    /*   visited - set of visited nodes                           */
    /*   component - list storing current connected component     */
    /**************************************************************/
    public static void dfs(int node, Map<Integer, List<Integer>> graph,
                           Set<Integer> visited, List<Integer> component) {
        visited.add(node);
        component.add(node);

        for (int neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited, component);
            }
        }
    }
}