package db;

public class Topics {

    private String idTopic;
    private String value;

    // constructors
    public Topics() {
        this.idTopic = null;
        this.setValue(null);
    }

    public Topics(String idTopic, String value) {
        this.idTopic = idTopic;
        this.setValue(value);
    }

    public String getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(String idTopic) {
        this.idTopic = idTopic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
