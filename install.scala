import java.io.IOException
import scala.io.Source
import java.nio.file._
import java.nio.file.attribute._
import java.nio.file.attribute.PosixFilePermission._
import java.nio.file.FileVisitResult._
import java.io.File
import scala.sys.process._
import java.net.URL
import scala.collection.JavaConverters._

def mkdir(path: Path) {
  if (!Files.exists(path)) {
    println("Creating: " + path)
    Files.createDirectory(path)
  }
}

def downloadFileTo(url: String, path: Path) {
  if (!Files.exists(path)) {
    println("Downloading %s to %s.".format(url, path))
    new URL(url) #> path.toFile !
  }
}

def uPlusX(paths: Path*) {
  paths.foreach{path =>
    val originalPermissions = Files.getPosixFilePermissions(path).asScala 
    val newPermissions = originalPermissions + OWNER_EXECUTE
    if (originalPermissions != newPermissions) {
      println("Granting owner execute permissions to %s.".format(path))
      Files.setPosixFilePermissions(path, newPermissions.asJava)
    }
  }
}

def withSource[T](source: => Source)(expression: Source => T): T = {
  val sourceInstance = source
  try {
    expression(sourceInstance)
  } finally {
    sourceInstance.close()
  }
}

// Uses the first line of content to determine if it needs to be added.
def addContent(path: Path, content: Path) {
  val exists = Files.exists(path)
  val shouldBeAppended = !exists || withSource(Source.fromFile(path.toFile)){destination =>
    withSource(Source.fromFile(content.toFile)){source =>
      source.getLines.find(_ => true).headOption match {
        case Some(firstLine) => !destination.getLines.contains(firstLine)
        case None => false
      }
    }
  }
  if (shouldBeAppended) {
    println("Appending %s to %s.".format(content, path))
    content.toFile #>> path.toFile !;
  }
}

def delete(path: Path) {
  if (Files.exists(path)) Files.walkFileTree(path, new SimpleFileVisitor[Path]() {
    override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
      Files.delete(file)
      return CONTINUE
    }
 
    override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
      if (exc == null) {
        Files.delete(dir)
        return CONTINUE
      } else {
        throw exc
      }
    }
  })
}

val fileSystem = FileSystems.getDefault()
// Assumes we're in the dotfiles directory.
val dotFiles = fileSystem.getPath(".")
val home = fileSystem.getPath(sys.props("user.home"))
val homeBin = home.resolve("bin")
val bashrc = home.resolve(".bashrc")

// Make all the folders.
mkdir(homeBin)

// Build vim structure, pulling down vundle install from github.

// Grab sbt script from github.
val localSBT = homeBin.resolve("sbt") 
downloadFileTo("https://raw.github.com/paulp/sbt-extras/master/sbt", localSBT)
uPlusX(localSBT)

// Clear down vim and set it up from scratch.
println("Setting up vim.")
val vimrc = home.resolve(".vimrc")
val dotvim = home.resolve(".vim")
val vimrcstage1 = dotFiles.resolve("vimrc-stage1")
val vimrcstage2 = dotFiles.resolve("vimrc-stage2")
delete(vimrc)
delete(dotvim)
"git clone https://github.com/gmarik/vundle.git %s/.vim/bundle/vundle".format(home.toAbsolutePath.toString) !;
addContent(vimrc, vimrcstage1)
"vim +BundleInstall +qall" !;
addContent(vimrc, vimrcstage2)

// Append to .bashrc.
// TODO: Expand to all files in /appendhome.
val appendHome = dotFiles.resolve("appendhome")
val bashrcAppend = appendHome.resolve(".bashrc")
addContent(bashrc, bashrcAppend)

// Setup vim.

