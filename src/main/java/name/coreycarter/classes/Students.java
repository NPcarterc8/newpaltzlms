package name.coreycarter.classes;

public class Students {
    public  String name;
    public  String major;
    public  String role;
    private int max_credits_per_semeter;

    public Students(String name, String major, String role, int max_credits_per_semeter){
        this.name = name;
        this.major = major;
        this.role = role;
        this.max_credits_per_semeter= max_credits_per_semeter;
    }
    public String getAll() {
        return "Name: " + name + ", Major: " + major + ", Role: " + role+" "+ max_credits_per_semeter;
    }
    public int get_max_credits_per_semeter() {
        return max_credits_per_semeter;
    }
    //comment it out to define this class from the start also the database need to be upgraded
    // Connection con;

    // public Students() throws SQLException, ClassNotFoundException {
    //     this.con = new dbconn().getConnection();
    // }
    
    // public String getAllFromStudentTable() throws SQLException {
    //     String query = "SELECT * from student_table";
    //     try (Statement stmts = this.con.createStatement();
    //          ResultSet rs = stmts.executeQuery(query)) {
    //         StringBuilder result = new StringBuilder();
    //         while (rs.next()) {
    //             result.append("names : ").append(rs.getString("name"))
    //                   .append(", information : ").append(rs.getInt("stu_id")).append("\n");
    //         }
    //         return result.toString();
    //     }
    // }
    
}
