#!/bin/bash
add-apt-repository -y ppa:webupd8team/java
add-apt-repository -y ppa:webupd8team/sublime-text-3
apt-get update
apt-get dist-upgrade
apt-get install -y git haskell-platform-prof vpnc gdebi-core oracle-java8-installer sublime-text-installer xclip ppa-purge zsh curl
cd /tmp
rm -f *.deb
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
wget http://downloads.typesafe.com/scala/2.11.5/scala-2.11.5.deb
gdebi -n google-chrome-stable_current_amd64.deb
gdebi -n scala-2.11.5.deb
rm -rf /home/sean/.zshrc
rm -rf /home/sean/.oh-my-zsh/
curl -L http://install.ohmyz.sh | sh
chown sean:sean /home/sean/.zshrc
chown -R sean:sean /home/sean/.oh-my-zsh/
chsh -s /bin/zsh sean