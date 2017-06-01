package materna.przemek.egzaminel.Database;

import java.util.Date;
import java.util.Objects;

public class Group {

    private int id;
    private String name;
    private String description;
    private long lastUpdate;

    public Group (int id, String name, String description, long lastUpdate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lastUpdate = lastUpdate;
    }

    public boolean hasTheSameId(Group g) {
        return g.id  == this.id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Group)) {
            return false;
        }

        Group g = (Group) object;
        return (this.id == g.id && this.lastUpdate == g.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastUpdate, Group.class.getName());
    }

    @Override
    public String toString() {
        String s = "Group(";
        s +=    id + ", "
                + name + ", "
                + description + ", "
                + getFormattedLastUpdate() + ")";
        return s;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public Date getFormattedLastUpdate() {
        return new Date(lastUpdate);
    }
}
