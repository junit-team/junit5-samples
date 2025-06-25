public class Build {
  public static void main(String[] args) throws Exception {
    var project = Project.ofCurrentWorkingDirectory();
    project.clean();
    project.compile();
    project.test();
  }
}
