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


// Top line functionality.
val haskellAppsToInstall = Seq("cabal-uninstall", "alex", "happy", "yesod-bin", "ghc-mod", "hdevtools", "stylish-haskell", "haddock")



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
  if (Files.isSymbolicLink(path)) {
    Files.delete(path)
  } else if (Files.exists(path)) Files.walkFileTree(path, new SimpleFileVisitor[Path]() {
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
val zshrc = home.resolve(".zshrc")

// Make all the folders.
mkdir(homeBin)

// Grab sbt script from github.
val localSBT = homeBin.resolve("sbt") 
downloadFileTo("https://raw.github.com/paulp/sbt-extras/master/sbt", localSBT)
uPlusX(localSBT)

// Append to .bashrc.
// TODO: Expand to all files in /appendhome.
val appendHome = dotFiles.resolve("appendhome")
val bashrcAppend = appendHome.resolve(".bashrc")
addContent(bashrc, bashrcAppend)
val zshrcAppend = appendHome.resolve(".zshrc")
addContent(zshrc, zshrcAppend)

// Get Haskell rigged up.
def installWithCabal(targets: Seq[String]) {
  println("Updating cabal database")
  "cabal update" !!;
  println("Updating cabal itself")
  "/home/sean/.cabal/bin/cabal install cabal-install" !!;
  println("Deleting cabal-installed")
  val cabalInstalled = homeBin.resolve("cabal-installed")
  delete(cabalInstalled)
  mkdir(cabalInstalled)
  targets.foreach{target =>
    println("Installing " + target)
    // Install the command in its own little sandbox.
    val installPath = cabalInstalled.resolve(target)
    mkdir(installPath)
    Process("/home/sean/.cabal/bin/cabal sandbox init", installPath.toFile) !!;
    Process("/home/sean/.cabal/bin/cabal install -j --force-reinstalls " + target, installPath.toFile) !!;

    // Create symbolic link in the bin folder to it.
    val targetBinFolder = installPath.resolve(".cabal-sandbox").resolve("bin")
    Files.newDirectoryStream(targetBinFolder).asScala.filter(path => Files.isExecutable(path)).foreach{executable =>
      val linkPath = homeBin.resolve(executable.getFileName)
      println("Creating " + linkPath + " -> " + executable)
      delete(linkPath)
      Files.createSymbolicLink(linkPath, executable)
      linkPath.toFile.setExecutable(true, true)
    }
  }
}

println("Setup Haskell")
val dotCabal = home.resolve(".cabal")
val dotGHC = home.resolve(".ghc")
delete(dotCabal)
delete(dotGHC)
installWithCabal(haskellAppsToInstall)

val cabalLib = dotCabal.resolve("lib")
val installedPackages = ("ghc-pkg --user list" !!).split("\n").tail.map(_.trim.replace("(", "").replace(")", "").replace("}", "").replace("{", ""))
installedPackages.foreach{installedPackage =>
  println("Uninstalling: "  + installedPackage)
  ("ghc-pkg unregister --force " + installedPackage) !!;
  delete(dotCabal.resolve(installedPackage))
}

// Sort out dircolors.
// http://michaelheap.com/getting-solarized-working-on-ubuntu/
downloadFileTo("https://raw.github.com/seebi/dircolors-solarized/master/dircolors.256dark", home.resolve(".dircolors"))
"""gconftool-2 --set /apps/gnome-terminal/profiles/Default/use_theme_background --type bool false""" !!;
"""gconftool-2 --set /apps/gnome-terminal/profiles/Default/use_theme_colors --type bool false""" !!;
"""gconftool-2 --set /apps/gnome-terminal/profiles/Default/palette --type string #070736364242:#D3D301010202:#858599990000:#B5B589890000:#26268B8BD2D2:#D3D336368282:#2A2AA1A19898:#EEEEE8E8D5D5:#00002B2B3636:#CBCB4B4B1616:#58586E6E7575:#65657B7B8383:#838394949696:#6C6C7171C4C4:#9393A1A1A1A1:#FDFDF6F6E3E3""" !!;
"""gconftool-2 --set /apps/gnome-terminal/profiles/Default/background_color --type string #00002B2B3636""" !!;
"""gconftool-2 --set /apps/gnome-terminal/profiles/Default/foreground_color --type string #65657B7B8383""" !!;
