# Dotfiles additional content for .bashrc.
# Personal bin folder.
export PATH=~/.cabal/bin:~/.local/bin:$PATH

# Directory colouring.
if [ -x /usr/bin/dircolors ]; then
    test -r ~/.dircolors && eval "$(dircolors -b ~/.dircolors)" || eval "$(dircolors -b)"
fi
