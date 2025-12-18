import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mainController {

    // storage is going to be set here
    Map<String, Branch> branches = new HashMap<>();
    Map<String, String> workingDirectory = new HashMap<>();
    String currentBranchName;

    public void init() {
        //creation of an empty root
        Commit root = new Commit("Root Commit", "System", new HashMap<>(), new ArrayList<>());

        // creation of a main
        Branch main = new Branch("main", root);
        branches.put("main", main);
        currentBranchName = "main";

        System.out.println("Repository initialized on branch: main");
    }
    /* what is left to do
    maybe editing a file like a text editor
    commit
    create branch
    switch branch
    get branch history
     */
}
