# How to Contribute

## Getting Started

* Make sure you have a [GitHub account](https://github.com/signup/free).
* Submit a ticket for your issue, assuming one does not already exist.
  * Clearly describe the issue including steps to reproduce when it is a bug.
  * Make sure you fill in the earliest version that you know has the issue.
* Fork the repository on GitHub.
* Add yourself to the `<contributors/>` block in `pom.xml` with the role,
  "contributor".

## Making Changes

* All commits must be releasable, always.  Avoid partial commits of your tree.
  Send patches to others rather than push a non-releasable commit.
* Work on the `master` branch.  Exceptions:
  * Use release branches if the fix applies there.  Always cherry pick fixes
    to `master` if relevant.
  * Do not push local feature branches into the repo without group consensus.

## Code Style

* Lines are 80 characters maximum.  Exceptions:
  * Import lines may be long to avoid awkward wrapping.
* Use standard Java code style.  You know it when you see it.  Exceptions:
  * For empty classes and methods, it is OK to collapse the curly braces
    rather than put them on separate lines.
  * For block syntax, it is OK to leave out optional curly braces if the
    meaning of the code does not change.
  * For `extends` and `implements`, it is OK to break the line early.  If you
    do so, indent is 8 characters, ala standard Java style.
* Ensure all changes use UNIX file endings and UTF-8 encoding, and that only
  scripts have an execute bit (source files are not executable).
* Check for unnecessary whitespace with `git diff --check` before committing.
