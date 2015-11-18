#!/bin/bash
apt-get install -y software-properties-common apt-transport-https
add-apt-repository -y ppa:webupd8team/sublime-text-3
wget -q -O- http://download.fpcomplete.com/ubuntu/fpco.key | apt-key add -
echo 'deb http://download.fpcomplete.com/ubuntu/wily stable main' | tee /etc/apt/sources.list.d/fpco.list
apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
echo 'deb https://apt.dockerproject.org/repo ubuntu-wily main' | tee /etc/apt/sources.list.d/docker.list
apt-get update
apt-get dist-upgrade -y
apt-get install -y git
apt-get install -y vpnc
apt-get install -y gdebi
apt-get install -y openjdk-8-jdk
apt-get install -y sublime-text-installer
apt-get install -y xclip
apt-get install -y ppa-purge
apt-get install -y zsh
apt-get install -y curl
apt-get install -y docker-engine
apt-get install -y htop
apt-get install -y python-pip
apt-get install -y vlc
apt-get install -y ant
apt-get install -y ack-grep
apt-get install -y stack
wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
gdebi -n google-chrome-stable_current_amd64.deb
wget -q http://downloads.typesafe.com/scala/2.11.5/scala-2.11.5.deb
gdebi -n scala-2.11.5.deb
pip install awscli
rm -f *.deb
