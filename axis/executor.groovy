def list = []
def slaves = jenkins.slaves
slaves.each { slave ->
    slave.getAssignedLabels().find {
        if( it.getName() == "vs2015" ) {
            list += slave.getSelfLabel().getName()
            return true
        }
    }
}

combinations.each {
    if( list.find { l -> it.label == l } ) {
         result[it.label] = result[it.label] ?: []
         result[it.label] << it
    }
}

execution.listener.logger.println result

return result
