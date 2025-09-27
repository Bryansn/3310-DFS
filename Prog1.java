
/***************************************************************/
/* Bryan Sanchez                                               */
/* Login ID: bryans1                                           */
/* CS 3310, Fall 2025                                          */
/* Programming Assignment 1                                    */
/* Prog1: connect components of undirected graphs              */
/***************************************************************/
import java.io.*;       //reads file
import java.util.*;     //list, sets, maps, and scanners

public class Prog1 {

    /**************************************************************/
    /* Method: Main                                               */
    /* Purpose: Reads input file and processes each graph line    */
    /* Parameters:                                                */
    /* String[] args (file)                                       */
    /* Returns: none                                              */
    /**************************************************************/

    public static void main(String[] args) {
        //makes sure you provide a file
        if (args.length != 1) {
            //error output if file was not read
            System.out.println("Usage: java Prog1 <input_file>");
            return;
        }
        //store filename 
        String filename = args[0];

        try {
            //gets the file I want to read 
            File inputFile = new File(filename);
            //use scanner to read file line by line
            Scanner scanner = new Scanner(inputFile);

            //keep track of how many graphs are processed
            int graphCount = 1;

            //while there is another line in the file keep going
            while (scanner.hasNextLine()) {
                //read the line and remove extra spaces
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                //example "Graph 1: "
                System.out.println("Graph" + graphCount + ":");

                //use processGrapghLine and store answer in components
                List<List<Integer>> components = processGraphLine(line);

                System.out.print(components.size() + " connected components: ");

                //loop through each connected component
                for (List<Integer> component : components) {
                    //example {1,2,4}
                    System.out.print("{");
                    for (int i : component) {
                        System.out.print(i + " ");
                    }
                    System.out.print("}");
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
    /* String line - a line like "5 (1,2) (3,4) (3,5)"            */
    /* Returns:                                                   */
    /* List of components                                         */
    /**************************************************************/
    public static List<List<Integer>> processGraphLine(String line) {
        //seperate line into sections
        String[] tokens = line.split(" ");
        //first number is num of vertices
        int numVertices = Integer.parseInt(tokens[0]);

        //create map where each vertex points to a list of the 
        //vertices it connects to
        Map<Integer, List<Integer>> graph = new HashMap<>();

        //loop numVertices times
        for (int i = 1; i <= numVertices; i++) {
            //insert values into map
            graph.put(i, new ArrayList<>());
        }
        
        //skip first num, start at one and loop
        for (int i = 1; i < tokens.length; i++) {
            //remove parentheses
            String edge = tokens[i].replaceAll("[()]", "");
            //split by comma ,
            String[] parts = edge.split(",");
            //spit the 2 vertex and assign to vertex A, B
            int vertexA = Integer.parseInt(parts[0]);
            int vertexB = Integer.parseInt(parts[1]);

            //add each vertex to each others list
            graph.get(vertexA).add(vertexB);
            graph.get(vertexB).add(vertexA);
        }

        //make empty collection to store duplicates
        Set<Integer> visited = new HashSet<>();
        //list of connected componets 
        List<List<Integer>> components = new ArrayList<>();

        //loop through each vertex
        for (int i = 1; i <= numVertices; i++) {
            //check if you visited it 
            if (!visited.contains(i)) {
                //empty list for all connected vertices
                List<Integer> component = new ArrayList<>();
                //call dfs 
                dfs(i, graph, visited, component);
                //sort componets
                Collections.sort(component);
                //add list into main component
                components.add(component);
            }
        }

        return components;
    }

    /**************************************************************/
    /* Method: dfs                                                */
    /* Purpose: DFS traversal to find all nodes in a component    */
    /* Parameters:                                                */
    /* int node - current node                                    */
    /* graph - adjacency list                                     */
    /* visited - set of visited nodes                             */
    /* component - list storing current connected component       */
    /**************************************************************/
    public static void dfs(int node, Map<Integer, List<Integer>> graph,
            Set<Integer> visited, List<Integer> component) {
        //add node to visited
        visited.add(node);
        //add node to component
        component.add(node);
        //add each element to neighbor 
        for (int neighbor : graph.get(node)) {
            //if you have not visited the vertex
            if (!visited.contains(neighbor)) {
                //continue dfs and explore all connections
                dfs(neighbor, graph, visited, component);
            }
        }
    }
}
