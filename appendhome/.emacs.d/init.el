(require 'package)

(setq package-archives
      '(("gnu" . "http://elpa.gnu.org/packages/")
        ("marmalade" . "http://marmalade-repo.org/packages/")
        ("melpa" . "http://melpa.milkbox.net/packages/")))

(package-initialize)
(when (not package-archive-contents)
    (package-refresh-contents))

(defvar my-packages '(haskell-mode
                      solarized-theme
                      ghc))

(dolist (p my-packages)
    (when (not (package-installed-p p))
          (package-install p)))

(load-theme 'solarized-dark t)

(add-hook 'haskell-mode-hook 'turn-on-haskell-indentation)

(autoload 'ghc-init "ghc" nil t)
(add-hook 'haskell-mode-hook (lambda () (ghc-init)))

;disable backup
(setq backup-inhibited t)

;disable auto save
(setq auto-save-default nil)
