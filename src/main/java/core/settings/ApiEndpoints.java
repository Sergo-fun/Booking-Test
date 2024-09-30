package core.settings;

public enum ApiEndpoints {
    PING ("/ping"), //CONST
    BOOkING ("/booking"),
    DELETE ("/delete"),
    AUTH("/auth");
    private final String path;

    ApiEndpoints(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
