# Dotfiles additional content for .bashrc.
# Personal bin folder.
export PATH=$PATH:~/bin:~/.cabal/bin:~/.local/bin

# Directory colouring.
if [ -x /usr/bin/dircolors ]; then
    test -r ~/.dircolors && eval "$(dircolors -b ~/.dircolors)" || eval "$(dircolors -b)"
fi
