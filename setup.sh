#!/bin/bash
apt-get install -y software-properties-common apt-transport-https
apt-get remove -y oneconf
add-apt-repository -y ppa:webupd8team/sublime-text-3
wget -q -O- http://download.fpcomplete.com/ubuntu/fpco.key | apt-key add -
echo 'deb http://download.fpcomplete.com/ubuntu/vivid stable main' | tee /etc/apt/sources.list.d/fpco.list
apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
echo 'deb https://apt.dockerproject.org/repo ubuntu-wily main' | tee /etc/apt/sources.list.d/docker.list
apt-get update
apt-get dist-upgrade -y
apt-get install -y git vpnc gdebi openjdk-8-jdk sublime-text-installer xclip ppa-purge zsh curl lxc-docker htop python-pip vlc ant ack-grep stack
wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
wget -q http://downloads.typesafe.com/scala/2.11.5/scala-2.11.5.deb
dpkg -i google-chrome-stable_current_amd64.deb
dpkg -i scala-2.11.5.deb
pip install awscli
rm -f *.deb
