#!/bin/bash
apt-get install -y software-properties-common apt-transport-https
apt-get remove -y oneconf
add-apt-repository -y ppa:webupd8team/sublime-text-3
wget -q -O- http://download.fpcomplete.com/ubuntu/fpco.key | apt-key add -
echo 'deb http://download.fpcomplete.com/ubuntu/vivid stable main'|sudo tee /etc/apt/sources.list.d/fpco.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9
sh -c "echo deb https://get.docker.io/ubuntu docker main\
> /etc/apt/sources.list.d/docker.list"
apt-get update
apt-get dist-upgrade -y
apt-get install -y git vpnc gdebi openjdk-8-jdk sublime-text-installer xclip ppa-purge zsh curl lxc-docker htop python-pip vlc ant ack-grep stack
wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
wget -q http://downloads.typesafe.com/scala/2.11.5/scala-2.11.5.deb
dpkg -i google-chrome-stable_current_amd64.deb
dpkg -i scala-2.11.5.deb
pip install awscli
rm -f *.deb
