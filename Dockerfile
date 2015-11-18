FROM ubuntu:15.10
RUN adduser --disabled-password sean
RUN apt-get install -y wget
RUN wget https://raw.githubusercontent.com/seanparsons/dotfiles/master/setup.sh && chmod u+x setup.sh && ./setup.sh
RUN useradd -ms /bin/bash sean
USER sean
WORKDIR /home/sean
RUN wget https://raw.githubusercontent.com/seanparsons/dotfiles/master/usersetup.sh && chmod u+x usersetup.sh && ./usersetup.sh
