package name.coreycarter.classes;

public class Students {

    public String name;
    public String major;
    public String role;
    private int max_credits_per_semeter;
    private int start_date;

    public Students(String name, String major, String role, int max_credits_per_semeter, int start_date) {
        this.name = name;
        this.major = major;
        this.role = role;
        this.max_credits_per_semeter = max_credits_per_semeter;
        this.start_date = start_date;
    }

    public String getAll() {
        return "Name: " + name + ", Major: " + major + ", Role: " + role + ", max_credits_per_semeter" + max_credits_per_semeter + ", start_date: " + start_date;
    }

    public int get_max_credits_per_semeter() {
        return max_credits_per_semeter;
    }

    public int start_date() {
        return start_date;
    }

}
