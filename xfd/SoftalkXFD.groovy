import hudson.util.RemotingDiagnostics

static callDisplayName(manager)
{
  def res = manager.build.getResult();
  if( res.isWorseThan(hudson.model.Result.SUCCESS) ) {
    def command = "softalkw.exe /W:";
    def find = false;
    manager.build.changeSet.each { cs ->
      cs.items.each {
        command += "${it.getDisplayName()} ";
        find = true;
      }
    }
    
    if( find ) {
      def computer = manager.build.getBuiltOn().toComputer();
      def channel = computer.getChannel();
      
      def script = """
          '${command}'.execute();
      """

      RemotingDiagnostics.executeGroovy( script, channel );
    }
  }
}

static callCulpritsName(manager)
{
  def res = manager.build.getResult();
  if( res.isWorseThan(hudson.model.Result.SUCCESS) ) {
    def users = manager.build.getCulprits();
    if( users != null ) {
      def command = "softalkw.exe /W:";
      def find = false;
      users.each {
        command += "${it.getDisplayName()} ";
        find = true;
      }
      if( find ) {
        command += " アウトーーー！";
        manager.listener.logger.println(command);

        def computer = manager.build.getBuiltOn().toComputer();
        def channel = computer.getChannel();
        
        def script = """
            '${command}'.execute();
        """

        RemotingDiagnostics.executeGroovy( script, channel );
      }
    }
  }
}
