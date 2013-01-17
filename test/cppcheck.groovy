println "*** Cppcheck Result Begin ***"
args.each {
  def cppcheck = new XmlParser().parse(it)
  cppcheck.error.each{
    println "${it.attribute("id")}: ${it.attribute("msg")}"
    println "    file = ${it.attribute("file")} "
    println "    line = ${it.attribute("line")} "
    println "----------------------------------------------"
    println " "
  }
}
println "*** Cppcheck Result End ***"
