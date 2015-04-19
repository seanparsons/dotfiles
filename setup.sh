#!/bin/bash
apt-get remove -y oneconf
add-apt-repository -y ppa:webupd8team/java
add-apt-repository -y ppa:webupd8team/sublime-text-3
#add-apt-repository -y ppa:hvr/ghc
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9
sh -c "echo deb https://get.docker.io/ubuntu docker main\
> /etc/apt/sources.list.d/docker.list"
apt-get update
apt-get dist-upgrade -y
apt-get install -y git haskell-platform-prof vpnc gdebi-core oracle-java8-installer sublime-text-installer xclip ppa-purge zsh curl lxc-docker htop python-pip vlc ant ack-grep
pip install awscli
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
