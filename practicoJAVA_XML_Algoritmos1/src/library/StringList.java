package library;

import java.util.ArrayList;
import java.util.List;

public class StringList {
    private List<String> list;

    public StringList() {
        list = new ArrayList<String>();
    }

    public void add(String s) {
        list.add(s);
    }

    public int size() {
        return list.size();
    }

    public String get(int i) {
        try {
            return list.get(i);
        } catch (Exception e) {
            return null;
        }
    }
}
