import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Commit {
    static int idCounter = 0;
    String id;
    String Message;
    String Author;
    Map<String, String> files;
    List<Commit> parents;

    public Commit(String Message, String Author, Map<String, String> files, List<Commit> parents) {
        this.id = String.valueOf(++idCounter);
        this.Message = Message;
        this.Author = Author;
        this.files = new HashMap<>(files);
        this.parents = parents;
    }

    public String toString() {

        return "Commit ID: " + id + " [ " + Message + " ] " + " [ " + Author + " ] ";
    }
}

