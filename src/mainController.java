import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mainController {

    Map<String, Branch> branches = new HashMap<>();
    Map<String, String> workingDirectory = new HashMap<>();
    String currentBranchName;

    // --- BASIC OPERATIONS ---

    public void init() {
        Commit root = new Commit("Root Commit", "System", new HashMap<>(), new ArrayList<>());
        Branch main = new Branch("main", root);
        branches.put("main", main);
        currentBranchName = "main";
        workingDirectory.clear();
        System.out.println("Repository initialized on branch: main");
    }

    public void editFile(String filename, String content) {
        workingDirectory.put(filename, content);
        System.out.println("Edited file: " + filename);
    }

    public void getStatus() {
        Branch current = branches.get(currentBranchName);
        Map<String, String> headFiles = current.head.files;
        List<String> modified = new ArrayList<>();

        for (String file : workingDirectory.keySet()) {
            if (!headFiles.containsKey(file) || !workingDirectory.get(file).equals(headFiles.get(file))) {
                modified.add(file);
            }
        }
        System.out.println("On branch " + currentBranchName + " | Modified: " + modified);
    }

    public void commit(String message, String author) {
        if (message == null || message.isEmpty()) {
            System.out.println("Error: Message cannot be empty");
            return;
        }

        Branch currentBranch = branches.get(currentBranchName);

        // Create parents list (Single parent for normal commit)
        List<Commit> parents = new ArrayList<>();
        if (currentBranch.head != null) {
            parents.add(currentBranch.head);
        }

        Commit newCommit = new Commit(message, author, workingDirectory, parents);
        currentBranch.head = newCommit;

        System.out.println("Committed: " + message + " (ID: " + newCommit.id + ")");
    }

    public void createBranch(String branchName) {
        if (branches.containsKey(branchName)) {
            System.out.println("Error: Branch exists");
            return;
        }
        Commit target = branches.get(currentBranchName).head;
        branches.put(branchName, new Branch(branchName, target));
        System.out.println("Created branch: " + branchName);
    }

    public void switchBranch(String branchName) {
        if (!branches.containsKey(branchName)) {
            System.out.println("Error: Branch does not exist");
            return;
        }
        currentBranchName = branchName;
        Branch branch = branches.get(branchName);
        if (branch.head != null) {
            workingDirectory = new HashMap<>(branch.head.files);
        }
        System.out.println("Switched to branch: " + branchName);
    }

    public void deleteBranch(String branchName) {
        if (!branches.containsKey(branchName) || branchName.equals(currentBranchName)) {
            System.out.println("Error: Cannot delete branch");
            return;
        }
        branches.remove(branchName);
        System.out.println("Deleted branch: " + branchName);
    }

    public void getBranchHistory() {
        System.out.println("--- History for " + currentBranchName + " ---");
        Commit current = branches.get(currentBranchName).head;
        while (current != null) {
            System.out.println(current);
            if (current.parents == null || current.parents.isEmpty()) break;
            current = current.parents.get(0);
        }
        System.out.println("---------------------------");
    }

    // --- NEW: REVERT ADT (Section D) ---
    // Postcondition: Creates a new commit that mirrors the state of the target commit ID
    public void revert(String commitId) {
        // 1. Find the commit (Simple search through current history)
        Commit target = findCommitById(commitId);

        if (target == null) {
            System.out.println("Error: Commit ID " + commitId + " not found in history.");
            return;
        }

        // 2. Restore that state to Working Directory
        workingDirectory = new HashMap<>(target.files);
        System.out.println("Reverting state to Commit ID: " + commitId);

        // 3. Auto-commit this change
        commit("Revert to commit " + commitId, "System");
    }

    // Helper for Revert
    private Commit findCommitById(String id) {
        Commit current = branches.get(currentBranchName).head;
        while (current != null) {
            if (current.id.equals(id)) return current;
            if (current.parents.isEmpty()) break;
            current = current.parents.get(0);
        }
        return null;
    }

    // --- NEW: MERGE ADT (Section E) ---
    // Postcondition: Creates a commit with TWO parents, combining files from both branches
    public void merge(String targetBranchName) {
        if (!branches.containsKey(targetBranchName)) {
            System.out.println("Error: Branch " + targetBranchName + " does not exist.");
            return;
        }

        Branch currentBranch = branches.get(currentBranchName);
        Branch targetBranch = branches.get(targetBranchName);

        System.out.println("Merging " + targetBranchName + " into " + currentBranchName + "...");

        // 1. Combine Files (Simple Strategy: Target Overwrites Current if conflict)
        Map<String, String> targetFiles = targetBranch.head.files;
        workingDirectory.putAll(targetFiles); // Adds new files and updates existing ones

        // 2. Create Merge Commit with TWO parents
        List<Commit> parents = new ArrayList<>();
        parents.add(currentBranch.head); // Parent 1: Where we were
        parents.add(targetBranch.head);  // Parent 2: What we merged in

        Commit mergeCommit = new Commit(
                "Merged branch " + targetBranchName,
                "System",
                workingDirectory,
                parents
        );

        // 3. Update Head
        currentBranch.head = mergeCommit;
        System.out.println("Merge successful. New Commit ID: " + mergeCommit.id);
    }
}

// this file has been refactored many times with the help of AI