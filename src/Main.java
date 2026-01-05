public class Main {
    public static void main(String[] args) {
        mainController git = new mainController();

        System.out.println("=== 1. SETUP ===");
        git.init();
        git.editFile("config.txt", "Version 1.0");
        git.commit("Initial setup", "Alice");

        System.out.println("\n=== 2. BRANCHING & CODING ===");
        git.createBranch("feature-x");
        git.switchBranch("feature-x");

        git.editFile("feature.java", "System.out.println('Feature X');");
        git.commit("Added Feature X", "Bob");

        System.out.println("\n=== 3. MERGING ===");
        // Switch back to main and merge feature-x into it
        git.switchBranch("main");
        git.merge("feature-x");

        // Verify that 'feature.java' is now inside 'main'
        git.getStatus();

        System.out.println("\n=== 4. REVERTING ===");
        // Let's make a mistake
        git.editFile("config.txt", "Version 2.0 BROKEN");
        git.commit("Bad update", "Alice");

        // Oops, let's revert to the previous commit (ID 2 was the Merge)
        // Note: You might need to check IDs in your output. Assuming '2' here for example.
        git.revert("2");

        System.out.println("\n=== 5. FINAL HISTORY ===");
        git.getBranchHistory();
    }
}

// Test were provided by AI