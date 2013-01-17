def tr = manager.build.getTestResultAction().getResult()
tr.description = "built on ${manager.build.getBuiltOn().getNodeName()}"
def st = tr.getSuites()
st.each { suite ->
  def cr = suite.getCases()
  cr.each { res ->
    res.description = "${manager.build.getTime().format("MM/dd(E) HH:mm::ss")}"
  }
}