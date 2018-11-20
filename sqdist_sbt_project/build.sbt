name := "sqdist"

version := "0.1"

scalaVersion := "2.11.8"

val sparkVersion = "2.3.1"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-mllib-local" % sparkVersion,
)

mainClass in (Compile, run) := Some("test.TestSqdit")