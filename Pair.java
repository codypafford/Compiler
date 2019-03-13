public class Pair {

    private String Name;
    private String Type;


    public Pair(String line) {
        String[] pieces = line.split("\\s+");
        Name = pieces[0];

        if (pieces.length > 1 ) {
            try {
                Type = pieces[1];
            }
            catch (NumberFormatException e) {
                //pass
            }
        }

    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}