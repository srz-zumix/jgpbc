def result = []
def jenkins = hudson.model.Hudson.instance
def slaves = jenkins.slaves
slaves.each { slave ->
    slave.getAssignedLabels().find {
        if( it.getName() == "vs2015" ) {
            result += slave.getSelfLabel().getName()
            return true
        }
    }
}
return result
