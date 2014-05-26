import hudson.util.RemotingDiagnostics

def jenkins = hudson.model.Hudson.instance
def pattern = ~/(.*):([0-9]+): error:(.*)/
def cs = manager.build.getChangeSet()

def computer = manager.build.getBuiltOn().toComputer();
def channel = computer.getChannel();

manager.build.logFile.eachLine {
    matcher = pattern.matcher(it)
    if(matcher?.matches()) {
        def file = matcher.group(1)
        def line = matcher.group(2)
        def command = "svn blame ${file}"
        def script = """
            '${command}'.execute();
        """
        def strxml = RemotingDiagnostics.executeGroovy( script, channel );
        def xml = new XmlParser().parseText(strxml)
        xml?.target.entry.each { e ->
          def srcline = e.attribute("line-number")
          if( srcline == line ) {
            manager.listener.logger.println e.commit.author.text()
          }
        }
    }
}
